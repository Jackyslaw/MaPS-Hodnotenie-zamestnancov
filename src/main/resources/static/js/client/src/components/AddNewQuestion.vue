<template>

    <div style="position: relative;">
        <a class="btn-floating btn-large waves-effect waves-light pink custom-floating-button" @click.prevent="openModal(true)"><i class="material-icons">add</i></a>

        <div id="add-question" class="modal modal-fixed-footer">
            <div class="modal-content">
                <h4>Add New Question</h4>
                <small>Adding new question for the <b>{{mngmnt}}</b></small>
                <div class="custom-form">
                    <table>
                        <tr>
                            <td class="admin-add-question-td-smaller">
                                <label for="admin-question-add-title">Title:</label>
                            </td>
                            <td>
                                <input v-model="message.title" id="admin-question-add-title" type="text" class="browser-default admin-add-question-input" />
                            </td>
                        </tr>
                        <tr>
                            <td class="admin-add-question-td-smaller">
                                <label for="admin-question-add-qcat">Question category:</label>
                            </td>
                            <td>
                                <select v-model="message.questionCategoryId" id="admin-question-add-qcat" class="browser-default admin-add-question-input">
                                    <option v-for="val in questionCategories" :value="val.questionCategoryId">
                                        {{val.categoryName}}
                                    </option>
                                </select>
                            </td>
                        </tr>
                        <tr v-for="(val, key) in qtFormats">
                            <td class="admin-add-question-td-smaller">
                                <label :for="'admin-question-add-' + key">{{val}}:</label>
                            </td>
                            <td>
                                <input v-model="message.text[key]" :id="'admin-question-add-' + key" type="text" class="browser-default admin-add-question-input" />
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <button style="padding-left: 16px;" class="modal-action btn waves-effect waves-light light-green lighten-1 vue-tabs-a-over" @click.prevent="addQuestion()" type="submit">Add
                    <i class="material-icons left">add</i>
                </button>
                <a class="btn modal-action modal-close waves-effect waves-light red vue-tabs-a-over">Close</a>
            </div>
        </div>
    </div>

</template>
<script>
import ajax from '../ajax';
export default {
    name: '',
    props: ['qtFormats', 'mngmnt', 'questionCategories'],
    data: () => {
        return {
            message: {
                title: '',
                questionCategoryId: 0,
                text: {},
            },
        };
    },
    methods: {
        openModal(clear = false) {
            const self = this;
            if(clear) {
                this.message.title = '';
                Object.keys(this.message.text).map((key) => {
                    self.message.text[key] = ''
                });
            }
            $('#add-question').modal('open');
        },
        closeModal() {
            $('#add-question').modal('close');
        },
        addQuestion() {
            this.closeModal();
            Materialize.toast('Your question has been successfully added to ' + this.mngmnt, 3000, 'rounded light-green lighten-1');
            this.$emit('addNewQuestion', this.message);
        },
    },
    components: {
        
    },
    mounted() {
        const self = this;
        this.$nextTick(() => {
            $('.modal').modal();
            Object.keys(self.qtFormats).map((key) => self.message.text[key] = '');
        });
    },
}
</script>
<style>

.admin-add-question-input {
    width: 100%;
    outline: none;
    border-radius: 5px;
    border: 1px solid #b9b9b9;
    padding-left: 10px;
    color: #616161;
}

.admin-add-question-td-smaller {
    table-layout: unset;
    width: 9em;
}

.custom-floating-button {
    width: 40px !important;
    height: 40px !important;
    position: absolute;
    right: -7%;
    top: -60px;

    i {
        line-height: 15px!important;
        font-size: 1.6rem;
    }
}

.vue-tabs-a-over {
    color: #f3f3f3 !important;
}

</style>