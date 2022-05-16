<template>
  <div>
    <el-row>
      <el-col :span="8">
        hi
      </el-col>
      <el-col :span="8">
        hi
      </el-col>
      <el-col :span="8">

        <el-image
          style="width: 100px; height: 100px;border-radius: 50%;"
          :src="url"
          fit="fit"></el-image>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "ContributorPanel",
  data() {
    return {
      url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg'
    }
  }
}
</script>

<style scoped>

</style>
