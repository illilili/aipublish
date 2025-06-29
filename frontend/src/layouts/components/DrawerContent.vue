<script setup>
import { ref } from 'vue'
import {
  VerticalNavLink,
  VerticalNavSectionTitle,
} from '@layouts'


const isMenuVisible = ref(false)
</script>

<template>
  <div
    class="hamburger-menu-container"
    @mouseleave="isMenuVisible = false"
  >
    <button
      class="hamburger-button"
      @mouseenter="isMenuVisible = true"
      aria-label="메뉴 열기"
    >
      <svg
        width="30"
        height="30"
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M4 6H20M4 12H20M4 18H20"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
        />
      </svg>
    </button>

    <Transition name="fade">
      <ul v-if="isMenuVisible" class="menu-list">
        <VerticalNavSectionTitle :item="{ heading: '작가 등록/관리' }" />
        <VerticalNavLink
          :item="{
            title: '작가 ',
            to: '/writes',
          }"
        />
        <VerticalNavLink
          :item="{
            title: '작가 목록',
            to: '/writerLists',
          }"
        />
        <VerticalNavLink
          :item="{
            title: '작가 상세',
            to: '/writerDetails',
          }"
        />
        <VerticalNavSectionTitle :item="{ heading: '회원 관리' }" />
        <VerticalNavLink
          :item="{
            title: '사용자',
            to: '/users',
          }"
        />
        <VerticalNavLink
          :item="{
            title: '포인트',
            to: '/points',
          }"
        />
        <VerticalNavLink
          :item="{
            title: '포인트 잔액 조회',
            to: '/pointBalances',
          }"
        />
        <VerticalNavLink
          :item="{
            title: '마이페이지 조회',
            to: '/viewMyPages',
          }"
        />
        <VerticalNavSectionTitle :item="{ heading: '원고 작성/출간요청' }" />
        <VerticalNavLink
          :item="{
            title: '원고',
            to: '/books',
          }"
        />
        <VerticalNavLink
          :item="{
            title: '원고 상세 정보',
            to: '/bookDetails',
          }"
        />
        <VerticalNavSectionTitle :item="{ heading: 'AI 자동화' }" />
        <VerticalNavLink
          :item="{
            title: 'AI 전자책 프로세서',
            to: '/aiBookProcessors',
          }"
        />
      </ul>
    </Transition>
  </div>
</template>

<style scoped>

.hamburger-menu-container {
  position: relative;
  display: inline-block;
}


.hamburger-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 10px;
  color: #333; 
}


.menu-list {
  position: absolute;
  top: 100%; 
  left: 0;
  background-color: white;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  list-style: none;
  padding: 8px;
  margin: 5px 0 0 0;
  min-width: 250px; 
  z-index: 1000; 
}


.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
