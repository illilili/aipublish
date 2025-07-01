import { defineStore } from 'pinia';
import axios from '@axios';

export const useUserStore = defineStore('user', {
  state: () => ({
    user: JSON.parse(localStorage.getItem('user')) || null,
    // ✅ 마이페이지의 상세 정보를 저장할 별도 state 추가
    // 이 객체에 name, email, isKtCustomer, subscription 등 모든 정보가 담깁니다.
    userView: null, 
  }),
  getters: {
    isLoggedIn: (state) => !!state.user,
    currentUser: (state) => state.user,
    // ✅ 마이페이지 뷰 데이터를 반환하는 getter
    currentUserView: (state) => state.userView,
  },
  actions: {
    async register(payload) {
      return axios.post('/users/registeruser', payload);
    },
    async login(credentials) {
      try {
        const response = await axios.post('/users/login', credentials);
        this.user = response.data; // 로그인 시 기본 정보 저장
        localStorage.setItem('user', JSON.stringify(this.user));
        return true;
      } catch (error) {
        this.logout();
        throw error;
      }
    },
    logout() {
      this.user = null;
      this.userView = null; // 로그아웃 시 상세 정보도 초기화
      localStorage.removeItem('user');
    },

    /**
     * ✅ [핵심 추가] 마이페이지 상세 정보를 서버에서 가져와 state를 업데이트합니다.
     */
    async fetchUserView() {
      if (!this.user || !this.user.id) {
        console.error("마이페이지 조회 실패: 로그인된 사용자 ID가 없습니다.");
        return;
      }
      try {
        // 백엔드의 마이페이지 조회 API(/users/{id}/views)를 호출합니다.
        const response = await axios.get(`/users/${this.user.id}/views`);
        // 서버에서 받은 최신 상세 정보로 userView state를 업데이트합니다.
        this.userView = response.data;
      } catch (error) {
        console.error("마이페이지 상세 정보 조회 실패:", error);
      }
    },
  },
});