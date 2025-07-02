// src/pages/BookDetailsPage.js
import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import './BookDetailsPage.css';
import Header from '../mainPage/Header';
import Footer from '../mainPage/Footer';
import { useAuth } from '../contexts/AuthContext';

const BookDetailsPage = () => {
  const { id: bookId } = useParams(); // URL에서 bookId 가져오기
  const navigate = useNavigate();
  const { isLoggedIn, user, logout } = useAuth(); // 로그인 상태와 사용자 정보

  const [book, setBook] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');
  const [hasAccess, setHasAccess] = useState(false); // 도서 열람 권한 (구매/구독)
  const [isDeleting, setIsDeleting] = useState(false); // 삭제 버튼 로딩

  useEffect(() => {
    const fetchBookDetails = async () => {
      if (!isLoggedIn || !user || !user.id) {
        // 로그인되지 않았으면 로그인 페이지로 리디렉션
        alert('도서를 열람하려면 로그인이 필요합니다.');
        navigate('/login');
        return;
      }

      setIsLoading(true);
      setError('');
      try {
        // API 명세: GET /books/{id}?userId={userId}
        // 이 API는 조회수 증가, 구독/구매 확인 및 포인트 차감 로직을 포함합니다.
        const response = await axios.get(`/books/${bookId}?userId=${user.id}`);
        setBook(response.data);
        setHasAccess(true); // API 호출 성공 시 열람 권한 부여
      } catch (err) {
        console.error('도서 상세 정보 불러오기 실패:', err);
        if (err.response && err.response.status === 400) { // 400 BAD_REQUEST (예: 포인트 부족)
          setError(`도서 열람 불가: ${err.response.data || '포인트가 부족하거나 기타 문제로 도서를 열람할 수 없습니다.'}`);
        } else if (err.response && err.response.status === 404) {
          setError('도서를 찾을 수 없습니다.');
        } else if (err.response && err.response.data && typeof err.response.data === 'string') {
          setError(`오류: ${err.response.data}`);
        } else if (err.response && err.response.data && err.response.data.message) {
          setError(`오류: ${err.response.data.message}`);
        } else {
          setError('도서 정보를 불러오는 데 실패했습니다.');
        }
        setHasAccess(false); // 오류 발생 시 열람 불가
      } finally {
        setIsLoading(false);
      }
    };

    fetchBookDetails();
  }, [bookId, isLoggedIn, user, navigate]); // bookId, user 변경 시 다시 실행

  const handleDeleteBook = async () => {
    if (!user || !user.isAdmin) {
      alert('도서를 삭제할 권한이 없습니다.');
      return;
    }
    if (!window.confirm('정말로 이 도서를 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.')) {
      return;
    }

    setIsDeleting(true);
    try {
      // API 명세: DELETE /books/{id}?userId={userId} (관리자만 가능)
      await axios.delete(`/books/${bookId}?userId=${user.id}`);
      alert('도서가 성공적으로 삭제되었습니다.');
      navigate('/'); // 메인 페이지로 이동
    } catch (err) {
      console.error('도서 삭제 실패:', err);
      if (err.response && err.response.status === 403) {
        setError('도서 삭제 권한이 없습니다.');
      } else if (err.response && err.response.data && typeof err.response.data === 'string') {
        setError(`삭제 실패: ${err.response.data}`);
      } else if (err.response && err.response.data && err.response.data.message) {
        setError(`삭제 실패: ${err.response.data.message}`);
      } else {
        setError('도서 삭제 중 오류가 발생했습니다.');
      }
    } finally {
      setIsDeleting(false);
    }
  };


  if (isLoading) {
    return (
      <div className="book-details-page-wrapper loading">
        <Header />
        <main className="book-details-main-content">
          <div className="loading-spinner"></div>
          <p>도서 상세 정보를 불러오는 중...</p>
        </main>
        <Footer />
      </div>
    );
  }

  if (error) {
    return (
      <div className="book-details-page-wrapper error">
        <Header />
        <main className="book-details-main-content">
          <p className="error-message">{error}</p>
          <button onClick={() => navigate('/')} className="back-button">메인으로 돌아가기</button>
        </main>
        <Footer />
      </div>
    );
  }

  if (!book) { // 로딩이 끝났는데 책 데이터가 없는 경우
    return (
      <div className="book-details-page-wrapper error">
        <Header />
        <main className="book-details-main-content">
          <p className="error-message">도서를 찾을 수 없습니다.</p>
          <button onClick={() => navigate('/')} className="back-button">메인으로 돌아가기</button>
        </main>
        <Footer />
      </div>
    );
  }

  return (
    <div className="book-details-page-wrapper">
      <Header />
      <main className="book-details-main-content">
        <div className="book-details-container">
          <div className="book-header">
            <img src={book.coverImageUrl} alt={book.title} className="book-cover-large" />
            <div className="book-meta">
              <h1 className="book-title">{book.title}</h1>
              <p className="book-author">작가 ID: {book.userId}</p>
              <p className="book-category">카테고리: {book.category}</p>
              <p className="book-price">가격: {book.price} 포인트</p>
              <p className="book-views">조회수: {book.viewCount}</p>
              <p className="book-status">상태: <span className={`status-badge ${book.status?.toLowerCase()}`}>{book.status}</span></p>
              <p className="book-published">출간일: {new Date(book.createdAt).toLocaleDateString()}</p>
              {user && user.isAdmin && ( // 관리자일 경우 삭제 버튼 표시
                <button onClick={handleDeleteBook} className="delete-button" disabled={isDeleting}>
                  {isDeleting ? '삭제 중...' : '도서 삭제 (관리자)'}
                </button>
              )}
               {user && user.id === book.userId && ( // 본인이 쓴 책일 경우 편집 버튼 표시
                <button onClick={() => navigate(`/books/register/${book.bookId}`)} className="edit-button">
                  도서 편집
                </button>
              )}
            </div>
          </div>

          <div className="book-summary-section">
            <h2>도서 요약</h2>
            <p>{book.summary}</p>
          </div>

          <div className="book-content-section">
            <h2>도서 내용</h2>
            {hasAccess ? (
              <div className="book-content-text">
                {book.content}
              </div>
            ) : (
              <div className="access-denied-message">
                <p>이 도서는 열람 권한이 없습니다. 구독하시거나, 충분한 포인트를 보유하고 있는지 확인해주세요.</p>
              </div>
            )}
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default BookDetailsPage;