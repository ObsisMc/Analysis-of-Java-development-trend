<template>
  <div class="hello">
    <test ref="test"></test>
    <LanguageRanks ref="ranks"></LanguageRanks>
    <RelationshipJava ref="relation"></RelationshipJava>
  </div>
</template>

<script>
import test from "./test";
import LanguageRanks from "./LanguageRanks";
import RelationshipJava from "./RelationshipJava";

export default {
  name: 'HelloWorld',

  data() {
    return {
      name: '张雪',
      xData: ['2020-02', '2020-03', '2020-04', '2020-05'],
      yData: [30, 132, 80, 134],
    }
  },
  components: {
    RelationshipJava,
    LanguageRanks,
    test
  },
  mounted() {
    this.$refs.test.initChart(this.name, this.xData, this.yData);
    this.$refs.ranks.initEcharts();
    this.$refs.ranks.runLines();
    this.$refs.relation.draw();
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}
</style>
