<template>
  <div>
    <!-- 搜索框 -->
    <div style="margin: 10px 0">
      <el-input
        suffix-icon="el-icon-search"
        placeholder="请输入书名"
        v-model="title"
        style="width: 200px; margin-left: 5px"
      ></el-input>
      <el-input
        suffix-icon="el-icon-message"
        placeholder="请输入作者"
        v-model="author"
        style="width: 200px; margin-left: 5px"
      ></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="loadData"
        >搜索</el-button
      >
      <el-button style="margin-left: 5px" type="warning" @click="reset"
        >刷新</el-button
      >
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">
        新增
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

  <!-- 图书数据 -->
    <el-table
      :data="tableData"
      border
      stripe
      header-cell-class-name="headerBackground"
      @selection-change="handleSelectionChanged"
    >
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column prop="id" label="图书id" width="100"></el-table-column>
      <el-table-column prop="title" label="书名" width="200"></el-table-column>
      <el-table-column prop="author" label="作者"></el-table-column>
      <el-table-column
        prop="totalCopies"
        label="总量"
        width="100"
      ></el-table-column>
      <el-table-column
        prop="availableCopies"
        label="余量"
        width="100"
      ></el-table-column>
      <el-table-column prop="createTime" label="馆藏时间"></el-table-column>

      <el-table-column label="操作" width="400">
        <template slot-scope="scope">
          <el-button type="primary" @click="handleBorrow(scope.row)"
            >借书<i class="el-icon-edit" style="margin-left: 4px"></i
          ></el-button>
          <el-button type="primary" @click="handleCheckQueue(scope.row)"
            >借阅队列<i class="el-icon-edit" style="margin-left: 4px"></i
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

    <!-- 修改图书信息 -->
    <el-dialog title="图书信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="书名">
          <el-input v-model="form.title" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="form.author" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="总量">
          <el-input v-model="form.totalCopies" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="余量">
          <el-input
            v-model="form.availableCopies"
            autocomplete="off"
          ></el-input>
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

    <!-- 借书 -->
    <el-dialog title="借书信息" :visible.sync="dialogBorrowVisible" width="30%">
      <el-form label-width="80px" size="small">

        <el-form-item label="图书ID">
          <el-input
            v-model="borrowForm.bookId"
            autocomplete="off"
            @input="onBookIdChange" :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="书名">
          <el-input v-model="borrowForm.title" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="作者">
          <el-input
            v-model="borrowForm.author"
            autocomplete="off"
            :disabled="true"
          ></el-input>
        </el-form-item>

        <el-form-item label="用户ID">
          <el-input
            v-model="borrowForm.uid"
            autocomplete="off"
            @input="onUserIdChange"
          ></el-input>
        </el-form-item>

        <el-form-item label="借阅时间">
          <div class="block">
            <el-date-picker
              v-model="borrowForm.borrowTime"
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
            dialogBorrowVisible = false;
            loadData();
          "
          >取 消</el-button
        >
        <el-button type="primary" @click="borrow">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 借阅队列dialog -->
    <el-dialog
      title="借阅队列"
      :visible.sync="dialogQueueVisible"
      width="70%"
      height="80%"
    >
      <el-table
        :data="queueTable"
        border
        stripe
        header-cell-class-name="headerBackground"
        @selection-change="handleSelectionChanged"
      >
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column prop="id" label="排队记录ID" width="100" v-if="false" ></el-table-column>
        <el-table-column prop="userId" label="用户ID" width="100" ></el-table-column>
        <el-table-column prop="queuePosition" label="序号"></el-table-column>

        <el-table-column label="操作" width="400">
          <template slot-scope="scope">
            <el-button type="primary" @click="borrowBook(scope.row)" :disabled="scope.row.availableCopies" >
              出借<i class="el-icon-edit" style="margin-left: 4px"></i >
            </el-button>
            <el-popconfirm
              confirm-button-text="好的"
              cancel-button-text="不用了"
              icon="el-icon-info"
              icon-color="red"
              title="确定删除该条记录吗？"
              style="margin-left: 5px"
              @confirm="handleDeleteQueueItem(scope.row.id)"
            >
              <el-button slot="reference" type="danger">
                删除<i class="el-icon-remove-outline" style="margin-left: 4px" ></i>
              </el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogQueueVisible = false" >确 定</el-button>
      </div>
    </el-dialog>

    <!-- 处理入队的dialog -->
    <el-dialog
      title="当前图书无余量，请排队"
      :visible.sync="dialogReserveVisible"
      width="30%"
    >
      <el-form label-width="80px" size="small">
        <el-form-item label="图书ID">
          <el-input
            v-model="reserveForm.bookId"
            autocomplete="off"
            :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="书名">
          <el-input
            v-model="reserveForm.title"
            autocomplete="off"
            :disabled="true"
          ></el-input>
        </el-form-item>
        <el-form-item label="用户ID">
          <el-input v-model="reserveForm.uid" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="排队次序">
          <el-input
            v-model="reserveForm.queuePosition"
            autocomplete="off"
            :disabled="true"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogReserveVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleReserve">确 定</el-button>
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
      totalCopies: "",
      availableCopies: "",

      dialogFormVisible: false,
      form: {},

      queueTable: [],
      dialogQueueVisible:false,
      dialogReserveVisible: false,
      dialogBorrowVisible:false,
      reserveForm: {},
      borrowForm:{},
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
        .get("/book/", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            search: this.title,
            // author: this.author
          },
        })
        .then((res) => {
          console.log(res);
          this.tableData = res.data.records;
          this.totalNum = res.data.total;
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
      this.adding = true;
      this.form = {};
    },
    handleEditRow(row) {
      this.form = row;
      this.adding = false;
      this.dialogFormVisible = true;
    },
    handleDeleteRow(id) {
      console.log(id);
      this.request.delete("/book/" + id).then((res) => {
        if (res) {
          this.$message.success("删除成功");
          this.loadData();
        } else {
          this.$message.error("删除失败");
          this.loadData();
        }
      });
    },
    handleBorrow(row) {
      if (row.availableCopies > 0) {
        this.dialogBorrowVisible = true;
      } else {
        this.dialogReserveVisible = true;
        this.reserveForm.bookId = row.bookId;
        this.reserveForm.bookName = row.bookName;

        this.request
          .get("/borrow_queue/", {
            params: {
              bookId: bookId,
            },
          })
          .then((res) => {
            console.log(res);
            this.queueTable = res.data
          });

        this.reserveForm.queuePosition = this.queueTable.length + 1;
      }
    },

    // 排队相关
    handleCheckQueue(row) {
      this.dialogQueueVisible = true;
      let bookId = row.bookId;
      this.request
        .get("/borrow_queue/", {
          params: {
            bookId: bookId,
          },
        })
        .then((res) => {
          console.log(res);
        });
    },
    handleReserve(){
      this.request
        .get("/borrow_queue/", {
          params: {
            bookId: this.reserveForm.bookId,
            bookName:this.reserveForm.bookName,
            uid:this.this.reserveForm.uid,
            queuePosition: this.reserveForm.queuePosition
          },
        })
        .then((res) => {
          console.log(res);
        });
        this.dialogReserveVisible=false;
    },

    deleteBatch() {
      let ids = this.multipleSelection.map((v) => v.id); // [ {}, {}, {} ] => [ 1,2,3 ]
      console.log(ids);
      ids.forEach((id) => {
        this.request.delete("/book/" + id).then((res) => {
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
      console.log(this.multipleSelection);
    },
    save() {
      if (!this.adding) {
        this.request.put("/book/", this.form).then((res) => {
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
      } else {
        this.request.post("/book/", this.form).then((res) => {
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