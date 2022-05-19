<template>
  <div id="metricRadar" style="height:100%;width: 100%"></div>
</template>

<script>
export default {
  name: "MetricRadar",
  props: ["hgt"],
  data(){
    return {
      radius: this.hgt.substring(0, this.hgt.lastIndexOf("p")) * 0.4
    }
  },
  methods: {
    draw() {
      var chartDom = document.getElementById('metricRadar');
      var myChart = this.$echarts.init(chartDom);
      var option;
      option = {
        title: {
          text: 'Multiple Radar'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          left: 'center',
          data: [
            'Maximum',
            'Average',
          ]
        },
        radar: [
          {
            indicator: [
              {text: 'Star', max: 100},
              {text: 'Fork ', max: 100},
              {text: 'Watch', max: 100},
              {text: 'Open issue', max: 100},
              {text: 'Open PR', max: 100},
              {text: 'Contributor', max: 100},
              {text: 'User', max: 100}
            ],
            center: ['50%', '50%'],
            radius: this.radius
          }
        ],
        series: [
          {
            type: 'radar',
            tooltip: {
              trigger: 'item'
            },
            areaStyle: {},
            data: [
              {
                value: [60, 73, 85, 40],
                name: 'Maximum'
              },
              {
                value: [80, 73, 85, 40],
                name: 'Average'
              }
            ]
          }
        ]
      }
      option && myChart.setOption(option);
    }
  }
}
</script>

<style scoped>

</style>
