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
            productName = content.find(class_='pr-new-br').get_text(strip=True)

            # Stok bilgisini kontrol et
            stock_status = content.find_all("button", class_=["sold-out", "notify-me-btn"])
            is_in_stock = not bool(stock_status)  # Eğer eleman varsa stokta değil, yoksa stokta var
            if is_in_stock:
                productPrice = content.find(class_='product-price-container').get_text(strip=True)
                productPrice = productPrice if productPrice else "Fiyat bilgisi yok"
            else:
                old_price_element = content.find("span", class_="prc-dsc")
                productPrice = old_price_element.get_text(strip=True) if old_price_element else "Fiyat bilgisi yok"


            return jsonify({
                "name": productName,
                "price": str(productPrice),
                "stock": str(is_in_stock)  # True = Stokta var, False = Stokta yok
            })
        elif "amazon" in url:
            productName = content.find(id = 'productTitle').get_text()
            out_of_stock_element = content.find(id="outOfStock")
            is_in_stock = not bool(out_of_stock_element)
            if is_in_stock:
                productPrice = content.find(class_="a-price aok-align-center reinventPricePriceToPayMargin priceToPay").get_text()
                productPrice = productPrice.get_text(strip=True) if productPrice else "Fiyat bilgisi yok"
            else:
                productPrice = "Stokta yok"

            return jsonify({
                "name": productName,
                "price": str(productPrice),
                "stock": str(is_in_stock)
            })
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000)



