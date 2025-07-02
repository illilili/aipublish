import { defineStore } from 'pinia';
import axios from '@axios';

export const useBookStore = defineStore('book', {
  state: () => ({
    books: [],
    detailedBook: null,
    isLoading: false,
  }),
  getters: {
    bookList: (state) => state.books,
    bookDetails: (state) => state.detailedBook,
  },
  actions: {
    // --- 데이터 조회 (GET) ---
    async fetchBooks(userId = null) {
      this.isLoading = true;
      try {
        const params = userId ? { userId } : {};
        const response = await axios.get('/books', { params });
        const booksData = response.data?._embedded?.books || response.data;
        if (Array.isArray(booksData)) this.books = booksData;
      } finally {
        this.isLoading = false;
      }
    },
    async fetchBestsellers() {
      this.isLoading = true;
      try {
        const response = await axios.get('/books/bestsellers');
        const booksData = response.data?._embedded?.books || response.data;
        if (Array.isArray(booksData)) this.books = booksData;
      } finally {
        this.isLoading = false;
      }
    },
    async fetchPurchasedBooks(userId) {
      this.isLoading = true;
      try {
        const response = await axios.get('/books/purchased', { params: { userId } });
        const booksData = response.data?._embedded?.books || response.data;
        if (Array.isArray(booksData)) this.books = booksData;
      } finally {
        this.isLoading = false;
      }
    },
    async fetchBookDetails({ bookId, userId }) {
      this.isLoading = true;
      try {
        const response = await axios.get(`/books/${bookId}`, { params: { userId } });
        this.detailedBook = response.data;
      } finally {
        this.isLoading = false;
      }
    },
    
    // --- 데이터 생성/수정 (POST) ---
    async saveBook(payload) {
      return axios.post('/books/savebookcommand', payload);
    },
    async submitBook(payload) {
      return axios.post('/books/submitbookcommand', payload);
    },
    async updateMetadata(payload) {
      return axios.post('/books/updatemetadata', payload);
    },
    
    // --- 데이터 삭제 (DELETE) ---
    async deleteBook({ bookId, userId }) {
      return axios.delete(`/books/${bookId}`, { params: { userId } });
    },
  },
});