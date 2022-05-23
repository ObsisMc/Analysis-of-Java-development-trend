<template>
  <div id="threeByTime" style="width: 100%;height:500px;"></div>
</template>

<script>
import axios from "axios";

export default {
  name: "ThreeValueByTime",
  data() {
    return {
      chartDom: null,
      myChart: null,
      option: null,
      title: "Number of Repo, User and Issue in recent 10 years"
    }
  },
  methods: {
    getDataSet(raw_data) {
      console.log(typeof raw_data)
      let dataset = [["Name", "Value", "Year"]];
      for (let i = 0; i < raw_data.length; i++) {
        let name = raw_data[i].type;

        let data1 = raw_data[i].dataset;
        for (let j = 0; j < data1.length; j++) {
          let year = data1[j].year;

          let languages = data1[j].languages;
          for (let k = 0; k < languages.length; k++) {
            if (languages[k].name === "Java") {
              dataset.push([name, languages[k].value, year]);
              break;
            }
          }
        }
      }
      return dataset;
    },
    draw() {
      axios.get("http://localhost:8080/api/user_issue_repo").then(response => {
        // var _rawData = response.data;
        var _rawData = this.getDataSet(response.data);

        const types = ["user", "repo", "issue"];

        const datasetWithFilters = [];
        const seriesList = [];
        let n = 0;
        this.$echarts.util.each(types, function (tp) {
          var datasetId = 'dataset_' + tp;
          n++;
          datasetWithFilters.push({
            id: datasetId,
            fromDatasetId: 'dataset_raw',
            transform: {
              type: 'filter',
              config: {
                and: [
                  {dimension: 'Year', gte: 2012},
                  {dimension: 'Name', '=': tp}
                ]
              }
            }
          });
          seriesList.push({
            type: 'line',
            datasetId: datasetId,
            showSymbol: false,
            name: tp,
            yAxisIndex: 0,
            labelLayout: {
              moveOverlap: 'shiftY'
            },
            emphasis: {
              focus: 'series'
            },
            encode: {
              x: 'Year',
              y: 'Value',
              // label: ['Country', 'Income'],
              // itemName: 'Year',
              tooltip: ['Value']
            }
          });
        });
        var option = {
          legend: {
            data: types,
            top: "bottom"
          },
          animationDuration: 1000,
          dataset: [
            {
              id: 'dataset_raw',
              source: _rawData
            },
            ...datasetWithFilters
          ],
          title: {
            text: this.title,
            left: "center"
          },
          tooltip: {
            order: 'valueDesc',
            trigger: 'axis'
          },
          xAxis: {
            type: 'category',
            nameLocation: 'middle'
          },
          yAxis: [
            {name: "Number"}
          ],
          grid: {
            right: 140
          },
          series: seriesList
        };
        this.myChart.setOption(option);

      })
    },
    init() {
      this.chartDom = document.getElementById('threeByTime');
      this.myChart = this.$echarts.init(this.chartDom);
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
