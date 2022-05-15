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
    <el-button @click="refresh" icon="el-icon-refresh-left" type="primary"
               style="float: right; z-index: 100"></el-button>
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
    }
  },
  data() {
    return {
      myColors: ['#1f77b4', '#629fc9', '#94bedb', '#c9e0ef'],
      Words: [{topic: "no Word", number: 1}],
      dialogVisible: false,
      selectedWord: '',
      value: ''
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

wordcloud:hover {
  cursor: pointer；
}
</style>
