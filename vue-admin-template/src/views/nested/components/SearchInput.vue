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

export default {
  name: "SearchInput",
  data() {
    return {
      state: '',
      finished: 0,
      need: 1
    }
  },
  methods: {
    querySearch(input, cb) {
      cb([{value:"input"}]);
    },
    handleSelect() {
      console.log(this.state);
      let repoURL = this.state.startsWith("http") ? this.state : "https://github.com/" + this.state;
      this.$evenBus.$emit("getCommitsByTime", repoURL);
    }
  },
  mounted() {
    this.$evenBus.$on("finishSearchRepo", () => {
      this.finished += 1;
      if (this.finished === this.need) {
        this.$message({
          message: "Finish searching repository",
          type: "success"
        })
        this.finished = 0;
      }
    })
  }
}
</script>

<style scoped>

</style>
