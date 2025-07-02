// src/components/BookCard.js
import React from 'react';
import './BookCard.css';

const BookCard = ({ book }) => {
  const [loading, setLoading] = React.useState(true);

  return (
    <a href="#" className="book-card-link"> {/* 실제 링크는 React Router Link 사용 */}
      <div className="book-card">
        <div className="book-cover-wrapper">
          {loading && (
            <div className="book-cover-placeholder">
              <div className="spinner"></div> {/* 간단한 로딩 스피너 */}
            </div>
          )}
          <img
            src={book.image}
            alt={book.title}
            className="book-cover-image"
            onLoad={() => setLoading(false)}
            onError={(e) => {
              e.target.src = `https://via.placeholder.com/200x300?text=No+Image`; // 이미지 로드 실패 시 대체 이미지
              setLoading(false);
            }}
            style={{ display: loading ? 'none' : 'block' }}
          />
        </div>
        <div className="book-info">
          <h3 className="book-title">{book.title}</h3>
          <p className="book-subtitle">{book.subtitle}</p>
        </div>
      </div>
    </a>
  );
};

export default BookCard;