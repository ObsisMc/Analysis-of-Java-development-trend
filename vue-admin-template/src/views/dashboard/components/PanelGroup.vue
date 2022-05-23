<template>
  <div>
    <el-row :gutter="40" class="panel-group">
      <el-col :xs="16" :sm="16" :lg="8" class="card-panel-col">
        <div class="card-panel" @click="showPartOneChart">
          <div class="card-panel-icon-wrapper icon-people">
            <svg-icon icon-class="peoples" class-name="card-panel-icon"/>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              Users
            </div>
            <count-to :start-val="0" :end-val="users" :duration="2600" class="card-panel-num"/>k
          </div>
        </div>
      </el-col>
      <el-col :xs="16" :sm="16" :lg="8" class="card-panel-col">
        <div class="card-panel" @click="showPartOneChart">
          <div class="card-panel-icon-wrapper icon-message">
            <svg-icon icon-class="git-repository" class-name="card-panel-icon"/>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              Repositories
            </div>
            <count-to :start-val="0" :end-val="repos" :duration="3000" class="card-panel-num"/>k
          </div>
        </div>
      </el-col>
      <el-col :xs="16" :sm="16" :lg="8" class="card-panel-col">
        <div class="card-panel" @click="showPartOneChart">
          <div class="card-panel-icon-wrapper icon-money">
            <svg-icon icon-class="issue-opened" class-name="card-panel-icon"/>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              Open issues
            </div>
            <count-to :start-val="0" :end-val="issues" :duration="3200" class="card-panel-num"/>k
          </div>
        </div>
      </el-col>
    </el-row>
    <el-dialog
      :visible.sync="dialogVisible"
      width="60%">
      <span slot="footer" class="dialog-footer"> </span>
      <ThreeValueByTime ref="threeCharts"></ThreeValueByTime>
    </el-dialog>
  </div>

</template>

<script>
import CountTo from 'vue-count-to'
import ThreeValueByTime from "@/views/dashboard/components/ThreeValueByTime";
import axios from "axios";

export default {
  components: {
    ThreeValueByTime,
    CountTo
  },
  data() {
    return {
      dialogVisible: false,
      users: 3492,
      repos: 10672,
      issues: 22065
    }
  },
  methods: {
    showPartOneChart() {
      this.dialogVisible = !this.dialogVisible;
    },
    getData(){
    }
  },
  mounted() {

  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width: 550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
