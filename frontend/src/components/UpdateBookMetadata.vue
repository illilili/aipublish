<template>

    <v-card outlined>
        <v-card-title>
            UpdateBookMetadata
        </v-card-title>

        <v-card-text>
            <Number label="BookId" v-model="value.bookId" :editMode="editMode"/>
            <Date label="CreatedAt" v-model="value.createdAt" :editMode="editMode"/>
            <String label="Summary" v-model="value.summary" :editMode="editMode"/>
            <String label="CoverImageUrl" v-model="value.coverImageUrl" :editMode="editMode"/>
            <String label="Category" v-model="value.category" :editMode="editMode"/>
            <Number label="Price" v-model="value.price" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="updateBookMetadata"
            >
                UpdateBookMetadata
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
    name: 'UpdateBookMetadataCommand',
    components:{
    },
    props: {},
    data: () => ({
        editMode: true,
        value: {},
    }),
    created() {
        this.value.bookId = 0;
        this.value.createdAt = '2025-06-27';
        this.value.summary = '';
        this.value.coverImageUrl = '';
        this.value.category = '';
        this.value.price = 0;
    },
    watch: {
    },
    methods: {
        updateBookMetadata() {
            this.$emit('updateBookMetadata', this.value);
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

