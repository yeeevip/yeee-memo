<template>
  <el-dialog title="详情" :close-on-click-modal="false" :visible.sync="visible">
    <el-form :model="dataForm" ref="dataForm" label-width="120px">
      <el-row :gutter="20">
        <#list tableClass.allFields as field>
        <#if field.shortTypeName == 'Integer'>
        <el-col :span="12">
          <el-form-item label="${field.remarks}:">
            <label>{{ dataForm.${field.fieldName} == 1 ? '是' : '否' }}</label>
          </el-form-item>
        </el-col>
        <#else>
        <el-col :span="12">
          <el-form-item label="${field.remarks}:">
            <label>{{ dataForm.${field.fieldName} }}</label>
          </el-form-item>
        </el-col>
      </#if>
    </#list>
    </el-row>
    </el-form>
    <span slot="footer" class="dialog-footer">
          <el-button @click="visible = false">取消</el-button>
        </span>
  </el-dialog>
</template>

<script>
export default {
  data () {
    return {
      id: null,
      visible: false,
      dataForm: {}
    }
  },
  methods: {
    init (id) {
      this.id = id
      this.visible = true
      this.$nextTick(() => {
        this.dataForm = {}
        // this.$refs['dataForm'].resetFields()
        if (this.id) {
          this.getInfo()
        }
      })
    },
// 获取详情
    getInfo () {
      this.$http.json().post('/${tableClass.pagesPath}/info', {
        'id': this.id
      }).then(({data: res}) => {
        if (res.code !== 200) {
          return this.$message.error(res.msg)
        }
        this.dataForm = {
          ...this.dataForm,
          ...res.data
        }
        this.dataFormOrigin = {
          ...this.dataFormOrigin,
          ...res.data
        }
      }).catch(() => {})
    }
  }
}
</script>
