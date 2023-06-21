<template>
  <div class="wrapper">
    <div
      style="
        margin: 200px auto;
        background-color: #fff;
        width: 350px;
        height: 300px;
        padding: 20px;
        border-radius: 10px;
      "
    >
      <div style="margin: 20px 0; text-align: center; font-size: 24px">
        <b>登录</b>
      </div>

      <el-form :model="user" :rules="rules" ref="loginForm">
        <el-form-item prop="username">
          <el-input
            size="medium"
            style="margin: 10px 0"
            prefix-icon="el-icon-user"
            v-model="user.username"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            size="medium"
            style="margin: 10px 0"
            prefix-icon="el-icon-lock"
            show-password
            v-model="user.password"
          ></el-input>
        </el-form-item>

        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button
            type="primary"
            size="small"
            autocomplete="off"
            @click="login"
            >登录</el-button
          >
          <el-button
            type="warning"
            size="small"
            autocomplete="off"
            @click="$router.push('/register')"
            >注册</el-button
          >
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { resetRouter, setRoutes } from "@/router";

export default {
  name: "Login",
  data() {
    return {
      user: {
        username: "",
        password: "",
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 3, max: 20, message: "长度在3到20个字符", trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 3, max: 20, message: "长度在3到20个字符", trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    login() {
      this.$refs["loginForm"].validate((valid) => {
        if (valid) {
          this.request.post("/user/login", this.user).then((res) => {
            console.log(res)
            if (res.code !== "0") {
              this.$message.error(res.msg);
            } else {
              localStorage.setItem("user", JSON.stringify(res.data));
              localStorage.setItem("menus", []);
              // reset router or next time u login ,the router is the old one and some errors will occur
              resetRouter();
              setRoutes();
              this.$router.push("/home");
              this.$message.success("登陆成功");
            }
          });
        } else {
          return false;
        }
      });
    },
  },
};
</script>

<style scoped>
.wrapper {
  height: 100vh;
  background-image: linear-gradient(to bottom right, #fc466b, #3f5efb);
  overflow: hidden;
}
</style>