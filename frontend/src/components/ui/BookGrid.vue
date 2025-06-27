<template>
    <v-container>
        <v-snackbar
            v-model="snackbar.status"
            :timeout="snackbar.timeout"
            :color="snackbar.color"
        >
            
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
                <v-btn style="margin-left: 5px;" @click="submitBookCommandDialog = true" class="contrast-primary-text" small color="primary" :disabled="!hasRole('Writer')">
                    <v-icon small>mdi-minus-circle-outline</v-icon>원고 등록/출간 요청
                </v-btn>
                <v-dialog v-model="submitBookCommandDialog" width="500">
                    <SubmitBookCommand
                        @closeDialog="submitBookCommandDialog = false"
                        @submitBookCommand="submitBookCommand"
                    ></SubmitBookCommand>
                </v-dialog>
                <v-btn style="margin-left: 5px;" @click="saveBookCommandDialog = true" class="contrast-primary-text" small color="primary" :disabled="!hasRole('Writer')">
                    <v-icon small>mdi-minus-circle-outline</v-icon>원고 저장
                </v-btn>
                <v-dialog v-model="saveBookCommandDialog" width="500">
                    <SaveBookCommand
                        @closeDialog="saveBookCommandDialog = false"
                        @saveBookCommand="saveBookCommand"
                    ></SaveBookCommand>
                </v-dialog>
            </div>
            <BookDetails @search="search" style="margin-bottom: 10px; background-color: #ffffff;"></BookDetails>
            <div class="mb-5 text-lg font-bold"></div>
            <div class="table-responsive">
                <v-table>
                    <thead>
                        <tr>
                        <th>Id</th>
                        <th>UserId</th>
                        <th>Title</th>
                        <th>Content</th>
                        <th>Summary</th>
                        <th>CoverImageUrl</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Status</th>
                        <th>ViewCount</th>
                        <th>CreatedAt</th>
                        <th>AI 전자책 프로세서 ID</th>
                        <th>AI 전자책 프로세서</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(val, idx) in value" 
                            @click="changeSelectedRow(val)"
                            :key="val"  
                            :style="val === selectedRow ? 'background-color: rgb(var(--v-theme-primary), 0.2) !important;':''"
                        >
                            <td class="font-semibold">{{ idx + 1 }}</td>
                            <td class="whitespace-nowrap" label="UserId">{{ val.userId }}</td>
                            <td class="whitespace-nowrap" label="Title">{{ val.title }}</td>
                            <td class="whitespace-nowrap" label="Content">{{ val.content }}</td>
                            <td class="whitespace-nowrap" label="Summary">{{ val.summary }}</td>
                            <td class="whitespace-nowrap" label="CoverImageUrl">{{ val.coverImageUrl }}</td>
                            <td class="whitespace-nowrap" label="Category">{{ val.category }}</td>
                            <td class="whitespace-nowrap" label="Price">{{ val.price }}</td>
                            <td class="whitespace-nowrap" label="Status">{{ val.status }}</td>
                            <td class="whitespace-nowrap" label="ViewCount">{{ val.viewCount }}</td>
                            <td class="whitespace-nowrap" label="CreatedAt">{{ val.createdAt }}</td>
                            <td class="whitespace-nowrap" label="AI 전자책 프로세서">
                                <AiBookProcessorId :editMode="editMode" v-model="val.aiBookProcessorId"></AiBookProcessorId>
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
                        <div style="color:white; font-size:17px; font-weight:700;">Book 등록</div>
                        <v-spacer></v-spacer>
                        <v-icon
                            color="white"
                            small
                            @click="closeDialog()"
                        >mdi-close</v-icon>
                    </v-toolbar>
                    <v-card-text>
                        <Book :offline="offline"
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
                        <div style="color:white; font-size:17px; font-weight:700;">Book 수정</div>
                        <v-spacer></v-spacer>
                        <v-icon
                            color="white"
                            small
                            @click="closeDialog()"
                        >mdi-close</v-icon>
                    </v-toolbar>
                    <v-card-text>
                        <div>
                            <Number label="BookId" v-model="selectedRow.bookId" :editMode="true"/>
                            <Number label="UserId" v-model="selectedRow.userId" :editMode="true"/>
                            <String label="Title" v-model="selectedRow.title" :editMode="true"/>
                            <String label="Content" v-model="selectedRow.content" :editMode="true"/>
                            <String label="Summary" v-model="selectedRow.summary" :editMode="true"/>
                            <String label="CoverImageUrl" v-model="selectedRow.coverImageUrl" :editMode="true"/>
                            <String label="Category" v-model="selectedRow.category" :editMode="true"/>
                            <Number label="Price" v-model="selectedRow.price" :editMode="true"/>
                            <String label="Status" v-model="selectedRow.status" :editMode="true"/>
                            <Number label="ViewCount" v-model="selectedRow.viewCount" :editMode="true"/>
                            <Date label="CreatedAt" v-model="selectedRow.createdAt" :editMode="true"/>
                            <AiBookProcessorId offline label="AI 전자책 프로세서 ID" v-model="selectedRow.aiBookProcessorId" :editMode="true"/>
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

<script>
import { ref } from 'vue';
import { useTheme } from 'vuetify';
import BaseGrid from '../base-ui/BaseGrid.vue'


export default {
    name: 'bookGrid',
    mixins:[BaseGrid],
    components:{
    },
    data: () => ({
        path: 'books',
        submitBookCommandDialog: false,
        saveBookCommandDialog: false,
    }),
    watch: {
    },
    methods:{
        async submitBookCommand(params){
            try{
                var path = "submitBookCommand".toLowerCase();
                var temp = await this.repository.invoke(this.selectedRow, path, params)
                // 스넥바 관련 수정 필요
                // this.$EventBus.$emit('show-success','SubmitBookCommand 성공적으로 처리되었습니다.')
                for(var i = 0; i< this.value.length; i++){
                    if(this.value[i] == this.selectedRow){
                        this.value[i] = temp.data
                    }
                }
                this.submitBookCommandDialog = false
            }catch(e){
                console.log(e)
            }
        },
        async saveBookCommand(params){
            try{
                var path = "saveBookCommand".toLowerCase();
                var temp = await this.repository.invoke(this.selectedRow, path, params)
                // 스넥바 관련 수정 필요
                // this.$EventBus.$emit('show-success','SaveBookCommand 성공적으로 처리되었습니다.')
                for(var i = 0; i< this.value.length; i++){
                    if(this.value[i] == this.selectedRow){
                        this.value[i] = temp.data
                    }
                }
                this.saveBookCommandDialog = false
            }catch(e){
                console.log(e)
            }
        },
    }
}

</script>