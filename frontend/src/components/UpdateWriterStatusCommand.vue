<template>

    <v-card outlined>
        <v-card-title>
            UpdateWriterStatusCommand
        </v-card-title>

        <v-card-text>
            <String label="Status" v-model="value.status" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="updateWriterStatusCommand"
            >
                UpdateWriterStatusCommand
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
    name: 'UpdateWriterStatusCommandCommand',
    components:{
    },
    props: {},
    data: () => ({
        editMode: true,
        value: {},
    }),
    created() {
        this.value.status = '';
    },
    watch: {
    },
    methods: {
        updateWriterStatusCommand() {
            this.$emit('updateWriterStatusCommand', this.value);
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

