import time

import requests
from bs4 import BeautifulSoup

def checkProduct():
    URL = 'https://www.trendyol.com/apple/iphone-16-plus-128gb-laciverttas-p-857296130'
    headers = {"User-Agent": "agentinfo"}
    page = requests.get(URL, headers=headers)
    content = BeautifulSoup(page.content, 'html.parser')

    productName = content.find(class_='pr-new-br').get_text()
    print(productName)

    productPrice = content.find(class_='product-price-container').get_text()
    print(productPrice)

    newPrice = int((productPrice.replace('.', ''))[0:5])
    print(newPrice)
    print(type(newPrice))

while(True):
    checkProduct()
    time.sleep(3)