<template>
  <div>
    <el-row>
      <el-collapse v-model="activeNames" @change="handleChange">
        <el-collapse-item name="1">
          <template slot="title">
          <span style="color:dodgerblue; margin: 0 20px;">
            Advanced Search
          </span>
            <i class="el-icon-search" style=" color:dodgerblue"></i>
          </template>

          <el-form ref="form" :model="form" label-width="120px">
            <el-form-item label="Created after">
              <el-date-picker type="date" placeholder="Select date" v-model="form.date"
                              style="width: 100%;"></el-date-picker>
            </el-form-item>
            <el-form-item label="Size">
              <el-col :span="4">
                <el-input placeholder="Minimum size" v-model="form.sizeMin" style="width: 100%;"></el-input>
              </el-col>
              <el-col class="line" :span="2">-</el-col>
              <el-col :span="4">
                <el-input placeholder="Maximum size" v-model="form.sizeMax" style="width: 100%;"></el-input>
              </el-col>

            </el-form-item>
            <el-form-item label="Star">
              <el-col :span="4">
                <el-input placeholder="Minimum star" v-model="form.starMin" style="width: 100%;"></el-input>
              </el-col>
              <el-col class="line" :span="2">-</el-col>
              <el-col :span="4">
                <el-input placeholder="Maximum star" v-model="form.starMax" style="width: 100%;"></el-input>
              </el-col>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="onSubmit">Create</el-button>
              <el-button @click="onCancel">Clear</el-button>
            </el-form-item>
          </el-form>
        </el-collapse-item>
      </el-collapse>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "Search",
  data() {
    return {
      form: {
        region: '',
        date: '',
        sizeMin: '',
        sizeMax: '',
        starMin: '',
        starMax: ''
      },
      activeNames: []
    }
  },
  methods: {
    onSubmit() {
      this.$message('submit!')
    },
    onCancel() {
      this.form.date = '';
      this.form.sizeMin = '';
      this.form.sizeMax = '';
      this.form.starMin = '';
      this.form.starMax = '';
      this.$message({
        message: 'cancel!',
        type: 'warning'
      });

    },
    handleChange(val) {
      console.log(val);
    }
  }
}
</script>

<style scoped>

</style>
