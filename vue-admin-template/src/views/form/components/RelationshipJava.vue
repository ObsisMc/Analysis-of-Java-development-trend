<template>
<div>
  <div id="relationJava" style="width:100%" :style="{height:hgt}">
  </div>
</div>
</template>

<script>
export default {
  name: "RelationshipJava",
  props: ["hgt"],
  methods:{
    draw(){
      var myChart = this.$echarts.init(document.getElementById('relationJava'));
      var option;
      this.$axios.get("json/adjust_relation55000to60000.json").then(response=>{
        var graph = response.data;
        graph.nodes.forEach(function (node) {
          node.label = {
            show: node.symbolSize > 30
          };
        });
        option = {
          title: {
            text: 'Language Relationship',
            subtext: 'Default layout',
            top: 'bottom',
            left: 'right'
          },
          tooltip: {},
          legend: [
            {
              // selectedMode: 'single',
              data: graph.categories.map(function (a) {
                return a.name;
              })
            }
          ],
          animationDuration: 1500,
          animationEasingUpdate: 'quinticInOut',
          series: [
            {
              name: 'Les Miserables',
              type: 'graph',
              layout: 'none',
              data: graph.nodes,
              links: graph.links,
              categories: graph.categories,
              roam: true,
              label: {
                position: 'right',
                formatter: '{b}'
              },
              lineStyle: {
                color: 'source',
                curveness: 0.3
              },
              emphasis: {
                focus: 'adjacency',
                lineStyle: {
                  width: 10
                }
              }
            }
          ]
        };
        myChart.setOption(option);
      })
    }
  }
}
</script>

<style scoped>

</style>
