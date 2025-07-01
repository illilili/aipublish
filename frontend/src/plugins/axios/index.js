import axios from 'axios'

export const userApi = axios.create({
  baseURL: import.meta.env.VITE_API_USER,
  headers: {
    'Content-Type': 'application/json',
  },
})

export const bookApi = axios.create({
  baseURL: import.meta.env.VITE_API_BOOK,
  headers: {
    'Content-Type': 'application/json',
  },
})

export const aiApi = axios.create({
  baseURL: import.meta.env.VITE_API_AI,
  headers: {
    'Content-Type': 'application/json',
  },
})

export const writerApi = axios.create({
  baseURL: import.meta.env.VITE_API_WRITER,
  headers: {
    'Content-Type': 'application/json',
  },
})


export default axios;