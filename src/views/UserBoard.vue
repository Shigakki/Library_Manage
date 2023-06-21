<template>
  <div>
    <div style="margin: 10px 0">
      <el-input 
        suffix-icon="el-icon-search"
        placeholder="请输入名字"
        v-model="username"
        style="width: 200px; margin-left: 5px"
      ></el-input>
      <!-- <el-input
        suffix-icon="el-icon-message"
        placeholder="请输入邮箱"
        v-model="email"
        style="width: 200px; margin-left: 5px"
      ></el-input>
      <el-input
        suffix-icon="el-icon-phone"
        placeholder="请输入电话"
        v-model="phone"
        style="width: 200px; margin-left: 5px"
      ></el-input> -->
      <el-button style="margin-left: 5px" type="primary" @click="loadData"
        >搜索</el-button
      >
      <el-button style="margin-left: 5px" type="warning" @click="reset"
        >刷新</el-button
      >
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd"
        >新增<i class="el-icon-circle-plus-outline" style="margin-left: 4px"></i
      ></el-button>
      <el-popconfirm
        confirm-button-text="好的"
        cancel-button-text="不用了"
        icon="el-icon-info"
        icon-color="red"
        title="确定批量删除选中的数据？"
        style="margin: 5px"
        @confirm="deleteBatch"
      >
        <el-button type="danger" slot="reference"
          >批量删除<i
            class="el-icon-remove-outline"
            style="margin-left: 4px"
          ></i
        ></el-button>
      </el-popconfirm>

      <el-upload
        action="http://localhost:9090/user/import"
        style="display: inline-block; margin: 4px"
        accept="xlsx"
        :on-success="handleImportSuccess"
        :show-file-list="false"
      >
        <el-button type="primary" @click="importUsersData"
          >导入<i class="el-icon-upload" style="margin-left: 4px"></i
        ></el-button>
      </el-upload>
      <el-button type="primary" @click="exportUsersData"
        >导出<i class="el-icon-download" style="margin-left: 4px"></i
      ></el-button>
    </div>

    <el-table
      :data="tableData"
      border
      stripe
      header-cell-class-name="headerBackground"
      @selection-change="handleSelectionChanged"
    >
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column prop="id" label="用户ID" width="140"></el-table-column>
      <el-table-column prop="username" label="姓名" width="120"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="phone" label="手机"></el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>

      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEditRow(scope.row)"
            >编辑<i class="el-icon-edit" style="margin-left: 4px"></i
          ></el-button>
          <el-popconfirm
            confirm-button-text="好的"
            cancel-button-text="不用了"
            icon="el-icon-info"
            icon-color="red"
            title="确定删除该条记录吗？"
            style="margin-left: 5px"
            @confirm="handleDeleteRow(scope.row.id)"
          >
            <el-button slot="reference" type="danger">
              删除<i
                class="el-icon-remove-outline"
                style="margin-left: 4px"
              ></i>
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
        :total="totalNum"
      >
      </el-pagination>
    </div>

    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="用户ID" v-if="!this.adding" >
          <el-input v-model="form.id" autocomplete="off" disabled></el-input>
        </el-form-item>
        <!-- <el-form-item label="角色">
        <el-select clearable v-model="form.role" placeholder="请选择角色" style="width: 100%">
          <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.ukey"></el-option>
        </el-select>
      </el-form-item> -->
        <el-form-item label="用户名">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <!--              <el-form-item label="活动区域" :label-width="formLabelWidth">-->
        <!--                <el-select v-model="form.region" placeholder="请选择活动区域">-->
        <!--                  <el-option label="区域一" value="shanghai"></el-option>-->
        <!--                  <el-option label="区域二" value="beijing"></el-option>-->
        <!--                </el-select>-->
        <!--              </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
          @click="
            dialogFormVisible = false;
            loadData();
          "
          >取 消</el-button
        >
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "UserBoard",
  data() {
    return {
      tableData: [],
      totalNum: 0,
      pageNum: 1,
      pageSize: 10,

      username: "",
      phone: "",
      email: "",
      address: "",
      password: "",

      dialogFormVisible: false,
      form: {},
      adding:false,
      multipleSelection: [],
      roles: [],
    };
  },
  methods: {
    loadData() {
      // request.get('http://localhost:9090/user/page?pageNum='+this.pageNum+'&pageSize='+this.pageSize)
      //     .then(res=>{
      //       this.tableData=res.records;
      //       this.totalNum=res.total;
      //     })
      this.request
        .get("/user/", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            search: this.username,
          },
        })
        .then((res) => {
          console.log(res);
          this.tableData = res.data.records;
          this.totalNum = res.data.total;
        });
    },
    reset() {
      this.username = "";
      this.email = "";
      this.phone = "";
      this.address = "";
      this.loadData();
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
      this.adding = true;
      this.form = {};
    },
    handleEditRow(row) {
      this.form = row;
      this.adding = false;
      this.dialogFormVisible = true;
    },
    handleDeleteRow(id) {
      this.request.delete("/user/" + id).then((res) => {
        if (res) {
          this.$message.success("删除成功");
          this.loadData();
        } else {
          this.$message.error("删除失败");
          this.loadData();
        }
      });
    },
    deleteBatch() {
      let ids = this.multipleSelection.map((v) => v.id); // [ {}, {}, {} ] => [ 1,2,3 ]
      ids.forEach((id) => {
        this.request.delete("/user/" + id).then((res) => {
          if (res) {
            this.$message.success("批量删除成功");
            this.loadData();
          } else {
            this.$message.error("批量删除失败");
            this.loadData();
          }
        });
      });
    },
    handleSelectionChanged(val) {
      this.multipleSelection = val;
    },
    save() {
      if (this.adding) {
        this.request.post("/user/", this.form).then((res) => {
          if (res) {
            this.$message.success("保存成功");
            this.dialogFormVisible = false;
            this.loadData();
          } else {
            this.$message.error("保存失败");
            this.dialogFormVisible = false;
            this.loadData();
          }
        });
      }else {
        this.request.put("/user/", this.form).then((res) => {
          if (res) {
            this.$message.success("保存成功");
            this.dialogFormVisible = false;
            this.loadData();
          } else {
            this.$message.error("保存失败");
            this.dialogFormVisible = false;
            this.loadData();
          }
        });
      }
    },
    importUsersData() {},
    exportUsersData() {
      window.open("http://localhost:9090/user/export");
    },
    handleImportSuccess() {
      this.$message.success("文件导入成功");
      this.loadData();
    },
  },

  created() {
    this.loadData();
  },
};
</script>

<style scoped>
.headerBackground {
  background-color: #eee !important;
}
</style>