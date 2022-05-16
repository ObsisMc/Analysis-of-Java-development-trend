<template>
<div class="dashboard-container">
    <SearchInput></SearchInput>
  <ContributorPanel></ContributorPanel>
</div>
</template>

<script>
import SearchInput from "@/views/nested/components/SearchInput";
import ContributorPanel from "@/views/nested/components/ContributorPanel";
export default {
  name: "index",
  components:{ContributorPanel, SearchInput}
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
