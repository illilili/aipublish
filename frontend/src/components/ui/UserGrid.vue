<template>
  <v-container fluid class="approval-page-container pa-4 pa-md-6">
    <v-card class="pa-4" flat>
      <v-card-title class="d-flex align-center pa-2">
        <h2 class="text-h5 font-weight-bold">출판 요청 관리</h2>
        <v-spacer></v-spacer>
        <v-text-field
          v-model="search"
          label="제목 또는 작가명 검색"
          prepend-inner-icon="mdi-magnify"
          variant="outlined"
          density="compact"
          hide-details
          style="max-width: 300px;"
        ></v-text-field>
      </v-card-title>

      <v-tabs v-model="activeTab" color="primary" class="mt-4">
        <v-tab value="pending">
          <v-badge color="error" :content="pendingRequests.length" inline>
            대기중인 요청
          </v-badge>
        </v-tab>
        <v-tab value="approved">승인한 내역</v-tab>
        <v-tab value="rejected">거부한 내역</v-tab>
      </v-tabs>

      <v-window v-model="activeTab">
        <v-window-item value="pending">
          <v-data-table
            :headers="headers"
            :items="pendingRequests"
            :search="search"
            class="elevation-1 mt-4"
            items-per-page-text="페이지 당 항목 수"
          >
            <template v-slot:item.status="{ item }">
              <v-chip :color="getStatusColor(item.status)" variant="tonal" size="small">{{ item.status }}</v-chip>
            </template>
            <template v-slot:item.actions="{ item }">
              <v-btn color="primary" variant="tonal" size="small" @click="openReviewDialog(item)">
                검토하기
              </v-btn>
            </template>
          </v-data-table>
        </v-window-item>
        <v-window-item value="approved">
          <v-data-table :headers="headers" :items="approvedRequests" :search="search" class="elevation-1 mt-4"></v-data-table>
        </v-window-item>
        <v-window-item value="rejected">
          <v-data-table :headers="headers" :items="rejectedRequests" :search="search" class="elevation-1 mt-4"></v-data-table>
        </v-window-item>
      </v-window>
    </v-card>

    <v-dialog v-model="reviewDialog" max-width="900px" transition="dialog-bottom-transition">
      <v-card v-if="selectedRequest">
        <v-toolbar color="primary" dark>
          <v-toolbar-title class="font-weight-bold">출판 요청 검토</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn icon @click="closeDialog"><v-icon>mdi-close</v-icon></v-btn>
        </v-toolbar>
        
        <v-card-text class="pa-6">
          <v-row>
            <v-col cols="12" md="4" class="text-center">
              <v-img :src="selectedRequest.coverUrl" class="rounded-lg elevation-4 mx-auto" max-height="400" aspect-ratio="2/3"></v-img>
            </v-col>
            <v-col cols="12" md="8">
              <h3 class="text-h5 font-weight-bold mb-1">{{ selectedRequest.title }}</h3>
              <p class="text-body-1 text-grey-darken-1 mb-4">
                <v-icon small class="mr-1">mdi-account-edit</v-icon>
                {{ selectedRequest.author }}
              </p>
              
              <v-chip small class="mb-4" label color="info">{{ selectedRequest.genre }}</v-chip>
              
              <div class="text-subtitle-1 font-weight-bold mb-2">작품 소개</div>
              <v-sheet color="grey-lighten-4" class="pa-4 rounded-lg" min-height="150px">
                <p class="text-body-2" style="white-space: pre-wrap;">{{ selectedRequest.synopsis }}</p>
              </v-sheet>
            </v-col>
          </v-row>
        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions class="pa-4">
          <div class="text-caption text-grey">요청일: {{ selectedRequest.requestDate }}</div>
          <v-spacer></v-spacer>
          <v-btn
            size="large"
            variant="flat"
            color="error"
            @click="rejectRequest(selectedRequest.id)"
          >
            <v-icon left>mdi-close-circle-outline</v-icon>
            거부
          </v-btn>
          <v-btn
            size="large"
            variant="flat"
            color="success"
            class="ml-2"
            @click="approveRequest(selectedRequest.id)"
          >
            <v-icon left>mdi-check-circle-outline</v-icon>
            승인
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
export default {
  name: 'PublicationApproval',
  data: () => ({
    search: '',
    activeTab: 'pending',
    reviewDialog: false,
    selectedRequest: null,
    headers: [
      { title: '요청일', key: 'requestDate', align: 'start' },
      { title: '책 제목', key: 'title' },
      { title: '작가명', key: 'author' },
      { title: '장르', key: 'genre' },
      { title: '상태', key: 'status' },
      { title: '관리', key: 'actions', sortable: false, align: 'center' },
    ],
    // --- API 연동 전, 화면 구성을 위한 목업(mockup) 데이터 ---
    requests: [
      { id: 1, requestDate: '2025-06-29', title: "AI 시대의 글쓰기", author: "김작가", genre: "IT/기술", status: '대기중', coverUrl: "https://images.unsplash.com/photo-1589998059171-988d887df646?q=80&w=2070&auto=format&fit=crop", synopsis: "인공지능과 함께하는 새로운 글쓰기 패러다임을 제시합니다. ChatGPT와 같은 도구를 활용하여 창의력을 극대화하는 방법을 다룹니다." },
      { id: 2, requestDate: '2025-06-28', title: "새벽의 바다", author: "이소설", genre: "소설", status: '대기중', coverUrl: "https://images.unsplash.com/photo-1509021436665-8f07dbf5bf1d?q=80&w=1974&auto=format&fit=crop", synopsis: "삶의 끝에서 희망을 발견하는 한 남자의 여정을 그린 감동적인 소설. 제주도의 아름다운 풍광을 배경으로 펼쳐집니다." },
      { id: 3, requestDate: '2025-06-27', title: "Vue.js 완전 정복", author: "박개발", genre: "IT/기술", status: '승인됨', coverUrl: "https://images.unsplash.com/photo-1633356122544-f134324a6cee?q=80&w=2070&auto=format&fit=crop", synopsis: "Vue 3의 Composition API부터 Pinia를 활용한 상태 관리까지, 실무 예제로 배우는 Vue.js의 모든 것." },
      { id: 4, requestDate: '2025-06-26', title: "잊혀진 왕국", author: "최역사", genre: "역사", status: '거부됨', coverUrl: "https://images.unsplash.com/photo-1563291074-2bf8677a6952?q=80&w=1974&auto=format&fit=crop", synopsis: "고대 문헌에만 희미하게 남아있는 미지의 왕국에 대한 탐사 기록. 고고학적 증거와 저자의 상상력이 결합된 흥미로운 역사서." },
    ],
  }),
  computed: {
    pendingRequests() {
      return this.requests.filter(req => req.status === '대기중');
    },
    approvedRequests() {
      return this.requests.filter(req => req.status === '승인됨');
    },
    rejectedRequests() {
      return this.requests.filter(req => req.status === '거부됨');
    },
  },
  methods: {
    openReviewDialog(item) {
      this.selectedRequest = item;
      this.reviewDialog = true;
    },
    closeDialog() {
      this.reviewDialog = false;
      this.selectedRequest = null;
    },
    approveRequest(id) {
      // 1. API에 승인 요청 보내는 로직 (axios.post 등)
      // 2. 성공 시, 데이터 상태 변경
      const index = this.requests.findIndex(req => req.id === id);
      if (index !== -1) {
        this.requests[index].status = '승인됨';
      }
      this.closeDialog();
      // 성공 알림 스낵바 표시 등
    },
    rejectRequest(id) {
      // 1. API에 거부 요청 보내는 로직
      // 2. 성공 시, 데이터 상태 변경
      const index = this.requests.findIndex(req => req.id === id);
      if (index !== -1) {
        this.requests[index].status = '거부됨';
      }
      this.closeDialog();
      // 거부 알림 스낵바 표시 등
    },
    getStatusColor(status) {
      if (status === '대기중') return 'orange';
      if (status === '승인됨') return 'green';
      if (status === '거부됨') return 'red';
      return 'grey';
    },
  },
};
</script>

<style scoped>
.approval-page-container {
  background-color: #f7f8fc;
}
</style>
