<template>
  <div>
    <wordcloud
      :data="Words"
      nameKey="topic"
      valueKey="number"
      :color="myColors"
      :showTooltip="false"
      :wordClick="wordClickHandler">
    </wordcloud>
    <el-slider
      v-model="wordNum"
      @change="getWordCloud"
      height="200px"
      :max="200">
    </el-slider>
    <el-dialog
      title="Info"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose">
      <el-row>
        <el-col :span="4">
          Topic:
        </el-col>
        <el-col :span="20">
          <span style="font-weight: bold;font-size: 20px;">
            {{ selectedWord }}
          </span>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="4">
          Number:
        </el-col>
        <el-col :span="20">
          <span style="font-weight: bold;font-size: 20px;">
            {{ value }}
          </span>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="dialogVisible = false">Back</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
import wordcloud from 'vue-wordcloud';

export default {
  name: "WordCloud",
  components: {
    wordcloud
  },
  data() {
    return {
      myColors: ['#1f77b4', '#629fc9', '#94bedb', '#c9e0ef'],
      Words: [{topic: "no Word", number: 1}],
      wordNum: 100,
      dialogVisible: false,
      selectedWord: '',
      value: ''
    }
  },
  methods: {
    wordClickHandler(name, value, vm) {
      this.selectedWord = name;
      this.value = value;
      this.dialogVisible = !this.dialogVisible;
    },
    refresh() {
      this.$axios.get("json/wordCloudVis.json").then(response => {
        // console.log(response.data);
        this.Words = response.data;
      })
    },
    handleClose() {
      this.dialogVisible = !this.dialogVisible;
    },
    getWordCloud() {
      this.$axios.get("http://localhost:8080/api/word_cloud", {
        params: {
          number: this.wordNum
        }
      }).then(response => {
        // console.log(response.data);
        this.Words = response.data;
      }).catch(error => {

      }).finally(() => {

      })
    }
  },
  mounted() {
    this.getWordCloud();
  }
}
</script>

<style scoped>

wordcloud:hover {
  cursor: pointer；
}
</style>
