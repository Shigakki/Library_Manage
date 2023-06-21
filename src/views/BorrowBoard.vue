<template>
  <div>
    <!-- 搜索框 -->
    <div style="margin: 10px 0">
      <el-input
        suffix-icon="el-icon-search"
        placeholder="请输入用户id"
        v-model="uid"
        style="width: 200px; margin-left: 5px"
      ></el-input>
      <el-input
        suffix-icon="el-icon-search"
        placeholder="请输入用户名"
        v-model="username"
        style="width: 200px; margin-left: 5px"
      ></el-input>
      <el-input
        suffix-icon="el-icon-message"
        placeholder="请输入图书ID"
        v-model="bookId"
        style="width: 200px; margin-left: 5px"
      ></el-input>
      <el-input
        suffix-icon="el-icon-message"
        placeholder="请输入书名"
        v-model="title"
        style="width: 200px; margin-left: 5px"
      ></el-input>
      <el-select
        v-model="form.status"
        placeholder="请选择状态"
        suffix-icon="el-icon-message"
        style="width: 200px; margin-left: 5px"
      >
        <el-option label="全部" value="shanghai"></el-option>
        <el-option label="已归还" value="shanghai"></el-option>
        <el-option label="未归还" value="beijing"></el-option>
      </el-select>
      <el-button style="margin-left: 5px" type="primary" @click="loadData"
        >搜索</el-button
      >
      <el-button style="margin-left: 5px" type="warning" @click="reset"
        >刷新</el-button
      >
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">
        借书
        <i class="el-icon-circle-plus-outline" style="margin-left: 4px"></i>
      </el-button>

      <el-popconfirm
        confirm-button-text="好的"
        cancel-button-text="不用了"
        icon="el-icon-info"
        icon-color="red"
        title="确定批量删除选中的数据？"
        style="margin: 5px"
        @confirm="deleteBatch"
      >
        <el-button type="danger" slot="reference">
          批量删除
          <i class="el-icon-remove-outline" style="margin-left: 4px"></i>
        </el-button>
      </el-popconfirm>
    </div>

    <el-table
      :data="tableData"
      border
      stripe
      header-cell-class-name="headerBackground"
      @selection-change="handleSelectionChanged"
    >
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column prop="uid" label="用户ID" width="140"></el-table-column>
      <el-table-column
        prop="username"
        label="用户名"
        width="140"
      ></el-table-column>
      <el-table-column
        prop="bookId"
        label="图书ID"
        width="140"
      ></el-table-column>
      <el-table-column prop="title" label="书名" width="120"></el-table-column>
      <el-table-column prop="borrowDate" label="借阅时间"></el-table-column>
      <el-table-column prop="returnDate" label="归还时间"></el-table-column>

      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="success" @click="handleGiveBack(scope.row)"
            >还书<i class="el-icon-edit" style="margin-left: 4px"></i
          ></el-button>
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
            @confirm="handleDeleteRow(scope.row.uid)"
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

    <!-- 借书 -->
    <el-dialog title="借书信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="图书ID">
          <el-input
            v-model="form.id"
            autocomplete="off"
            @input="onBookIdChange"
          ></el-input>
        </el-form-item>
        <el-form-item label="书名">
          <el-input v-model="form.title" :key="this.refreshKey"></el-input>
        </el-form-item>
        <el-form-item label="作者">
          <el-input
            v-model="form.author"
            autocomplete="off"
            :key="this.refreshKey"
          ></el-input>
        </el-form-item>

        <el-form-item label="用户ID">
          <el-input
            v-model="form.uid"
            autocomplete="off"
            @input="onUserIdChange"
          ></el-input>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input
            v-model="form.username"
            autocomplete="off"
            :key="this.refreshKey"
          ></el-input>
        </el-form-item>

        <el-form-item label="借阅时间">
          <div class="block">
            <el-date-picker
              v-model="form.borrowDate"
              type="datetime"
              placeholder="选择日期时间"
              default-time="12:00:00"
            >
            </el-date-picker>
          </div>
          <!-- <el-input
            v-model="form.availableCopies"
            autocomplete="off"
          ></el-input> -->
        </el-form-item>
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

    <el-dialog title="还书" :visible.sync="dialogGiveBackVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="图书ID">
          <el-input
            v-model="giveBackForm.bookId"
            autocomplete="off"
            :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="书名">
          <el-input v-model="giveBackForm.title" :disabled="true"></el-input>
        </el-form-item>

        <el-form-item label="用户ID">
          <el-input
            v-model="giveBackForm.uid"
            autocomplete="off"
            @input="onUserIdChange"
          ></el-input>
        </el-form-item>

        <el-form-item label="借阅时间">
          <div class="block">
            <el-date-picker
              v-model="giveBackForm.borrowDate"
              type="datetime"
              placeholder="选择日期时间"
              default-time="12:00:00"
              :disabled="true"
            >
            </el-date-picker>
          </div>
        </el-form-item>
        <el-form-item label="归还时间">
          <div class="block">
            <el-date-picker
              v-model="giveBackForm.returnDate"
              type="datetime"
              placeholder="选择日期时间"
              default-time="12:00:00"
              :disabled="true"
            >
            </el-date-picker>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
          @click="
            dialogGiveBackVisible = false;
            loadData();
          "
          >取 消</el-button
        >
        <el-button type="primary" @click="giveBack">确 定</el-button>
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

      title: "",
      author: "",
      description: "",
      uid: "",
      username: "",
      bookId: "",

      totalCopies: "",
      availableCopies: "",

      dialogFormVisible: false,
      form: {},
      dialogGiveBackVisible: false,
      givebackForm: {},

      multipleSelection: [],
      roles: [],
      refreshKey: 0,

      queue: [],
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
        .get("/borrow_queue/", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            title: this.title,
            author: this.author,
          },
        })
        .then((res) => {
          this.tableData = res.records;
          this.totalNum = res.total;
        });
    },
    reset() {
      this.title = "";
      this.author = "";
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
      this.form = {};
    },
    handleEditRow(row) {
      this.form = row;
      this.dialogFormVisible = true;
    },
    handleGiveBack(row) {
      dialogGiveBackVisible = true;
    },
    giveBack() {
      this.request
        .put("/borrow_records/", this.giveBackForm)
        .then((res) => {
          console.log(res);
          // 删除最近一条
          this.request
            .delete("/borrow_queue/",this.givebackForm.bookId)
            .then((res) => {
              console.log(res);
            })
            .catch((error) => {});
        })
        .catch((error) => {});
    },
    onBookIdChange() {
      this.request.get("/book/" + this.form.id).then((res) => {
        if (res.code == "0" && res.data != null) {
          this.$nextTick(() => {
            console.log(this.form.title);
            this.form.title = res.data.title;
            this.form.author = res.data.author;
          });
          console.log(this.form.title);
        } else {
          this.$nextTick(() => {
            this.form.title = "";
            this.form.author = "";
          });
        }
        this.refreshKey++;
      });
    },
    onUserIdChange() {
      this.request.get("/user/" + this.form.uid).then((res) => {
        if (res.code == "0" && res.data != null) {
          this.$nextTick(() => {
            this.form.username = res.data.username;
          });
        } else {
          this.$nextTick(() => {
            this.form.username = "";
          });
        }
        this.refreshKey++;
      });
    },
    handleDeleteRow(id) {
      this.request.delete("/book/delete/" + id).then((res) => {
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
      let uids = this.multipleSelection.map((v) => v.uid); // [ {}, {}, {} ] => [ 1,2,3 ]
      this.request.post("/book/delete/batch", uids).then((res) => {
        if (res) {
          this.$message.success("批量删除成功");
          this.loadData();
        } else {
          this.$message.error("批量删除失败");
          this.loadData();
        }
      });
    },
    handleSelectionChanged(val) {
      this.multipleSelection = val;
    },
    save() {
      this.request.post("/book/insertOrUpdate", this.form).then((res) => {
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