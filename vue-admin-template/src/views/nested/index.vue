<template>
  <div class="dashboard-container">
    <div :style="{height:height}" style="border: transparent solid 1px;">
      <transition name="component-fade" mode="out-in">
        <component v-bind:is="view"></component>
      </transition>
    </div>
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
  computed: {
    height() {
      return window.innerHeight * 0.08 + "px";
    }
  },
  data() {
    return {
      loading: false,
      view: SearchInput
    }
  },
  methods: {},
  mounted() {
    this.$evenBus.$on("beginSearch", () => {
      this.loading = true;
      this.view = SearchLoading;
    })
    this.$evenBus.$on("endSearch", () => {
      this.loading = false;
      this.view = SearchInput;
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

.component-fade-enter-active, .component-fade-leave-active {
  transition: opacity .3s ease;
}

.component-fade-enter, .component-fade-leave-to
  /* .component-fade-leave-active for below version 2.1.8 */
{
  opacity: 0;
}
</style>
