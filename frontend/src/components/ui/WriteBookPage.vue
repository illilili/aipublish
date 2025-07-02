<template>
  <div class="writing-page-container">
    <v-container class="writing-content">
      <v-text-field
        v-model="book.title"
        placeholder="제목"
        variant="plain"
        class="title-input"
        hide-details
      ></v-text-field>
      
      <v-divider class="my-4"></v-divider>

      <v-textarea
        v-model="book.content"
        placeholder="출판하고 싶은 글을 작성해주세요!"
        variant="plain"
        class="content-input"
        hide-details
        no-resize
        rows="15"
      ></v-textarea>
    </v-container>

    <v-footer app class="writing-footer">
      <v-spacer></v-spacer>
      <v-btn variant="text" @click="handleSaveDraft" :loading="loading">글 저장</v-btn>
      <v-btn color="primary" @click="handleSubmitForPublication" :loading="loading" class="ms-4" elevation="2">출간요청</v-btn>
    </v-footer>

    <v-snackbar v-model="snackbar.show" :color="snackbar.color" :timeout="3000" location="top">
      {{ snackbar.text }}
    </v-snackbar>
  </div>
</template>

<script>
import { useUserStore } from '@/store/user';
import { useBookStore } from '@/store/book';

export default {
  name: 'WriteBookPage',
  data() {
    return {
      book: {
        bookId: null, // 수정 시 사용될 ID
        title: '',
        content: '',
      },
      loading: false,
      snackbar: { show: false, text: '', color: 'success' },
    };
  },
  methods: {
    /**
     * 임시 저장 (DRAFT)
     */
    async handleSaveDraft() {
      this.loading = true;
      const userStore = useUserStore();
      const bookStore = useBookStore();
      
      const payload = {
        userId: userStore.currentUser.id,
        title: this.book.title,
        content: this.book.content,
      };

      try {
        const response = await bookStore.saveBook(payload);
        // 새로 생성된 책의 ID를 저장해두면, 바로 출간 요청이 가능합니다.
        this.book.bookId = response.data.bookId;
        this.showSnackbar('글이 임시 저장되었습니다.', 'success');
      } catch (error) {
        this.showSnackbar('저장 중 오류가 발생했습니다.', 'error');
      } finally {
        this.loading = false;
      }
    },

    /**
     * 출간 요청 (SUBMITTED)
     * 먼저 글을 저장하여 bookId를 확보한 후, 출간 요청을 보냅니다.
     */
    async handleSubmitForPublication() {
      this.loading = true;
      const userStore = useUserStore();
      const bookStore = useBookStore();

      // 1. 먼저 글이 저장되어 bookId가 있는지 확인, 없으면 먼저 저장
      if (!this.book.bookId) {
        try {
          const savePayload = {
            userId: userStore.currentUser.id,
            title: this.book.title,
            content: this.book.content,
          };
          const response = await bookStore.saveBook(savePayload);
          this.book.bookId = response.data.bookId;
        } catch (error) {
          this.showSnackbar('저장 중 오류가 발생하여 출간 요청을 보내지 못했습니다.', 'error');
          this.loading = false;
          return;
        }
      }

      // 2. 확보된 bookId로 출간 요청
      try {
        await bookStore.submitBook({ bookId: this.book.bookId });
        this.showSnackbar('출간 요청이 완료되었습니다. 관리자 검토 후 출간됩니다.', 'success');
        this.$router.push('/books'); // 요청 완료 후 도서 목록 페이지로 이동
      } catch (error) {
        this.showSnackbar('출간 요청 중 오류가 발생했습니다.', 'error');
      } finally {
        this.loading = false;
      }
    },
    
    showSnackbar(text, color) {
      this.snackbar.text = text;
      this.snackbar.color = color;
      this.snackbar.show = true;
    },
  },
};
</script>

<style scoped>
.writing-page-container {
  background-color: #ffffff;
  height: 100%;
}
.writing-content {
  max-width: 800px;
  margin: 0 auto;
  padding-top: 48px;
}
.title-input >>> .v-field__input {
  font-size: 2.5rem;
  font-weight: 700;
  line-height: 1.2;
}
.content-input >>> .v-field__input {
  font-size: 1.1rem;
  line-height: 1.8;
  color: #333;
}
.writing-footer {
  background-color: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(4px);
  border-top: 1px solid #eee;
}
</style>