<template>
  <div class="wrapper">
    <div style="margin:200px auto;background-color: #fff; width: 350px; height: 400px; padding: 20px; border-radius: 10px">
      <div style="margin:20px 0; text-align: center; font-size: 24px"><b>注册</b></div>

      <el-form :model="user" :rules="rules" ref="loginForm">
        <el-form-item prop="uid">
          <el-input placeholder="请输入账号" size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.uid"></el-input>
        </el-form-item>
        <el-form-item prop="username">
          <el-input placeholder="请输入用户名" size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="请输入密码" size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password
                    v-model="user.password"></el-input>
        </el-form-item>

        <el-form-item prop="confirmedPassword">
          <el-input placeholder="确认密码" size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password
                    v-model="user.comfirmedPassword"></el-input>
        </el-form-item>

        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button type="primary" size="small" autocomplete="off" @click="login">确定</el-button>
          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/login')">返回登陆</el-button>
        </el-form-item>
      </el-form>

    </div>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      user: {
        uid:"",
        password: ""
      },
      rules:{
        uid:[
          {required:true, message:'请输入用户名',trigger:'blur'},
          {min:3,max:20, message:'长度在3到20个字符',trigger:'blur'}
        ],
        password:[
          {required:true, message:'请输入密码',trigger:'blur'},
          {min:3,max:20, message:'长度在3到20个字符',trigger:'blur'}
        ],
        comfirmedPassword:[
          {required:true, message:'确认密码',trigger:'blur'},
          {min:3,max:20, message:'长度在3到20个字符',trigger:'blur'}
        ]
      }
    }
  },
  methods:{
    login(){
      this.$refs["loginForm"].validate((valid)=>{
        if (valid) {
          if (this.user.password !== this.user.comfirmedPassword) {
            this.$message.error("两次输入的密码不相同")
            return false
          }
          this.request.post("/user/register", this.user)
              .then(res => {
                if (res.code!=='200') {
                  this.$message.error(res.message);
                } else {
                  this.$message.success("注册成功")
                  this.$router.push("/login")
                }
              });
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style scoped>
.wrapper {
  height: 100vh;
  background-image: linear-gradient(to bottom right, #FC466B, #3f5efB);
  overflow: hidden;
}
</style>