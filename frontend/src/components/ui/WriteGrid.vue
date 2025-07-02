<<<<<<< HEAD
<template>
    <v-container>
        <v-snackbar
            v-model="snackbar.status"
            :timeout="snackbar.timeout"
            :color="snackbar.color"
        >
            {{ snackbar.message }}
            <v-btn style="margin-left: 80px;" text @click="snackbar.status = false">
                Close
            </v-btn>
        </v-snackbar>
        <div class="panel">
            <div class="gs-bundle-of-buttons" style="max-height:10vh;">
                <v-btn @click="addNewRow" @class="contrast-primary-text" small color="primary">
                    <v-icon small style="margin-left: -5px;">mdi-plus</v-icon>등록
                </v-btn>
                <v-btn :disabled="!selectedRow" style="margin-left: 5px;" @click="openEditDialog()" class="contrast-primary-text" small color="primary">
                    <v-icon small>mdi-pencil</v-icon>수정
                </v-btn>
                <v-btn style="margin-left: 5px;" @click="registerWriterCommandDialog = true" class="contrast-primary-text" small color="primary" :disabled="!hasRole('Writer')">
                    <v-icon small>mdi-minus-circle-outline</v-icon>작가 등록 신청
                </v-btn>
                <v-dialog v-model="registerWriterCommandDialog" width="500">
                    <RegisterWriterCommand
                        @closeDialog="registerWriterCommandDialog = false"
                        @registerWriterCommand="registerWriterCommand"
                    ></RegisterWriterCommand>
                </v-dialog>
                <v-btn
                :disabled="!selectedRow || !hasRole('Admin')"
                style="margin-left: 5px;"
                @click="updateWriterStatusCommandDialog = true"
                class="contrast-primary-text"
                small
                color="primary"
                >
                <v-icon small>mdi-minus-circle-outline</v-icon>작가 상태 변경
                </v-btn>
                <v-dialog v-model="updateWriterStatusCommandDialog" width="500">
                    <UpdateWriterStatusCommand
                        @closeDialog="updateWriterStatusCommandDialog = false"
                        @updateWriterStatusCommand="updateWriterStatusCommand"
                    ></UpdateWriterStatusCommand>
                </v-dialog>
            </div>
            <WriterList @search="search" style="margin-bottom: 10px; background-color: #ffffff;"></WriterList>
            <WriterDetails @search="search" style="margin-bottom: 10px; background-color: #ffffff;"></WriterDetails>
            <div class="mb-5 text-lg font-bold"></div>
            <div class="table-responsive">
                <v-table>
                    <thead>
                        <tr>
                        <th>Id</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Bio</th>
                        <th>Status</th>
                        <th>CreatedAt</th>
                        <th>원고 ID</th>
                        <th>원고</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(val, idx) in value" 
                            @click="changeSelectedRow(val)"
                            :key="val"  
                            :style="val === selectedRow ? 'background-color: rgb(var(--v-theme-primary), 0.2) !important;':''"
                        >
                            <td class="font-semibold">{{ idx + 1 }}</td>
                            <td class="whitespace-nowrap" label="Name">{{ val.name }}</td>
                            <td class="whitespace-nowrap" label="Email">{{ val.email }}</td>
                            <td class="whitespace-nowrap" label="Bio">{{ val.bio }}</td>
                            <td class="whitespace-nowrap" label="Status">{{ val.status }}</td>
                            <td class="whitespace-nowrap" label="CreatedAt">{{ val.createdAt }}</td>
                            <td class="whitespace-nowrap" label="원고">
                                <BookId :editMode="editMode" v-model="val.bookId"></BookId>
                            </td>
                            <v-row class="ma-0 pa-4 align-center">
                                <v-spacer></v-spacer>
                                <Icon style="cursor: pointer;" icon="mi:delete" @click="deleteRow(val)" />
                            </v-row>
                        </tr>
                    </tbody>
                </v-table>
            </div>
        </div>
        <v-col>
            <v-dialog
                v-model="openDialog"
                transition="dialog-bottom-transition"
                width="35%"
            >
                <v-card>
                    <v-toolbar
                        color="primary"
                        class="elevation-0 pa-4"
                        height="50px"
                    >
                        <div style="color:white; font-size:17px; font-weight:700;">Write 등록</div>
                        <v-spacer></v-spacer>
                        <v-icon
                            color="white"
                            small
                            @click="closeDialog()"
                        >mdi-close</v-icon>
                    </v-toolbar>
                    <v-card-text>
                        <Write :offline="offline"
                            :isNew="!value.idx"
                            :editMode="true"
                            :inList="false"
                            v-model="newValue"
                            @add="append"
                        />
                    </v-card-text>
                </v-card>
            </v-dialog>
            <v-dialog
                v-model="editDialog"
                transition="dialog-bottom-transition"
                width="35%"
            >
                <v-card>
                    <v-toolbar
                        color="primary"
                        class="elevation-0 pa-4"
                        height="50px"
                    >
                        <div style="color:white; font-size:17px; font-weight:700;">Write 수정</div>
                        <v-spacer></v-spacer>
                        <v-icon
                            color="white"
                            small
                            @click="closeDialog()"
                        >mdi-close</v-icon>
                    </v-toolbar>
                    <v-card-text>
                        <div>
                            <Number label="UserId" v-model="selectedRow.userId" :editMode="true"/>
                            <String label="Name" v-model="selectedRow.name" :editMode="true"/>
                            <String label="Email" v-model="selectedRow.email" :editMode="true"/>
                            <String label="Bio" v-model="selectedRow.bio" :editMode="true"/>
                            <String label="Status" v-model="selectedRow.status" :editMode="true"/>
                            <Date label="CreatedAt" v-model="selectedRow.createdAt" :editMode="true"/>
                            <ManuscriptId offline label="원고 ID" v-model="selectedRow.manuscriptId" :editMode="true"/>
                            <v-divider class="border-opacity-100 my-divider"></v-divider>
                            <v-layout row justify-end>
                                <v-btn
                                    width="64px"
                                    color="primary"
                                    @click="save"
                                >
                                    수정
                                </v-btn>
                            </v-layout>
                        </div>
                    </v-card-text>
                </v-card>
            </v-dialog>
        </v-col>
    </v-container>
</template>
=======
<script setup>
import { ref } from 'vue'
import {
  VerticalNavLink,
  VerticalNavSectionTitle,
} from '@layouts'
>>>>>>> d6cbf789fbddf696c973b4e0f8d8b4c62fcc09dd

const isMenuVisible = ref(false)
let closeMenuTimer = null

<<<<<<< HEAD

export default {
    name: 'writeGrid',
    mixins:[BaseGrid],
    components:{
    },
    data: () => ({
        path: 'writes/apply',
        registerWriterCommandDialog: false,
        updateWriterStatusCommandDialog: false,

        snackbar: {
            status: false,
            message: '',
            color: 'success',
            timeout: 3000
        }

    }),
    watch: {
    },
    methods:{
        async registerWriterCommand(params){
            console.log('Params sent to /writes/apply:', JSON.stringify(params, null, 2))
            try{
                var path = "registerWriterCommand".toLowerCase();
                const temp = await this.repository.postTo('writes/apply', params);
                for(var i = 0; i< this.value.length; i++){
                    if(this.value[i] == this.selectedRow){
                        this.value[i] = temp.data
                    }
                }
                this.snackbar.message = '✅ 작가 등록 신청이 성공적으로 완료되었습니다.';
                this.snackbar.color = 'success';
                this.snackbar.status = true;

                this.registerWriterCommandDialog = false
            }catch(e){
                console.log(e)
                this.snackbar.message = '⚠️ 작가 등록 신청 중 오류가 발생했습니다.';
                this.snackbar.color = 'error';
                this.snackbar.status = true;

            }
        },
        async updateWriterStatusCommand(params){
            try{
                var path = "updateWriterStatusCommand".toLowerCase();
                var temp = await this.repository.invoke(this.selectedRow, path, params)
                // 스넥바 관련 수정 필요
                // this.$EventBus.$emit('show-success','UpdateWriterStatusCommand 성공적으로 처리되었습니다.')
                for(var i = 0; i< this.value.length; i++){
                    if(this.value[i] == this.selectedRow){
                        this.value[i] = temp.data
                    }
                }
                this.updateWriterStatusCommandDialog = false
            }catch(e){
                console.log(e)
            }
        },
    }
=======
const openMenu = () => {
  if (closeMenuTimer) {
    clearTimeout(closeMenuTimer)
    closeMenuTimer = null
  }
  isMenuVisible.value = true
>>>>>>> d6cbf789fbddf696c973b4e0f8d8b4c62fcc09dd
}

const closeMenuWithDelay = () => {
  closeMenuTimer = setTimeout(() => {
    isMenuVisible.value = false
  }, 600) // ← 0.6초 뒤에 닫힘
}
</script>

<template>
  <div
    class="hamburger-menu-container"
    @mouseenter="openMenu"
    @mouseleave="closeMenuWithDelay"
  >
    <button
      class="hamburger-button"
      @mouseenter="openMenu"
      aria-label="메뉴 열기"
    >
      <svg
        width="30"
        height="30"
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M4 6H20M4 12H20M4 18H20"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
        />
      </svg>
    </button>

    <Transition name="fade">
      <ul v-if="isMenuVisible" class="menu-list">
        <VerticalNavSectionTitle :item="{ heading: '작가 등록/관리' }" />
        <VerticalNavLink :item="{ title: '작가 ', to: '/writes' }" />
        <VerticalNavLink :item="{ title: '작가 목록', to: '/writerLists' }" />
        <VerticalNavLink :item="{ title: '작가 상세', to: '/writerDetails' }" />
        <VerticalNavLink :item="{ title: '작가 등록요청', to: '/writes_register' }" />
        <VerticalNavLink :item="{ title: '전자책 출간 제어판', to: '/publish-dashboard' }" />
        <VerticalNavSectionTitle :item="{ heading: '회원 관리' }" />
        <VerticalNavLink :item="{ title: '관리자', to: '/users' }" />
        <VerticalNavLink :item="{ title: '마이페이지 조회', to: '/viewMyPages' }" />
        <VerticalNavSectionTitle :item="{ heading: 'AI 기반 출판' }" />
        <VerticalNavLink :item="{ title: 'AI 기반 전자책 출간 요청', to: '/aiBookProcessors' }" />
      </ul>
    </Transition>
  </div>
</template>

<style scoped>
.hamburger-menu-container {
  position: relative;
  display: inline-block;
}

.hamburger-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 10px;
  color: #333;
}

.menu-list {
  position: absolute;
  top: 100%;
  left: 0;
  background-color: white;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  list-style: none;
  padding: 8px;
  margin: 5px 0 0 0;
  min-width: 250px;
  z-index: 1000;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
<script setup>
import { ref } from 'vue'
import {
  VerticalNavLink,
  VerticalNavSectionTitle,
} from '@layouts'

const isMenuVisible = ref(false)
let closeMenuTimer = null

const openMenu = () => {
  if (closeMenuTimer) {
    clearTimeout(closeMenuTimer)
    closeMenuTimer = null
  }
  isMenuVisible.value = true
}

const closeMenuWithDelay = () => {
  closeMenuTimer = setTimeout(() => {
    isMenuVisible.value = false
  }, 600) // ← 0.6초 뒤에 닫힘. 원하면 800 등으로 조절
}
</script>

<template>
  <div
    class="hamburger-menu-container"
    @mouseenter="openMenu"
    @mouseleave="closeMenuWithDelay"
  >
    <button
      class="hamburger-button"
      @mouseenter="openMenu"
      aria-label="메뉴 열기"
    >
      <svg
        width="30"
        height="30"
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M4 6H20M4 12H20M4 18H20"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
        />
      </svg>
    </button>

    <Transition name="fade">
      <ul v-if="isMenuVisible" class="menu-list">
        <VerticalNavSectionTitle :item="{ heading: '작가 등록/관리' }" />
        <VerticalNavLink :item="{ title: '작가 ', to: '/writes' }" />
        <VerticalNavLink :item="{ title: '작가 목록', to: '/writerLists' }" />
        <VerticalNavLink :item="{ title: '작가 상세', to: '/writerDetails' }" />
        <VerticalNavLink :item="{ title: '작가 등록요청', to: '/writes_register' }" />
        <VerticalNavLink :item="{ title: '전자책 출간 제어판', to: '/publish-dashboard' }" />
        <VerticalNavSectionTitle :item="{ heading: '회원 관리' }" />
        <VerticalNavLink :item="{ title: '관리자', to: '/users' }" />
        <VerticalNavLink :item="{ title: '마이페이지 조회', to: '/viewMyPages' }" />
        <VerticalNavSectionTitle :item="{ heading: 'AI 기반 출판' }" />
        <VerticalNavLink :item="{ title: 'AI 기반 전자책 출간 요청', to: '/aiBookProcessors' }" />
      </ul>
    </Transition>
  </div>
</template>

<style scoped>
.hamburger-menu-container {
  position: relative;
  display: inline-block;
}

.hamburger-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 10px;
  color: #333;
}

.menu-list {
  position: absolute;
  top: 100%;
  left: 0;
  background-color: white;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  list-style: none;
  padding: 8px;
  margin: 5px 0 0 0;
  min-width: 250px;
  z-index: 1000;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
