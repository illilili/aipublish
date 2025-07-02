<template>
  <v-container class="my-page-container pa-4 pa-md-8">
    <div v-if="loading" class="text-center pa-16">
      <v-progress-circular indeterminate color="primary" size="64"></v-progress-circular>
      <p class="mt-4 text-body-1">마이페이지 정보를 불러오는 중...</p>
    </div>
    
    <template v-else-if="isLoggedIn && userView">
      <v-card class="profile-dashboard pa-4 pa-md-6 mb-8" flat>
        <v-row align="center">
          <v-col cols="12" md="auto" class="text-center">
            <v-avatar color="grey-lighten-2" size="100">
              <v-icon size="60">mdi-account-circle</v-icon>
            </v-avatar>
          </v-col>
          <v-col>
            <h2 class="text-h5 font-weight-bold">{{ userView.name }}</h2>
            <p class="text-body-1 text-grey-darken-1">{{ userView.email }}</p>
            <v-chip :color="userView.isAdmin ? 'indigo' : 'teal'" class="mt-2" label small>
              {{ userView.isAdmin ? '관리자' : '일반회원' }}
            </v-chip>
          </v-col>
        </v-row>
      </v-card>

      <v-row>
        <v-col cols="12" md="6">
          <v-card flat class="pa-4">
            <v-card-title class="pa-0 pb-2">구독 정보</v-card-title>
            <v-card-text class="pa-0 text-h6">
              {{ userView.subscription ? '구독 중' : '미구독' }}
            </v-card-text>
          </v-card>
        </v-col>
        <v-col cols="12" md="6">
          <v-card flat class="pa-4">
            <v-card-title class="pa-0 pb-2">KT 고객 여부</v-card-title>
            <v-card-text class="pa-0 text-h6">
              {{ userView.isKtCustomer ? 'KT 고객님' : '해당 없음' }}
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </template>

    <div v-else class="text-center pa-16">
      <p>사용자 정보를 불러오지 못했습니다. 다시 로그인해주세요.</p>
      <v-btn to="/login" color="primary">로그인 페이지로 이동</v-btn>
    </div>
  </v-container>
</template>

<script>
import { mapState } from 'pinia';
import { useUserStore } from '@/store/user';

export default {
  name: "MyPage",
  data: () => ({
    loading: true, // 페이지 시작 시 로딩 상태로 시작
  }),
  computed: {
    // 스토어의 getter와 state를 매핑합니다.
    ...mapState(useUserStore, {
        isLoggedIn: 'isLoggedIn',
        userView: 'currentUserView', // 상세 정보
    }),
  },
  methods: {
    async loadMyPageData() {
      const userStore = useUserStore();
      if (!userStore.isLoggedIn) {
        this.$router.push('/login');
        return;
      }
      try {
        // 스토어의 상세 정보 조회 액션을 호출합니다.
        await userStore.fetchUserView();
      } catch (error) {
        console.error("마이페이지 로딩 중 에러:", error);
      } finally {
        this.loading = false; // 성공하든 실패하든 로딩 상태 종료
      }
    }
  },
  mounted() {
    // 페이지가 화면에 표시될 때 항상 최신 데이터를 불러옵니다.
    this.loadMyPageData();
  },
};
</script>

<style scoped>
.my-page-container { background-color: #f7f8fc; min-height: 100vh; }
.profile-dashboard { border: 1px solid #e0e0e0 !important; background-color: white; border-radius: 12px; }
</style>