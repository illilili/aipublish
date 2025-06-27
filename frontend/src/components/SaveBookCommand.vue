<template>

    <v-card outlined>
        <v-card-title>
            SaveBookCommand
        </v-card-title>

        <v-card-text>
            <Number label="BookId" v-model="value.bookId" :editMode="editMode"/>
            <Number label="UserId" v-model="value.userId" :editMode="editMode"/>
            <String label="Title" v-model="value.title" :editMode="editMode"/>
            <String label="Content" v-model="value.content" :editMode="editMode"/>
            <String label="Status" v-model="value.status" :editMode="editMode"/>
            <Number label="ManuscriptId" v-model="value.manuscriptId" :editMode="editMode"/>
            <Number label="WriterId" v-model="value.writerId" :editMode="editMode"/>
            <String label="Title" v-model="value.title" :editMode="editMode"/>
            <String label="Content" v-model="value.content" :editMode="editMode"/>
            <String label="Status" v-model="value.status" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="saveBookCommand"
            >
                SaveBookCommand
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
    name: 'SaveBookCommandCommand',
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
        this.value.status = '';
        this.value.manuscriptId = 0;
        this.value.writerId = 0;
        this.value.title = '';
        this.value.content = '';
        this.value.status = '';
    },
    watch: {
    },
    methods: {
        saveBookCommand() {
            this.$emit('saveBookCommand', this.value);
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

