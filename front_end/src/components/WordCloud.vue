<template>
  <div id="app">
    <wordcloud
      :data="defaultWords"
      nameKey="topic"
      valueKey="number"
      :color="myColors"
      :showTooltip="true"
      :wordClick="wordClickHandler">
    </wordcloud>
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
    }
  },
  data() {
    return {
      myColors: ['#1f77b4', '#629fc9', '#94bedb', '#c9e0ef'],
      defaultWords: [{
        "topic": "Cat",
        "number": 26
      },
        {
          "topic": "fish",
          "number": 19
        },
        {
          "topic": "things",
          "number": 18
        },
        {
          "topic": "look",
          "number": 16
        }
      ]
    }
  }
}
</script>

<style scoped>

</style>
