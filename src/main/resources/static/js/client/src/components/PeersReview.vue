<template>
    <div>
        <b>Peers Review</b>

        <div v-for="peerData in peers">
            <b>{{ getFullNameFromMngr(peerData.manager) }} <small>{{ peerData.manager.emailAddress }}</small></b><br>
            <div v-for="peerEmail in peerData.peers">
                {{ peerEmail }}
            </div>
            <button @click="reject(peerData)" class="btn waves-effect waves-light red lighten-1">
                Reject
            </button>

            <button @click="accept(peerData)" class="btn waves-effect waves-light light-green lighten-1">
                Accept
            </button>
        </div>
    </div>
</template>
<script>
import ajax from '../ajax';




export default {
    name: 'app-peers-review',
    data: () => ({
        peers: [],
    }),
    methods: {
        getFullNameFromMngr({ academicTitle, firstName, surname }) {
            const ac = academicTitle;
            const containsPhD = ac ? ac.toLowerCase().startsWith('phd') : null;
            return (ac && !containsPhD ? ac + ' ' : '')
                + firstName + ' ' + surname
                + (containsPhD ? ' ' + ac : '');
        },
        accept(peerData) {
            this.send(peerData.feedbackRoundId, 'accept', () => {
                this.peers.splice(this.peers.indexOf(peerData));
            });
        },
        reject(peerData) {
            this.send(peerData.feedbackRoundId, 'reject', () => {
                this.peers.splice(this.peers.indexOf(peerData));
            })
        },
        send(fbrndId, action, cb) {
            // ajax.post(`/admin/peers-review/${action}/${fbrndId}`, {}, cb);
            cb();
        },
    },
    components: {
        
    },
    mounted() {
        const self = this;
        this.$nextTick(() => {
            self.peers = self.$commonGlobals.reviewPeers;
        });
    },
}
</script>
<style>

</style>