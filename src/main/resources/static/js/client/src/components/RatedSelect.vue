<template>
  <div class="full-fill-white">
    <div class="managers-container">
      <div class="manager-container" v-for="manager in managers" @click="select(manager.idEmployee)">
        <span class="mngr-name">{{full_name(manager)}}</span>
        <div class="manager-info-container">
          <small class="manager-job-title">{{manager.job_title}}</small><br>
          <small class="manager-team">{{manager.team}}</small><br>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import ajax from '../ajax';

export default {
  name: 'app-rated-select',
  data: () => ({
    managers: [],
  }),
  methods: {
    full_name(mngr) {
      return this.FIBIMETER_CONSTANTS.getFullNameFromEmployee(mngr);
    },
    select(id) {
      this.$emit('onSelectRated', this.managers.find((mngr) => mngr.idEmployee === id));
    },
  },
  mounted() {
    const self = this;
    this.$nextTick(() => {
      ajax.get('/user/available-feedbacks', {}, ({ data }, err) => {
        if(!err) {
          self.managers = data;
        }
      });
    });
  },
  components: {
  },
}
</script>
<style>
.managers-container {
  padding-top: 20px;
}
.manager-container {
  padding: 10px;
  border-bottom: 1px solid rgba(136, 136, 136, 0.59);
  background-color: rgba(0, 0, 0, 0.04);
  color: #4c4c4c;
  cursor: pointer;
}

.manager-container:nth-child(1) {
  border-top: 1px solid rgba(136, 136, 136, 0.59);
}

.manager-container:nth-child(even) {
  background-color: rgba(0, 0, 0, 0.09);
}

.manager-info-container {
  line-height: 0.8em;
  border-top: 1px solid rgba(0, 0, 0, 0);
  padding-top: 5px;
}
.manager-job-title {
  font-weight: bold;
}
.manager-team {
  color: #8c8c8c;
}

.manager-container:hover {
  background-color: rgba(226, 0, 116, 0.11);
  .mngr-name {
    color: rgba(226,0,116,1);
  }
  .manager-info-container {
    padding-top: 5px;
    border-top: 1px solid #b7b7b7;

    .manager-job-title {
      color: rgb(204, 95, 151);
    }
    .manager-team {
      color: rgb(177, 99, 139);
    }
  }
}
</style>
