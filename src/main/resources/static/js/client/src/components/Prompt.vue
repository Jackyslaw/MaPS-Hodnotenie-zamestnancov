<template>
    <div :id="'prompt-' + pid" class="modal">
        <div class="modal-content">
            <slot>
            </slot>
        </div>
        <div class="modal-footer">
            <a href="#!" class="btn modal-action modal-close waves-effect waves-light red lighten-1">{{no}}</a>
            <a class="btn modal-action waves-effect waves-light light-green lighten-1" @click="yesClick">{{yes}}</a>
        </div>
    </div>
</template>
<script>
export default {
    name: '',
    props: ["pid", "yes", "no"],
    data: () => ({
        agreeCbs: [],
    }),
    methods: {
        yesClick() {
            this.agreeCbs.forEach(cb => cb());
            this.hide();
        },
        show(agreeCb) {
            $('#prompt-' + this.pid).modal('open');
            this.agreeCbs.push(agreeCb);
        },
        hide() {
            $('#prompt-' + this.pid).modal('close');
        },
    },
    components: {
        
    },
    mounted() {
        const self = this;
        this.$nextTick(() => {
            $('#prompt-' + this.pid).modal();
        });
    },
}
</script>
<style>

</style>