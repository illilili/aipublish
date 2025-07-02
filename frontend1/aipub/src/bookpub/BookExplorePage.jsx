// src/pages/BookExplorePage.js
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './BookExplorePage.css';
import Header from '../mainPage/Header';
import Footer from '../mainPage/Footer';
import BookCard from '../mainPage/BookCard'; // 기존 BookCard 재사용
import { useAuth } from '../contexts/AuthContext';

const BookExplorePage = () => {
  const { isLoggedIn, user } = useAuth();
  const [books, setBooks] = useState([]);
  const [bestsellers, setBestsellers] = useState([]);
  const [purchasedBooks, setPurchasedBooks] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');
  const [activeTab, setActiveTab] = useState('all'); // 'all', 'bestsellers', 'purchased'

  useEffect(() => {
    const fetchBooks = async () => {
      setIsLoading(true);
      setError('');
      try {
        // 1. 전체 도서 목록 (status=PUBLISHED만 가져오는 것이 일반적)
        const allBooksResponse = await axios.get('/books?status=PUBLISHED'); // 백엔드에서 PUBLISHED 필터링
        setBooks(allBooksResponse.data);

        // 2. 베스트셀러 목록
        const bestsellersResponse = await axios.get('/books/bestsellers');
        setBestsellers(bestsellersResponse.data);

        // 3. 구매한 도서 목록 (로그인 시에만)
        if (isLoggedIn && user?.id) {
          const purchasedBooksResponse = await axios.get(`/books/purchased?userId=${user.id}`);
          setPurchasedBooks(purchasedBooksResponse.data);
        } else {
          setPurchasedBooks([]); // 로그아웃 상태면 비움
        }

      } catch (err) {
        console.error('도서 목록 불러오기 실패:', err);
        setError('도서 목록을 불러오는 데 실패했습니다.');
      } finally {
        setIsLoading(false);
      }
    };

    fetchBooks();
  }, [isLoggedIn, user]); // 로그인 상태나 user 정보 변경 시 다시 불러옴


  if (isLoading) {
    return (
      <div className="book-explore-page-wrapper loading">
        <Header />
        <main className="book-explore-main-content">
          <div className="loading-spinner"></div>
          <p>도서 목록을 불러오는 중...</p>
        </main>
        <Footer />
      </div>
    );
  }

  if (error) {
    return (
      <div className="book-explore-page-wrapper error">
        <Header />
        <main className="book-explore-main-content">
          <p className="error-message">{error}</p>
          <button onClick={() => window.location.reload()} className="back-button">다시 시도</button>
        </main>
        <Footer />
      </div>
    );
  }

  const renderBookGrid = (bookList) => {
    if (bookList.length === 0) {
      return <p className="no-books-message">아직 등록된 도서가 없습니다.</p>;
    }
    return (
      <div className="book-grid">
        {bookList.map((book) => (
          <BookCard key={book.bookId} book={book} />
        ))}
      </div>
    );
  };

  return (
    <div className="book-explore-page-wrapper">
      <Header />
      <main className="book-explore-main-content">
        <div className="book-explore-container">
          <h1 className="explore-title">도서 둘러보기</h1>

          <div className="tab-navigation">
            <button
              className={`tab-button ${activeTab === 'all' ? 'active' : ''}`}
              onClick={() => setActiveTab('all')}
            >
              전체 도서 ({books.length})
            </button>
            <button
              className={`tab-button ${activeTab === 'bestsellers' ? 'active' : ''}`}
              onClick={() => setActiveTab('bestsellers')}
            >
              베스트셀러 ({bestsellers.length})
            </button>
            {isLoggedIn && (
              <button
                className={`tab-button ${activeTab === 'purchased' ? 'active' : ''}`}
                onClick={() => setActiveTab('purchased')}
              >
                구매한 도서 ({purchasedBooks.length})
              </button>
            )}
          </div>

          <div className="tab-content">
            {activeTab === 'all' && (
              <section className="books-section">
                <h2 className="section-subtitle">모든 출간 도서</h2>
                {renderBookGrid(books)}
              </section>
            )}
            {activeTab === 'bestsellers' && (
              <section className="books-section">
                <h2 className="section-subtitle">인기 베스트셀러</h2>
                {renderBookGrid(bestsellers)}
              </section>
            )}
            {activeTab === 'purchased' && isLoggedIn && (
              <section className="books-section">
                <h2 className="section-subtitle">내가 구매한 도서</h2>
                {renderBookGrid(purchasedBooks)}
              </section>
            )}
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default BookExplorePage;