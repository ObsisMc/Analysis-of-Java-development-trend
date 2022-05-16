<template>
  <div>
    <el-row>
      <el-col :span="10">
        hi
      </el-col>
      <el-col :span="10">
        <WorkTime ref="workTime" class="graph-container"></WorkTime>
      </el-col>
      <el-col :span="4" >
      <div style="float:right; position: relative; bottom: 50px; right:25px;" >
        <el-row>
          <el-image v-if="repo!=='Github'"
            style="border-radius: 50%;"
            :style="{width:radius, height:radius}"
            :src="url"
            fit="fit">
          </el-image>
          <svg-icon v-if="repo==='Github'" icon-class="github" :style="{width:radius, height:radius}" />
        </el-row>
        <el-row>
          <span class="card-repo-text">
            {{repo}}
          </span>
        </el-row>
        <el-row>
          <span class="card-panel-text">
            Owner: {{owner}}
          </span>
        </el-row>
      </div>
      </el-col>


    </el-row>
  </div>
</template>

<script>
import WorkTime from "@/views/nested/components/WorkTime";
export default {
  name: "ContributorPanel",
  components: {WorkTime},
  data() {
    return {
      url: 'https://avatars.githubusercontent.com/u/15308811?v=4',
      owner: 'github',
      repo: 'Github'
    }
  },
  computed:{
    radius(){
      return Math.min(window.innerWidth, window.innerHeight) *0.25 +"px";
    }
  },
  mounted() {
    this.$refs.workTime.draw();
  }
}
</script>

<style scoped>
.card-repo-text{
  font-size: 25px;
  margin-bottom: 12px;
}
.card-panel-text {
  line-height: 18px;
  color: rgba(0, 0, 0, 0.6);
  font-size: 16px;
  margin-bottom: 12px;
}
.graph-container {
  background-color: rgb(255, 255, 255);
  padding: 16px 16px;
  margin-bottom: 32px;
}
</style>
