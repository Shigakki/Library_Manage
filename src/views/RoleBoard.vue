<template>
  <div>


    <div style="margin: 10px 0">
      <el-input suffix-icon="el-icon-search" placeholder="请输入名称" v-model="name_search"
                style="width: 200px;margin-left: 5px"></el-input>
      <el-input suffix-icon="el-icon-message" placeholder="请输入描述" v-model="description_search"
                style="width: 200px;margin-left: 5px"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="loadData">搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset">刷新</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline"
                                                          style="margin-left: 4px"></i></el-button>
      <el-popconfirm
          confirm-button-text='好的'
          cancel-button-text='不用了'
          icon="el-icon-info"
          icon-color="red"
          title="确定批量删除选中的数据？"
          style="margin: 5px"
          @confirm="deleteBatch"
      >
        <el-button type="danger" slot="reference">批量删除<i class="el-icon-remove-outline"
                                                             style="margin-left: 4px"></i></el-button>
      </el-popconfirm>

    </div>

    <el-table :data="tableData" border stripe header-cell-class-name="headerBackground"
              @selection-change="handleSelectionChanged"
    >
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column prop="id" label="id" width="100"></el-table-column>
      <el-table-column prop="name" label="角色名称"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column prop="ukey" label="唯一标识"></el-table-column>

      <el-table-column label="操作" width="350px">
        <template slot-scope="scope">
          <el-button type="primary" @click="allocateMenu(scope.row.id)">分配菜单<i class="el-icon-menu"
                                                                                   style="margin-left: 4px"></i>
          </el-button>
          <el-button type="success" @click="handleEditRow(scope.row)">编辑<i class="el-icon-edit"
                                                                             style="margin-left: 4px"></i></el-button>
          <el-popconfirm
              confirm-button-text='好的'
              cancel-button-text='不用了'
              icon="el-icon-info"
              icon-color="red"
              title="确定删除该条记录吗？"
              style="margin-left: 5px"
              @confirm="handleDeleteRow(scope.row.id)"
          >
            <el-button slot="reference" type="danger">
              删除<i class="el-icon-remove-outline" style="margin-left: 4px"></i>
            </el-button>
          </el-popconfirm>

        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 15, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalNum">
      </el-pagination>
    </div>

    <el-dialog title="角色信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="角色名称">
          <el-input v-model="form.name" autocomplete="off" placeholder="请输入角色名称"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off" placeholder="请输入角色描述"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false; loadData()">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="菜单分配" :visible.sync="menuDialogVisible" width="30%">
      <el-tree
          :data="menuData"
          show-checkbox
          @check-change="handleCheckChange"
          node-key="id"
          :props="props"
          :default-expanded-keys="expands"
          :default-checked-keys="this.checks"
          ref="tree"
      >
        <span class="customer-tree-node" slot-scope="{node,data}">
          <span><i :class="data.icon"></i>{{ data.name }}</span>
        </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="menuDialogVisible = false; loadData()">取 消</el-button>
        <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "RoleBoard",
  data() {
    return {
      tableData: [],
      totalNum: 0,
      pageNum: 1,
      pageSize: 10,

      name_search: "",
      description_search: "",

      dialogFormVisible: false,
      menuDialogVisible: false,
      form: {},

      multipleSelection: [],

      menuData: [],
      props: {
        label: 'name',
      },
      expands: [],
      checks: [],

      editedRoleId: null,
      editedMenuIds: [],
    }
  },
  methods: {
    loadData() {
      // request.get('http://localhost:9090/user/page?pageNum='+this.pageNum+'&pageSize='+this.pageSize)
      //     .then(res=>{
      //       this.tableData=res.records;
      //       this.totalNum=res.total;
      //     })
      this.request.get('/role/page', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name_search,
          description: this.description_search,
        }
      }).then(res => {
        this.tableData = res.records;
        this.totalNum = res.total;
      })

    },
    reset() {
      this.name_search = ""
      this.description_search = ""
      this.loadData()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.loadData();
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.loadData();
    },
    handleAdd() {
      this.dialogFormVisible = true;
      this.form = {}
    },
    handleEditRow(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    handleDeleteRow(id) {
      this.request.delete("/role/delete/" + id)
          .then(res => {
            if (res) {
              this.$message.success('删除成功')
              this.loadData();
            } else {
              this.$message.error('删除失败')
              this.loadData();
            }
          })
    },
    deleteBatch() {
      let id = this.multipleSelection.map(v => v.id)  // [ {}, {}, {} ] => [ 1,2,3 ]
      this.request.post("/role/delete/batch", id)
          .then(res => {
            if (res) {
              this.$message.success('批量删除成功')
              this.loadData();
            } else {
              this.$message.error('批量删除失败')
              this.loadData();
            }
          })
    },
    handleSelectionChanged(val) {
      this.multipleSelection = val
    },
    save() {
      this.request.post("/role/insertOrUpdate", this.form)
          .then(res => {
            if (res) {
              this.$message.success('保存成功')
              this.dialogFormVisible = false;
              this.loadData();
            } else {
              this.$message.error('保存失败')
              this.dialogFormVisible = false;
              this.loadData();
            }
          })
    },
    allocateMenu(roleId) {
      this.editedRoleId = roleId
      this.request.get("/menu/list")
          .then(res => {
            this.menuData = res.data
            this.expands = this.menuData.map(v => v.id)
          })

      this.request.get("/role/roleMenu/" + roleId).then(res => {
        this.checks = res.data

        this.request.get("/menu/ids").then(res => {
          // error: undefined property(tree) will occur  if u dont render dialog first --- 2 ways to solve it
          // this.menuDialogVisible = true;
          let ids=res.data
          ids.forEach(id => {
            if (!this.checks.includes(id)) {
              // $nextTick ---- do the code after next move (rendering dialog)
              this.$nextTick(()=>{
                this.$refs.tree.setChecked(id, false)
              })

            }
          })
        })
      })

      this.menuDialogVisible = true;
    },
    handleCheckChange(data, checked, indeterminate) {
      // console.log(data, checked, indeterminate);
    },
    saveRoleMenu() {
      this.request.post("/role/roleMenu/" + this.editedRoleId, this.$refs.tree.getCheckedKeys())
          .then(res => {
            if (res.code === "200") {
              this.$message.success("绑定成功")
              this.menuDialogVisible = false
            } else {
              this.$message.error(res.message)
            }
          })

    },
  },
  created() {
    this.loadData();
  }
}
</script>

<style scoped>
.headerBackground {
  background-color: #eee !important;
}

</style>