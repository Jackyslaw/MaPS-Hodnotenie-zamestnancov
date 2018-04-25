<template>
  <div class="admin-edit-questions-list">

    <slot></slot>

    <app-side-controls ref="sideControls" :controllableComponent="eqc" scid="edit-questions-sidecontrols">
        <!-- filled-in -->
        <input v-if="sublevelCheckbox" type="checkbox" class="filled-in" id="include-sub-level-questions" v-model="includeSublevel" />
        <label v-if="sublevelCheckbox" for="include-sub-level-questions">Include sub-level questions</label>
        <button class="btn waves-effect waves-light light-green lighten-1 vue-tabs-a-over admin-questions-save" type="submit" @click="saveEditingQuestions" :disabled="!saveEnabled">
            {{ saveLabel }}
        </button>
        <br>
        <button style="margin-top: 5px" class="btn waves-effect waves-light red lighten-1 vue-tabs-a-over admin-questions-save" type="submit" @click="revertEditingQuestions" :disabled="!revertEnabled">
            Revert
        </button>
    </app-side-controls>

    <select id="admin-category-select">
        <option v-for="(mLvl, cI) in mngLvls" :value="cI">
            {{ mLvl.title }}
        </option>
    </select>
    <div>
        <app-editable-questions-list @onSaveEnabledChanged="onSaveEnabledChanged" @setEditQuestionsComponent="setEditQuestionsComponent" :mngLvls="mngLvls" :mngLvl="mngLvl" :includeSublevel="includeSublevel" :saveCallApi="saveCallApi"></app-editable-questions-list>
    </div>

  </div>
</template>
<script>
import EditableQuestionsList from './EditableQuestionsList.vue';
import SideControls from './SideControls.vue';

export default {
  name: 'app-edit-questions',
  props: {
      sublevelCheckbox: {
          default: true,
      },
      saveAlwaysEnabled: {
          default: false,
      },
      saveLabel: {
          default: 'Save',
      },
      saveCallApi: {
          default: true,
      },
  },
  data: function() {
      const self = this;
      return {
        mngLvl: 0,
        mngLvls: self.FIBIMETER_CONSTANTS.getManagementLevels(),
        eqc: null,
        saveEnabled: false,
        revertEnabled: false,
        includeSublevel: true,
      }
  },
  methods: {
      setEditQuestionsComponent(eqc) {
          this.eqc = eqc;
      },
      saveEditingQuestions() {
          let editedQuestions = {};
          if(this.eqc) {
            editedQuestions = this.eqc.saveEditingQuestions();
          }
          this.$emit('onSave', this.mngLvl, editedQuestions);
      },
      revertEditingQuestions() {
          if(this.eqc) {
            this.eqc.revertEditingQuestions();
          }
      },
      onSaveEnabledChanged(val) {
          if(!this.saveAlwaysEnabled) {
            this.saveEnabled = val;
          }
          this.revertEnabled = val;
      },
  },
  mounted() {
    const self = this;
    this.saveEnabled = this.saveAlwaysEnabled;
    this.$nextTick(() => {
        const $select = $('select#admin-category-select');
        $select.material_select();
        $select.on('change', function() {
            self.mngLvl = Number($(this).val());
        });
        setTimeout(() => {
            self.$refs.sideControls.show();
        });
    });
  },
  components: {
        'app-editable-questions-list': EditableQuestionsList,
        'app-side-controls': SideControls,
  },
}

</script>
<style>
.admin-edit-questions-list {
    width: 90%;
    margin: auto;
    margin-left: 0px;
}
</style>
