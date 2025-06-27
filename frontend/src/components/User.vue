<template>
    <div>
        <Number
            label="UserId"
            v-model="value.userId"
            :editMode="editMode"
        />
        <String
            label="Name"
            v-model="value.name"
            :editMode="editMode"
        />
        <String
            label="Email"
            v-model="value.email"
            :editMode="editMode"
        />
        <String
            label="PasswordHash"
            v-model="value.passwordHash"
            :editMode="editMode"
        />
        <Boolean
            label="IsKtCustomer"
            v-model="value.isKtCustomer"
            :editMode="editMode"
        />
        <Boolean
            label="Subscription"
            v-model="value.subscription"
            :editMode="editMode"
        />
        <Number
            offline
            label="포인트 ID"
            v-model="value.pointId"
            :editMode="editMode"
            @change="change"
        />
        <v-row class="ma-0 pa-0">
            <v-spacer></v-spacer>
            <v-btn width="64px" color="primary" @click="save">
                저장
            </v-btn>
        </v-row>
    </div>
</template>


<script>
import BaseEntity from './base-ui/BaseEntity.vue'

export default {
    name: 'User',
    mixins:[BaseEntity],
    components:{
    },
    
    data: () => ({
        path: "users",
        value: {
        }
    }),
    created(){
    },
    computed:{
    },
    methods: {
    },
}
</script>
