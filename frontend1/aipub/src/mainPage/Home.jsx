// src/pages/Home.js
import React from "react";
// 올바른 경로로 컴포넌트를 임포트합니다.
// Header는 상위 폴더인 components가 아닌 pages 폴더에 있는 경우의 임포트 경로가 Header가 아닌 components/Header가 되어야 합니다.
// 현재 Home.js가 src/pages 폴더에 있고, Header.js가 src/components 폴더에 있다면 아래처럼 바꿔야 합니다.
import Header from "./Header"; // 이 경로가 올바른지 확인
import HeroSection from "./HeroSection"; // 이 경로가 올바른지 확인
import BookCard from "./BookCard"; // 이 경로가 올바른지 확인
import Footer from "./Footer"; // 이 경로가 올바른지 확인
import './Home.css'; // Home 페이지 전용 스타일

const Home = () => {
  const books = [
    { id: 1, title: "안녕이라 그랬어", subtitle: "지금 우리에게 필요한 김애란식의 인사", image: "https://image.yes24.com/goods/124311848/L" },
    { id: 2, title: "마침내 특이점이 시작되다", subtitle: "인공지능을 예언한 미래학자", image: "https://image.yes24.com/goods/125345790/L" },
    { id: 3, title: "밤새들의 도시", subtitle: "2024 톨스토이문학상 수상작 김주혜 신작", image: "https://image.yes24.com/goods/125345474/L" },
    { id: 4, title: "인생의 컨닝 페이퍼", subtitle: "수천 건의 사건에서 찾아낸 인생 답안지", image: "https://image.yes24.com/goods/125740421/L" },
    { id: 5, title: "파친코", subtitle: "이민진 작가의 감동적인 가족 서사", image: "https://contents.kyobobook.co.kr/sih/fit-in/450x0/IMAGE/BOOK/IMAGE2/KOR/424/KOR9791190412424.jpg" },
    { id: 6, title: "아몬드", subtitle: "손원평 작가의 베스트셀러 소설", image: "https://contents.kyobobook.co.kr/sih/fit-in/450x0/IMAGE/BOOK/IMAGE2/KOR/080/KOR9791160408008.jpg" },
    { id: 7, title: "불편한 편의점", subtitle: "김호연 작가의 따뜻한 힐링 소설", image: "https://contents.kyobobook.co.kr/sih/fit-in/450x0/IMAGE/BOOK/IMAGE2/KOR/307/KOR9791165343075.jpg" },
    { id: 8, title: "죽은 자의 집 청소", subtitle: "김새별 작가의 특별한 직업 이야기", image: "https://contents.kyobobook.co.kr/sih/fit-in/450x0/IMAGE/BOOK/IMAGE2/KOR/138/KOR9791190038138.jpg" },
  ];

  return (
    <div className="home-page-wrapper">
      <Header />
      <main>
        <HeroSection
          title="AI와 함께, 당신의 이야기를 책으로"
          imageUrl="https://images.unsplash.com/photo-1456324504439-367cee3b3c32?q=80&w=2070&auto=format&fit=crop"
        />

        <section className="books-section container py-8">
          <h2 className="section-title text-center mb-6">추천 도서</h2>
          <div className="book-grid">
            {books.map((book) => (
              <BookCard key={book.id} book={book} />
            ))}
          </div>
        </section>

        {/* 추가 섹션 (예: 신간, 베스트셀러 등) */}
        <section className="books-section container py-8">
            <h2 className="section-title text-center mb-6">인기 도서</h2>
            <div className="book-grid">
                {books.slice(4, 8).map((book) => ( // 예시로 4개 더 추가
                <BookCard key={book.id} book={book} />
                ))}
            </div>
        </section>

      </main>
      <Footer />
    </div>
  );
};

export default Home;