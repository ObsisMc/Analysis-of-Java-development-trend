<template>
  <div>
    <el-row :gutter="0">
      <el-col :span="9" >
        <MultiMetric ref="multiMetric" class="graph-container"></MultiMetric>
      </el-col>
      <el-col :span="6" style="border: transparent solid 1px;">
        <div style="float:left; position: relative; bottom: 70px; ">
          <el-row style="margin-bottom: 10px;border: transparent solid 1px;" >
            <el-col :span="4" style="border: transparent solid 1px;"></el-col>
            <el-col :span="16">
              <el-image v-if="repo!=='All Github'"
                        style="border-radius: 50%;"
                        :style="{width:radius, height:radius}"
                        :src="url"
                        fit="fit">
              </el-image>
              <svg-icon v-if="repo==='All Github'" icon-class="github" :style="{width:radius, height:radius}"/>
            </el-col>
            <el-col :span="4" style="border: transparent solid 1px;"></el-col>
          </el-row>
          <el-row>
          <span class="card-repo-text">
            {{ repo }}
          </span>
          </el-row>
        </div>
        <SearchInput style="width: 100%"></SearchInput>
      </el-col>
      <el-col :span="9">
        <WorkTime  class="graph-container" style="float: right;"></WorkTime>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import WorkTime from "@/views/nested/components/WorkTime";
import MultiMetric from "@/views/nested/components/MultiMetric";
import SearchInput from "@/views/nested/components/SearchInput";

export default {
  name: "ContributorPanel",
  components: {SearchInput, MultiMetric, WorkTime},
  data() {
    return {
      url: 'https://avatars.githubusercontent.com/u/15308811?v=4',
      repo: 'All Github',
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
  position: relative;
  left:50%;
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
