<template>
  <div class="app-container">
    <el-row>
      <Search></Search>
    </el-row>
    <el-row style="margin-bottom: 16px">
      <WordCloud></WordCloud>
    </el-row>
    <el-row >
      <el-col class="graph-container" :span="11">
        <RelationshipJava ref="relation" :hgt="relationHeight"></RelationshipJava>
      </el-col>
      <el-col :span="1" style="border: 1px solid transparent"></el-col>
      <el-col class="graph-container" :span="12">

          <License ref="license"></License>

      </el-col>

    </el-row>


  </div>
</template>

<script>
import Search from "@/views/form/components/Search";
import WordCloud from "@/views/form/components/WordCloud";
import RelationshipJava from "@/views/form/components/RelationshipJava";
import License from "@/views/form/components/License";
export default {
  components: {License, RelationshipJava, Search, WordCloud},
  data() {
    return {

    }
  },
  computed:{
    relationHeight(){
      return window.innerHeight * 1.2 + "px";
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
  },
  mounted() {
    this.$refs.relation.draw();
    this.$refs.license.draw();
  }
}
</script>

<style scoped>

.graph-container {
  background-color: rgb(240, 242, 245);
  padding: 16px 16px;
  margin-bottom: 32px;
}
</style>

