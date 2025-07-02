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
    // ✅ [수정] 토큰 존재 여부까지 함께 확인하여 더 정확한 로그인 상태를 반환합니다.
    isLoggedIn: (state) => !!state.user && !!localStorage.getItem('accessToken'),
    currentUser: (state) => state.user,
    // ✅ 마이페이지 뷰 데이터를 반환하는 getter
    currentUserView: (state) => state.userView,
  },
  actions: {
    async register(payload) {
      return axios.post('/users/registeruser', payload);
    },

    // ✅ [핵심 수정] 로그인 성공 시, 사용자 정보와 토큰을 분리하여 저장합니다.
    async login(credentials) {
      try {
        const response = await axios.post('/users/login', credentials);
        
        // 1. 서버 응답에서 토큰과 나머지 사용자 데이터를 분리합니다.
        const { token, ...userData } = response.data;
        
        // 2. state에는 사용자 정보만 저장합니다.
        this.user = userData;
        
        // 3. localStorage에는 사용자 정보와 토큰을 각각 저장합니다.
        localStorage.setItem('user', JSON.stringify(this.user));
        if (token) {
          localStorage.setItem('accessToken', token);
        }

        // 4. 알림창 등에서 사용할 수 있도록 사용자 정보를 반환합니다.
        return this.user;

      } catch (error) {
        this.logout();
        throw error;
      }
    },

    // ✅ [핵심 수정] 로그아웃 시, 토큰도 함께 삭제합니다.
    logout() {
      this.user = null;
      this.userView = null; // 로그아웃 시 상세 정보도 초기화
      localStorage.removeItem('user');
      localStorage.removeItem('accessToken'); // 저장된 토큰도 반드시 삭제합니다.
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