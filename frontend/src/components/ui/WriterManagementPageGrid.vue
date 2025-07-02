<template>
  <v-container>
    <div v-if="loading" class="text-center pa-16">
      <v-progress-circular indeterminate color="primary"></v-progress-circular>
    </div>
    
    <div v-else-if="!isAdmin" class="text-center pa-16">
      <v-alert type="error" variant="outlined" prominent border="start">
        <template v-slot:title>접근 권한 없음</template>
        이 페이지는 관리자만 접근할 수 있습니다.
      </v-alert>
      <v-btn to="/" class="mt-6">메인으로 돌아가기</v-btn>
    </div>
    
    <v-card v-else>
      <v-card-title class="d-flex align-center">
        <v-icon left>mdi-account-group-outline</v-icon>
        작가 신청 관리
        <v-spacer></v-spacer>
        <v-btn color="primary" @click="fetchCandidates" :loading="loading">
          <v-icon left>mdi-refresh</v-icon>
          새로고침
        </v-btn>
      </v-card-title>
      

      <v-data-table
        :headers="headers"
        :items="candidates"
        :loading="loading"
        item-key="id"
        class="elevation-1 mt-4"
      >
      
      <template v-slot:item.createdAt="{ item }">
        {{ item.createdAt ? new Date(item.createdAt).toLocaleString() : '-' }}
      </template>
        <template v-slot:item.actions="{ item }">
          <div v-if="item.status === 'PENDING'">
            <v-btn small color="success" class="me-2" @click="updateStatus(item.id, 'APPROVED')">승인</v-btn>
            <v-btn small color="error" @click="updateStatus(item.id, 'REJECTED')">반려</v-btn>
          </div>
          <v-chip v-else :color="item.status === 'APPROVED' ? 'green' : 'red'" dark small>{{ item.status }}</v-chip>
        </template>
      </v-data-table>

    </v-card>
    
    
    <v-snackbar v-model="snackbar.show" :color="snackbar.color" :timeout="3000" location="top">
      {{ snackbar.text }}
    </v-snackbar>
  </v-container>
</template>

<script>
import { mapState } from 'pinia';
import { useUserStore } from '@/store/user';
import axios from '@axios';

export default {
  name: 'WriterManagementPage',
  data() {
    return {
      loading: true, // 페이지 시작 시 로딩 상태
      candidates: [],
      snackbar: { show: false, text: '', color: 'success' },
      headers: [
       // ✅ [수정] 모든 'value' 속성을 'key'로 변경했습니다.
        { text: '신청자 ID', align: 'start', key: 'userId' },
        { text: '이름', key: 'name' },
        { text: '이메일', key: 'email' },
        { text: '소개', key: 'bio', width: '30%' },
        { text: '신청일', key: 'createdAt' },
        { text: '상태', key: 'status' },
        { text: '작업', key: 'actions', align: 'end', sortable: false },
      ],
    };
  },
  computed: {
    // ✅ Pinia 스토어에서 로그인 정보와 관리자 여부를 가져옵니다.
    ...mapState(useUserStore, {
      user: 'currentUser',
      isAdmin: (store) => store.currentUser?.isAdmin || false,
    }),
  },
  methods: {
    async fetchCandidates() {
      this.loading = true;
      try {
        // ✅ this.user.id를 관리자 ID로 사용합니다.
        const response = await axios.get('/admin/writers', {
          params: { adminId: this.user.id }
        });
        console.log('API 응답 데이터:', response.data);  // 여기서 데이터 구조 확인
        this.candidates = response.data;
      } catch (error) {
        this.handleError(error, '목록 조회');
      } finally {
        this.loading = false;
      }
    },

    async updateStatus(candidateId, newStatus) {
      const payload = { status: newStatus };
      // ✅ this.user.id를 관리자 ID로 사용합니다.
      const params = { adminId: this.user.id };

      try {
        await axios.patch(`/admin/writers/${candidateId}/status`, payload, { params });
        this.showSnackbar(`신청자(ID: ${candidateId})의 상태가 ${newStatus}로 변경되었습니다.`, 'success');
        this.fetchCandidates();
      } catch (error) {
        this.handleError(error, '상태 변경');
      }
    },

    handleError(error, context) {
      console.error(`${context} 실패:`, error);
      const message = error.response?.data?.message || `${context} 중 오류가 발생했습니다.`;
      this.showSnackbar(message, 'error');
    },
    showSnackbar(text, color) {
      this.snackbar.text = text; this.snackbar.color = color; this.snackbar.show = true;
    },
  },
  // ✅ 페이지가 생성될 때 실행되는 로직
  created() {
    const userStore = useUserStore();
    // 1. 로그인 여부 확인
    if (!userStore.currentUser) {
      alert("로그인이 필요합니다.");
      this.$router.push('/login');
      return;
    }
    // 2. 관리자 권한 확인 후, 목록 자동 조회
    if (userStore.currentUser.isAdmin) {
      this.fetchCandidates();
    } else {
      this.loading = false; // 권한이 없으면 로딩 중단
    }
  },
};
</script>