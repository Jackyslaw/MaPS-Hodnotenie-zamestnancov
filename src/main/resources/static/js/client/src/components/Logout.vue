<template>
    <img class="logout-link" src="/images/sign-out.png" @click="logout" />
    <!--<a @click.prevent="logout">Logout</a> -->
</template>
<script>
import Lockr from '../lockr';
import ajax from '../ajax';

Lockr.prefix = 'fibimeter_';

export default {
  name: 'app-fibimeter',
  data: () => ({
  }),
  methods: {
      logout() {
          // ajax.post('login', {}, () => { ... })
          Lockr.rm('jwt');
          this.$eventBus.$emit('logout');
          this.$router.replace('/');
      }
  },
  mounted() {
    let self = this;
    this.$nextTick(() => {
    });
  },
}

</script>
<style scoped>
.logout-link {
    margin-top: -10px;
    margin-bottom: 2px;
    width: 50px;
    float: right;
    padding: 5px;
    cursor: pointer;
    -webkit-filter: blur(0.5pt) brightness(100%);
    filter: blur(0.5pt) brightness(100%);

    -webkit-transition: filter 0.2s ease-in;
    -moz-transition: filter 0.2s ease-in;
    -o-transition: filter 0.2s ease-in;
    transition: filter 0.2s ease-in;
}
.logout-link:hover {
    -webkit-filter: blur(0pt) brightness(150%);
    filter: blur(0pt) brightness(150%);
}
</style>
