<template>
  <div>
    <app-loading></app-loading>
    <app-header></app-header>
    <div class="content container">
      <router-view></router-view>
    </div>
  </div>
</template>
<script>
import Header from './Header.vue';
import Loading from './Loading.vue';

export default {
  name: 'app-fibimeter',
  data: () => ({
  }),
  methods: {
  },
  mounted() {
    let self = this;
    this.$nextTick(() => {
    });
  },
  components: {
    'app-header': Header,
    'app-loading': Loading,
  }
}

</script>
<style scoped>
.container {
    /*height:auto !important;
    height:100%;*/
    min-height:100%;
}
</style>
