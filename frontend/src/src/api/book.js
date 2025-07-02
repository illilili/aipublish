// src/
//   api/book.js
//   pages/PublishDashboard.vue
//   components/BookRow.vue (optional)
//   plugins/axios/index.js

import api from '/workspace/aipublish1/frontend/src/plugins/axios/index.js'

export const getMyBooks = (userId) => api.get(`/books?userId=${userId}`)
export const publishBook = (id) => api.post(`/books/${id}/publish`)
export const deleteBook = (id, userId) => api.delete(`/books/${id}?userId=${userId}`);

export const updateBook = (data) => api.post('/books/updatemetadata', data)
