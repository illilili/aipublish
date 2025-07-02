/* eslint-disable import/order */
import '@/@iconify/icons-bundle'
import App from '@/App.vue'
import vuetify from '@/plugins/vuetify'
import { loadFonts } from '@/plugins/webfontloader'
import router from '@/router'
import '@/styles/styles.scss'
import '@core/scss/index.scss'
import { createPinia } from 'pinia'
import { createApp } from 'vue'
import axios from 'axios'; // ✅ 이 axios 인스턴스를 사용합니다.
import { Icon } from '@iconify/vue';
import { VDataTable } from 'vuetify/labs/VDataTable'

loadFonts()

// ==================================================
// ✅ [핵심 추가] Axios 요청 인터셉터 설정
// 모든 API 요청을 가로채서, 인증이 필요한 경우 토큰을 헤더에 담아 보냅니다.
// ==================================================
axios.interceptors.request.use(config => {
  // 요청을 보내기 전에 localStorage에서 토큰을 가져옵니다.
  const token = localStorage.getItem('accessToken');
  
  // 공개 API 목록 (이 API들은 토큰 없이 요청됩니다)
  const publicApis = ['/users/registeruser', '/users/login'];

  // 토큰이 존재하고, 요청하는 API가 공개 API 목록에 포함되지 않을 경우에만 헤더를 추가합니다.
  if (token && !publicApis.includes(config.url)) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  
  return config;
}, error => {
  return Promise.reject(error);
});
// ==================================================


// Create vue app
const app = createApp(App)

// Setting Config
axios.defaults.baseURL = '';
app.config.globalProperties.$axios = axios;

// Component
app.component('Icon',Icon)

// Use plugins
app.use(vuetify)
app.use(createPinia())
app.use(router)

// Mount vue app
app.mount('#app')

//v-data table package
app.component('VDataTable', VDataTable)
