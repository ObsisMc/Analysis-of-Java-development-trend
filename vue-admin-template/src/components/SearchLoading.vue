<template>
  <div id="rateLoading" style="width: 100%;height: 100%;"></div>
</template>

<script>
export default {
  name: "searchLoading",
  methods:{
    load_draw() {
      var chartDom = document.getElementById('rateLoading');
      var myChart = this.$echarts.init(chartDom);
      var option = {
        graphic: {
          elements: [
            {
              type: 'group',
              left: 'center',
              top: 'center',
              children: new Array(7).fill(0).map((val, i) => ({
                type: 'rect',
                x: i * 20,
                shape: {
                  x: 0,
                  y: -40,
                  width: 10,
                  height: 80
                },
                style: {
                  fill: '#5470c6'
                },
                keyframeAnimation: {
                  duration: 1000,
                  delay: i * 200,
                  loop: true,
                  keyframes: [
                    {
                      percent: 0.5,
                      scaleY: 0.3,
                      easing: 'cubicIn'
                    },
                    {
                      percent: 1,
                      scaleY: 1,
                      easing: 'cubicOut'
                    }
                  ]
                }
              }))
            }
          ]
        }
      };
      option && myChart.setOption(option);
    }
  },
  mounted() {
    this.load_draw();
  }
}
</script>

<style scoped>

</style>
