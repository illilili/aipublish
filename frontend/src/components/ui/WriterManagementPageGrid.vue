<template>
  <v-container>
    <div v-if="!initialCheckDone" class="text-center pa-16">
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
        <v-icon start>mdi-account-group-outline</v-icon>
        작가 신청 관리
        <v-spacer></v-spacer>

        <v-btn
          color="primary"
          @click="fetchCandidates"
          :loading="loading"
          prepend-icon="mdi-refresh"
        >
          새로고침
        </v-btn>

      </v-card-title>
      
      <v-tabs v-model="currentTab" color="primary">
        <v-tab value="PENDING">승인 대기</v-tab>
        <v-tab value="APPROVED">승인 완료</v-tab>
        <v-tab value="REJECTED">반려 내역</v-tab>
      </v-tabs>
      <v-divider />

      <v-data-table
        :headers="headers"
        :items="candidates"
        :loading="loading"
        item-value="id"
        class="elevation-1"
      >
        <template v-slot:item.createdAt="{ item }">
          {{ item.createdAt ? new Date(item.createdAt).toLocaleString() : '-' }}
        </template>
        <template v-slot:item.status="{ item }">
           <v-chip :color="getStatusColor(item.status)" dark small>
             {{ item.status }}
           </v-chip>
        </template>
        <template v-slot:item.actions="{ item }">
          <div v-if="item.status === 'PENDING'">
            <v-btn small color="success" class="me-2" @click="updateStatus(item.id, 'APPROVED')">승인</v-btn>
            <v-btn small color="error" @click="updateStatus(item.id, 'REJECTED')">반려</v-btn>
          </div>
          <span v-else>-</span>
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
      currentTab: 'PENDING',
      candidates: [],
      initialCheckDone: false,
      loading: false,
      snackbar: { show: false, text: '', color: 'success' },
      headers: [
        { title: '신청자 ID', align: 'start', key: 'userId' },
        { title: '이름', key: 'name' },
        { title: '이메일', key: 'email' },
        { title: '소개', key: 'bio', width: '30%' },
        { title: '신청일', key: 'createdAt' },
        { title: '상태', key: 'status' },
        { title: '작업', key: 'actions', align: 'end', sortable: false },
      ],
    };
  },
  computed: {
    ...mapState(useUserStore, {
      user: 'currentUser',
      isAdmin: (store) => store.currentUser?.isAdmin || false,
    }),
  },
  watch: {
    // ✅ [수정] 탭이 변경되면 데이터를 다시 불러오도록 로직을 추가했습니다.
    currentTab() {
      if (this.isAdmin) {
        this.fetchCandidates();
      }
    },
  },
  methods: {
    // ✅ [수정_taehwan(250703)] 기본 v-data-table에 맞춰 파라미터를 제거했던 것을 복구합니다.
    async fetchCandidates() {
      console.log('fetchCandidates 함수 호출됨. isAdmin 상태:', this.isAdmin);
      if (!this.isAdmin) return;

      this.loading = true;
      try {
        const response = await axios.get('/admin/writers', {
          // ✅ [수정_태환(250703)] params에 adminId를 다시 추가합니다.
          params: { 
            status: this.currentTab,
            adminId: this.user.id // 현재 로그인한 관리자 ID
          },
        });
        
        this.candidates = response.data.content || response.data;

      } catch (error) {
        this.handleError(error, '목록 조회');
      } finally {
        this.loading = false;
      }
    },
    async updateStatus(candidateId, newStatus) {
      const payload = { status: newStatus };
      try {
        // ✅ [수정_태환(250703)] axios.patch 요청에 params 옵션을 추가하여 adminId를 전송합니다.
        await axios.patch(`/admin/writers/${candidateId}/status`, payload, {
          params: {
            adminId: this.user.id // 현재 로그인한 관리자 ID
          }
        });

        this.showSnackbar(`신청(ID: ${candidateId})이 성공적으로 처리되었습니다.`, 'success');
        this.fetchCandidates(); // 목록 새로고침
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
    getStatusColor(status) {
      if (status === 'APPROVED') return 'green';
      if (status === 'REJECTED') return 'red';
      return 'orange';
    }
  },
  created() {
    const userStore = useUserStore();
    if (!userStore.isLoggedIn) {
      alert("로그인이 필요합니다.");
      this.$router.push('/login');
    } else {
      console.log('created 훅 실행됨. isAdmin 상태:', this.isAdmin);
      this.initialCheckDone = true;
      
      // ✅ [추가] 관리자일 경우, 컴포넌트 생성 시 데이터를 직접 호출합니다.
      if (this.isAdmin) {
        this.fetchCandidates();
      }
    }
  },
};
</script>