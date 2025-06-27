<template>

    <v-card outlined>
        <v-card-title>
            SubmitBookCommand
        </v-card-title>

        <v-card-text>
            <Number label="BookId" v-model="value.bookId" :editMode="editMode"/>
            <Number label="UserId" v-model="value.userId" :editMode="editMode"/>
            <String label="Title" v-model="value.title" :editMode="editMode"/>
            <String label="Content" v-model="value.content" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="submitBookCommand"
            >
                SubmitBookCommand
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
    name: 'SubmitBookCommandCommand',
    components:{
    },
    props: {},
    data: () => ({
        editMode: true,
        value: {},
    }),
    created() {
        this.value.bookId = 0;
        this.value.userId = 0;
        this.value.title = '';
        this.value.content = '';
    },
    watch: {
    },
    methods: {
        submitBookCommand() {
            this.$emit('submitBookCommand', this.value);
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

