<template>
  <div class="app-container">
    <el-row>
      <Search></Search>
    </el-row>


  </div>
</template>

<script>
import Search from "@/views/form/components/Search";
export default {
  components: {Search},
  data() {
    return {

    }
  },
  methods: {
    onSubmit() {
      this.$message('submit!')
    },
    onCancel() {
      this.form.date='';
      this.form.sizeMin='';
      this.form.sizeMax='';
      this.form.starMin='';
      this.form.starMax='';
      this.$message({
        message: 'cancel!',
        type: 'warning'
      });

    },
    handleChange(val) {
      console.log(val);
    }
  }
}
</script>

<style scoped>
.line{
  text-align: center;
}
</style>

