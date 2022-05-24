<template>
  <div id="MultiMetric" style="border-radius: 50%;" :style="{height:radius,width:radius}"></div>
</template>

<script>
import axios from "axios";

export default {
  name: "MultiMetric",
  data() {
    return {
      myChart: null,
      option: null,
      data: null,
      defaultData: null
    }
  },
  computed: {
    radius() {
      return Math.max(window.innerHeight, window.innerWidth) * 0.3 + "px";
    }
  },
  methods: {
    draw() {
      this.option && this.myChart.setOption(this.option);
    },
    handleData(data) {
      let newData = [];
      return newData;

    },
    getData(url) {
      let success = true;
      axios.get("")
        .then(response => {
          let rawData = response.data;
          this.data = this.handleData(rawData);
          this.option.series.data.splice(1, this.data[1]);
          this.option.series.data.splice(1, this.data[2]);
        }).catch(e => {
        this.option.series.data = this.defaultData;
        success = false;
      }).finally(() => {
        this.draw();
        this.$evenBus.$emit("finishSearchRepo", success);
      })
    },
    init() {
      this.defaultData = [
        {
          value: 8,
          name: 'Contributors',
          title: {
            offsetCenter: ['0%', '-30%']
          },
          detail: {
            valueAnimation: true,
            offsetCenter: ['0%', '-20%']
          }
        },
        {
          value: 2,
          name: 'Comments (avg)',
          title: {
            offsetCenter: ['0%', '0%']
          },
          detail: {
            valueAnimation: true,
            offsetCenter: ['0%', '10%']
          }
        }
      ];
      this.myChart = this.$echarts.init(document.getElementById('MultiMetric'));
      this.option = {
        title: {
          text: 'Vitality',
          left: 'center'
        },
        series: [
          {
            type: 'gauge',
            max: 20,
            startAngle: 90,
            endAngle: -270,
            pointer: {
              show: false
            },
            progress: {
              show: true,
              overlap: false,
              roundCap: true,
              clip: false,
              itemStyle: {
                borderWidth: 1,
                borderColor: '#464646'
              }
            },
            axisLine: {
              lineStyle: {
                width: 40
              }
            },
            splitLine: {
              show: false,
              distance: 0,
              length: 10
            },
            axisTick: {
              show: false
            },
            axisLabel: {
              show: false,
              distance: 50
            },
            data: this.defaultData,
            title: {
              fontSize: 14
            },
            detail: {
              width: 50,
              height: 14,
              fontSize: 14,
              color: 'auto',
              borderColor: 'auto',
              borderRadius: 20,
              borderWidth: 1,
              formatter: '{value}'
            }
          }
        ]
      };
      this.draw();
    }
  },
  mounted() {
    this.init();
    this.$evenBus.$on("getMetrics", (url) => {
      this.getData(url);
    })
  }
}
</script>

<style scoped>

</style>
