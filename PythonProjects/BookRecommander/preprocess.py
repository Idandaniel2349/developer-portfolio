from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.preprocessing import MultiLabelBinarizer
from sklearn.preprocessing import MinMaxScaler
from sklearn.preprocessing import normalize
from scipy import sparse
import pandas as pd
import numpy as np
import joblib
import os

def process_and_save_data():
    # Get the folder where this script lives
    BASE_DIR = os.path.dirname(os.path.abspath(__file__))
    # Point to the data file (relative to this script)
    DATA_PATH = os.path.join(BASE_DIR, "data", "cleaned_books.csv")
    df = pd.read_csv(DATA_PATH, encoding='utf-8')
    
    add_book_info(df)

    # create lookup table, add to it id(index),rating_score and save    
    book_data = df[['title', 'author', 'desc', 'genre', 'rating', 'totalratings', 'img']].copy()
    book_data['id'] = book_data.index
    book_data.set_index('id', inplace=True)
    book_data['rating_score'] = calculate_rating_score(book_data)
    book_data.to_pickle(os.path.join(BASE_DIR, "data", "book_data.pkl"))

    # Create a TF-IDF matrix from the book_info column
    tfidf_matrix = create_TFIDF_matrix(df)
    tfidf_norm = normalize(tfidf_matrix)
    joblib.dump(tfidf_norm, os.path.join(BASE_DIR, "data", "tfidf_norm.pkl"))

    # create genre one hot
    genres_one_hot = sparse.csr_matrix(create_genre_one_hot(book_data))
    joblib.dump(genres_one_hot, os.path.join(BASE_DIR, "data", "genres_one_hot.pkl"))


def add_book_info(df):
    # create new book_info column in the df
    df['book_info'] = df['title'] + " " + df['author'] + " " + df['desc'] + " " + df['genre']
    df['book_info'] = df['book_info'].str.lower()
    df['book_info'] = df['book_info'].str.replace('/',' ')
    df['book_info'] = df['book_info'].str.replace(',',' ')
    # - Remove multiple spaces
    df['book_info'] = df['book_info'].str.replace(r'\s+', ' ', regex=True)
    # remove nan with empty string
    df['book_info'] = df['book_info'].fillna("")


# Create TF-IDF matrix that represent each book as a numeric vector
def create_TFIDF_matrix(df):
    tfidf = TfidfVectorizer(stop_words='english', max_features=50000)
    tfidf_matrix = tfidf.fit_transform(df['book_info'])
    return tfidf_matrix

# Calculate the rating score for each book in such a way that we balance quality vs popularity.
def calculate_rating_score(book_data, rating_col='rating', totalratings_col='totalratings', rating_weight=0.7, popularity_weight=0.3, min_ratings = 100, penalty_factor=0.5):
    # Normalize the rating to a 0-1 scale using min-max
    rating_norm = (book_data[rating_col] - book_data[rating_col].min()) / (book_data[rating_col].max() - book_data[rating_col].min())

    # Log-transform popularity to reduce skew, then Min-Max scale
    popularity_log = np.log1p(book_data[totalratings_col])  # log(1 + x)
    scaler = MinMaxScaler()
    popularity_norm = scaler.fit_transform(popularity_log.values.reshape(-1, 1)).flatten()

    # Combine the two scores
    combined_score = (rating_norm * rating_weight) + (popularity_norm * popularity_weight)

    #penalize books with very few ratings
    mask = book_data[totalratings_col] < min_ratings
    combined_score[mask] *= penalty_factor
    return combined_score

#one hot encode genres
def create_genre_one_hot(book_data, genre_col='genre'):
    # split genres
    genres_split = book_data[genre_col].apply(
        lambda x: x.split(',') if isinstance(x, str) else []
    )

    # MultiLabelBinarizer takes lists of labels (your genres) and converts them into a binary matrix.
    mlb = MultiLabelBinarizer()
    # fit finds all the unique items and transform create 0/1 matrix, where each row is a book and each column is a genre
    genres_one_hot = mlb.fit_transform(genres_split)

    return genres_one_hot

process_and_save_data()
