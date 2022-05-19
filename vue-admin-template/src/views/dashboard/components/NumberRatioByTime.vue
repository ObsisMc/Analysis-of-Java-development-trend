<template>
  <div id="NumberRatioByTime" style="width: 100%;height:100%"></div>
</template>

<script>
export default {
  name: "NumberRatioByTime",
  data() {
    return {
      mychart: null,
      option: {
        title: {
          text: 'Referer of a Website',
          subtext: 'Fake Data',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: 'Access From',
            type: 'pie',
            radius: '50%',
            data: [
              {value: 1048, name: 'Search Engine'},
              {value: 735, name: 'Direct'},
              {value: 580, name: 'Email'},
              {value: 484, name: 'Union Ads'},
              {value: 300, name: 'Video Ads'}
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }
    }
  },
  methods: {
    draw() {
      var chartDom = document.getElementById('NumberRatioByTime');
      this.myChart = this.$echarts.init(chartDom);
      this.option && this.myChart.setOption(this.option);
    },
    changeData() {
      let new_data = [
        {value: 300, name: 'Search Engine'},
        {value: 800, name: 'Direct'},
        {value: 400, name: 'Email'},
        {value: 100, name: 'Union Ads'},
        {value: 600, name: 'Video Ads'}
      ];
      // let l = this.option.series[0].data.length;
      // for (let i = 0; i < l; i++) {
      //   this.option.series[0].data[i].value += Math.random() * 50 - 25;
      // }
      this.option.series[0].data =new_data;
      this.myChart.setOption(this.option);
    }
  },
  mounted() {
    this.draw();
    this.$evenBus.$on("changeNumberRatio", () => {
      this.changeData();
    })
  }
}
</script>

<style scoped>

</style>
