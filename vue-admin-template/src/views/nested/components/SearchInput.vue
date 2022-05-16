<template>
  <div style="margin:16px 32px">
    <el-autocomplete
      class="inline-input"
      v-model="state"
      :fetch-suggestions="querySearch"
      placeholder="请输入内容"
      :trigger-on-focus="false"
      @select="handleSelect"
      style="width: 80%"
    ></el-autocomplete>
  </div>
</template>

<script>
export default {
  name: "SearchInput",
  data(){
    return {
      state: ''
    }
  },
  methods:{
    querySearch(){

    },
    handleSelect(){

    }
  }
}
</script>

<style scoped>

</style>
