<template>
  <div id="repoRank" style="width:100%" :style="{height:height}"></div>
</template>

<script>
export default {
  name: "RepoRank",
  computed: {
    height() {
      return window.innerHeight * 0.4 + "px";
    }
  },
  methods: {
    draw() {
      var chartDom = document.getElementById('repoRank');
      var myChart = this.$echarts.init(chartDom);
      var option;

      option = {
        title:{
          text: 'Repository Rank'
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
          data: ['Mon', 'Tue', 'Wed']
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
            data: [320, 302, 301]
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
            data: [120, 132, 101]
          },
          {
            name: 'Watch',
            type: 'bar',
            stack: 'total',
            label: {
              show: true
            },
            emphasis: {
              focus: 'series'
            },
            data: [220, 182, 191]
          }
        ]
      };
      myChart.on('click', function(params) {
        window.open("https://github.com/ObsisMc/CS209A_Project");
      });
      option && myChart.setOption(option);
    }
  },
  mounted() {
    this.draw();
  }
}
</script>

<style scoped>

</style>
