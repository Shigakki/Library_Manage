<template>
<el-card style="width: 500px; padding: 20px; border: 1px solid #CCC" title="图书馆照片">
    <el-form label-width="80px"  size="small">
      <el-upload
          class="avatar-uploader"
          action="http://localhost:9090/file/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          >
        <img v-if="form.avatarUrl" :src="form.avatarUrl" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>

      <el-form-item label="图书馆名">
        <el-input v-model="form.libraryName" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="开馆时间">
        <el-input v-model="form.openTime" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="闭馆时间">
        <el-input v-model="form.closeTime" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="简介">
        <el-input type="textarea" v-model="form.description" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item style="text-align: right">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </el-form-item>
    </el-form>
</el-card>
</template>

<script>
export default {
  name: "Person",
  data(){
    return{
      form:{},
      user: localStorage.getItem('user')?JSON.parse(localStorage.getItem('user')):{}
    }
  },
  created() {
    this.request.get('/library/getLibraryById/'+this.user.uid)
        .then(res=>{
          if (res.code === '200') {
            this.form=res.data;

            this.$emit('refreshUser')
          }

        })
  },
  methods:{
    save(){
      console.log(this.form)
      this.request.post("/library/insertOrUpdate",this.form)
          .then(res=>{
            if (res){
              this.$message.success('保存成功')
              this.$emit('refreshUser')
              this.getCurrentUser().then(res=>{
                res.token=JSON.parse(localStorage.getItem('user')).token
                localStorage.setItem('user',JSON.stringify(res))
              })
            }else {
              this.$message.error('保存失败')
            }
          })
    },
    getCurrentUser(){
      let uid=JSON.parse(localStorage.getItem('user')).uid
      this.request.get('/user/getByUid/'+uid)
          .then(res=>{
            this.user=res.data
            return res.data
          })
    },
    cancel(){
      this.$router.back()
    },
    handleAvatarSuccess(res){
      this.form.avatarUrl=res
    },
  }
}
</script>

<style>
.avatar-uploader{
  text-align: center;
  padding-bottom: 10px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>