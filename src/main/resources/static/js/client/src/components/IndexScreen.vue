<template>
  <div>
    <div class="logo-container">
      <div class="logo-sub-container">
        <img class="logo" src="/images/login_fibimeter_logo.png" />
      </div>
    </div>
    <app-login></app-login>
  </div>
</template>
<script>

import Login from './Login.vue';
export default {
  name: 'app-index-screen',
  data: () => {
    return {
    }
  },
  methods: {
  },
  mounted() {
    const self = this;
    this.$nextTick(() => {
    });
  },
  components: {
    'app-login': Login,
  },
}

</script>
<style>
.logo-container {
  text-align: center;
  margin-top: 3%;
}
.logo-sub-container {
  display: inline-block;

  padding-bottom: 1%;
  padding-top: 1%;
  border-bottom: 1px solid rgba(255, 255, 255, 0.47);
  padding-left: 50px;
  padding-right: 50px;
  border-bottom-left-radius: 20px;
  border-bottom-right-radius: 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.18);
  border-top-left-radius: 20px;
  border-top-right-radius: 20px;
}
</style>
