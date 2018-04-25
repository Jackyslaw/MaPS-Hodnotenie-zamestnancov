<template>
  <div class="full-fill-white">
    <div class="questions-header">
      <h5>{{ managerFullName }}</h5>
      <br>
      <span class="info-text importance-1">Fill in all the following questions.</span><br>
      <span class="info-text importance-2">1 = negative</span><span class="vertical-info-divider">|</span><span class="info-text importance-2">5 = positive</span><br>
      <span class="info-text importance-3">If you are not able to answer a question please select N/A option</span>
    </div>
    <form @submit.prevent="sendAnswers">
      <div :id="'question-container-' + question.id" class="question-container" v-for="(question, qI) in questions">
        <div class="card hoverable question-sub-container">
          <div class="question-text">
            <b>{{ qI + 1 }}. {{ question.text }}</b>
          </div>
          <div class="radio-container-cont">
            <input type="radio" class="hidden" value="-1" :name="'answers-' + question.id" />
            <div class="radio-container" v-for="i in answersCount" @click="answers[question.id] = i">
              <div class="radio-cont-inner radio-cont-upper unselectable">
                {{ i }}
              </div>
              <div class="radio-cont-inner radio-cont-lower">
                <input class="with-gap center radio-t-system" v-model="answers[question.id]" type="radio" :name="'answers-' + question.id" :value="(i)" :id="'answers-' + question.id + '-' + (i)" />
                <label class="input-rdio" v-bind:for="'answers-' + question.id + '-' + (i)"><span></span></label>
              </div>
            </div>
          </div>
          <div class="radio-cont-na">
            <input class="with-gap center radio-t-system" v-model="answers[question.id]" type="radio" :name="'answers-' + question.id" :value="(answersCount + 1)" :id="'answers-' + question.id + '-na'" />
            <label class="" v-bind:for="'answers-' + question.id + '-na'">N/A</label>
          </div>
        </div>
        <div class="error question-error" v-if="errors.uncomplete.indexOf(question.id) > -1">You forgot to fill this question</div>
      </div>
      <button class="questions-submit-btn btn waves-effect waves-light light-green lighten-1 right" type="submit">Submit
        <i class="material-icons right">send</i>
      </button>
      <div class="clearfix"></div>
    </form>
  </div>
</template>
<script>
import ajax from '../ajax';
import Lockr from '../lockr';
Lockr.prefix = 'fibimeter_';

export default {
  name: 'app-questions',
  data: () => ({
    manager: null,
    questions: [],
    answers: {},
    errors: {
      uncomplete: [],
    },

    answersCount: 5,
  }),
  methods: {
    loadAndShow(mngr) {
      const self = this;
      self.manager = mngr;
      ajax.get('/user/questions', { managerId: mngr.idEmployee }, (res, err) => {
        if(!err) {
          const questions = res.data;
          self.questions = questions;
          self.answers = self.tryLoadAnswersFromLS();
          if(!self.answers) {
            self.answers = {};
            questions.forEach(q => self.answers[q.id] = -1);
          }
        }
      });
    },
    sendAnswers() {
      const self = this;

      if(!this.validateAnswers()) {
        return;
      }

      const pureData = {  };
      ajax.post('/user/answer', { managerId: self.manager.idEmployee, answers: self.mapBeforeSend(self.answers) }, ({ data }, err) => {
        Materialize.toast('The answers were successfully submitted.', 3000, 'rounded light-green lighten-1');
        self.$emit('onAnswerSent');
      });
    },
    saveAnswersToLS(answrs) {
      Lockr.set('mngrFeedBack_' + this.manager.id, answrs);
    },
    tryLoadAnswersFromLS() {
      return Lockr.get('mngrFeedBack_' + this.manager.id, false);
    },
    mapBeforeSend(answrs) {
      return Object.keys(answrs).map(qId => JSON.parse(JSON.stringify({ questionId: qId, answer: '' + answrs[qId] })));
    },
    validateAnswers() {
      const self = this;
      if(Object.keys(this.answers).length === 0) {
        alert('This is bad.');
      }
      const uncomplete = Object.keys(this.answers).filter(qId => self.answers[qId] === -1);
      const isJohnSnow = Object.keys(this.answers).every(qId => self.answers[qId] === 6);
      if(uncomplete.length > 0) {
        this.errors.uncomplete = uncomplete;
        Materialize.toast('You forgot to fill in some questions!',  3000, 'rounded red lighten-1');
        return false;
      } else if(isJohnSnow) {
        Materialize.toast('You know nothing John Snow!',  3000, 'rounded light-green lighten-1');
      }
      return true;
    },
  },
  mounted() {
    let self = this;
    this.$nextTick(() => {
      self.$emit('onQuestionsMounted', mngr => self.loadAndShow.call(self, mngr));
    });
  },
  computed: {
    managerFullName() {
      if(this.manager) {
        return this.FIBIMETER_CONSTANTS.getFullNameFromEmployee(this.manager);
      } else {
        return "";
      }
    }
  },
  watch: {
    answers: {
        handler: function (val, oldVal) {
            this.saveAnswersToLS(val);
        },
        deep: true,
    },
  },
  components: {
  },
}

</script>
<style>
.info-text {
  color: #949494;
}
.info-text.importance-1 {
  font-size: 17px;
  font-weight: 700;
}

.info-text.importance-2 {
  font-size: 15px;
  font-weight: 400;
}

.vertical-info-divider {
  color: #d8d8d8;
  font-size: 20px;
  padding-left: 30px;
  padding-right: 30px;
  line-height: 50px;
}

.info-text.importance-3 {
  font-size: 13px;
  font-weight: 300;
}

.radio-container {
  float: left;
  margin-right: 10px;
  width: 30px;
  height: 60px;
  cursor: pointer;
}
.radio-cont-inner {
  width: 100%;
  height: 50%;
  text-align: center;
  color: rgba(189, 169, 179, 0.61);
  font-weight: normal;

  -webkit-transition: color 0.2s ease-out;
  -moz-transition: color 0.2s ease-out;
  -o-transition: color 0.2s ease-out;
  transition: color 0.2s ease-out;
}

.radio-cont-na {
  position: absolute;
  right: 20%;
  top: 50%;
}

.input-rdio {
  margin-left: 3px;
}
.question-container {
  padding: 15px 20px;

  .question-sub-container {
    padding: 10px;
  }

  .question-text {
    color: #585858;
    width: 100%;
  }
  .radio-container-cont {
    display: table;
    margin: 10px auto;
    overflow: hidden;
    background-color: #fbf9f9;
    padding: 20px;
    border-radius: 5px;

    .radio-container:hover {
      .radio-cont-inner {
        color: rgba(136, 126, 131, 0.88);
      }
    }

    .radio-container {
      background-color: #f5f5f5;
      border-radius: 20px;
      border: 1px solid #fff;
    }
  }
}

.questions-submit-btn {
  margin-right: 20px;
  margin-bottom: 20px;
}

.questions-header {

  text-align: center;
  padding-top: 5px;
  color: #505050;
  border-bottom: 6px solid #f5f5f5;
  background-color: #f9f9f9;

  h5 {
    line-height: 1.5em;
    display: inline-block;
    border-bottom: 1px solid #d6d6d6;
  }
}

.question-error {
  color: red;
  font-weight: bold;
}
</style>
