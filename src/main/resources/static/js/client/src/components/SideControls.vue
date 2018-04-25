<template>
    <div :id="scid" class="side-controls">
        <slot v-if="controllableComponent">

        </slot>
    </div>
</template>
<script>

export default {
    name: 'app-side-controls',
    props: ['controllableComponent', 'scid'],
    data: () => ({
        initialTop: 0,
    }),
    methods: {
        show() {
            $('.side-controls#' + this.scid).animate({
                right: '0',
            }, 900);
        },
        hide() {
            $('.side-controls#' + this.scid).animate({
                right: '-14%',
            }, 500);
        },
        bindToFollow() {
            const self = this;
            $(window).scroll(function() {
                
                const scrollTop = $(this).scrollTop() + self.initialTop;
                $('.side-controls#' + self.scid).animate({
                    'top': scrollTop
                }, { duration: 200, queue: false });
            });
        },
    },
    components: {
        
    },
    mounted() {
        const self = this;
        this.$nextTick(() => {
            self.initialTop = $('.side-controls#' + self.scid).offset().top;
            self.bindToFollow();
        });
    },
}
</script>
<style>

.side-controls {
    position: absolute;
    right: -14%;
    top: 30%;
    width: 14%;
    padding: 10px;
    background-color: #fff;
    -webkit-box-shadow: -2px 10px 35px -4px rgba(0,0,0,0.75);
    -moz-box-shadow: -2px 10px 35px -4px rgba(0,0,0,0.75);
    box-shadow: -2px 10px 35px -4px rgba(0,0,0,0.75);
    border-bottom-left-radius: 5px;
    border-top-left-radius: 15px;

    label {
        font-size: 0.7em !important;
        line-height: 1.1em !important;
    }
}

</style>