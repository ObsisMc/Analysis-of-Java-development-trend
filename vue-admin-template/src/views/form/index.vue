<template>
  <div class="app-container">
    <el-row>
      <Search></Search>
    </el-row>
    <el-row>
      <WordCloud></WordCloud>
    </el-row>
    <el-row >
      <el-col class="graph-container">
        <RelationshipJava ref="relation" :hgt="relationHeight"></RelationshipJava>
      </el-col>

    </el-row>


  </div>
</template>

<script>
import Search from "@/views/form/components/Search";
import WordCloud from "@/views/form/components/WordCloud";
import RelationshipJava from "@/views/form/components/RelationshipJava";
export default {
  components: {RelationshipJava, Search, WordCloud},
  data() {
    return {

    }
  },
  computed:{
    relationHeight(){
      return window.innerHeight * 0.4 + "px";
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

