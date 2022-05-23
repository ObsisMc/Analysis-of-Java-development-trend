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
      option: null
    }
  },
  methods: {
    getDataSet(raw_data) {
      raw_data = eval("(" + raw_data + ")");
      let dataset = [["name", "value", "year"]];
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
      axios.get("json/demo.json").then(response => {
        var _rawData = response.data;
        // var _rawData = this.getDataSet(response.data);

        var option;
        const countries = [
          'Finland',
          'France',
          'Germany'
        ];
        // const types = ["issue", "user", "repo"];

        const datasetWithFilters = [];
        const seriesList = [];
        let n = 0;
        this.$echarts.util.each(countries, function (country) {
          var datasetId = 'dataset_' + country;
          n++;
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
            yAxisIndex: n === 2 ? 1 : 0,
            labelLayout: {
              moveOverlap: 'shiftY'
            },
            emphasis: {
              focus: 'series'
            },
            encode: {
              x: 'Year',
              y: 'Income',
              // label: ['Country', 'Income'],
              // itemName: 'Year',
              tooltip: ['Income']
            }
          });
        });
        option = {
          legend:{
            data:countries,
            top:"bottom"
          },
          animationDuration: 3000,
          dataset: [
            {
              id: 'dataset_raw',
              source: _rawData
            },
            ...datasetWithFilters
          ],
          title: {
            text: 'Number of users & repos & issues by time',
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
            {name: "Number of users & repos"},
            {
              name: 'Number of issues',
              alignTicks: true,
              type: 'value'
            }
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
