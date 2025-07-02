import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    userId: null,
    userName: '',
  }),
  actions: {
    setUser(id, name) {
      this.userId = id
      this.userName = name
    },
  },
})
