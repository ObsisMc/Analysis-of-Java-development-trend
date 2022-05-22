<template>
  <div>
    <div class="floatPanel" style="text-align: center">
      <div v-if="!loading">
        <el-tag size="mini" class="tag">Search remain: {{ search }}</el-tag>
        <br/>
        <el-tag size="mini" class="tag">Core remain: {{ core }}</el-tag>
      </div>

      <br/>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "RateLimitCheck",
  data() {
    return {
      search: 0,
      core: 0,
      loading: false
    }
  },
  computed: {},
  methods: {},
  mounted() {
    console.log(this.$store.getters.authenticated);
    let autenticated = this.$store.getters.authenticated;
    axios.get("https://api.github.com/rate_limit", {
      headers: {
        "Authorization": autenticated ? ("token " + this.$store.getters.passwd) : ''
      },
      timeout: 5000
    }).then(response => {
      let data = response.data;
      this.search = data.resources.search.remaining;
        this.core = data.rate.remaining;
    }).catch(() => {
      this.$message({
        message: "Fail to get rate limit",
        type: "error"
      });
    })

    this.$evenBus.$on("checkRateLimit", () => {
      alert("check");
    });
  }
}
</script>

<style scoped>
.floatPanel {
  width: 200px;
  height: 50px;
  border-radius: 0 0 25px 25px;
  background: whitesmoke;
  position: fixed;
  right: 45%;
  z-index: 900;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.floatPanel::after {
  content: '';
  position: absolute;
  left: -25px;
  bottom: 25px;
  width: 25px;
  height: 25px;
  background: radial-gradient(circle at 0% 100%, transparent 25px, whitesmoke 0)
}

.floatPanel::before {
  content: '';
  position: absolute;
  right: -25px;
  bottom: 25px;
  width: 25px;
  height: 25px;
  background: radial-gradient(circle at 100% 100%, transparent 25px, whitesmoke 0)
}

.tag {
  margin-bottom: 5px;
}
</style>
