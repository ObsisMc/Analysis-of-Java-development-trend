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
      need: 3,
      success:0,
      fullName: "All Github"
    }
  },
  methods: {
    querySearch(input, cb) {

    },
    handleSelect() {
      let repoURL = this.state.startsWith("http") ? this.state : "https://github.com/" + this.state;
      let pathList = this.state.split("/");
      this.fullName =  pathList[0] + "/" + pathList[1];
      if(this.state.startsWith("http")){
        this.fullName =  pathList[3] + "/" + pathList[4];
      }

      this.$evenBus.$emit("beginSearch");
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
      }else{
        this.success ++;
      }
      if (this.finished === this.need) {
        if(this.success > 0){
          this.$evenBus.$emit("updateNameAvatar", this.fullName);
        }
        this.$message({
          message: "Finish searching repository",
          type: "info"
        })
        this.finished = 0;
        this.success = 0;
        this.error = !this.error;
        this.$evenBus.$emit("endSearch");
      }

    })
  }
}
</script>

<style scoped>

</style>
