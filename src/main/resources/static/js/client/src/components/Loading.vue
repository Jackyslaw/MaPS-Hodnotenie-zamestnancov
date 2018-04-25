<template>
    <div id="loading" v-show="loadingProcesses !== 0">
        <div id="loading-circle" class="preloader-wrapper active">
            <div class="spinner-layer t-systems-border-color">
                <div class="circle-clipper left">
                    <div class="circle"></div>
                </div>
                <div class="gap-patch">
                    <div class="circle"></div>
                </div>
                <div class="circle-clipper right">
                    <div class="circle"></div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
export default {
    name: 'app-loading',
    data: () => ({
        loadingProcesses: 0,
    }),
    methods: {
    },
    components: {
        
    },
    mounted() {
        const self = this;
        this.$nextTick(() => {
            self.$eventBus.$on('loadingStart', () => self.loadingProcesses++);
            self.$eventBus.$on('loadingEnd', () => self.loadingProcesses--);
        });
    },
}
</script>
<style>

#loading {
    z-index: 10000000;
    position: fixed;
    width: 100%;
    height: 100%;
    
    #loading-circle {
        left: 50%;
        top: 50%;
        margin-top: -25px;
        margin-left: -25px;
    }
}

</style>