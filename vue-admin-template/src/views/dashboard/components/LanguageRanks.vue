<template>
  <div>
    <div id="languagerank" style="width: 100%;" :style="{height:hgt}"></div>
    <el-tooltip placement="top">
      <el-button @click="draw" type="primary" circle size="mini"
                 icon="el-icon-refresh-left" style="float: right"
                 :disabled="buttonValid">
      </el-button>
      <div slot="content">
        <span v-if="buttonValid">
          Need to wait...
        </span>
        <span v-if="!buttonValid">
          Run again!
        </span>

      </div>
    </el-tooltip>


  </div>

</template>

<script>
export default {
  name: "LanguageRanks",
  props: ["hgt"],
  data() {
    return {
      myChart: null,
      buttonValid: false,
      beginYear: 2012,
      topK: 5,
      option: null,
      title: "Repo Increase of Popular Languages in recent 10 years (w)",
      languageColor: {
        JavaScript: '#88008b',
        HTML: '#f00',
        Python: '#ffde00',
        Java: '#002a8f',
        CSS: '#0035ff',
        "Jupyter Notebook": '#edff39',
        TypeScript: '#39ffa6',
        "C#": "#5f008f",
        PHP: "#4578ea",
        "C++": "#30008f",
        Ruby: "#00538f",
        C: "#625959"
      }
    }
  },
  methods: {
    handleData(raw_data, beginYear, topK) {
      let data = [];
      raw_data = eval("(" + raw_data + ")");
      for (let i = 0; i < raw_data.length; i++) {
        let tmpObject = raw_data[i];
        let year = tmpObject.year;
        if (beginYear > year) {
          continue;
        }
        let tmpData = {"year": year, "language": [], "nums": []};
        let languages = tmpObject.language;
        for (let j = 0; j < languages.length; j++) {
          tmpData.language.push(languages[j].name);
          tmpData.nums.push(languages[j].num/10000);
        }
        data.push(tmpData);
        console.log(tmpData);
      }

      return data;
    },
    draw() {
      this.buttonValid = !this.buttonValid;

      this.$axios.get("http://localhost:8080/api/increase_rank").then(response => {
        let responseData = this.handleData(response.data, this.beginYear, this.topK);
        // let responseData = response.data;

        // 基于准备好的dom，初始化echarts实例
        var updateFrequency = 2000;	// 数据更新速度
        var years = [];
        var startIndex = 0;
        for (var i = 0; i < responseData.length; ++i) {
          years.push(responseData[i])
        }
        // 获取第一个数据
        var startYear = years[startIndex].year;
        var startName = years[startIndex].language;
        var startCut = years[startIndex].nums;

        this.option = {
          title: {
            text: this.title
          },
          // 图标的上下左右边界
          grid: {
            top: 50,
            bottom: 30,
            left: 120,
            right: 120
          },
          // x 轴相关
          xAxis: {
            max: 'dataMax',
            splitLine: {
              show: true,
              lineStyle: {
                color: 'rgba(100,100,100, 0.4)',
                type: 'dashed'
              }
            },

            axisLabel: {
              // 圆整 X 轴 参数
              formatter: function (n) {
                return Math.round(n) + '';
              }
            }
          },
          dataset: {
            source: responseData
          },
          // y 轴数据
          yAxis: {
            type: 'category',
            inverse: true, 	// 大在上面，小在下面排序
            max: this.topK,			// 最多显示个数
            data: startName,
            axisLabel: {
              show: true,
              textStyle: {
                fontSize: 14
              },
              rich: {
                flag: {
                  fontSize: 25,
                  padding: 5
                }
              }
            },
            animationDuration: 300,
            animationDurationUpdate: 300
          },
          series: [{
            realtimeSort: true,
            seriesLayoutBy: 'column',
            type: 'bar',
            itemStyle: {
              /* color: 'rgb(13,208,229)' */
              color: (param) => {
                return this.languageColor[param.name];
              }
            },
            encode: {
              x: 0,
              y: 3
            },
            label: {
              show: true,
              precision: 1,
              position: 'right',
              valueAnimation: true,
              fontFamily: 'monospace',
              /* formatter: function (data) {
                  return startCut[data.dataIndex] + "%";
              } */
            },
            data: startCut
          }],

          animationDuration: 0,
          animationDurationUpdate: updateFrequency,
          animationEasing: 'linear',
          animationEasingUpdate: 'linear',
          graphic: {
            // 年代的文字 Text
            elements: [{
              type: 'text',
              right: 120,
              bottom: 60,
              style: {
                text: startYear,
                font: 'bolder 40px monospace',
                fill: 'rgba(100, 100, 100, 0.25)'
              },
              z: 100
            }]
          }
        };
        // 使用刚指定的配置项和数据显示图表。
        this.myChart.setOption(this.option);
        for (var i = startIndex; i < responseData.length - 1; ++i) {
          ((i) => {
            setTimeout(() => {
              this.updateYear(years[i + 1]);
            }, (i + 1) * updateFrequency);
          })(i);
        }

        setTimeout(() => {
          this.buttonValid = !this.buttonValid;
        }, 20000);
      }).catch(function (error) { // 请求失败处理
          console.log(error);
        }
      ).finally(() => {

      })
    },
    // 更新数据
    updateYear(year) {
      this.option.yAxis.data = year.language; // split
      this.option.series[0].data = year.nums; //split
      this.option.graphic.elements[0].style.text = year.year;
      // 使用刚指定的配置项和数据显示图表。
      this.myChart.setOption(this.option);
    },
    init() {

    }
  },
  mounted() {
    this.myChart = this.$echarts.init(document.getElementById('languagerank'));
    this.draw();
  }
}
</script>

<style scoped>

</style>
