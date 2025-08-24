import pickle
import os
import joblib
from sklearn.metrics.pairwise import cosine_similarity

class BookRecommender:
    def __init__(self):
        # Get the folder where this script lives
        BASE_DIR = os.path.dirname(os.path.abspath(__file__))

        # load the book_data table(for lookup), tf-idf for similarity and genres_one_hot for genre similarity
        with open(os.path.join(BASE_DIR, "data", "book_data.pkl"), 'rb') as f:
            self.book_data = pickle.load(f)

        with open(os.path.join(BASE_DIR, "data", "tfidf_norm.pkl"), 'rb') as f:
            self.tfidf_norm = joblib.load(f)

        with open(os.path.join(BASE_DIR, "data", "genres_one_hot.pkl"), 'rb') as f:
            self.genres_one_hot = joblib.load(f)


    def recommend_books(self, book_title, num_recommendations, cosine_sim_weight=0.6, rating_score_weight=0.2, genre_sim_weight=0.2):
        # handle input and get input book index
        input_book_index = self.book_data[self.book_data['title'] == book_title].index[0]

        # calculate cosine similarity
        cosine_sim = self._calculate_similarity(input_book_index, self.tfidf_norm)

        # get rating score
        rating_score = self.book_data['rating_score'].values

        #calculate genre similarity
        genre_sim = self._calculate_genre_similarity(input_book_index, self.genres_one_hot)

        # calculate final score
        final_score = (cosine_sim * cosine_sim_weight) + (rating_score * rating_score_weight) + (genre_sim * genre_sim_weight)

        # get indexes of top 'num_recommendations' books
        final_score[input_book_index] = -1
        recommended_indexes = final_score.argsort()[::-1][:num_recommendations]

        # map indexes to book data
        recommended_books = self.book_data.iloc[recommended_indexes]
        return recommended_books

    def _calculate_similarity(self, book_index, tfidf_norm):

        book_vector = tfidf_norm[book_index]

        #calculate cosine similarity as dot product (since we normalized length to 1 and cosine similarity = A*B/len(A)*len(B)
        cosine_sim = tfidf_norm.dot(book_vector.T).toarray().flatten()
        return cosine_sim

    def _calculate_genre_similarity(self, book_index, genres_one_hot):
        #take vector for input book
        book_vector = genres_one_hot[book_index]

        # calculate genre similarity
        genre_similarity = cosine_similarity(book_vector, genres_one_hot).flatten()
        return genre_similarity
