<template>
  <div id="license" style="width: 100%; height: 500px;"></div>
</template>

<script>
import axios from "axios";

export default {
  name: "License",
  data() {
    return {
      chartDom: null,
      myChart: null,
      option: null
    }
  },
  methods: {
    draw() {
      this.option && this.myChart.setOption(this.option);
    },
    init() {
      this.chartDom = document.getElementById('license');
      this.myChart = this.$echarts.init(this.chartDom);

      let data = [];
      axios.get("http://localhost:8080/api/popular_license")
        .then(response => {
          let totalN = 0;
          for(let i=0;i<5;i++){
            totalN += response.data[i].value;
            data.push(response.data[i]);
          }
          for(let i=0;i<5;i++){
            data[i].value =  (data[i].value / totalN);
            data[i].value = data[i].value.toFixed(2);
          }
          console.log(data[0])
        }).catch(error => {
          console.log(error)
        this.$notify.error({
          message: "Fail to get popular licenses"
        })
        data = [
          {value: 40, name: 'rose 1'},
          {value: 38, name: 'rose 2'},
          {value: 32, name: 'rose 3'},
          {value: 30, name: 'rose 4'},
          {value: 28, name: 'rose 5'},
          {value: 26, name: 'rose 6'},
          {value: 22, name: 'rose 7'},
          {value: 18, name: 'rose 8'}
        ];
      }).finally(() => {
        this.option = {
          title: {
            text: 'Popular license',
            left: "center"
          },
          legend: {
            left: "left",
            orient: 'vertical',
          },
          tooltip: {
            trigger: 'item'
          },
          toolbox: {
            show: true,
            feature: {
              mark: {show: true},
              dataView: {show: true, readOnly: false},
              restore: {show: true},
              saveAsImage: {show: true}
            }
          },
          series: [
            {
              name: 'Popular Licenses',
              type: 'pie',
              radius: ['40%', '70%'],
              avoidLabelOverlap: false,
              itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
              },
              label: {
                show: false,
                position: 'center'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '40',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: false
              },
              data: data
            }
          ]
        };
        this.draw();
      })

    }
  },
  mounted() {
    this.init();
  }
}
</script>

<style scoped>

</style>
