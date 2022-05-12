<template>
  <div class="dashboard-container">
    hi
    <LanguageRanks ref="rank"></LanguageRanks>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import LanguageRanks from "@/components/LanguageRanks";
export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  components: {
    LanguageRanks
  },
  mounted() {
    this.$refs.rank.initEcharts();
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
