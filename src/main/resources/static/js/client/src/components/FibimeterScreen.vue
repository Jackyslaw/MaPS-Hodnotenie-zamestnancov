<template>
    <div class="full-fill-white" style="padding: 20px">
        <app-logout></app-logout>
        <vue-tabs ref="vueTabs" @tab-change="tabChange">
            <vue-tab v-if="role.isAdmin" title="Edit Questions">
                <app-edit-questions></app-edit-questions>
            </vue-tab>
            <vue-tab v-if="role.isAdmin" title="New Feedback Round">
                <app-new-feedback-round></app-new-feedback-round>
            </vue-tab>
            <vue-tab v-if="role.isAdmin && peersToReviewCount > 0">
                <span slot="title">Peers Review <span style="min-width: 1rem;" class="new badge blue" data-badge-caption="">{{peersToReviewCount}}</span></span>
                <app-peers-review></app-peers-review>
            </vue-tab>
            <vue-tab v-if="role.isManager && selectPeers" @noMorePeers="noMorePeers" title="Select Peers">
                <app-select-peers></app-select-peers>
            </vue-tab>
            <vue-tab v-if="role.isUser" title="Feedback">
                <app-rating></app-rating>
            </vue-tab>
            <vue-tab v-if="role.isAdmin" title="Statistics">
                <app-statistics></app-statistics>
            </vue-tab>
        </vue-tabs>

        <div v-if="!selectPeers && !role.isAdmin && !role.isUser && role.isManager">
            There's no available activity for now. Wait for admin who'll send you a peers to select.
        </div>
    </div>
</template>
<script>
import Logout from './Logout.vue';
import EditQuestions from './EditQuestions.vue';
import Rating from './Rating.vue';
import NewFeedbackRound from './NewFeedbackRound.vue';
import PeersReview from './PeersReview.vue';
import SelectPeers from './SelectPeers.vue';
import Statistics from './Statistics.vue';
import {VueTabs, VTab} from 'vue-nav-tabs';
import 'vue-nav-tabs/themes/vue-tabs.css';
import ajax from '../ajax';
import Lockr from '../lockr';
Lockr.prefix = 'fibimeter_';

export default {
  name: 'app-fibimeter-screen',
  data: () => {
    return {
        role: {
            isAdmin: false,
            isManager: false,
            isUser: false,
        },
        peersToReviewCount: 0,
        selectPeers: false,
    };
  },
  methods: {
    getRolesAndOtherStuff() {
        const self = this;
        ajax.get('/role', {}, (res, error) => {
            if(error && error.code === 500) {
                Lockr.rm('jwt');
                self.$eventBus.$emit('logout');
                self.$router.replace('/');
            } else {
                self.role.isAdmin = res.data.isAdmin;
                self.role.isManager = res.data.isManager;
                self.role.isUser = res.data.isUser;
                if(self.role.isAdmin) {
                    self.getReviewPeersCount();
                }
                if(self.role.isManager) {
                    self.getSelectPeers();
                }
            }
        });
    },

    getReviewPeersCount() {
        const self = this;
        ajax.get('/admin/peers-review/get', {}, ({ data, headers }) => {
            if(!data || !data.peers) {
                self.peersToReviewCount = 0;
                self.$commonGlobals.reviewPeers = [];
                return;
            }
            self.peersToReviewCount = data.peers.length;
            self.$commonGlobals.reviewPeers = data.peers;
        });
    },

    getSelectPeers() {
        const self = this;

        ajax.get('/manager/feedback-rounds', {}, ({data, headers}) => {
            self.selectPeers = data.length > 0;
            self.$commonGlobals.feedbackRounds = data;
        });
    },

    noMorePeers() {
        self.selectPeers = false;
        self.$refs.vueTabs.activeTabIndex = 0;
    },

    tabChange(ntab) {
        Lockr.set('last-active-tab', ntab);
    },
  },
  mounted() {
    const self = this;
    this.$nextTick(() => {
        self.getRolesAndOtherStuff();
        const tab = Lockr.get('last-active-tab', false);
        if(tab) {
            self.$refs.vueTabs.activeTabIndex = tab;
        }
    });
  },
  components: {
      'app-edit-questions': EditQuestions,
      'app-new-feedback-round': NewFeedbackRound,
      'app-rating': Rating,
      'app-peers-review': PeersReview,
      'app-select-peers': SelectPeers,
      'app-statistics': Statistics,
      'app-logout': Logout,
      'vue-tabs': VueTabs,
      'vue-tab': VTab,
  },
}

</script>
<style>
</style>
