<template>
  <el-menu :default-openeds="['1', '3']" style="height: 100%; overflow-x: hidden"
           background-color="rgb(48,65,86)" text-color="#fff" active-text-color="#ffd04b"
           :collapse-transition="false"
           :collapse="isCollapsed"
           router=""
           @select="handleSelect"
  >
    <div style="height: 60px;line-height: 60px; text-align: center">
      <img src="../assets/logo.png" alt="" style="width: 20px; position: relative;top: 5px; margin-right: 5px">
      <b style="color: white" v-show="logoTextShow">后台管理系统</b>
    </div>

    <div v-for="item in this.menus" :key="item.id">

        <!-- <el-menu-item :index="item.path">
            <i :class="item.icon"></i>
            <span slot="title">{{ item.name }}</span>
        </el-menu-item> -->

        <el-menu-item :index="item.path+''">
          <template slot="title">
            <i :class="item.icon"></i>
            <span slot="title">{{ item.name }}</span>
          </template>
          
        </el-menu-item>
    </div>
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props: {
    isCollapsed: Boolean,
    logoTextShow: Boolean,
  },
  data() {
    return {
      menus:[
        {
          path:'/book-board',
          name:'图书管理'
        },
        {
          path:'/borrow-board',
          name:'图书借还'
        },
        {
          path:'/myInfo',
          name:'图书馆简介'
        },
         {
          path:'/user-board',
          name:'用户管理'
        },
      ],
      // opens:localStorage.getItem('menus')? JSON.parse(localStorage.getItem('menus')).map(v=>v.id+'') :[],
    }
  },
  methods: {
    handleSelect(index) {
      // console.log(this.$router.options)
    }
  }
}
</script>

<style scoped>

</style>