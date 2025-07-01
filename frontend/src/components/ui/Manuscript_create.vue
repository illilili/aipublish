<template>
  <div class="writing-background">
    <v-container class="writing-container">
      <v-card class="writing-panel" flat>
        <v-card-text class="pa-md-12 pa-8">
          <v-text-field
            v-model="manuscript.title"
            placeholder="제목"
            variant="underlined"
            class="title-input mb-8"
            hide-details
          ></v-text-field>

          <v-textarea
            v-model="manuscript.content"
            placeholder="출판 하고 싶은 글을 작성해주세요!"
            variant="plain"
            auto-grow
            no-resize
            rows="20"
            class="content-editor"
            hide-details
          ></v-textarea>
        </v-card-text>

        <v-card-actions class="pa-4 action-bar">
          <v-spacer></v-spacer>
          <v-btn
            size="large"
            variant="text"
            class="action-btn"
            @click="saveDraft"
          >
            글 저장
          </v-btn>
          <v-btn
            size="large"
            variant="flat"
            color="primary"
            class="action-btn ml-2"
            @click="requestPublication"
          >
            출간요청
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-container>

    <v-snackbar v-model="snackbar.show" :color="snackbar.color" :timeout="3000">
      {{ snackbar.text }}
    </v-snackbar>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'WritePage',
  props: {
    userId: {
      type: String,
      required: true,
      default: '1', // 임시
    }
  },
  data() {
    return {
      manuscript: {
        title: '',
        content: '',
        bookId: null
      },
      snackbar: {
        show: false,
        text: '',
        color: 'success'
      }
    }
  },
  methods: {
    async saveDraft() {
      try {
        const payload = {
          bookId: this.manuscript.bookId || null,
          userId: this.userId,
          title: this.manuscript.title,
          content: this.manuscript.content,
          status: 'DRAFT'
        }

        const res = await axios.post('/books/savebookcommand', payload)

        // 서버가 새 bookId를 생성해주었을 경우
        if (res.data && res.data.bookId) {
          this.manuscript.bookId = res.data.bookId
        }

        this.showSnackbar('글이 임시 저장되었습니다.')
      } catch (e) {
        console.error(e)
        this.showSnackbar('저장 중 오류가 발생했습니다.', 'error')
      }
    },

    async requestPublication() {
      if (!this.manuscript.bookId) {
        this.showSnackbar('먼저 글을 저장해 주세요.', 'error')
        return
      }

      try {
        await axios.post('/books/submitbookcommand', {
          bookId: this.manuscript.bookId
        })

        this.showSnackbar('출간 요청이 완료되었습니다. 관리자 검토 후 결과가 통보됩니다.')

        // 출간요청 후 AI 자동화 화면으로 이동
        this.$router.push('/aiBookProcessors')
      } catch (e) {
        console.error(e)
        this.showSnackbar('출간 요청 중 오류가 발생했습니다.', 'error')
      }
    },

    showSnackbar(text, color = 'success') {
      this.snackbar.text = text
      this.snackbar.color = color
      this.snackbar.show = true
    }
  }
}
</script>

<style scoped>
.writing-background {
  background-color: #f4f5f7;
  min-height: 100vh;
  padding-top: 48px;
}

.writing-container {
  max-width: 900px;
}

.writing-panel {
  border-radius: 8px;
  border: 1px solid #e0e0e0;
}

.title-input >>> .v-input__control .v-field__input {
  font-size: 2.5rem !important;
  font-weight: 700;
  color: #000000;
  padding-bottom: 8px;
}

.content-editor >>> .v-field__input {
  font-size: 1.125rem !important;
  line-height: 1.8 !important;
  color: #000000;
  font-family: 'Noto Serif KR', serif;
}

.action-bar {
  border-top: 1px solid #eee;
  background-color: #fafafa;
}

.action-btn {
  font-weight: 600;
}
</style>
