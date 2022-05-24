<template>
  <div style="margin:16px 32px">
    <el-autocomplete
      class="inline-input"
      v-model="state"
      :fetch-suggestions="querySearch"
      placeholder="请输入内容"
      :trigger-on-focus="false"
      @select="handleSelect"
      :select-when-unmatched=true
      style="width: 80%"
    ></el-autocomplete>
  </div>
</template>

<script>
import axios from "axios";
import SearchLoading from "@/components/SearchLoading";

export default {
  name: "SearchInput",
  components: {SearchLoading},
  data() {
    return {
      state: '',
      finished: 0,
      error: false,
      need: 3
    }
  },
  methods: {
    querySearch(input, cb) {
      cb([{value: "input"}]);
    },
    handleSelect() {
      let repoURL = this.state.startsWith("http") ? this.state : "https://github.com/" + this.state;
      let pathList = this.state.split("/");
      let fullName =  pathList[0] + "/" + pathList[1];
      if(this.state.startsWith("http")){
        fullName =  pathList[3] + "/" + pathList[4];
      }

      this.$evenBus.$emit("beginSearch",fullName);
      this.$evenBus.$emit("getCommitsByTime", repoURL);
      this.$evenBus.$emit("getMetrics", repoURL);
    }
  },
  mounted() {
    this.$evenBus.$on("finishSearchRepo", (state) => {
      this.finished += 1;
      if (!state && !this.error) {
        this.$notify.error({
          message: "Invalid URL, private repo, time out or rate limit"
        })
        this.error = !this.error;
      }
      if (this.finished === this.need) {
        this.$message({
          message: "Finish searching repository",
          type: "info"
        })
        this.finished = 0;
        this.error = !this.error;
        this.$evenBus.$emit("endSearch");
      }

    })
  }
}
</script>

<style scoped>

</style>
