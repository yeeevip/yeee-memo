<template>
  <div class="mod-${tableClass.simpleTableName}">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="listData()">
      <#list tableClass.allFields as field>
      <el-form-item>
        <#if field.shortTypeName == 'Integer'>
        <el-select v-model="dataForm.${field.fieldName}" clearable  placeholder="${field.remarks}">
          <el-option label="${field.remarks}" value="" />
        </el-select>
        <#else>
        <el-input v-model="dataForm.${field.fieldName}" placeholder="${field.remarks}" clearable></el-input>
      </#if>
      </el-form-item>
    </#list>
    <el-form-item>
      <el-button size="small" @click="listData()">查询</el-button>
      <el-button v-if="$hasPerm('${tableClass.pagesPath?replace("/", ":")}:add')" type="primary" size="small" @click="editHandle()">新增</el-button>
      <el-button v-if="$hasPerm('${tableClass.pagesPath?replace("/", ":")}:del')" type="danger" size="small" @click="delHandle()" :disabled="dataListSelections.length <= 0">删除</el-button>
    </el-form-item>
    </el-form>
    <el-table :data="dataList" border stripe v-loading="dataListLoading" :max-height="tableHeight"
              @selection-change="selectionChangeHandle" @sort-change="sortChangeHandle" style="width: 100%;">
      <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
      <#list tableClass.allFields as field>
      <#if field.shortTypeName == 'Integer'>
      <el-table-column prop="${field.fieldName}" label="${field.remarks}" sortable="custom" header-align="center" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.${field.fieldName} === 1" size="small" type="success">${field.remarks}</el-tag>
          <el-tag v-else-if="scope.row.${field.fieldName} === 2" size="small" type="danger">${field.remarks}</el-tag>
        </template>
      </el-table-column>
      <#else>
      <el-table-column prop="${field.fieldName}" label="${field.remarks}" sortable="custom" header-align="center" align="center"></el-table-column>
    </#if>
  </#list>
  <el-table-column label="操作" fixed="right" header-align="center" align="center" width="100">
    <template slot-scope="scope">
      <el-button v-if="$hasPerm('${tableClass.pagesPath?replace("/", ":")}:info')" type="text" size="small" @click="infoHandle(scope.row.id)" icon="el-icon-document" title="详情"></el-button>
      <el-button v-if="$hasPerm('${tableClass.pagesPath?replace("/", ":")}:upd')" type="text" size="small" @click="editHandle(scope.row.id)" icon="el-icon-edit" title="编辑"></el-button>
      <el-button v-if="$hasPerm('${tableClass.pagesPath?replace("/", ":")}:del')" type="text" size="small" @click="delHandle(scope.row.id)" icon="el-icon-delete" title="删除"></el-button>
    </template>
  </el-table-column>
  </el-table>
  <el-pagination v-if="this.gridOptions.isPage"
                 @size-change="sizeChangeHandle"
                 @current-change="currentChangeHandle"
                 :current-page="pageNo"
                 :page-sizes="pageSizes"
                 :page-size="pageSize"
                 :total="total"
                 layout="total, sizes, prev, pager, next, jumper">
  </el-pagination>
  <!-- 弹窗, 新增 / 修改 -->
  <edit v-if="editVisible" ref="edit" @refreshDataList="listData"></edit>
  <!-- 弹窗, 详情 -->
  <info v-if="infoVisible" ref="info"></info>
  </div>
</template>

<script>
import edit from './${tableClass.simpleTableName}-edit'
import info from './${tableClass.simpleTableName}-info'
import grid from '@/mixins/grid'
export default {
  mixins: [grid],
  data () {
    return {
      module: '/${tableClass.pagesPath}/',
      gridOptions: {
        isQuery: true
      },
      defOrders: [
        {k: 'createTime', t: 'desc'}
      ],
      // dataMode: {
      //   name: 'EQ'
      // },
      dataForm: {}
    }
  },
  components: {
    edit, info
  },
  methods: {
  }
}
</script>
