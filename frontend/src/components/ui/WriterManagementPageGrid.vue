<template>
  <v-container>
    <v-card>
      <v-card-title class="d-flex align-center">
        <v-icon left>mdi-account-group-outline</v-icon>
        작가 신청 관리
        <v-spacer></v-spacer>
        <v-text-field
          v-model="adminId"
          label="관리자 ID"
          dense
          hide-details
          variant="solo"
          class="me-4"
          style="max-width: 150px;"
          placeholder="ex: 1"
        ></v-text-field>
        <v-btn color="primary" @click="fetchCandidates" :loading="loading">
          <v-icon left>mdi-magnify</v-icon>
          신청 목록 조회
        </v-btn>
      </v-card-title>

      <v-data-table
        :headers="headers"
        :items="candidates"
        :loading="loading"
        item-key="userId"
        class="elevation-1 mt-4"
      >
        <template v-slot:item.createdAt="{ item }">
          {{ new Date(item.createdAt).toLocaleString() }}
        </template>

        <template v-slot:item.actions="{ item }">
          <div v-if="item.status === 'PENDING'">
            <v-btn small color="success" class="me-2" @click="updateStatus(item.userId, 'APPROVED')">
              승인
            </v-btn>
            <v-btn small color="error" @click="updateStatus(item.userId, 'REJECTED')">
              반려
            </v-btn>
          </div>
          <v-chip v-else :color="item.status === 'APPROVED' ? 'green' : 'red'" dark small>
            {{ item.status }}
          </v-chip>
        </template>
      </v-data-table>
    </v-card>
    
    <v-snackbar v-model="snackbar.show" :color="snackbar.color" :timeout="3000" location="top">
      {{ snackbar.text }}
    </v-snackbar>
  </v-container>
</template>

<script>
import axios from '@axios';

export default {
  name: 'WriterManagementPage',
  data() {
    return {
      loading: false,
      adminId: '2', // 테스트용 관리자 ID (실제로는 로그인 정보에서 가져와야 함)
      candidates: [],
      snackbar: { show: false, text: '', color: 'success' },
      headers: [
        { title: '신청자 ID', value: 'userId', sortable: true },
        { title: '이름', value: 'name', sortable: true },
        { title: '이메일', value: 'email' },
        { title: '소개', value: 'bio', width: '30%' },
        { title: '신청일', value: 'createdAt', sortable: true },
        { title: '상태', value: 'status', sortable: true },
        { title: '작업', value: 'actions', sortable: false, align: 'end' },
      ],
    };
  },
  methods: {
    /**
     * 작가 신청 목록을 조회합니다.
     * API: GET /admin/writers?adminId={userid}
     */
    async fetchCandidates() {
      if (!this.adminId) {
        this.showSnackbar('관리자 ID를 입력해주세요.', 'warning');
        return;
      }
      this.loading = true;
      try {
        const response = await axios.get('/admin/writers', {
          params: { adminId: this.adminId }
        });
        this.candidates = response.data;
        this.showSnackbar('작가 신청 목록을 성공적으로 조회했습니다.', 'success');
      } catch (error) {
        this.handleError(error, '목록 조회');
      } finally {
        this.loading = false;
      }
    },

    /**
     * 작가의 상태(승인/반려)를 변경합니다.
     * API: PATCH /admin/writers/{writerid}/status?adminId={userid}
     */
    async updateStatus(candidateId, newStatus) {
      if (!this.adminId) {
        this.showSnackbar('관리자 ID를 입력해주세요.', 'warning');
        return;
      }
      
      const payload = { status: newStatus };
      const params = { adminId: this.adminId };

      try {
        await axios.patch(`/admin/writers/${candidateId}/status`, payload, { params });
        this.showSnackbar(`신청자(ID: ${candidateId})의 상태가 ${newStatus}로 변경되었습니다.`, 'success');
        
        // 목록을 새로고침하여 변경된 상태를 즉시 반영합니다.
        this.fetchCandidates();
      } catch (error) {
        this.handleError(error, '상태 변경');
      }
    },

    // 공통 에러 및 스낵바 처리
    handleError(error, context) {
      console.error(`${context} 실패:`, error);
      const message = error.response?.data?.message || `${context} 중 오류가 발생했습니다.`;
      this.showSnackbar(message, 'error');
    },
    showSnackbar(text, color) {
      this.snackbar.text = text;
      this.snackbar.color = color;
      this.snackbar.show = true;
    },
  },
  mounted() {
    // 페이지가 로드될 때 자동으로 목록을 불러오고 싶다면 이 메소드를 호출합니다.
    // this.fetchCandidates();
  },
};
</script>