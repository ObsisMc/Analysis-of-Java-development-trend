<template>
<div>
  <el-row>
    <CommitByTime :height="commitHeight" class="graph-container"></CommitByTime>
  </el-row>

</div>
</template>

<script>
import CommitByTime from "@/views/nested/components/timeseries/CommitByTime";
export default {
  name: "TimeShow",
  components:{CommitByTime},
  computed:{
    commitHeight(){
      return window.innerHeight * 0.35 + "px";
    }
  },
  mounted(){
  }
}
</script>

<style scoped>
.graph-container {
  background-color: rgb(255, 255, 255);
  padding: 16px 16px;
  margin-bottom: 32px;
}
</style>
