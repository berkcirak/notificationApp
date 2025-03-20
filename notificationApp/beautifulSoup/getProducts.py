from flask import Flask, request, jsonify
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

app = Flask(__name__)

@app.route('/compute_similarity', methods=['POST'])
def compute_similarity():
    data = request.json
    new_product = data['newProduct']
    all_products = data['allProducts']

    if not all_products or not new_product:
        return jsonify([])

    # Ürün isimlerini listeye çevir
    product_names = [p['productName'] for p in all_products]

    # TF-IDF vektörleştirme ile benzerlik matrisini oluştur
    vectorizer = TfidfVectorizer(stop_words='turkish')
    tfidf_matrix = vectorizer.fit_transform(product_names)

    # Yeni eklenen ürünün vektörünü al
    new_product_vector = vectorizer.transform([new_product['productName']])

    # Kosinüs benzerliği hesapla
    cosine_sim = cosine_similarity(new_product_vector, tfidf_matrix)

    # En benzer 5 ürünü al (ilk eleman kendisi olabileceği için atlıyoruz)
    similar_indices = cosine_sim.argsort()[0][-6:-1][::-1]
    recommended_product_ids = [all_products[i]['id'] for i in similar_indices]

    return jsonify(recommended_product_ids)

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5001)
