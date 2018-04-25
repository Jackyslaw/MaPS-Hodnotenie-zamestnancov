<template>
    <div>
        <app-managers-search @onSelectManager="onSelectManager" :managers="managers" v-show="step === 0">
            <div style="color: #a20000;"><h6><strong>Select a manager for new feedback round</strong></h6></div>
            <br>
        </app-managers-search>

        <app-peers-select @onSelectPeers="onSelectPeers" v-show="step === 1">
            <div style="color: #a20000;"><h6><strong>Select peers (or leave a text field empty)</strong></h6></div>
            <div class="form-control" style="margin-top: 20px">
                <input id="startImmediately" type="checkbox" v-model="startImmediately" />
                <label for="startImmediately">Start feedback round immediately</label>
            </div>
            <small>If start immediately is not checked an email is sent to selected manager to add his own peers</small>
        </app-peers-select>


        <app-edit-questions @onSave="onEditQuestionsSave" :sublevelCheckbox="false" :saveAlwaysEnabled="true" saveLabel="Save / Done" :saveCallApi="false" v-show="step === 2">
            <div style="color: #a20000;"><h6><strong>Select manager's level to filter questions for this new feedback round</strong></h6></div>
            <br>
            <small><b>Hint:</b> Questions are also editable at this point</small>
        </app-edit-questions>

        <app-date-time-picker @onPicked="onDateTimePicked" v-show="step === 3">
            <div style="color: #a20000;"><h6><strong>Select date and time when this new feedback round should start</strong></h6></div>
        </app-date-time-picker>

        <div class="new-feedback-round-buttons">
            <button @click="step--" class="btn waves-effect waves-light red lighten-1" v-if="step > 0">
                Go Back
            </button>

            <button @click="submitNewFeedbackRound" class="btn waves-effect waves-light light-green lighten-1" :disabled="readyToSubmit()">
                Submit
            </button>
        </div>
    </div>
</template>
<script>
import ajax from '../ajax';
import ManagersSearch from './ManagersSearch.vue';
import PeersSelect from './PeersSelect.vue';
import EditQuestions from './EditQuestions.vue';
import DateTimePicker from './DateTimePicker.vue';

export default {
    name: '',
    data: () => ({
        NUMBER_OF_STEPS: 4,
        managers: [],
        step: 0,

        selectedManager: null,
        startImmediately: false,
        editedQuestions: null,
        selectedPeers: null,
        mngLvl: null,

        dateTime: null,
    }),
    methods: {
        onSelectManager(mngr) {
            this.selectedManager = mngr;
            this.step++;
        },
        onSelectPeers(peers) {
            this.selectedPeers = peers;
            this.step++;
        },
        onEditQuestionsSave(mngLvl, editedQuestions) {
            this.mngLvl = mngLvl;
            this.editedQuestions = editedQuestions;
            console.log(editedQuestions);
            this.step++;
        },
        onDateTimePicked(dateTime) {
            this.dateTime = dateTime;
        },
        submitNewFeedbackRound() {
            const self = this;
            // const mngLvls = this.FIBIMETER_CONSTANTS.orderManagementLevels;
            const peers = this.selectedPeers.map((p) => {
                return p.tag;
            });
            ajax.post('/admin/start-feedbackround', {
                managerId: this.selectedManager.idEmployee,
                dateStart: this.dateTime,
                dateEnd: new Date(2999, 0, 1, 1, 0, 0, 0).getTime(),
                editedQuestions: this.editedQuestions,
                adminSelectedPeers: { peers },
                managerCanAndShouldSelectPeers: !this.startImmediately,
            }, ({data, headers}) => {
                Materialize.toast(`Feedback round submitted successfully.`, 3000, 'rounded light-green lighten-1');
                this.step = 0;
            });
        },
        readyToSubmit() {
            return !(this.dateTime && !isNaN(this.mngLvl) && this.selectedManager && this.selectedPeers);
        }
    },
    components: {
        'app-managers-search': ManagersSearch,
        'app-peers-select': PeersSelect,
        'app-edit-questions': EditQuestions,
        'app-date-time-picker': DateTimePicker,
    },
    mounted() {
        const self = this;
        this.$nextTick(() => {
            ajax.get('/admin/all-managers', {}, ({ data, headers }) => {
                self.managers = data;
            });
        });
    },
    watch: {
        step(val) {
            if(val > this.NUMBER_OF_STEPS - 1) {
                this.step = this.NUMBER_OF_STEPS - 1;
            }
            if(val < 0) {
                this.step = 0;
            }
        }
    },
}
</script>
<style>
.new-feedback-round-buttons {
    margin-top: 20px;
}
</style>
