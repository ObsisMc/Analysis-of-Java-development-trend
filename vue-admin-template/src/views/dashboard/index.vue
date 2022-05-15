<template>
  <div class="dashboard-container">
    <panel-group/>
    <el-row class="graph-container" :style="{height:rankHeight}" style="padding: 16px 16px;">
      <TotalRank ref="totalRank"></TotalRank>
    </el-row>
    <el-row :gutter="40">
      <el-col :span="12" class="graph-container" :style="{height:addRankHight,width:addRankWidth}" >
        <LanguageRanks ref="rank" :hgt="rankhgt"></LanguageRanks>
      </el-col>
      <el-col :span="12" class="graph-container">
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
import TotalRank from "@/views/dashboard/components/TotalRank";

export default {
  name: 'Dashboard',
  data() {
    return {}
  },
  computed: {
    ...mapGetters([
      'name'
    ]),
    rankHeight() {
      return window.innerHeight * 0.4 + "px";
    },
    addRankHight() {
      return window.innerHeight * 0.5 + "px";
    },
    addRankWidth() {
      return window.innerWidth * 0.4 + "px";
    },
    rankhgt(){
      return window.innerHeight * 0.45 + "px";
    }
  },
  components: {
    TotalRank,
    WordCloud,
    RelationshipJava,
    LanguageRanks,
    PanelGroup
  },
  mounted() {
    this.$refs.rank.draw();
    this.$refs.relation.draw();
    this.$refs.totalRank.draw();
  },
  watch: {}
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

.graph-container {
  background: #fff;
  padding: 16px 16px;
  margin-bottom: 32px;
}
</style>
