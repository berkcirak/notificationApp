import time

import requests
from flask import Flask, request, jsonify
from bs4 import BeautifulSoup

app = Flask(__name__)

@app.route('/scrape', methods=['GET'])
def scrape_product():
    url = request.args.get('url')
    if not url:
        return jsonify({"error": "No URL provided"}), 400

    headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36"
    }

    try:
        page = requests.get(url, headers=headers)
        content = BeautifulSoup(page.content, 'html.parser')

        if "trendyol" in url:
            productName = content.find(class_='pr-new-br')
            productName = productName.get_text(strip=True) if productName else "Ürün adı bulunamadı"

            # Stok bilgisini kontrol et
            stock_status = content.find_all("button", class_=["sold-out", "notify-me-btn"])
            is_in_stock = not bool(stock_status)  # Eğer eleman varsa stokta değil, yoksa stokta var

            productPrice_element = content.find("span", class_="prc-dsc")
            originalPrice_element = content.find("span", class_="prc-org")

            productPrice = productPrice_element.get_text(strip=True) if productPrice_element else "Fiyat bilgisi yok"
            originalPrice = originalPrice_element.get_text(strip=True) if originalPrice_element else "Fiyat bilgisi yok"

            image_elements = content.find_all("img")
            image_url = None

            breadcrumb_elements = content.select('.product-detail-breadcrumb-item[title]')
            categories = [elem.get("title").strip() for elem in breadcrumb_elements if elem.get("title")]
            product_category = categories[-3] if categories else None

            description = ""
            desc_list = content.select(".detail-desc-list li")
            desc_started = False

            description_items = []
            for li in desc_list:
                li_text = li.get_text(strip=True)
                if "İade ve Değişim Koşulları" in li_text or "return-info" in li.get("class", []):
                    desc_started = True
                    continue
                if desc_started:
                    description_items.append(li_text)

            description = "\n".join(description_items) if description_items  else None

            for img in image_elements:
                src =img.get("src")
                if src and "jpg" in src:
                    image_url = src
                    break

            return jsonify({
                "name": productName,
                "price": str(productPrice),
                "stock": str(is_in_stock),  # True = Stokta var, False = Stokta yok
                "originalPrice": str(originalPrice),
                "imageUrl": image_url,
                "productCategory": product_category,
                "description": description
            })
        elif "amazon" in url:
            productName = content.find(id='productTitle')
            productName = productName.get_text(strip=True) if productName else "Ürün adı bulunamadı"

            out_of_stock_element = content.find(id="outOfStock")
            is_in_stock = not bool(out_of_stock_element)

            # ✔ GÜNCEL FİYAT → productPrice
            price_whole = content.select_one("span.a-price-whole")
            price_fraction = content.select_one("span.a-price-fraction")  # Bazı ürünlerde küsurat kısmı burada
            # Alternatif olarak:
            # price_fraction = content.select_one("span.a-price-decimal + span")

            if price_whole:
                whole = price_whole.get_text(strip=True).replace(",", "")
                fraction = price_fraction.get_text(strip=True) if price_fraction else "00"
                productPrice = f"{whole}.{fraction} TL"
            else:
                productPrice = "Fiyat bilgisi yok"

            import re

            original_price_span = content.select_one(
                "div.a-section.a-spacing-small.aok-align-center span.aok-offscreen"
            )

            if original_price_span:
                raw_text = original_price_span.get_text(strip=True)
                match = re.search(r"\d{1,3}(?:\.\d{3})*", raw_text)  # ✔ SADECE 44.829 gibi değerler için
                originalPrice = match.group(0) + " TL" if match else "Fiyat bilgisi yok"
            else:
                originalPrice = "Fiyat bilgisi yok"

            image_element = content.find("img", id="landingImage")
            image_url = None
            if image_element:
                image_url = image_element.get("data-old-hires") or image_element.get("src")

            breadcrumb_links = content.select('#wayfinding-breadcrumbs_feature_div ul li a')
            categories = [link.get_text(strip=True) for link in breadcrumb_links if link.get_text(strip=True)]

            product_category = categories[-1] if categories else None

            #description
            # Açıklama bilgilerini çek (Amazon için)
            description = ""
            desc_list = content.select(
                "ul.a-unordered-list.a-vertical.a-spacing-mini li.a-spacing-mini > span.a-list-item")
            description_items = [li.get_text(strip=True) for li in desc_list if li.get_text(strip=True)]

            description = "\n".join(description_items) if description_items else None

            return jsonify({
                "name": productName,
                "price": str(productPrice),
                "stock": str(is_in_stock),
                "originalPrice": str(originalPrice),
                "imageUrl": image_url,
                "productCategory": product_category,
                "description": description
            })
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000)



