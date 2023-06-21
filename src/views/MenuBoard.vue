<template>
<div>


  <div style="margin: 10px 0">
    <el-input suffix-icon="el-icon-search" placeholder="请输入名称" v-model="name_search" style="width: 200px;margin-left: 5px"></el-input>
    <el-button style="margin-left: 5px" type="primary" @click="loadData">搜索</el-button>
    <el-button style="margin-left: 5px" type="warning" @click="reset">刷新</el-button>
  </div>

  <div style="margin: 10px 0">
    <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline" style="margin-left: 4px"></i></el-button>
    <el-popconfirm
        confirm-button-text='好的'
        cancel-button-text='不用了'
        icon="el-icon-info"
        icon-color="red"
        title="确定批量删除选中的数据？"
        style="margin: 5px"
        @confirm="deleteBatch"
    >
      <el-button type="danger" slot="reference">批量删除<i class="el-icon-remove-outline" style="margin-left: 4px"></i></el-button>
    </el-popconfirm>

  </div>

  <el-table :data="tableData" border stripe header-cell-class-name="headerBackground"
            @selection-change="handleSelectionChanged" row-key="id" default-expand-all
  >
    <el-table-column
        type="selection"
        width="55">
    </el-table-column>
    <el-table-column prop="id" label="id" width="100"></el-table-column>
    <el-table-column prop="name" label="名称"></el-table-column>
    <el-table-column prop="path" label="路径"></el-table-column>
    <el-table-column prop="pagePath" label="页面路径"></el-table-column>
    <el-table-column label="图标" align="center">
      <template slot-scope="scope">
        <span :class="scope.row.icon" style="font-size: 18px;"></span>
      </template>
    </el-table-column>
    <el-table-column prop="description" label="描述"></el-table-column>

    <el-table-column label="操作" width="350px">
      <template slot-scope="scope">
        <el-button type="primary" @click="handleAdd(scope.row.id)" v-if="!scope.row.pid">新增子菜单<i class="el-icon-plus" style="margin-left: 4px"></i></el-button>
        <el-button type="success" @click="handleEditRow(scope.row)">编辑<i class="el-icon-edit" style="margin-left: 4px"></i></el-button>
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

  <el-dialog title="菜单信息" :visible.sync="dialogFormVisible" width="30%">
    <el-form label-width="80px"  size="small">
      <el-form-item label="名称">
        <el-input v-model="form.name" autocomplete="off" placeholder="请输入名称"></el-input>
      </el-form-item>
      <el-form-item label="路径">
        <el-input v-model="form.path" autocomplete="off" placeholder="请输入路径"></el-input>
      </el-form-item>
      <el-form-item label="页面路径">
        <el-input v-model="form.pagePath" autocomplete="off" placeholder="请输入页面路径"></el-input>
      </el-form-item>
      <el-form-item label="图标">
        <el-select clearable v-model="form.icon" placeholder="请选择" style="width: 80%">
          <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
            <i :class="item.value"/> {{item.name}}
          </el-option>

        </el-select>
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="form.description" autocomplete="off" placeholder="请输入描述"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false; loadData()">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
</div>
</template>

<script>
export default {
  name: "MenuBoard",
  data(){
    return {
      tableData: [],
      totalNum:0,

      name_search:"",

      dialogFormVisible:false,
      form:{},

      multipleSelection:[],
      options:[]
    }
  },
  methods:{
    loadData(){
      // request.get('http://localhost:9090/user/page?pageNum='+this.pageNum+'&pageSize='+this.pageSize)
      //     .then(res=>{
      //       this.tableData=res.records;
      //       this.totalNum=res.total;
      //     })
      this.request.get('/menu/list',{
        params:{
          name:this.name_search,
        }
      }).then(res=>{
        console.log(res)
        this.tableData=res.data;
      })
    },
    reset(){
      this.name_search=""
      this.loadData()
    },
    handleSizeChange(pageSize){
      this.pageSize=pageSize;
      this.loadData();
    },
    handleCurrentChange(pageNum){
      this.pageNum=pageNum;
      this.loadData();
    },
    handleAdd(id){
      this.request.get('/menu/icons').then(res=>{
        this.options=res.data;
      })

      this.dialogFormVisible=true;
      this.form={}
      if (!isNaN(id)){
        console.log('here')
        this.form.pid=id
      }
    },
    handleEditRow(row){
      this.request.get('/menu/icons').then(res=>{
        this.options=res.data;
      })

      this.form=row
      this.dialogFormVisible=true
    },
    handleDeleteRow(id){
      this.request.delete("/menu/delete/"+id)
          .then(res=>{
            if (res){
              this.$message.success('删除成功')
              this.loadData();
            }else {
              this.$message.error('删除失败')
              this.loadData();
            }
          })
    },
    deleteBatch(){
      let id=this.multipleSelection.map(v=>v.id)  // [ {}, {}, {} ] => [ 1,2,3 ]
      this.request.post("/menu/delete/batch",id)
          .then(res=>{
            if (res){
              this.$message.success('批量删除成功')
              this.loadData();
            }else {
              this.$message.error('批量删除失败')
              this.loadData();
            }
          })
    },
    handleSelectionChanged(val){
      this.multipleSelection=val
    },
    save(){
      this.request.post("/menu/insertOrUpdate",this.form)
          .then(res=>{
            if (res){
              this.$message.success('保存成功')
              this.dialogFormVisible=false;
              this.loadData();
            }else {
              this.$message.error('保存失败')
              this.dialogFormVisible=false;
              this.loadData();
            }
          })
    },
  },
  created(){
    this.loadData();
  }
}
</script>

<style scoped>
.headerBackground{
  background-color: #eee !important;
}

</style>