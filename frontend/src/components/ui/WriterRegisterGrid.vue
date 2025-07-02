<template>
  <div class="application-background">
    <v-container class="application-container">
      <div v-if="!user" class="text-center pa-16">
        <p>작가 신청을 위해 로그인이 필요합니다.</p>
        <v-btn to="/login" color="primary" class="mt-4">로그인 하러 가기</v-btn>
      </div>

      <div v-else-if="isLoading" class="text-center pa-16">
        <v-progress-circular indeterminate color="primary" size="64"></v-progress-circular>
        <p class="mt-4">신청 상태를 확인하는 중...</p>
      </div>
      
      <v-scroll-y-transition v-else appear>
        <v-card class="application-panel" flat>
          <v-card-text class="pa-md-10 pa-6">
            <div v-if="applicationStatus === 'APPROVED'" class="text-center">
              <v-icon color="success" size="56" class="mb-4">mdi-check-decagram</v-icon>
              <h1 class="text-h4 font-weight-bold">승인 완료</h1>
              <p class="text-body-1 text-grey-darken-1 mt-2">축하합니다! AIPublish의 작가로 활동하실 수 있습니다.</p>
            </div>
            <div v-else-if="applicationStatus === 'PENDING'" class="text-center">
              <v-icon color="info" size="56" class="mb-4">mdi-clock-outline</v-icon>
              <h1 class="text-h4 font-weight-bold">검토 중</h1>
              <p class="text-body-1 text-grey-darken-1 mt-2">신청서가 정상적으로 접수되었습니다. 관리자 검토 후 결과가 반영됩니다.</p>
            </div>
            <div v-else-if="applicationStatus === 'REJECTED'" class="text-center">
              <v-icon color="error" size="56" class="mb-4">mdi-close-octagon-outline</v-icon>
              <h1 class="text-h4 font-weight-bold">신청 반려</h1>
              <v-btn color="primary" class="mt-6" @click="applicationStatus = null">다시 신청하기</v-btn>
            </div>
            
            <div v-else>
              <div class="text-center mb-8">
                <v-icon color="primary" size="56" class="mb-4">mdi-feather</v-icon>
                <h1 class="text-h4 font-weight-bold">작가 등록 신청</h1>
              </div>
              <v-form @submit.prevent="submitApplication">
                <div class="section-title">기본 정보</div>
                <v-row>
                  <v-col cols="12" sm="6"><v-text-field :model-value="user.name" label="이름" variant="outlined" readonly></v-text-field></v-col>
                  <v-col cols="12" sm="6"><v-text-field :model-value="user.email" label="이메일" variant="outlined" readonly></v-text-field></v-col>
                </v-row>
                <div class="section-title mt-6">작가 정보</div>
                <v-textarea v-model="form.introduction" label="작가 소개*" variant="outlined" rows="5" :rules="[rules.required]"></v-textarea>
                <v-checkbox v-model="form.agreement" :rules="[rules.required]" label="제출된 정보는 작가 등록 심사를 위해서만 사용되는 것에 동의합니다."></v-checkbox>
                <v-btn :loading="isSubmitting" :disabled="!isFormValid" type="submit" color="primary" size="x-large" block class="mt-6">신청하기</v-btn>
              </v-form>
            </div>
          </v-card-text>
        </v-card>
      </v-scroll-y-transition>
    </v-container>
    <v-snackbar v-model="snackbar.show" :color="snackbar.color" :timeout="4000" location="top">{{ snackbar.text }}</v-snackbar>
  </div>
</template>

<script>
import axios from '@axios'; // axios 인스턴스 경로

export default {
  name: 'AuthorApplicationDirect',
  data() {
    return {
      user: null, // 로그인 사용자 정보
      applicationStatus: null, // 'PENDING', 'APPROVED', 'REJECTED'
      isLoading: true, // 페이지 초기 로딩 상태
      isSubmitting: false, // 제출 버튼 로딩 상태
      form: {
        introduction: '',
        agreement: false,
      },
      snackbar: { show: false, text: '', color: 'success' },
      rules: {
        required: value => !!value || '필수 항목입니다.',
      },
    };
  },
  computed: {
    isFormValid() {
      return this.form.introduction && this.form.agreement;
    },
  },
  methods: {
    /**
     * 현재 사용자의 작가 신청 상태를 조회합니다.
     */
    async checkApplicationStatus() {
      this.isLoading = true;
      try {
        const response = await axios.get(`/writer-candidates/${this.user.id}`);
        if (response.data) {
          this.applicationStatus = response.data.status;
        }
      } catch (error) {
        if (error.response && error.response.status === 404) {
          this.applicationStatus = null; // 신청 내역 없음
        } else {
          this.showSnackbar('신청 상태를 불러오는 중 오류가 발생했습니다.', 'error');
        }
      } finally {
        this.isLoading = false;
      }
    },

    /**
     * 작가 등록 신청서를 제출합니다.
     */
    async submitApplication() {
      if (!this.isFormValid) return;
      this.isSubmitting = true;

      // 백엔드에 맞춰 payload의 'bio' 키를 'introduction'으로 변경합니다.
      const payload = {
        userId: this.user.id,
        bio: this.form.introduction,
      };

      try {
        const response = await axios.post('/writers/apply', payload);
        this.showSnackbar('작가 등록 신청이 정상적으로 접수되었습니다.', 'success');
        this.applicationStatus = response.data.status; // 'PENDING'
      } catch (e) {
        const errorMessage = e.response?.data?.message || '신청 중 오류가 발생했습니다. 개발자 콘솔을 확인해주세요.';
        this.showSnackbar(errorMessage, 'error');
        console.error("작가 신청 실패:", e.response || e);
      } finally {
        this.isSubmitting = false;
      }
    },

    showSnackbar(text, color = 'success') {
      this.snackbar.text = text;
      this.snackbar.color = color;
      this.snackbar.show = true;
    },
  },

  /**
   * 컴포넌트 생성 시 localStorage에서 사용자 정보를 직접 가져옵니다.
   */
  created() {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      this.user = JSON.parse(storedUser);
      // 로그인 정보가 있으면, 작가 신청 상태를 바로 확인합니다.
      this.checkApplicationStatus();
    } else {
      this.isLoading = false; // 로그인 정보가 없으면 로딩 종료
    }
  }
}
</script>



<style scoped>
/* 이전 답변의 스타일과 동일 */
.application-background { background-color: #f7f8fc; min-height: 100vh; padding: 48px 0; }
.application-container { max-width: 800px; }
.application-panel { border-radius: 12px; border: 1px solid #e0e0e0; }
.section-title { font-size: 1.1rem; font-weight: 600; margin-bottom: 1rem; padding-bottom: 0.5rem; border-bottom: 1px solid #eee; color: #333; }
</style>