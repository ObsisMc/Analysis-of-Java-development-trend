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
      myCharts: null
    }
  },
  methods: {
    draw() {
      this.myChart.setOption(this.option);
    },
    getData(url) {
      axios.get("http://localhost:8080/api/commit_times", {
        params: {
          url: url
        }
      }).then(response => {
        var responseData = response.data;

        let data = [];
        let date = [];
        for (let key in responseData) {
          date.push(key);
          data.push(responseData[key]);
        }
        this.data = data;
        this.date = date;
        this.option.xAxis.data = date;
        this.option.series.data = data;
        this.draw();
        this.$evenBus.$emit("finishSearchRepo");
      }).catch(error => {
        // todo: maybe bug
        this.option = this.defaultOption;
        this.draw();
        this.$message({
          message: "Invalid URL or non-public repository",
          type: "error"
        })
      }).finally(() => {

      })
    },
    init(){
      this.chartDom = document.getElementById('commitByTime');
      this.myChart = this.$echarts.init(this.chartDom);

      let base = +new Date(1968, 9, 3);
      let oneDay = 24 * 3600 * 1000 * Math.random();
      let date = [];
      let data = [Math.random() * 300];
      for (let i = 1; i < 20000; i++) {
        var now = new Date((base += oneDay));
        date.push([now.getFullYear(), now.getMonth() + 1, now.getDate()].join('-'));
        data.push(Math.round((Math.random() - 0.5) * 20 + data[i - 1]));
      }

      this.defaultOption = {
        tooltip: {
          trigger: 'axis',
          position: function (pt) {
            return [pt[0], '10%'];
          }
        },
        title: {
          left: 'center',
          text: 'Commits by time'
        },
        toolbox: {
          feature: {
            dataZoom: {
              yAxisIndex: 'none'
            },
            restore: {},
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: date
        },
        yAxis: {
          type: 'value',
          boundaryGap: [0, '100%']
        },
        dataZoom: [
          {
            type: 'inside',
            start: 0,
            end: 10
          },
          {
            start: 0,
            end: 10
          }
        ],
        series: [
          {
            name: 'commits number',
            type: 'line',
            symbol: 'none',
            sampling: 'lttb',
            itemStyle: {
              color: 'rgb(255, 70, 131)'
            },
            areaStyle: {
              color: new this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: 'rgb(255, 158, 68)'
                },
                {
                  offset: 1,
                  color: 'rgb(255, 70, 131)'
                }
              ])
            },
            data: data
          }
        ]
      };
      this.option = this.defaultOption;
    }
  },
  mounted() {
    this.$evenBus.$on("getCommitsByTime", (url) => {
      this.getData(url)
    })
    this.init();
    this.draw();
  }
}
</script>

<style scoped>

</style>
