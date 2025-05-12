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

    # Ürün isimleri ve açıklamalarını birleştir
    combined_texts = []
    for p in all_products:
        name = p.get('productName', '')
        desc = p.get('description', '')
        combined_texts.append(f"{name} {desc}")

    # Yeni ürünün ismi ve açıklamasını birleştir
    new_product_text = f"{new_product.get('productName', '')} {new_product.get('description', '')}"

    # TF-IDF vektörleştirme
    vectorizer = TfidfVectorizer(stop_words=None)
    tfidf_matrix = vectorizer.fit_transform(combined_texts)

    # Yeni ürünün TF-IDF vektörü
    new_product_vector = vectorizer.transform([new_product_text])

    # Kosinüs benzerliğini hesapla
    cosine_sim = cosine_similarity(new_product_vector, tfidf_matrix)

    # En benzer 5 ürünü al (kendisi dahil olabileceğinden dolayı kendisi dışındakileri seçiyoruz)
    similar_indices = cosine_sim.argsort()[0][-6:-1][::-1]
    recommended_product_ids = [all_products[i]['id'] for i in similar_indices]

    return jsonify(recommended_product_ids)


if __name__ == '__main__':
    app.run(host="0.0.0.0", debug=True, port=5001)
