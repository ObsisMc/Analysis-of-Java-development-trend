<template>
  <div>
    <el-row :gutter="0">
      <el-col :span="9" >
        <MultiMetric ref="multiMetric" class="graph-container"></MultiMetric>
      </el-col>
      <el-col :span="6" style="border: transparent solid 1px;">
        <div style="float:left; position: relative; bottom: 55px; ">
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
              <span class="card-repo-text">
            {{ repo }}
          </span>
            </el-col>
            <el-col :span="4" style="border: transparent solid 1px;"></el-col>
          </el-row>
          <el-row>

          </el-row>
          <div :style="{height:height}" style="border: transparent solid 1px; margin-top: 20px;">
            <transition name="component-fade" mode="out-in">
              <component v-bind:is="view" style="width: 100%; position: center" ></component>
            </transition>
          </div>
        </div>

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
import SearchLoading from "@/components/SearchLoading";
export default {
  name: "ContributorPanel",
  components: {SearchInput, MultiMetric, WorkTime,SearchLoading},
  data() {
    return {
      url: 'https://avatars.githubusercontent.com/u/15308811?v=4',
      repo: 'All Github',
      contributors: 0,
      comments: 0,
      releases: 0,
      archives: 0,
      view: SearchInput
    }
  },
  computed: {
    radius() {
      return Math.min(window.innerWidth, window.innerHeight) * 0.3 + "px";
    },
    height() {
      return window.innerHeight * 0.08 + "px";
    }
  },
  mounted() {
    this.$refs.multiMetric.draw();
    this.$evenBus.$on("beginSearch", () => {
      this.loading = true;
      this.view = SearchLoading;
    })
    this.$evenBus.$on("endSearch", () => {
      this.loading = false;
      this.view = SearchInput;
    })
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

.component-fade-enter-active, .component-fade-leave-active {
  transition: opacity .3s ease;
}

.component-fade-enter, .component-fade-leave-to
  /* .component-fade-leave-active for below version 2.1.8 */
{
  opacity: 0;
}
</style>
