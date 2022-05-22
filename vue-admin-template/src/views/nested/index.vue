<template>
  <div class="dashboard-container">
    <SearchLoading v-if="loading"></SearchLoading>
    <SearchInput></SearchInput>
    <ContributorPanel></ContributorPanel>
    <TimeShow></TimeShow>
  </div>
</template>

<script>
import SearchInput from "@/views/nested/components/SearchInput";
import ContributorPanel from "@/views/nested/components/ContributorPanel";
import TimeShow from "@/views/nested/components/TimeShow";
import SearchLoading from "@/components/SearchLoading";

export default {
  name: "index",
  components: {SearchLoading, ContributorPanel, SearchInput, TimeShow},
  data() {
    return {
      loading: false
    }
  },
  methods: {},
  mounted() {
    this.$evenBus.$on("beginSearch",()=>{
      this.loading = true;
    })
    this.$evenBus.$on("endSearch",()=>{
      this.loading = false;
    })
  }

}
</script>

<style scoped>

.dashboard-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;
  margin: 30px;

}

</style>
