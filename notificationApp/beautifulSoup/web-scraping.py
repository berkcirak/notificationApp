import time

import requests
from flask import Flask, request, jsonify
from bs4 import BeautifulSoup
from playwright.sync_api import sync_playwright
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
        elif "vatanbilgisayar" in url:
            price_element = content.select_one("div.product-detail-price-big span.product-list__price")
            product_price = price_element.get_text(strip=True) + " TL" if price_element else "Fiyat bilgisi yok"

            original_price_element = content.select_one(".product-list__current-price")
            originalPrice = original_price_element.get_text(strip=True) if original_price_element else "Fiyat bilgisi yok"

            name_element = content.select_one("h1.product-list__product-name")
            product_name = name_element.get_text(strip=True) if name_element else "Ürün adı bulunamadı"

            breadcrumb_items = content.select("ul.breadcrumb li a")
            categories = [item.get_text(strip=True) for item in breadcrumb_items]
            product_category = categories[-3] if len(categories) >= 3 else None

            description_div = content.select_one("div.product-list__description")
            description = description_div.get_text(strip=True) if description_div else "Açıklama bulunamadı"

            image_element = content.select_one("img.wrapper-main-slider__image")
            image_url = image_element.get("src") if image_element else None

            stock_element = content.select_one("a.btn-nonstock")
            is_in_stock = not bool(stock_element)

            return jsonify({
                "name": product_name,
                "price": str(product_price),
                "stock": str(is_in_stock),
                "originalPrice": str(originalPrice),
                "imageUrl": image_url,
                "productCategory": product_category,
                "description": description
            })
        elif "teknosa" in url:
            price_element = content.select_one("span.prc.prc-third")
            price = price_element.get_text(strip=True) if price_element else "Fiyat bilgisi yok"

            name_element = content.select_one("input.productNameReplace")
            product_name = name_element.get("value").strip() if name_element else "Ürün adı bulunamadı"

            breadcrumb_items = content.select("ol.breadcrumb li")
            if len(breadcrumb_items) >= 2:
                product_category = breadcrumb_items[-2].get_text(strip=True)
            else:
                product_category = None

            # Açıklama (teknik özellikler tablosu)
            tech_table = content.select("table.tablefeature td")
            description_items = [td.get_text(strip=True) for td in tech_table if td.get_text(strip=True)]
            description = "\n".join(description_items) if description_items else "Açıklama yok"

            breadcrumb = content.find("ol", class_="breadcrumb")
            image_url = None
            if breadcrumb:
                current = breadcrumb
                while current:
                    current = current.find_next()
                    if current.name == "img":
                        image_url = (
                                current.get("src") or
                                current.get("data-src") or
                                current.get("srcset") or
                                current.get("data-srcset")
                        )
                        break

            # İndirimli fiyat varsa çek
            original_price_element = content.select_one("span.prc.prc-first")
            original_price = original_price_element.get_text(strip=True) if original_price_element else "Fiyat bilgisi yok"

            # 🔍 Stok durumu: Eğer "Sepete Ekle" butonu varsa stokta var
            stock_button = content.select_one("button.add-to-cart-button")
            is_in_stock = not bool(stock_button)  # True = stokta, False = yok

            return jsonify({
                "name": product_name,
                "price": str(price),
                "stock": str(is_in_stock),  # bunu istersen ileride img altına sepete ekle butonu var mı diye güncelleyebiliriz
                "originalPrice": str(original_price),
                "imageUrl": image_url,
                "productCategory": product_category,
                "description": description
            })

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000)


