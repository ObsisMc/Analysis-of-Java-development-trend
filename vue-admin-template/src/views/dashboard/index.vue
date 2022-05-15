<template>
  <div class="dashboard-container">
    <panel-group />
    <el-row>
      <el-col :span="6">
        <LanguageRanks ref="rank"></LanguageRanks>
      </el-col>
      <el-col :span="6">
        <RelationshipJava ref="relation"></RelationshipJava>
      </el-col>
    </el-row>


    <WordCloud ref="wordCloud"></WordCloud>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import LanguageRanks from "@/components/LanguageRanks";
import RelationshipJava from "@/components/RelationshipJava";
import WordCloud from "@/components/WordCloud";
import PanelGroup from "@/views/dashboard/components/PanelGroup";

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  components: {
    WordCloud,
    RelationshipJava,
    LanguageRanks,
    PanelGroup
  },
  mounted() {
    this.$refs.rank.initEcharts();
    this.$refs.relation.draw();

  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }

  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}

.dashboard-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

}

</style>
