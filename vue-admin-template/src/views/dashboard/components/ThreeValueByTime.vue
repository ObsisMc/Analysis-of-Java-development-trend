<template>
  <div id="threeByTime" style="width: 100%;height:500px;"></div>
</template>

<script>
import axios from "axios";

export default {
  name: "ThreeValueByTime",
  data() {
    return {
      test: 0
    }
  },
  methods: {
    draw() {
      axios.get("json/demo.json").then(response => {
        var _rawData = response.data;
        var chartDom = document.getElementById('threeByTime');
        var myChart = this.$echarts.init(chartDom);
        var option;
        const countries = [
          'Finland',
          'France',
          'Germany'
        ];
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
        myChart.setOption(option);

      })
    }
  },
  mounted() {
    this.draw();
  }
}
</script>

<style scoped>

</style>
