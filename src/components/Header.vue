<template>
  <!--  因为根元素只能有一个div，所以要在最外层加一个div -->
  <div style="font-size: 12px;line-height: 60px; display: flex">
    <div style="flex:1; font-size: 18px">
      <span :class="collapseBtnClass" style="cursor:pointer;font-size: 20px;" @click="collapse"></span>
      <el-breadcrumb separator="/" style="display: inline-block; margin-left: 12px; font-size: 17px">
        <el-breadcrumb-item :to="'/home'">首页</el-breadcrumb-item>
        <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <el-dropdown style="width: 150px; cursor: pointer;text-align: right">
      <img :src="user.avatarUrl" style="width: 50px;height: 50px;line-height: 60px;border-radius: 100px;text-align: right">
      <span>{{ user.username }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
      <el-dropdown-menu slot="dropdown" style="font-size: 150px;width: 120px; text-align: center">
        <el-dropdown-item>
          <span @click="goToMyInfo" style="text-decoration: none">个人信息</span>
        </el-dropdown-item>
        <el-dropdown-item>
          <span to="/login" style="text-decoration: none" @click="logout">退出登录</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
import {resetRouter} from "@/router";

export default {
  name: "Header",
  props: {
    collapseBtnClass: String,
    collapse: Function,
    pathName:String,
    user:Object
  },
  data() {
    return {
      currentPathName: localStorage.getItem("currentPathName"),
    }
  },
  watch:{
    '$route':function (){
        this.currentPathName=localStorage.getItem("currentPathName")
    }
  },
  methods:{
    logout(){
      localStorage.removeItem("user")
      localStorage.removeItem('menus')
      this.$router.push('/login')
      this.$message.success("退出成功")

      // reset router or next time u login ,the router is the old one and some errors will occur
      resetRouter()
    },
    goToMyInfo(){
      this.$router.push('/myInfo')
    }
  }
}
</script>

<style scoped>

</style>