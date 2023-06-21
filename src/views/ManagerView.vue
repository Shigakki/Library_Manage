<template xmlns="http://www.w3.org/1999/html">
  <div class="home" style="height: 100%;">
    <el-container style="height: 100%">

      <el-aside :width="sideWidth+'px'" style="background-color: rgb(238, 241, 246); box-shadow: rgb(0,21,41,0.32) 2px 0px 6px">
        <Aside :isCollapsed="isCollapsed" :logoTextShow="logoTextShow" ></Aside>
      </el-aside>

      <el-container>

        <el-header style="border-bottom: 1px solid #ccc;">
          <Header :collapseBtnClass="collapseBtnClass" :collapse="collapse" :user="user"></Header>
        </el-header>

        <el-main>
<!--表示当前页面的子路由(routes里的children)会在 router view 中展示-->
          <router-view @refreshUser="getCurrentUser"></router-view>

        </el-main>

      </el-container>

    </el-container>
  </div>
</template>

<script>

import Aside from "@/components/Aside";
import Header from "@/components/Header";

export default {
  name: 'HomeView',
  components: {
    Aside,
    Header
  },
  data() {
    return {
      collapseBtnClass:'el-icon-s-fold',

      isCollapsed:false,
      sideWidth:200,
      logoTextShow:true,

      user: {}

    }
  },
  created() {
    this.getCurrentUser()
  },
  methods:{
    collapse(){
      this.isCollapsed=!this.isCollapsed;
      if (this.isCollapsed){
        this.sideWidth=64
        this.collapseBtnClass='el-icon-s-unfold'
        this.logoTextShow=false
      }else{
        this.sideWidth=200
        this.collapseBtnClass='el-icon-s-fold'
        this.logoTextShow=true
      }
    },
    getCurrentUser(){
      let uid=JSON.parse(localStorage.getItem('user')).id
      this.request.get('/user/'+uid)
          .then(res=>{
            this.user=res.data
          })
    }

  }
}
</script>

<style>
.headerBackground{
  background-color: #eee !important;
}

</style>