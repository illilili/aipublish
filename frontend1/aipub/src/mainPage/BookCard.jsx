// src/components/BookCard.js (수정 필요)
import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './BookCard.css';

const BookCard = ({ book }) => {
  const [loading, setLoading] = useState(true);

  return (
    <Link to={`/books/${book.bookId}`} className="book-card-link">
      <div className="book-card">
        <div className="book-cover-wrapper">
          {loading && (
            <div className="book-cover-placeholder">
              <div className="spinner"></div>
            </div>
          )}
          <img
            src={book.coverImageUrl} // <-- 이 부분을 book.coverImageUrl로 수정
            alt={book.title}
            className="book-cover-image"
            onLoad={() => setLoading(false)}
            onError={(e) => {
              e.target.src = `https://via.placeholder.com/200x300?text=No+Image`; // 오류 시 대체 이미지
              setLoading(false);
              console.error('이미지 로드 실패:', e.target.src); // 디버깅용
            }}
            style={{ display: loading ? 'none' : 'block' }}
          />
        </div>
        <div className="book-info">
          <h3 className="book-title">{book.title}</h3>
          <p className="book-subtitle">{book.summary || book.subtitle}</p> {/* subtitle 대신 summary 사용 고려 */}
        </div>
      </div>
    </Link>
  );
};

export default BookCard;