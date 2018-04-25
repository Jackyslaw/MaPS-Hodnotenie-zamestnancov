<template>
  <div>
    <app-questions v-if="manager && isNaN(manager)" @onQuestionsMounted="questionsMounted" @onAnswerSent="answerSent"></app-questions>
    <app-rated-select v-if="!manager" @onSelectRated="selectedManager"></app-rated-select>
  </div>
</template>
<script>

import Questions from './Questions.vue';
import RatedSelect from './RatedSelect.vue';

import Lockr from '../lockr';

Lockr.prefix = 'fibimeter_';

export default {
  name: 'app-rating',
  data: () => ({
    manager: null,
  }),
  methods: {
    selectedManager(mngr) {
      this.manager = mngr;
    },
    questionsMounted(func) {
      func(this.manager);
    },
    checkIfLoggedIn() {
      const token = Lockr.get('jwt', false);
      if(!token) {
          this.$router.replace('/');
      }
    },
    answerSent() {
      this.manager = null;
    }
  },
  mounted() {
    let self = this;
    this.$nextTick(() => {
      self.checkIfLoggedIn();
    });
  },
  components: {
    'app-questions': Questions,
    'app-rated-select': RatedSelect,
  },
}

</script>
<style>

</style>
