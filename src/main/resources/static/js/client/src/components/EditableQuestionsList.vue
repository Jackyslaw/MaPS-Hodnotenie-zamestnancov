<template>
  <div>
    <app-add-new-question :questionCategories="questionCategories" :qtFormats="qtFormats" :mngmnt="intLvlStringRepresentation(mngLvl)" @addNewQuestion="addNewQuestion"></app-add-new-question>
    <app-prompt ref="revertPrompt" :pid="'revert-edit' + Math.floor(Math.random() * 999)" yes="Yes" no="No">
        <h4>Revert Editing</h4>
        <p>Are you sure you want to revert your editing questions?</p>
        <small>All your unsaved changes will be lost</small>
    </app-prompt>
    <div class="admin-question" v-for="(question, qI) in currentQuestions" v-if="question.stateChange !== 'DELETED'">
        <div>
            <div class="admin-question-txt-container">
                <div class="admin-question-title" style="cursor: pointer;" @click.prevent="showHide(question.id)">
                    <input @click.prevent.stop="0" @keyup.enter="finishEditingQuestion()" :id="'admin-edit-question-text-input-' + question.id + '-title'" v-if="editing === question.id && editingKey === 'title'" v-model="question.title" type="text" class="browser-default admin-editing-question-input" />
                    <div class="inline-block" v-else>
                        <div v-if="question.btwLvl" class="inline-block admin-question-lvl circle tooltipped" data-position="bottom" data-delay="50" :data-tooltip="lvlStringRepresentation(question.lvl)">
                            {{ question.btwLvl[0].toUpperCase() }}
                        </div>
                        <span>
                            {{ (qI + 1) }}. {{ question.title }}
                        </span>
                    </div>
                </div>
                <div class="admin-question-sub-text-cont" v-show="question.shown">
                    <div class="admin-question-text admin-question-sub" v-for="(value, key) in question.text">
                        <input :id="'admin-edit-question-text-input-' + question.id + '-' + key" @keyup.enter="finishEditingQuestion()" v-if="editing === question.id && editingKey === key" type="text" v-model="question.text[key]" class="browser-default admin-editing-question-input" />
                        <span class="truncated-span" style="font-size: 11px" v-else>{{ value }}</span>
                        <hr class="admin-question-sub-text-underline">
                    </div>
                </div>
                <div style="clear: both;"></div>
            </div>
            <div class="admin-question-txt-container">
                <div class="admin-question-category">
                    <select @change="editQuestion(question.id, 'category');" v-model="question.questionCategoryId" :id="'admin-edit-question-text-input-' + question.id + '-category'" class="browser-default admin-add-question-input">
                        <option v-for="val in questionCategories" :value="val.questionCategoryId">
                            {{val.categoryName}}
                        </option>
                    </select>
                </div>
            </div>

            <div class="admin-question-controls" @click.prevent="showHide(question.id)">
                <div class="admin-question-title-controls">
                    <i v-if="editing === -1" class="material-icons icon-btn admin-edit" @click.stop.prevent="editQuestion(question.id, 'title')">edit</i>
                    <i v-if="editing === -1" class="material-icons icon-btn admin-remove" @click.stop.prevent="removeQuestion(question.id)">remove_circle_outline</i>
                    <i v-if="editing === question.id && (editingKey === 'title' || editingKey === 'category')" class="material-icons icon-btn admin-save-edit" @click.stop.prevent="finishEditingQuestion()">save</i>
                    
                    <div class="admin-question-caret-container">
                        <span class="admin-question-caret" v-if="question.shown">&#9650;</span>
                        <span class="admin-question-caret" v-else>&#9660;</span>
                    </div>
                </div>
                <div v-show="question.shown">
                    <div v-for="(val, key) in question.text" class="admin-question-text-control">
                        <i v-if="editing === -1" class="material-icons icon-btn admin-edit" @click.stop.prevent="editQuestion(question.id, key)">edit</i>
                        <i v-if="editing === question.id && editingKey === key" class="material-icons icon-btn admin-save-edit" @click.stop.prevent="finishEditingQuestion()">save</i>
                    </div>
                </div>
            </div>
            <br />
        </div>
    </div>
  </div>
</template>
<script>
import AddNewQuestion from './AddNewQuestion.vue';
import Prompt from './Prompt.vue';
import { stringUtil } from '../utils';
import ajax from '../ajax';

const createEmptyQuestion = () => {
    return { id: null, title: '',  text: {
        self: '',
        underman: '',
        shipmate: '',
        chief: '',
    }};
}


export default {
  name: 'app-editable-questions-list',
  props: ['mngLvl', 'mngLvls', 'includeSublevel', 'saveCallApi'],
  data: () => {
    return {
        currentQuestions: [],
        questions: null,
        addingQuestion: createEmptyQuestion(),
        uniqueId: 3,
        editing: -1,
        editingKey: 'title',
        qtFormats: {
            self: 'Self Evaluation',
            underman: 'Underman Evaluation',
            shipmate: 'Shipmate Evaluation',
            chief: 'Chief Evaluation',
        },

        saveEnabled: false,
        questionCategories: [],
    };
  },
  methods: {
      saveEditingQuestions() {
        this.setQuestions();
        if(!this.saveCallApi) {
            return this.questions;
        } else {
            ajax.post('/admin/valid-questions', this.questions, () => {
                Materialize.toast('Edited questions have been successfully saved.', 3000, 'rounded light-green lighten-1');
            });
        }
        this.saveEnabled = false;
      },
      revertEditingQuestions() {
          this.$refs.revertPrompt.show(() => {
            this.loadQuestions(questions => {
                this.questions = questions;
                this.getQuestions();
            });
            this.saveEnabled = false;
          })
      },
      removeQuestion(id) {
          const actualQ = this.currentQuestions.find(q => q.id === id);
          actualQ.stateChange = 'DELETED';
          this.getQuestions();
          this.saveEnabled = true;
      },
      editQuestion(id, key = 'title') {
          this.editing = id;
          this.editingKey = key;
          this.$nextTick(() => {
            $('body').find('#admin-edit-question-text-input-' + id + '-' + key)
                .focus()
                .select();
          });
      },
      finishEditingQuestion(question, key) {
        const actualQ = this.currentQuestions.find(q => q.id === this.editing);
        if(actualQ) {
            actualQ.stateChange = 'CHANGED';
        }
        this.editing = -1;
        this.editingKey = 'title';
        this.saveEnabled = true;
      },
      addNewQuestion(q) {
          const copy = JSON.parse(JSON.stringify(q));
          copy.id = Math.floor(Math.random() * 5000);
          copy.stateChange = 'NEW';
          copy.lvl = this.mngLvl;
          this.currentQuestions.push(copy);
          this.saveEnabled = true;
      },
      getQuestions(questions, mngLvl, unshowAll = false) {
        questions = questions || this.questions;
        mngLvl = (!mngLvl && isNaN(mngLvl)) ? this.mngLvl : mngLvl;
        let result = [];
        for(let i = mngLvl; i >= 0; i--) {
            if(i === mngLvl || this.includeSublevel) {
                const copiedQ = [ ...questions[this.FIBIMETER_CONSTANTS.orderManagementLevels[i]] ].map(q => {
                    if(unshowAll) {
                        q.shown = false;
                    }
                    if(i !== mngLvl) {
                        q.btwLvl = this.FIBIMETER_CONSTANTS.orderManagementLevels[i];
                    } else {
                        q.btwLvl = null;
                    }
                    q.lvl = this.FIBIMETER_CONSTANTS.orderManagementLevels[i];
                    return q;
                });
                result = [ ...result, ...copiedQ ];
            }
        }
        this.currentQuestions = result;
        this.$nextTick(() => $('.tooltipped').tooltip({delay: 50}));
      },
      showHide(id) {
          this.traverseQuestions(this.questions, (q) => {
              if(q.id === id && !q.shown) {
                  q.shown = true;
              } else {
                  q.shown = false;
              }
          });

          this.getQuestions(this.questions, this.mngLvl);
      },
      traverseQuestions(questions, action) {
          for(let key in this.questions) {
              for(let i in this.questions[key]) {
                  action(this.questions[key][i], i, this.questions[key]);
              }
          }
      },
      lvlStringRepresentation(lvl) {
          return this.FIBIMETER_CONSTANTS.lvlStringRepresentation(lvl);
      },
      intLvlStringRepresentation(lvl) {
          return this.lvlStringRepresentation(this.FIBIMETER_CONSTANTS.orderManagementLevels[this.mngLvl]);
      },
      loadQuestions(cb) {
        ajax.get('/admin/valid-questions', {}, ({ data, headers }) => {
            cb(data);
        });
      },
      setQuestions() {
        Object.keys(this.questions).forEach(level => this.questions[level] = []);
        this.currentQuestions.forEach((currentQ) => {
            this.questions[currentQ.lvl].push(currentQ);
        })
      },
        loadQuestionCategories(cb) {
            ajax.get('/admin/question-categories', {}, ({ data }) => {
                cb(data);
            });
        },
        getQuestionCategory(id) {
            return this.questionCategories.find((val) => val.questionCategoryId === id);
        },
},
  mounted() {
    const self = this;
    this.$nextTick(() => {
        self.$emit('setEditQuestionsComponent', self);
        self.loadQuestions((questions) => {
            self.questions = questions;
            self.getQuestions(self.questions, self.mngLvl);
        });
        self.loadQuestionCategories((questionCategories) => {
            self.questionCategories = questionCategories;
        });
    });
  },
  watch: {
      mngLvl(val) {
          this.getQuestions(self.questions, val, true);
      },
      saveEnabled(val, oldVal) {
          if(val !== oldVal) {
              this.$emit('onSaveEnabledChanged', val);
          }
      },
      includeSublevel(val) {
          this.includeSublevel = val;
          this.getQuestions();
          return false;
      }
  },
  components: {
      'app-add-new-question': AddNewQuestion,
      'app-prompt': Prompt,
  },
}

</script>
<style>

.truncated-span {
    display:inline-block;
    width:180px;
    white-space: nowrap;
    overflow:hidden !important;
    text-overflow: ellipsis;
}

hr.admin-question-sub-text-underline {
    width: 150%;
    border-top: 1px solid #fff;
}

.admin-question-text-control {
    margin-top: 10px;
    height: 30px;
}

.admin-question-caret-container {
    float: right;
    margin-top: 3px;
    margin-left: 7px;
    cursor: pointer;
}

.admin-question-sub {
    height: 30px;
    margin-top: 10px;
}

.admin-question-lvl {
    font-weight: 100;
    font-size: 10px;
    border: 1px solid #d6d6d6;
    margin-right: 5px;
    min-width: 18px;
    padding: 3px;
    text-align: center;
    line-height: 10px;
}

.admin-add-question {
    margin-top: 20px;

    .admin-add-question-text {
        width: 89%;
        line-height: 25px;
        font-size: 16px;
        padding-left: 5px;
        border: 1px solid #8e8e8e;
        color: #a01f61;
        border-radius: 20px;
        position: relative;
        top: 3px;
        background-color: #f5f5f5;
        outline: none;
    }
}

.admin-question {
    margin-top: 10px;
    border-bottom: 1px solid #e6e6e6;
    padding-bottom: 10px;
    overflow: hidden;
    
    .admin-editing-question-input {
        width: 100%;
        border: none;
        outline: none;
        cursor: text;
    }

    .admin-question-txt-container {
        float: left;
        width: 40%;
        height: auto;

        .admin-question-title {
            font-weight: bold;
            width: 100%;
            height: 40px;
            overflow: ellipsis;
        }

        .admin-question-text {
            margin-left: 50px;
        }

        .admin-question-category {
            font-weight: normal;
            width: 100%;
            height: 40px;
            overflow: ellipsis;
        }

        .admin-add-question-input {
            width: 100%;
            outline: none;
            border-radius: 5px;
            border: 1px solid #b9b9b9;
            padding-left: 10px;
            color: #616161;
        }
    }

    .admin-question-txt-container.more-space {
        width: 93%;
    }

    .admin-question-controls {
        float: right;
        
        .admin-question-title-controls {
            height: 40px;
        }
    }
}
</style>
