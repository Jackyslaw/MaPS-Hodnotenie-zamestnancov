<template>
    <div>
        <slot></slot>
        <div class="chips-placeholder add-peers-chip">
        </div>
        <small><b>Hint:</b> to add new peer hit enter.</small><br>
        <button style="margin-top: 20px" @click="done" class="btn small waves-effect waves-light light-green lighten-1">Done
        </button>
    </div>
</template>
<script>
export default {
    name: 'app-peers-select',
    data: () => ({
        
    }),
    methods: {
        done() {
            this.$emit('onSelectPeers', $('.chips-placeholder').material_chip('data'));
        }
    },
    components: {
        
    },
    mounted() {
        const self = this;
        this.$nextTick(() => {
            $('.chips-placeholder').material_chip({
                placeholder: 'Add peer',
                secondaryPlaceholder: 'Add another peer',
            });
        });
    },
}
</script>
<style>
.chips-placeholder.add-peers-chip {
    margin-bottom: 2px;
}
</style>