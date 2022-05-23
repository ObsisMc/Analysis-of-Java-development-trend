<template>
  <div id="NumberRatioByTime" style="width: 100%;height:100%"></div>
</template>

<script>
import axios from "axios";

export default {
  name: "NumberRatioByTime",
  data() {
    return {
      chartDom: null,
      myChart: null,
      option: null,
      year: 2021,
      title: 'Percentage of Popular Languages in ',
      data: [],
      top_k: 7
    }
  },
  methods: {
    draw() {
      this.option && this.myChart.setOption(this.option);
    },
    selectYear(selected_year, top_k) {
      let new_data = [];
      this.year = Number(selected_year);
      for (let i = 0; i < this.data.length; i++) {
        let year = this.data[i].year;
        if (year === this.year) {
          new_data = this.data[i].language.slice(0, top_k);
          break;
        }
      }
      return new_data
    },
    init() {
      let data = [];
      axios.get("http://localhost:8080/api/total_rank_pie")
        .then(response => {
          this.data = eval("(" + response.data + ")");
          data = this.selectYear(2021, this.top_k);
        }).catch(e => {
        this.$notify.error({
          message: "Fail to get " + this.title
        })
        data = [
          {value: 1048, name: 'Search Engine'},
          {value: 735, name: 'Direct'},
          {value: 580, name: 'Email'},
          {value: 484, name: 'Union Ads'},
          {value: 300, name: 'Video Ads'}
        ]
      }).finally(() => {
        this.option = {
          title: {
            text: this.title + this.year,
            subtext: 'Fake Data',
            left: 'center'
          },
          tooltip: {
            trigger: 'item'
          },
          legend: {
            orient: 'vertical',
            left: 'left'
          },
          series: [
            {
              type: 'pie',
              radius: '50%',
              data: data,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        }
        this.chartDom = document.getElementById('NumberRatioByTime');
        this.myChart = this.$echarts.init(this.chartDom);
        this.draw();
      })
    }
  },
  mounted() {
    this.init();
    this.$evenBus.$on("changeNumberRatio", (year) => {
      let data = this.selectYear(year, this.top_k);
      this.option = {
        title: {
          text: this.title + this.year,
          subtext: 'Fake Data',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            type: 'pie',
            radius: '50%',
            data: data,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }
      this.option.title.text = this.title + this.year;
      this.draw();
    })
  }
}
</script>

<style scoped>

</style>
