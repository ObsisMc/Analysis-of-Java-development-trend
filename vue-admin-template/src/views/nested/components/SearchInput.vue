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
      need: 1
    }
  },
  methods: {
    querySearch(input, cb) {
      cb([{value: "input"}]);
    },
    handleSelect() {
      this.$evenBus.$emit("beginSearch");
      let repoURL = this.state.startsWith("http") ? this.state : "https://github.com/" + this.state;
      this.$evenBus.$emit("getCommitsByTime", repoURL);
    }
  },
  mounted() {
    this.$evenBus.$on("finishSearchRepo", (state) => {
      this.finished += 1;
      console.log(state, this.error);
      if (!state && !this.error) {
        this.$notify.error({
          message: "Invalid URL or private repo"
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
