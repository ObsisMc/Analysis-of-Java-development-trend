<template>
  <div id="totalRank" style="height:100%;width: 100%"></div>
</template>

<script>
import axios from "axios";

export default {
  name: "TotalRank",
  data() {
    return {
      width: 0,
      myCharts: null,
      chartDom: null,
      option: null
    }
  },
  methods: {
    handleGet(data, k) {
      // let languages = data.head;
      let languages = ["Java","JavaScript","Python","HTML","Ruby","C#","CSS","PHP","C","C++"];
      let dataset = [["name", "rank", "year"]];

      let raw_data = data.dataset;

      let i = 0;
      let tmp_k = 0;
      let current_y = '0000';
      while (i < raw_data.length) {
        let data = raw_data[i];
        let year = data.year;
        if (current_y !== year) {
          tmp_k = 1;
          current_y = year;
        }
        if (tmp_k <= k && data.value > 0) {
          let tmp = [data.language, tmp_k, data.year];
          dataset.push(tmp);
          tmp_k++;
        }
        i++;
      }
      return {"head": languages, "dataset": dataset};
    },
    draw() {
      const datasetWithFilters = [];
      const seriesList = [];
      const rankMax = 7;
      let languages = ["Email", "Union Ads", "Video Ads"]
      let raw_data = [["name", "rank", "year"], ["Email", 1, 2018], ["Union Ads", 2, 2018], ["Video Ads", 3, 2018], ["Email", 1, 2019], ["Email", 2, 2020], ["Email", 3, 2021],
        ["Union Ads", 2, 2019], ["Union Ads", 1, 2020], ["Union Ads", 1, 2021],
        ["Video Ads", 3, 2019], ["Video Ads", 3, 2020], ["Video Ads", 2, 2021],]

      axios.get("http://localhost:8080/api/total_rank")
        .then(response => {
          let handled_data = this.handleGet(response.data, rankMax);
          languages = handled_data.head;
          raw_data = handled_data.dataset;
        }).catch(error => {

      }).finally(() => {
        this.$echarts.util.each(languages, function (language) {
          var datasetId = 'dataset_' + language;
          datasetWithFilters.push({
            id: datasetId,
            fromDatasetId: 'dataset_raw',
            transform: {
              type: 'filter',
              config: {
                and: [
                  {dimension: 'year', gte: 2011},
                  {dimension: 'name', '=': language}
                ]
              }
            }
          })
          seriesList.push({
            type: 'line',
            datasetId: datasetId,
            name: language,
            smooth: true,
            endLabel: {
              show: true,
              formatter: function(params){
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

        this.option = {
          dataset: [
            {
              id: 'dataset_raw',
              source: raw_data
            },
            ...datasetWithFilters
          ],
          title: {
            text: 'Rank of languages ',
            left: "center"
          },
          tooltip: {
            trigger: 'axis',
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
            axisLine: {
              show: false
            }
          },
          yAxis: {
            type: 'value',
            inverse: true,
            min: 1,
            max: rankMax,
            minInterval: 1,
          },
          series: seriesList
        };
        this.myChart.setOption(this.option);
      })


    },
    init() {
      this.chartDom = document.getElementById('totalRank');
      this.myChart = this.$echarts.init(this.chartDom);
      this.myChart.on('updateAxisPointer', (event) => {
        this.$evenBus.$emit("changeNumberRatio");
      });
    }
  },
  mounted() {
    this.init();
    this.draw();
  }
}
</script>

<style scoped>

</style>
