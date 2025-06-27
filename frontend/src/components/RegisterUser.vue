<template>

    <v-card outlined>
        <v-card-title>
            RegisterUser
        </v-card-title>

        <v-card-text>
            <String label="Name" v-model="value.name" :editMode="editMode"/>
            <String label="Email" v-model="value.email" :editMode="editMode"/>
            <String label="PasswordHash" v-model="value.passwordHash" :editMode="editMode"/>
            <Boolean label="IsKtCustomer" v-model="value.isKtCustomer" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="registerUser"
            >
                RegisterUser
            </v-btn>
            
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="close"
            >
                Close
            </v-btn>
        </v-card-actions>
    </v-card>

</template>

<script>

export default {
    name: 'RegisterUserCommand',
    components:{
    },
    props: {},
    data: () => ({
        editMode: true,
        value: {},
    }),
    created() {
        this.value.name = '';
        this.value.email = '';
        this.value.passwordHash = '';
        this.value.isKtCustomer = false;
    },
    watch: {
    },
    methods: {
        registerUser() {
            this.$emit('registerUser', this.value);
        },
        close() {
            this.$emit('closeDialog');
        },
        change() {
            this.$emit("update:modelValue", this.value);
        },
    }
}
</script>

