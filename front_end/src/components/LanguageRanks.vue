<template>
  <div>
    <div id="languagerank" style="width: 600px;height:400px;"></div>
    <div id="languagerankline" style="width: 600px;height:400px;"></div>
  </div>

</template>

<script>
export default {
  name: "LanguageRanks",
  methods: {
    initEcharts() {
      var newArr = null;
      this.$axios.get("/static/rank.json").then(response => {
        newArr = response.data;
        // 柱形颜色
        const countryColors = {
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
        };

        // 基于准备好的dom，初始化echarts实例
        var myChart = this.$echarts.init(document.getElementById('languagerank'));
        var updateFrequency = 2000;	// 数据更新速度
        var years = [];
        var startIndex = 0;
        for (var i = 0; i < newArr.length; ++i) {
          years.push(newArr[i])
        }
        // 获取第一个数据
        var startYear = years[startIndex].year;
        var startName = years[startIndex].language;
        var startCut = years[startIndex].nums;

        var option = {
          // 图标的上下左右边界
          grid: {
            top: 10,
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
            source: newArr
          },
          // y 轴数据
          yAxis: {
            type: 'category',
            inverse: true, 	// 大在上面，小在下面排序
            max: 5,			// 最多显示个数
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
              color: function (param) {

                return countryColors[param.name];
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
        myChart.setOption(option);
        for (var i = startIndex; i < newArr.length - 1; ++i) {
          (function (i) {
            setTimeout(function () {
              updateYear(years[i + 1]);
            }, (i + 1) * updateFrequency);
          })(i);
        }

        // 更新数据
        function updateYear(year) {
          option.yAxis.data = year.language; // split
          option.series[0].data = year.nums; //split
          option.graphic.elements[0].style.text = year.year;
          // 使用刚指定的配置项和数据显示图表。
          myChart.setOption(option);
        }
      }).catch(function (error) { // 请求失败处理
          console.log(error);
        }
      )
    },
    runLines() {
      this.$axios.get("/static/demoline.json").then(response => {
        var _rawData = response.data;
        var myChart = this.$echarts.init(document.getElementById('languagerankline'));
        // var countries = ['Australia', 'Canada', 'China', 'Cuba', 'Finland', 'France', 'Germany', 'Iceland', 'India', 'Japan', 'North Korea', 'South Korea', 'New Zealand', 'Norway', 'Poland', 'Russia', 'Turkey', 'United Kingdom', 'United States'];
        const countries = [
          'Finland',
          'France',
          'Germany',
          'Iceland',
          'Norway',
          'Poland',
          'Russia',
          'United Kingdom'
        ];
        const datasetWithFilters = [];
        const seriesList = [];
        this.$echarts.util.each(countries, function (country) {
          var datasetId = 'dataset_' + country;
          datasetWithFilters.push({
            id: datasetId,
            fromDatasetId: 'dataset_raw',
            transform: {
              type: 'filter',
              config: {
                and: [
                  {dimension: 'Year', gte: 1950},
                  {dimension: 'Country', '=': country}
                ]
              }
            }
          });
          seriesList.push({
            type: 'line',
            datasetId: datasetId,
            showSymbol: false,
            name: country,
            endLabel: {
              show: true,
              formatter: function (params) {
                return params.value[3] + ': ' + params.value[0];
              }
            },
            labelLayout: {
              moveOverlap: 'shiftY'
            },
            emphasis: {
              focus: 'series'
            },
            encode: {
              x: 'Year',
              y: 'Income',
              label: ['Country', 'Income'],
              itemName: 'Year',
              tooltip: ['Income']
            }
          });
        });
        console.log(seriesList);
        var option = {
          series: seriesList,
          animationDuration: 10000,
          dataset: [
            {
              id: 'dataset_raw',
              source: _rawData
            },
            ...datasetWithFilters
          ],
          title: {
            text: 'Income of Germany and France since 1950'
          },
          tooltip: {
            order: 'valueDesc',
            trigger: 'axis'
          },
          xAxis: {
            type: 'category',
            nameLocation: 'middle'
          },
          yAxis: {
            name: 'Income'
          },
          grid: {
            right: 140
          }
        };
        myChart.setOption(option);
      });
    }
  }
}
</script>

<style scoped>

</style>
