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

            productPrice = productPrice_element.get_text(strip=True) if productPrice_element else None
            originalPrice = originalPrice_element.get_text(strip=True) if originalPrice_element else None

            image_elements = content.find_all("img")
            image_url = None

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
                "imageUrl": image_url
            })
        elif "amazon" in url:
            productName = content.find(id = 'productTitle').get_text()
            productName = productName.get_text(strip=True) if productName else "Ürün adı bulunamadı"

            out_of_stock_element = content.find(id="outOfStock")
            is_in_stock = not bool(out_of_stock_element)

            productPrice = "Fiyat bilgisi yok"
            originalPrice = None

            if is_in_stock:
                price_element = content.find(class_="a-price aok-align-center reinventPricePriceToPayMargin priceToPay")
                productPrice = price_element.get_text(strip=True) if price_element else "Fiyat bilgisi yok"

                originalPrice_element = content.find("span", class_= "a-price a-text-price")
                if originalPrice_element:
                    offscreen_span = originalPrice_element.find("span", class_ = "a-offscreen")
                    originalPrice = offscreen_span.get_text(strip=True) if offscreen_span else None

            return jsonify({
                "name": productName,
                "price": str(productPrice),
                "stock": str(is_in_stock),
                "originalPrice": str(originalPrice)
            })
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000)



