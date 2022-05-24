<template>
  <div>
    <el-row>
      <el-col :span="10">
        <MultiMetric ref="multiMetric" class="graph-container"></MultiMetric>
      </el-col>
      <el-col :span="10">
        <WorkTime  class="graph-container"></WorkTime>
      </el-col>
      <el-col :span="4">
        <div style="float:right; position: relative; bottom: 70px; right:15px;">
          <el-row style="margin-bottom: 10px;">
            <el-image v-if="repo!=='Github'"
                      style="border-radius: 50%;"
                      :style="{width:radius, height:radius}"
                      :src="url"
                      fit="fit">
            </el-image>
            <svg-icon v-if="repo==='Github'" icon-class="github" :style="{width:radius, height:radius}"/>
          </el-row>
          <el-row>
          <span class="card-repo-text">
            {{ repo }}
          </span>
          </el-row>
          <el-row>
          <span class="card-panel-text">
            Owner: {{ owner }}
          </span>
          </el-row>
          <el-row style="margin-top: 5px;">
            <svg-icon icon-class="people" style="color: dodgerblue; font-size:20px;" ></svg-icon>
            <span class="card-panel-text" style="margin-left: 5px">
              <strong>{{contributors}}</strong>  contributors
            </span>
          </el-row>
          <el-row style="margin-top: 5px;">
            <svg-icon icon-class="comment_fill" style="color: green; font-size:20px;" ></svg-icon>
            <span class="card-panel-text" style="margin-left: 5px">
              <strong>{{comments}}</strong>  comments
            </span>
          </el-row>
          <el-row style="margin-top: 5px;">
            <svg-icon icon-class="release-fill" style="color: grey; font-size:20px;" ></svg-icon>
            <span class="card-panel-text" style="margin-left: 5px">
              <strong>{{releases}}</strong>  released
            </span>
          </el-row>
          <el-row style="margin-top: 5px;">
            <svg-icon icon-class="archive" style="color: red; font-size:20px;" ></svg-icon>
            <span class="card-panel-text" style="margin-left: 5px">
              <strong>{{archives}}</strong>  archived
            </span>
          </el-row>
        </div>
      </el-col>


    </el-row>
  </div>
</template>

<script>
import WorkTime from "@/views/nested/components/WorkTime";
import MultiMetric from "@/views/nested/components/MultiMetric";

export default {
  name: "ContributorPanel",
  components: {MultiMetric, WorkTime},
  data() {
    return {
      url: 'https://avatars.githubusercontent.com/u/15308811?v=4',
      owner: 'github',
      repo: 'Github',
      contributors: 0,
      comments: 0,
      releases: 0,
      archives: 0
    }
  },
  computed: {
    radius() {
      return Math.min(window.innerWidth, window.innerHeight) * 0.3 + "px";
    }
  },
  mounted() {
    this.$refs.multiMetric.draw();
  }
}
</script>

<style scoped>
.card-repo-text {
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
