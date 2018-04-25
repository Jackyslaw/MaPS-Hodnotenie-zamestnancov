<template>
    <div>
        <div v-if="frs.length > 0">
            <b>Select Peers for Feedback Round: {{ frs[0].idFeedbackRound }}</b>
            
            <app-peers-select @onSelectPeers="selectedPeers"></app-peers-select>
        </div>
    </div>
</template>
<script>
import ajax from '../ajax';
import PeersSelect from './PeersSelect.vue';

export default {
    name: 'app-select-peers',
    data: () => ({
        frs: [],
    }),
    methods: {
        selectedPeers(peers) {
            const self = this;
            if (peers.length === 0) {
                Materialize.toast(`Peers list is empty. Consider to leave it blank without submitting if you don't need it.`, 3000, 'rounded red lighten-1');
            } else {
                ajax.post(`/manager/select-peers/${self.frs[0].idFeedbackRound}`, { peers },
                ({ data, headers }) => {
                    Materialize.toast(`Peers successfully added, wait for admin to accept them.`, 3000, 'rounded light-green lighten-1');
                    self.frs.splice(0, 1);
                    if(self.frs.length === 0) {
                        self.$emit('noMorePeers');
                    }
                });
            }
        },
    },
    components: {
        'app-peers-select': PeersSelect,
    },
    mounted() {
        const self = this;
        this.$nextTick(() => {
            self.frs = self.$commonGlobals.feedbackRounds;
        });
    },
}
</script>
<style>

</style>