<template>
  <div class="dashboard-container">
    <transition name="fade" mode="out-in">
    <SearchLoading v-if="loading"></SearchLoading>
    </transition>
    <transition name="fade" mode="out-in">
    <SearchInput v-if="!loading"></SearchInput>
    </transition>
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
.fade-enter-active, .fade-leave-active {
  transition: opacity .5s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
  opacity: 0;
}

</style>
