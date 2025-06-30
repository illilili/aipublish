<template>
  <div class="application-background">
    <v-container class="application-container">
      <v-scroll-y-transition appear>
        <v-card class="application-panel" flat>
          <v-card-text class="pa-md-10 pa-6">
            <div class="text-center mb-8">
              <v-icon color="primary" size="56" class="mb-4">mdi-feather</v-icon>
              <h1 class="text-h4 font-weight-bold">작가 등록 신청</h1>
              <p class="text-body-1 text-grey-darken-1 mt-2">AIPublish의 작가가 되어 당신의 이야기를 세상에 공유하세요.</p>
            </div>

            <v-form ref="applicationForm" @submit.prevent="submitApplication">
              <div class="section-title">기본 정보</div>
              <v-row>
                <v-col cols="12" sm="6">
                  <v-text-field
                    v-model="form.name" label="이름"
                    variant="outlined"
                    prepend-inner-icon="mdi-account-outline"
                    :rules="[rules.required]" ></v-text-field>
                </v-col>
                <v-col cols="12" sm="6">
                  <v-text-field
                    v-model="form.email" label="이메일"
                    variant="outlined"
                    prepend-inner-icon="mdi-email-outline"
                    :rules="[rules.required, rules.email]" ></v-text-field>
                </v-col>
              </v-row>
              
              <div class="section-title mt-6">작가 정보</div>
              <v-text-field
                v-model="form.penName" label="필명 (작가명)"
                placeholder="사용하실 필명을 입력해주세요."
                variant="outlined"
                prepend-inner-icon="mdi-pen"
                :rules="[rules.required]"
                class="mb-4"
              ></v-text-field>
              
              <v-textarea
                v-model="form.bio"
                label="작가 소개"
                placeholder="자신에 대해 자유롭게 소개해주세요. (주요 관심사, 글쓰기 스타일 등)"
                variant="outlined"
                rows="5"
                counter
                :rules="[rules.required]"
                class="mb-4"
              ></v-textarea>

              <v-checkbox
                v-model="form.agreement"
                :rules="[rules.requiredAgreement]" label="제출된 정보는 작가 등록 심사를 위해서만 사용되는 것에 동의합니다."
              ></v-checkbox>

              <v-btn
                :loading="loading"
                type="submit"
                color="primary"
                size="x-large"
                block
                class="mt-6 submit-btn"
              >
                신청하기
              </v-btn>
            </v-form>
          </v-card-text>
        </v-card>
      </v-scroll-y-transition>
    </v-container>

    <v-snackbar v-model="snackbar.show" :color="snackbar.color" :timeout="4000" location="top">
      {{ snackbar.text }}
    </v-snackbar>
  </div>
</template>

<script>
import axios from 'axios'; // Axios 임포트

export default {
  name: 'AuthorApplication',
  data() {
    return {
      user: {
        name: '초기값_이름',
        email: '초기값_이메일@example.com',
      },
      form: {
        name: '',
        email: '',
        penName: '',
        bio: '',
        agreement: false,
      },
      loading: false,
      snackbar: { show: false, text: '', color: 'success' },
      rules: {
        required: value => !!value || '필수 항목입니다.',
        email: value => {
          const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
          return pattern.test(value) || '유효한 이메일 주소를 입력해주세요.';
        },
        requiredAgreement: value => !!value || '약관에 동의해야 신청할 수 있습니다.',
      },
    };
  },
  computed: {
    // isFormValid는 더 이상 버튼의 disabled 속성에는 직접 사용되지 않습니다.
    // 대신 v-form의 validate() 메서드를 사용합니다.
  },
  created() {
    this.form.name = this.user.name;
    this.form.email = this.user.email;
  },
  methods: {
    async submitApplication() {
      const { valid } = await this.$refs.applicationForm.validate();

      if (!valid) {
        this.showSnackbar('모든 필수 항목을 올바르게 입력해주세요.', 'error');
        return;
      }
      
      this.loading = true;

      const applicationData = {
        name: this.form.penName,
        email: this.form.email,
        bio: this.form.bio,
      };
      
      console.log('Submitting Application to Backend:', applicationData);

      try {
        // --- 이 부분만 수정되었습니다 ---
        // 이제 Vite 개발 서버의 프록시가 이 요청을 백엔드로 전달합니다.
        const backendUrl = '/writes'; 
        // ---------------------------------
        
        const response = await axios.post(backendUrl, applicationData);

        if (response.status === 200 || response.status === 201) {
          this.showSnackbar('작가 등록 신청이 완료되었습니다. 관리자 검토 후 결과를 알려드립니다.');
          console.log('Application submitted successfully:', response.data);

          setTimeout(() => {
            this.$router.push('/viewMyPages');
          }, 2000);
        } else {
          this.showSnackbar(`신청 실패: ${response.status} ${response.statusText}`, 'error');
          console.error('Application submission failed with status:', response.status, response.data);
        }
      } catch (error) {
        console.error('Error submitting application:', error);
        // 에러 핸들링은 이전 답변에서 이미 개선되었으므로 그대로 둡니다.
        if (error.response) {
          console.error('Error response data:', error.response.data);
          console.error('Error response status:', error.response.status);
          this.showSnackbar(`신청 실패: ${error.response.data.message || error.response.statusText || '서버 오류'}`, 'error');
        } else if (error.request) {
          console.error('Error request:', error.request);
          this.showSnackbar('신청 실패: 서버에 연결할 수 없습니다. (CORS 또는 네트워크 문제일 수 있습니다)', 'error');
        } else {
          console.error('Error message:', error.message);
          this.showSnackbar(`신청 실패: ${error.message || '알 수 없는 오류'}`, 'error');
        }
      } finally {
        this.loading = false;
      }
    },
    showSnackbar(text, color = 'success') {
      this.snackbar.text = text;
      this.snackbar.color = color;
      this.snackbar.show = true;
    },
  },
};
</script>

<style scoped>
/* 기존 스타일 유지 */
.application-background {
  background-color: #f7f8fc;
  min-height: 100vh;
  padding: 48px 0;
}

.application-container {
  max-width: 800px;
}

.application-panel {
  border-radius: 12px;
  border: 1px solid #e0e0e0;
}

.section-title {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid #eee;
  color: #333;
}

.submit-btn {
  font-weight: 700;
  letter-spacing: 0.5px;
}
</style>
