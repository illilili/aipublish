<template>
  <div class="dashboard-container">
    <h2 class="dashboard-title">전자책 출간 제어판</h2>
    <div class="table-card">
      <div v-if="loading" class="loading-text">로딩 중...</div>
      <table v-else>
        <thead>
          <tr>
            <th>제목</th><th>표지</th><th>요약</th>
            <th>상태</th><th>출간요청</th><th>관리</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="book in books" :key="book.bookId">
            <td>{{ book.title }}</td>
            <td>
              <img :src="book.coverUrl" alt="cover" width="60" v-if="book.coverUrl"/>
              <span v-else>없음</span>
            </td>
            <td>
              <button @click="showSummary(book)">미리보기</button>
            </td>
            <td>
              <span :class="['badge', book.status]">{{ book.status }}</span>
            </td>
            <td>
              <button 
                :disabled="book.status !== '임시저장'" 
                @click="requestPublish(book.bookId)">
                출간요청
              </button>
            </td>
            <td>
              <button @click="editBook(book)">수정</button>
              <button @click="removeBook(book.bookId)">삭제</button>
            </td>
          </tr>
          <tr v-if="books.length === 0">
            <td colspan="6" style="color:#aaa">등록된 전자책이 없습니다.</td>
          </tr>
        </tbody>
      </table>
    </div>
    <!-- 요약 모달 -->
    <div v-if="showModal" class="modal preview-modal">
      <h3>{{ currentBook.title }} 요약</h3>
      <div class="modal-content" style="flex:1;">
        <span v-if="currentBook.summary && currentBook.summary.trim()">{{ currentBook.summary }}</span>
        <span v-else>요약이 생성되지 않았습니다.</span>
      </div>
      <button class="close-btn" @click="showModal = false">닫기</button>
    </div>
    <!-- 수정 모달 -->
    <div v-if="editModal" class="modal">
      <h3>책 정보 수정</h3>
      <input v-model="editBookData.title" placeholder="제목" />
      <textarea v-model="editBookData.content" placeholder="내용" rows="8"></textarea>
      <div style="display:flex; justify-content:flex-end; gap:10px;">
        <button @click="saveEdit">저장</button>
        <button @click="editModal=false">취소</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyBooks, publishBook, deleteBook, updateBook } from '../src/api/book.js'

const books = ref([])
const loading = ref(true)
const showModal = ref(false)
const currentBook = ref({})
const editModal = ref(false)
const editBookData = ref({})
const userId = localStorage.getItem('userId') || 'testUser'

onMounted(async () => {
  await fetchBooks()
})

const fetchBooks = async () => {
  loading.value = true
  try {
    const res = await getMyBooks(userId)
    books.value = res.data
  } catch (e) {
    alert('도서 목록 조회 실패!')
  } finally {
    loading.value = false
  }
}
const requestPublish = async (id) => {
  await publishBook(id)
  alert('출간 요청 완료!')
  await fetchBooks()
}
const removeBook = async (id) => {
  if (!id) {
    alert('책 ID가 올바르지 않습니다!');
    return;
  }
  if (confirm('정말 삭제하시겠습니까?')) {
    await deleteBook(id,userId)
    await fetchBooks()
  }
}
const showSummary = (book) => {
  currentBook.value = book
  showModal.value = true
}
const editBook = (book) => {
  editBookData.value = { ...book }
  editModal.value = true
}
const saveEdit = async () => {
  await updateBook(editBookData.value)
  editModal.value = false
  await fetchBooks()
}
</script>

<style scoped>
.dashboard-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: 90vh;
  background: #f6f7fb;
  padding-top: 40px;
}
.dashboard-title {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 30px;
}
.table-card {
  width: 90vw;
  max-width: 1100px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 24px rgba(80,100,150,0.10);
  padding: 32px 24px 32px 24px;
  margin-bottom: 24px;
}
table {
  width: 100%;
  border-collapse: collapse;
  font-size: 1.04rem;
  background: #fff;
}
th, td {
  text-align: center;
  padding: 14px 8px;
  border-bottom: 1px solid #e6e9f0;
}
th {
  background: #f6f7fb;
  font-weight: bold;
  color: #3d4663;
}
tr:last-child td { border-bottom: none; }
button {
  border: none;
  background: #e5e8fa;
  padding: 6px 15px;
  margin: 2px;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.2s;
}
button:hover {
  background: #c7e3ff;
}
.badge { padding: 3px 8px; border-radius: 8px; }
.badge.임시저장 { background: #eee; color: #555; }
.badge.출간요청중 { background: #fde68a; color: #aa8800; }
.badge.완료 { background: #d1fae5; color: #047857; }
.badge.실패 { background: #fee2e2; color: #b91c1c; }
.modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 480px;
  min-height: 280px;
  max-width: 90vw;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 32px rgba(60,80,120,0.18);
  padding: 36px 28px 28px 28px;
  z-index: 2000;
  display: flex;
  flex-direction: column;
  align-items: stretch;
}
.modal h3 {
  font-size: 1.3rem;
  font-weight: bold;
  margin-bottom: 18px;
}
.modal .modal-content {
  font-size: 1.07rem;
  color: #222;
  margin-bottom: 26px;
  word-break: break-all;
}
.modal textarea, .modal input {
  width: 100%;
  margin-top: 10px;
  padding: 10px;
  border: 1px solid #d3d9e3;
  border-radius: 8px;
  font-size: 1.04rem;
  resize: vertical;
  margin-bottom: 12px;
}
.modal button {
  margin-top: 10px;
  margin-right: 10px;
  min-width: 80px;
  padding: 8px 18px;
  font-size: 1rem;
}
.loading-text {
  font-size: 1.2rem;
  color: #8d96ae;
  padding: 44px 0 44px 0;
  text-align: center;
}
.modal.preview-modal {
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  min-height: 280px;
}
.close-btn {
  align-self: stretch;
  margin-top: auto;
  margin-bottom: 0;
}
</style>
