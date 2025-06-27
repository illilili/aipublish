<template>
    <div class="panel">
        <div class="label-title">{{label}}</div>
        <div class="table-responsive">
            <v-btn v-if="editMode" @click="addDetailRow()">추가</v-btn>
            <v-btn v-if="editMode" style="margin-left: 3px;" @click="detailDeleteRow()">삭제</v-btn>
            <v-table v-if="!editMode">
                <thead>
                    <tr>
                        <th>Id</th>
                        <th>articleId</th>
                        <th>writerId</th>
                        <th>price</th>
                        <th>viewCount</th>
                        <th>createdAt</th>
                        <th>updatedAt</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(detailVal, idx) in selectedRow.articles" :key="detailVal" >
                        <td class="font-semibold">{{ idx + 1 }}</td>
                        <td class="whitespace-nowrap">{{ detailVal.articleId }}</td>
                        <td class="whitespace-nowrap">{{ detailVal.writerId }}</td>
                        <td class="whitespace-nowrap">{{ detailVal.price }}</td>
                        <td class="whitespace-nowrap">{{ detailVal.viewCount }}</td>
                        <td class="whitespace-nowrap">{{ detailVal.createdAt }}</td>
                        <td class="whitespace-nowrap">{{ detailVal.updatedAt }}</td>
                        <td class="whitespace-nowrap">
                            <Icon icon="mi:delete" @click="deleteRow(detailVal)" />
                        </td>
                    </tr>
                </tbody>
            </v-table>
            <v-table v-else>
                <thead>
                    <tr>
                        <th>Id</th>
                        <th>articleId</th>
                        <th>writerId</th>
                        <th>price</th>
                        <th>viewCount</th>
                        <th>createdAt</th>
                        <th>updatedAt</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(addVal, idx) in newValue" :key="addVal" >
                        <td class="font-semibold">{{ idx + 1 }}</td>
                        <td class="whitespace-nowrap">
                            <Number style="margin-left: -5px; width: 150px;" :editMode="editMode" v-model="addVal.articleId"/>
                        </td>
                        <td class="whitespace-nowrap">
                            <Number style="margin-left: -5px; width: 150px;" :editMode="editMode" v-model="addVal.writerId"/>
                        </td>
                        <td class="whitespace-nowrap">
                            <Number style="margin-left: -5px; width: 150px;" :editMode="editMode" v-model="addVal.price"/>
                        </td>
                        <td class="whitespace-nowrap">
                            <Number style="margin-left: -5px; width: 150px;" :editMode="editMode" v-model="addVal.viewCount"/>
                        </td>
                        <td class="whitespace-nowrap">
                            <Date style="margin-left: -5px; width: 150px;" :editMode="editMode" v-model="addVal.createdAt"/>
                        </td>
                        <td class="whitespace-nowrap">
                            <Date style="margin-left: -5px; width: 150px;" :editMode="editMode" v-model="addVal.updatedAt"/>
                        </td>
                    </tr>
                </tbody>
            </v-table>
        </div>
    </div>
</template>

<script>
import BaseDetailGrid from '../base-ui/BaseDetailGrid.vue';

export default {
    name: 'Article',
    mixins: [BaseDetailGrid],
    components: {
    },
    props: {
        label: String,
        editMode: Boolean,
        selectedRow: Object,
    },
    data: ()=>({
    }),
    created(){
    },
    methods: {
    }
}
</script>
