<template>
  <div id="repoRank" style="width:100%" :style="{height:height}"></div>
</template>

<script>
import axios from "axios";

export default {
  name: "RepoRank",
  data() {
    return {
      chartDom: null,
      myChart: null,
      option: null
    }
  },
  computed: {
    height() {
      return window.innerHeight * 0.45 + "px";
    }
  },
  methods: {
    draw() {
      this.chartDom = document.getElementById('repoRank');
      this.myChart = this.$echarts.init(this.chartDom);

      axios.get("http://localhost:8080/api/top_repo_rank")
        .then(response => {
          let data = response.data;

          let yData = [];
          let xData = [[], []]
          for (let i = 0; i < data.length; i++) {
            let tmp = data[i];
            yData.push(tmp.full_name);
            xData[0].push(tmp.stargazers_count);
            xData[1].push(tmp.forks_count);
          }

          this.option = {
            title: {
              text: 'Top 3 Repositories'
            },
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                // Use axis to trigger tooltip
                type: 'shadow' // 'shadow' as default; can also be 'line' or 'shadow'
              }
            },
            legend: {},
            grid: {
              left: '3%',
              right: '4%',
              bottom: '3%',
              containLabel: true
            },
            xAxis: {
              type: 'value'
            },
            yAxis: {
              type: 'category',
              inverse: true,
              data: yData
            },
            series: [
              {
                name: 'Star',
                type: 'bar',
                stack: 'total',
                label: {
                  show: true
                },
                emphasis: {
                  focus: 'series'
                },
                data: xData[0]
              },
              {
                name: 'Fork',
                type: 'bar',
                stack: 'total',
                label: {
                  show: true
                },
                emphasis: {
                  focus: 'series'
                },
                data: xData[1]
              }
            ]
          };
          this.myChart.on('click', function (params) {
            window.open("https://github.com/" + params.name);
          });
          this.option && this.myChart.setOption(this.option);
        })

    }
  },
  mounted() {
    this.draw();
  }
}
</script>

<style scoped>

</style>
