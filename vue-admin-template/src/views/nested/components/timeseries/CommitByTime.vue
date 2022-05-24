<template>
  <div id="commitByTime" style="width:100%;" :style="{height:height}">
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "CommitByTime",
  props: ["height"],
  data() {
    return {
      defaultOption: null,
      option: null,
      chartDom: null,
      myChart: null
    }
  },
  methods: {
    draw() {
      this.myChart.setOption(this.option);
    },
    getData(url) {
      let success = true;
      axios.get("http://localhost:8080/api/commit_times", {
        params: {
          url: url,
          identity: this.$store.getters.passwd
        }
      }).then(response => {
        var responseData = eval("(" + response.data + ")");
        let data = [];
        let date = [];
        for (let i = 0; i < responseData.length; i++) {
          data.push(responseData[i].count);
          date.push(responseData[i].date);
        }
        this.option.xAxis.data = date;
        this.option.series.data = data;
        this.draw();

        this.$evenBus.$emit("getWorkTime", url);
      }).catch(error => {
        // todo: maybe bug
        this.option = this.defaultOption;
        this.draw();
        success = false;
      }).finally(() => {
        this.$evenBus.$emit("finishSearchRepo", success);
      })
    },
    init() {
      this.chartDom = document.getElementById('commitByTime');
      this.myChart = this.$echarts.init(this.chartDom);

      const date = [];
      const data = []
      this.defaultOption = {
        // Make gradient line here
        visualMap: [
          {
            show: false,
            type: 'continuous',
          }
        ],
        title: [
          {
            left: 'center',
            text: 'Commit Number by time'
          }
        ],
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          data: date
        },
        yAxis: [{}],
        series:
          {
            type: 'line',
            showSymbol: false,
            data: data
          }

      };
      this.option = this.defaultOption;
      this.draw();
    }
  },
  mounted() {
    this.$evenBus.$on("getCommitsByTime", (url) => {
      this.getData(url)
    })
    this.init();
  }
}
</script>

<style scoped>

</style>
