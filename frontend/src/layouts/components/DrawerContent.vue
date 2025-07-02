<script setup>
import {
  VerticalNavLink,
  VerticalNavSectionTitle,
} from '@layouts'

// 이전의 메뉴를 열고 닫는 스크립트는 모두 삭제합니다.
</script>

<template>
  <aside class="vertical-nav-sidebar">
    <div class="nav-header">
      <h1 class="logo-title">AI-Publish</h1>
    </div>

    <ul class="nav-menu-list">

      <VerticalNavSectionTitle :item="{ heading: '사용자 메뉴' }" />
      <VerticalNavLink :item="{ title: '마이페이지 조회', to: '/viewMyPages' }" />
      <VerticalNavLink :item="{ title: '포인트', to: '/points' }" />
      <VerticalNavLink :item="{ title: '포인트 잔액 조회', to: '/pointBalances' }" />
      <VerticalNavLink :item="{ title: '회원 목록', to: '/users' }" />

      <VerticalNavSectionTitle :item="{ heading: '작가 및 출판' }" />
      <VerticalNavLink :item="{ title: '작가 등록 신청', to: '/writer_register' }" />
      <VerticalNavLink :item="{ title: '원고', to: '/books' }" />
      <VerticalNavLink :item="{ title: '원고 상세 정보', to: '/bookDetails' }" />
      <VerticalNavLink :item="{ title: 'AI 전자책 프로세서', to: '/aiBookProcessors' }" />

      <VerticalNavSectionTitle :item="{ heading: '관리자' }" />
      <VerticalNavLink :item="{ title: '작가 신청 관리', to: '/admin/writer_management' }" />
      <VerticalNavLink :item="{ title: '도서 관리', to: '/book_mangement' }" />
      <VerticalNavLink :item="{ title: '작가 목록', to: '/writerLists' }" />
      <VerticalNavLink :item="{ title: '작가 상세', to: '/writerDetails' }" />
      <VerticalNavLink :item="{ title: '전자책 출간 제어판', to: '/publish-dashboard' }" />
      
      <VerticalNavLink :item="{ title: '작가', to: '/writes' }" />
    </ul>
  </aside>
</template>

<style scoped>
.vertical-nav-sidebar {
  position: fixed; /* 화면 스크롤과 상관없이 항상 같은 위치에 고정 */
  top: 0;
  left: 0;
  height: 100vh; /* 화면 전체 높이 */
  width: 260px; /* 사이드바 너비 */
  background-color: #ffffff; /* 배경색 */
  border-right: 1px solid #e0e0e0; /* 오른쪽에 구분선 추가 */
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05); /* 은은한 그림자 효과 */
  display: flex;
  flex-direction: column;
  z-index: 1000; /* 다른 요소들보다 위에 표시되도록 설정 */
}

.nav-header {
  padding: 1.5rem;
  border-bottom: 1px solid #e0e0e0;
  text-align: center;
}

.logo-title {
  font-size: 1.5rem;
  font-weight: 700;
  color: #333;
  margin: 0;
}

.nav-menu-list {
  list-style: none;
  padding: 1rem;
  margin: 0;
  overflow-y: auto; /* 메뉴가 길어지면 스크롤 생성 */
  flex-grow: 1;
}
</style>