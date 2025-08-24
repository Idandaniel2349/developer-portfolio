import streamlit as st
from recommender import BookRecommender

# Initialize the BookRecommender
recommender = BookRecommender()

st.title("Book Recommender")

# user input
book_title = st.selectbox("Select a book title:", options = recommender.book_data['title'].tolist())
num_recommendations = st.slider("Number of recommendations", 1, 1000, 5)

if st.button("Recommend"):
    recommended_books = recommender.recommend_books(book_title, num_recommendations)

    if recommended_books is not None and not recommended_books.empty:
        st.subheader("Recommended books:")

        cards_per_row = 4
        rows = [recommended_books.iloc[i:i+cards_per_row] for i in range(0, len(recommended_books), cards_per_row)]

        for row_df in rows:
            cols = st.columns(cards_per_row)
            for col, (_, book) in zip(cols, row_df.iterrows()):
                 with col:
                    # Image
                    st.image(book['img'], width=150)

                    # Title
                    st.markdown(f"**{book['title']}**")

                    # Author
                    st.markdown(f"**{book['author']}**")

                    # Rating and genre
                    st.markdown(f"⭐ {book['rating']} ({book['totalratings']} ratings)  |  🏷️ {book['genre']}")

                    # Short description (truncate if too long)
                    max_len = 100
                    short_desc = book['desc'][:max_len] + "..." if len(book['desc']) > max_len else book['desc']

                    # Expander for full description
                    with st.expander("Read more"):
                        st.markdown(book['desc'])

                    # Show short description by default
                    st.markdown(short_desc)

    else:
        st.error("No recommendations found. Try another book.")
