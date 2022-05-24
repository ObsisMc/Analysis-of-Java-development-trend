<template>
  <div id="workTime" style="border-radius: 50%;" :style="{height:radius,width:radius}"></div>
</template>

<script>
import axios from "axios";

export default {
  name: "WorkTime",
  data() {
    return {
      myChart: null,
      defaultData: {
        hours: ["0h", "1h", "2h", "3h", "4h", "5h", "6h",
          "7h", "8h", "9h", "10h", "11h", "12h", "13h",
          "14h", "15h", "16h", "17h", "18h", "19h",
          "20h", "21h", "22h", "23h"],
        days: ["Sun", "Mon", "Tue", "Wed", "Thr", "Fri", "Sat"],
        data: [[0, 0, 5], [0, 1, 1], [0, 2, 0], [0, 3, 0], [0, 4, 0], [0, 5, 0], [0, 6, 0], [0, 7, 0], [0, 8, 0], [0, 9, 0], [0, 10, 0], [0, 11, 2], [0, 12, 4], [0, 13, 1], [0, 14, 1], [0, 15, 3], [0, 16, 4], [0, 17, 6], [0, 18, 4], [0, 19, 4], [0, 20, 3], [0, 21, 3], [0, 22, 2], [0, 23, 5], [1, 0, 7], [1, 1, 0], [1, 2, 0], [1, 3, 0], [1, 4, 0], [1, 5, 0], [1, 6, 0], [1, 7, 0], [1, 8, 0], [1, 9, 0], [1, 10, 5], [1, 11, 2], [1, 12, 2], [1, 13, 6], [1, 14, 9], [1, 15, 11], [1, 16, 6], [1, 17, 7], [1, 18, 8], [1, 19, 12], [1, 20, 5], [1, 21, 5], [1, 22, 7], [1, 23, 2], [2, 0, 1], [2, 1, 1], [2, 2, 0], [2, 3, 0], [2, 4, 0], [2, 5, 0], [2, 6, 0], [2, 7, 0], [2, 8, 0], [2, 9, 0], [2, 10, 3], [2, 11, 2], [2, 12, 1], [2, 13, 9], [2, 14, 8], [2, 15, 10], [2, 16, 6], [2, 17, 5], [2, 18, 5], [2, 19, 5], [2, 20, 7], [2, 21, 4], [2, 22, 2], [2, 23, 4], [3, 0, 7], [3, 1, 3], [3, 2, 0], [3, 3, 0], [3, 4, 0], [3, 5, 0], [3, 6, 0], [3, 7, 0], [3, 8, 1], [3, 9, 0], [3, 10, 5], [3, 11, 4], [3, 12, 7], [3, 13, 14], [3, 14, 13], [3, 15, 12], [3, 16, 9], [3, 17, 5], [3, 18, 5], [3, 19, 10], [3, 20, 6], [3, 21, 4], [3, 22, 4], [3, 23, 1], [4, 0, 1], [4, 1, 3], [4, 2, 0], [4, 3, 0], [4, 4, 0], [4, 5, 1], [4, 6, 0], [4, 7, 0], [4, 8, 0], [4, 9, 2], [4, 10, 4], [4, 11, 4], [4, 12, 2], [4, 13, 4], [4, 14, 4], [4, 15, 14], [4, 16, 12], [4, 17, 1], [4, 18, 8], [4, 19, 5], [4, 20, 3], [4, 21, 7], [4, 22, 3], [4, 23, 0], [5, 0, 2], [5, 1, 1], [5, 2, 0], [5, 3, 3], [5, 4, 0], [5, 5, 0], [5, 6, 0], [5, 7, 0], [5, 8, 2], [5, 9, 0], [5, 10, 4], [5, 11, 1], [5, 12, 5], [5, 13, 10], [5, 14, 5], [5, 15, 7], [5, 16, 11], [5, 17, 6], [5, 18, 0], [5, 19, 5], [5, 20, 3], [5, 21, 4], [5, 22, 2], [5, 23, 0], [6, 0, 1], [6, 1, 0], [6, 2, 0], [6, 3, 0], [6, 4, 0], [6, 5, 0], [6, 6, 0], [6, 7, 0], [6, 8, 0], [6, 9, 0], [6, 10, 1], [6, 11, 0], [6, 12, 2], [6, 13, 1], [6, 14, 3], [6, 15, 4], [6, 16, 0], [6, 17, 0], [6, 18, 0], [6, 19, 0], [6, 20, 1], [6, 21, 2], [6, 22, 2], [6, 23, 6]]
      },
      data: {
        hours: ["0h", "1h", "2h", "3h", "4h", "5h", "6h",
          "7h", "8h", "9h", "10h", "11h", "12h", "13h",
          "14h", "15h", "16h", "17h", "18h", "19h",
          "20h", "21h", "22h", "23h"],
        days: ["Sun", "Mon", "Tue", "Wed", "Thr", "Fri", "Sat"],
        data: null
      },
      option: null
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
    handleData(rawData) {
      let newData = [];
      rawData = eval("(" + rawData + ")");
      let max=0;
      for (let i = 0; i < rawData.length; i++) {
        let tmp = rawData[i];
        newData.push([Number(tmp.y), Number(tmp.x), Number(tmp.value)]);
        max = max>Number(tmp.value)?max:Number(tmp.value);
      }
      for (let i = 0; i < newData.length; i++) {
        let tmp = newData[i];
        tmp[2] = tmp[2] / max * 10;
      }
      return newData;
    },
    handleInitData(rawData) {
      for (let i = 0; i < rawData.data.length; i++) {
        rawData.data[i][2] /= 300;
      }
      return rawData;
    },
    getData(url) {
      let success = true;
      axios.get("http://localhost:8080/api/commit_hour", {
        params: {
          url: url
        }
      })
        .then(response => {
          let rawData = response.data;
          let data = {
              hours: ["0h", "1h", "2h", "3h", "4h", "5h", "6h",
                "7h", "8h", "9h", "10h", "11h", "12h", "13h",
                "14h", "15h", "16h", "17h", "18h", "19h",
                "20h", "21h", "22h", "23h"],
              days: ["Sun", "Mon", "Tue", "Wed", "Thr", "Fri", "Sat"],
              data: this.handleData(rawData)
            };

          this.option = {
            title: {
              text: 'Punch Card of Github',
              left: 'center'
            },
            legend: {
              data: ['Punch Card'],
              left: 'center',
              top: "bottom"
            },
            polar: {},
            tooltip: {
              formatter: (params) => {
                return (
                  params.value[2] +
                  ' commits in ' +
                  data.hours[params.value[1]] +
                  ' of ' +
                  data.days[params.value[0]]
                );
              }
            },
            angleAxis: {
              type: 'category',
              data: data.hours,
              boundaryGap: false,
              splitLine: {
                show: true
              },
              axisLine: {
                show: false
              }
            },
            radiusAxis: {
              type: 'category',
              data: data.days,
              axisLine: {
                show: false
              },
              axisLabel: {
                rotate: 45
              }
            },
            series: [
              {
                name: 'Punch Card',
                type: 'scatter',
                coordinateSystem: 'polar',
                symbolSize: function (val) {
                  return val[2] * 2;
                },
                data: data.data,
                animationDelay: function (idx) {
                  return idx * 5;
                }
              }
            ]
          };

        }).catch(e => {
        success = false;
        console.log(e)
        this.option.series.data = this.defaultData.data;

      }).finally(() => {
        this.draw();
        this.$evenBus.$emit("finishSearchRepo", success);
      })
    },
    init() {
      this.myChart = this.$echarts.init(document.getElementById('workTime'));
      axios.get("json/commitTime.json")
        .then(response => {
          this.defaultData = this.handleInitData(response.data);
          this.option = {
            title: {
              text: 'Punch Card of Github',
              left: 'center'
            },
            legend: {
              data: ['Punch Card'],
              left: 'center',
              top: "bottom"
            },
            polar: {},
            tooltip: {
              formatter: (params) => {
                return (
                  params.value[2] +
                  ' commits in ' +
                  this.defaultData.hours[params.value[1]] +
                  ' of ' +
                  this.defaultData.days[params.value[0]]
                );
              }
            },
            angleAxis: {
              type: 'category',
              data: this.defaultData.hours,
              boundaryGap: false,
              splitLine: {
                show: true
              },
              axisLine: {
                show: false
              }
            },
            radiusAxis: {
              type: 'category',
              data: this.defaultData.days,
              axisLine: {
                show: false
              },
              axisLabel: {
                rotate: 45
              }
            },
            series: [
              {
                name: 'Punch Card',
                type: 'scatter',
                coordinateSystem: 'polar',
                symbolSize: function (val) {
                  return val[2] * 2;
                },
                data: this.defaultData.data,
                animationDelay: function (idx) {
                  return idx * 5;
                }
              }
            ]
          };
          this.draw();
        })

    }
  }, mounted() {
    this.init();
    this.$evenBus.$on("getWorkTime", url => {
      this.getData(url);
    })
  }
}
</script>

<style scoped>

</style>
