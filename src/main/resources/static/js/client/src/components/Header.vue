<template>
  <div class="header">
    <app-logout v-if="logged"></app-logout>
  </div>
</template>
<script>
import Logout from './Logout.vue';

import Lockr from '../lockr';

Lockr.prefix = 'fibimeter_';

export default {
  name: 'app-header',
  data: () => {
    return {
        logged: false,
    }
  },
  methods: {
    checkLogged() {
    this.logged = (Lockr.get('jwt', false) !== false);
    }
  },
  computed: {
  },
  mounted() {
    let self = this;
    this.$nextTick(() => {
        self.checkLogged();
        self.$eventBus.$on('logout', () => self.logged = false);
        self.$eventBus.$on('login', () => self.logged = true);
    });
  },
  components: {
      'app-logout': Logout,
  },
}

</script>
<style>
.header {
    background-color: #f3f3f3;
    -webkit-box-shadow: 0px 7px 20px -1px rgba(0,0,0,0.75);
    -moz-box-shadow: 0px 7px 20px -1px rgba(0,0,0,0.75);
    box-shadow: 0px 7px 20px -1px rgba(0,0,0,0.75);
    position: relative;
    z-index: 10;
    overflow: hidden;
    display: none;
}
</style>
