<template>
  <div id="app" style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
    <wordcloud
      :data="Words"
      nameKey="topic"
      valueKey="number"
      :color="myColors"
      :showTooltip="true"
      :wordClick="wordClickHandler">
    </wordcloud>
    <el-button @click="changeRange">
      change
    </el-button>
  </div>
</template>

<script>
import wordcloud from 'vue-wordcloud';

export default {
  name: "WordCloud",
  components: {
    wordcloud
  },
  methods: {
    wordClickHandler(name, value, vm) {
      console.log('wordClickHandler', name, value, vm);
    },
    changeRange(){
      this.$axios.get("json/wordCloudVis.json").then(response => {
        // console.log(response.data);
        this.Words = response.data;
      })
    }
  },
  data() {
    return {
      myColors: ['#1f77b4', '#629fc9', '#94bedb', '#c9e0ef'],
      Words: [{topic:"no Word", number:1}]
    }
  },
  mounted() {
    this.$axios.get("json/wordCloudVis.json").then(response => {
      // console.log(response.data);
      this.Words = response.data;
    })
  }
}
</script>

<style scoped>

</style>
