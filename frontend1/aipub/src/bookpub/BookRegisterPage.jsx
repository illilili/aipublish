// src/pages/BookRegisterPage.js
import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom'; // useParams 추가
import axios from 'axios';
import './BookRegisterPage.css';
import Header from '../mainPage/Header';
import Footer from '../mainPage/Footer';
import { useAuth } from '../contexts/AuthContext';

const BookRegisterPage = () => {
  const { isLoggedIn, user } = useAuth();
  const navigate = useNavigate();
  const { bookId: paramBookId } = useParams(); // URL에서 bookId 가져오기

  const [bookForm, setBookForm] = useState({
    bookId: null, // 기존 도서 수정 시 사용
    title: '',
    content: '',
    summary: '',
    coverImageUrl: '',
    category: '',
    price: 0,
    status: 'DRAFT', // 초기 상태: 초안
  });
  const [currentStep, setCurrentStep] = useState(1); // 1: 원고 작성, 2: 메타데이터, 3: 확인
  const [isLoading, setIsLoading] = useState(true); // 초기 로딩 (기존 도서 불러오기)
  const [submitLoading, setSubmitLoading] = useState(false); // 버튼 클릭 시 로딩
  const [error, setError] = useState('');

  const categories = ['문학', '과학', '역사', '예술', '기술', '기타']; // 예시 카테고리

  // 컴포넌트 로드 시, 기존 도서 정보 불러오기 또는 로그인 확인
  useEffect(() => {
    const loadBookOrCheckAuth = async () => {
      if (!isLoggedIn || !user || !user.id) {
        alert('도서 등록/편집은 로그인 후 이용 가능합니다.');
        navigate('/login');
        return;
      }

      if (paramBookId) {
        // 기존 도서 편집 모드
        try {
          // userId를 쿼리 파라미터로 보내는 API가 없으므로, 모든 도서를 가져와 필터링하거나
          // 백엔드에 `/books/{bookId}/byUser/{userId}` 같은 API가 필요합니다.
          // 여기서는 `/books/{bookId}` API만 사용한다고 가정하고, 사용자 일치 여부는 프론트에서 확인합니다.
          const response = await axios.get(`/books/${paramBookId}?userId=${user.id}`); // userId도 함께 보내 조회수 증가 로직을 타게 함
          const bookData = response.data;

          if (bookData.userId !== user.id) {
            alert('이 도서를 편집할 권한이 없습니다.');
            navigate('/mypage'); // 마이페이지 등으로 리디렉션
            return;
          }

          setBookForm({
            bookId: bookData.bookId,
            title: bookData.title,
            content: bookData.content,
            summary: bookData.summary || '',
            coverImageUrl: bookData.coverImageUrl || '',
            category: bookData.category || '',
            price: bookData.price || 0,
            status: bookData.status,
          });
        } catch (err) {
          console.error('도서 정보 불러오기 실패:', err);
          setError('도서 정보를 불러오는 데 실패했습니다. 올바른 도서 ID인지 확인해주세요.');
          // navigate('/mypage'); // 에러 시 리디렉션
        }
      }
      setIsLoading(false);
    };

    loadBookOrCheckAuth();
  }, [isLoggedIn, user, navigate, paramBookId]);

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setBookForm(prevForm => ({
      ...prevForm,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const handleSaveDraft = async () => {
    setError('');
    if (!bookForm.title || !bookForm.content) {
      setError('제목과 내용은 필수 입력 항목입니다.');
      return;
    }
    setSubmitLoading(true);
    try {
      let response;
      if (bookForm.bookId) {
        // 기존 도서는 일단 SaveBookCommand로 다시 저장할 수 있는 API가 백엔드에 없음
        // 실제로는 PUT /books/{bookId} 나 PATCH /books/{bookId} API가 필요합니다.
        // 현재는 SaveBookCommand는 '생성'에 가깝고, UpdateBookMetadataCommand는 '메타데이터 업데이트'에 가깝습니다.
        // 여기서는 UpdateBookMetadataCommand를 사용하여 일부만 업데이트하는 것으로 처리합니다.
        // 완전한 원고 수정 저장을 위해서는 백엔드 API 설계가 필요합니다.
        // 임시로, 업데이트 로직이 없다고 가정하고 경고 메시지를 띄웁니다.
        alert('현재는 작성 중인 원고를 다시 저장하는 API가 직접 제공되지 않습니다. 메타데이터 업데이트 또는 출간 요청을 이용해주세요.');
        // 실제로는 아래와 유사한 PATCH API가 필요
        // response = await axios.patch(`/api/books/${bookForm.bookId}`, {
        //     title: bookForm.title,
        //     content: bookForm.content,
        //     userId: user.id
        // });
      } else {
        // 새로운 원고 저장 (DRAFT 상태)
        // API 명세: POST /books/savebookcommand
        response = await axios.post('/books/savebookcommand', {
          userId: user.id,
          title: bookForm.title,
          content: bookForm.content,
          status: 'DRAFT', // 항상 DRAFT로 저장
        });
        setBookForm(prevForm => ({ ...prevForm, bookId: response.data.bookId, status: response.data.status }));
        alert('원고가 초안으로 저장되었습니다!');
      }

    } catch (err) {
      console.error('원고 저장 실패:', err);
      setError('원고 저장 중 오류가 발생했습니다.');
    } finally {
      setSubmitLoading(false);
    }
  };

  const handleUpdateMetadata = async () => {
    setError('');
    if (!bookForm.bookId) {
        setError('먼저 원고를 저장하여 도서 ID를 받아야 메타데이터를 업데이트할 수 있습니다.');
        return;
    }
    if (!bookForm.summary || !bookForm.coverImageUrl || !bookForm.category || bookForm.price === null) {
      setError('요약, 표지 URL, 카테고리, 가격은 필수 입력 항목입니다.');
      return;
    }
    setSubmitLoading(true);
    try {
      // API 명세: POST /books/updatemetadata
      const response = await axios.post('/books/updatemetadata', {
        bookId: bookForm.bookId,
        summary: bookForm.summary,
        coverImageUrl: bookForm.coverImageUrl,
        category: bookForm.category,
        price: bookForm.price,
      });
      setBookForm(prevForm => ({ ...prevForm, status: response.data.status })); // PUBLISHED로 업데이트될 것
      alert('도서 메타데이터가 성공적으로 업데이트되었습니다!');
      // 메타데이터 업데이트 후 바로 출간요청 상태로 변경되는 것이 자연스러움 (백엔드에서 PUBLISHED로 상태 변경)
    } catch (err) {
      console.error('메타데이터 업데이트 실패:', err);
      setError('메타데이터 업데이트 중 오류가 발생했습니다.');
    } finally {
      setSubmitLoading(false);
    }
  };

  const handleSubmitBook = async () => {
    setError('');
    if (!bookForm.bookId) {
        setError('먼저 원고를 저장하고 메타데이터를 업데이트해야 출간 요청할 수 있습니다.');
        return;
    }
    if (bookForm.status !== 'PUBLISHED') { // PUBLISHED 상태여야만 출간 요청 가능하도록
        setError('메타데이터 업데이트를 완료해야 출간 요청을 할 수 있습니다.');
        return;
    }
    if (!window.confirm('정말로 이 도서를 출간 요청하시겠습니까?')) {
        return;
    }
    setSubmitLoading(true);
    try {
      // API 명세: POST /books/submitbookcommand
      const response = await axios.post('/books/submitbookcommand', {
        bookId: bookForm.bookId,
      });
      setBookForm(prevForm => ({ ...prevForm, status: response.data.status })); // SUBMITTED로 업데이트될 것
      alert('도서 출간 요청이 성공적으로 제출되었습니다!');
      navigate('/mypage'); // 출간 요청 후 마이페이지로 이동
    } catch (err) {
      console.error('도서 출간 요청 실패:', err);
      setError('도서 출간 요청 중 오류가 발생했습니다.');
    } finally {
      setSubmitLoading(false);
    }
  };

  if (isLoading) {
    return (
      <div className="book-register-page-wrapper loading">
        <Header />
        <main className="book-register-main-content">
          <div className="loading-spinner"></div>
          <p>도서 정보를 불러오는 중...</p>
        </main>
        <Footer />
      </div>
    );
  }

  // 로그인되지 않았거나 사용자 정보가 없는 경우 (useEffect에서 이미 처리하지만, 안전망)
  if (!isLoggedIn || !user || !user.id) {
    return (
        <div className="book-register-page-wrapper">
            <Header />
            <main className="book-register-main-content">
                <div className="error-message-box">
                    <p>로그인이 필요합니다.</p>
                    <button onClick={() => navigate('/login')} className="submit-button">로그인 하러 가기</button>
                </div>
            </main>
            <Footer />
        </div>
    );
  }

  return (
    <div className="book-register-page-wrapper">
      <Header />
      <main className="book-register-main-content">
        <div className="book-register-container">
          <h1 className="auth-title">도서 {paramBookId ? '편집' : '등록'} 및 출간 요청</h1>
          {error && <p className="error-message mb-4">{error}</p>}

          <div className="step-navigation">
            <button className={`step-button ${currentStep === 1 ? 'active' : ''}`} onClick={() => setCurrentStep(1)}>1. 원고 작성</button>
            <button className={`step-button ${currentStep === 2 ? 'active' : ''}`} onClick={() => setCurrentStep(2)} disabled={!bookForm.bookId && currentStep < 2}>2. 메타데이터</button>
            <button className={`step-button ${currentStep === 3 ? 'active' : ''}`} onClick={() => setCurrentStep(3)} disabled={bookForm.status !== 'PUBLISHED' && currentStep < 3}>3. 출간 요청</button>
          </div>

          <div className="form-section">
            {currentStep === 1 && (
              <form onSubmit={(e) => { e.preventDefault(); handleSaveDraft(); }}>
                <div className="form-group">
                  <label htmlFor="title">도서 제목*</label>
                  <input
                    type="text"
                    id="title"
                    name="title"
                    value={bookForm.title}
                    onChange={handleInputChange}
                    placeholder="도서 제목을 입력하세요"
                    required
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="content">도서 내용*</label>
                  <textarea
                    id="content"
                    name="content"
                    value={bookForm.content}
                    onChange={handleInputChange}
                    placeholder="도서의 본문을 입력하세요"
                    rows="20"
                    required
                  ></textarea>
                </div>
                <button type="submit" className="submit-button" disabled={submitLoading}>
                  {submitLoading ? '저장 중...' : (bookForm.bookId ? '원고 재저장 (미지원)' : '초안 저장')}
                </button>
                {bookForm.bookId && <p className="status-info">도서 ID: {bookForm.bookId}, 현재 상태: {bookForm.status}</p>}
                {!bookForm.bookId && <p className="status-info">원고 저장 시 도서 ID가 생성됩니다.</p>}
              </form>
            )}

            {currentStep === 2 && (
              <form onSubmit={(e) => { e.preventDefault(); handleUpdateMetadata(); }}>
                <div className="form-group">
                  <label htmlFor="summary">도서 요약*</label>
                  <textarea
                    id="summary"
                    name="summary"
                    value={bookForm.summary}
                    onChange={handleInputChange}
                    placeholder="도서의 내용을 간략하게 요약해주세요."
                    rows="5"
                    required
                  ></textarea>
                </div>
                <div className="form-group">
                  <label htmlFor="coverImageUrl">표지 이미지 URL*</label>
                  <input
                    type="url"
                    id="coverImageUrl"
                    name="coverImageUrl"
                    value={bookForm.coverImageUrl}
                    onChange={handleInputChange}
                    placeholder="표지 이미지의 URL을 입력하세요 (예: https://example.com/cover.jpg)"
                    required
                  />
                  {bookForm.coverImageUrl && (
                      <div className="image-preview">
                          <img src={bookForm.coverImageUrl} alt="Cover Preview" />
                      </div>
                  )}
                </div>
                <div className="form-group">
                  <label htmlFor="category">카테고리*</label>
                  <select
                    id="category"
                    name="category"
                    value={bookForm.category}
                    onChange={handleInputChange}
                    required
                  >
                    <option value="">카테고리 선택</option>
                    {categories.map(cat => <option key={cat} value={cat}>{cat}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label htmlFor="price">가격 (포인트)*</label>
                  <input
                    type="number"
                    id="price"
                    name="price"
                    value={bookForm.price}
                    onChange={handleInputChange}
                    placeholder="도서 가격을 입력하세요 (포인트)"
                    min="0"
                    required
                  />
                </div>
                <button type="submit" className="submit-button" disabled={submitLoading || !bookForm.bookId}>
                  {submitLoading ? '업데이트 중...' : '메타데이터 업데이트'}
                </button>
                <p className="status-info">도서 ID: {bookForm.bookId}, 현재 상태: {bookForm.status}</p>
              </form>
            )}

            {currentStep === 3 && (
              <div className="publish-review-section">
                <h3>최종 검토 및 출간 요청</h3>
                <div className="review-item">
                  <strong>제목:</strong> <span>{bookForm.title}</span>
                </div>
                <div className="review-item">
                  <strong>요약:</strong> <span>{bookForm.summary}</span>
                </div>
                <div className="review-item">
                  <strong>카테고리:</strong> <span>{bookForm.category}</span>
                </div>
                <div className="review-item">
                  <strong>가격:</strong> <span>{bookForm.price} 포인트</span>
                </div>
                <div className="review-item">
                  <strong>표지:</strong> <img src={bookForm.coverImageUrl} alt="Cover" className="review-cover" />
                </div>
                <p className="status-info">도서 ID: {bookForm.bookId}, 현재 상태: {bookForm.status}</p>
                {bookForm.status === 'PUBLISHED' ? (
                    <button className="submit-button" onClick={handleSubmitBook} disabled={submitLoading}>
                      {submitLoading ? '출간 요청 중...' : '출간 요청'}
                    </button>
                ) : (
                    <p className="error-message">메타데이터를 'PUBLISHED' 상태로 업데이트해야 출간 요청할 수 있습니다.</p>
                )}
              </div>
            )}
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default BookRegisterPage;