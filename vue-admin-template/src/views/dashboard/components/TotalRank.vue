<template>
  <div id="totalRank" style="height:100%;width: 100%"></div>
</template>

<script>
export default {
  name: "TotalRank",
  data() {
    return {
      width: 0,
      myCharts: null,
      chartDom: null
    }
  },
  methods: {
    draw() {
      const datasetWithFilters = [];
      const seriesList = [];
      const languages = ["Email", "Union Ads", "Video Ads"]
      const raw_data = [["name", "rank", "year"],["Email", 1, 2018], ["Email", 1, 2019], ["Email",2, 2020], ["Email", 3, 2021],
        ["Union Ads", 2, 2018],["Union Ads", 2, 2019], ["Union Ads", 1, 2020], ["Union Ads", 1, 2021],
        ["Video Ads", 3, 2018], ["Video Ads", 3, 2019], ["Video Ads", 3, 2020], ["Video Ads", 2, 2021],]
      this.$echarts.util.each(languages, function (language) {
        var datasetId = 'dataset_' + language;
        datasetWithFilters.push({
          id: datasetId,
          fromDatasetId: 'dataset_raw',
          transform: {
            type: 'filter',
            config: {
              and: [
                {dimension: 'year', gte: 2016},
                {dimension: 'name', '=': language}
              ]
            }
          }
        })
        seriesList.push({
          type: 'line',
          datasetId: datasetId,
          name: language,
          smooth:0.5,
          endLabel: {
            show: true,
            formatter: function (params) {
              return params.seriesName;
            }
          },
          labelLayout: {
            moveOverlap: 'shiftY'
          },
          emphasis: {
            focus: 'series'
          },
          encode: {
            x: 'year',
            y: 'rank',
            tooltip: ['rank']
          }
        });
      });

      var option = {
        dataset: [
          {
            id: 'dataset_raw',
            source: raw_data
          },
          ...datasetWithFilters
        ],
        title: {
          text: 'Language rank by time'
        },
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          axisLine:{
            show:false
          }
        },
        yAxis: {
          type: 'value',
          inverse: true,
          min: 1,
          max: 3,
          minInterval: 1,
        },
        series: seriesList
        //   [
        //   {
        //     name: 'Email',
        //     type: 'line',
        //     smooth: true,
        //     data: [1, 2, 1, 1, 1, 1, 1],
        //     endLabel: {
        //       show: true,
        //       formatter: function (params) {
        //         return params.seriesName;
        //       }
        //     }
        //   },
        //   {
        //     name: 'Union Ads',
        //     type: 'line',
        //     smooth: true,
        //     data: [2, 1, 2, 2, 2, 2, 2]
        //   },
        //   {
        //     name: 'Video Ads',
        //     type: 'line',
        //     data: [3, 3, 3, 3, 3, 3, 3]
        //   },
        //   {
        //     name: 'Direct',
        //     type: 'line',
        //     data: [4, 4, 4, 4, 4, 4, 4]
        //   }
        // ]
      };
      this.myChart.on('updateAxisPointer', (event) => {
        this.$evenBus.$emit("changeNumberRatio");
      });
      this.myChart.setOption(option);
    }
  },
  mounted() {
    this.chartDom = document.getElementById('totalRank');
    this.myChart = this.$echarts.init(this.chartDom);
    this.draw();
  }
}
</script>

<style scoped>

</style>
