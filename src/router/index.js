import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/ManagerView.vue'
import ManagerView from "@/views/ManagerView";

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: '管理',
        component: () => import('../views/ManagerView'),
        redirect: "/login",
        children: [
            {path: 'home', name: '', component: () => import('../views/Home'),},
            {path: 'user-board', name: '系统管理/用户管理', component: () => import('../views/UserBoard'),},
            {path: '/myInfo', name: '图书馆信息', component: () => import('../views/MyInfo')},
            {path: '/upload-file-board', name: '文件管理', component: () => import('../views/FileUpload')},
            {path: '/role-board', name: '角色管理', component: () => import('../views/RoleBoard')},
            {path: '/menu-board', name: '菜单管理', component: () => import('../views/MenuBoard')},
            {path: '/book-board', name: '图书管理', component: () => import('../views/BookBoard'),},
            {path: '/borrow-board', name: '借还图书', component: () => import('../views/BorrowBoard'),},
        ]
    },
    {path: '/about', name: 'about',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
    },
    {path: '/login', name: 'Login', component: () => import('../views/Login.vue')},
    {path: '/register', name: 'Register', component: () => import('../views/Register.vue')},
    {path: '/404', name: '404', component: () => import('../views/404.vue')},

]


const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export const resetRouter=()=>{
    router.matcher = new VueRouter({
        mode: 'history',
        base: process.env.BASE_URL,
        routes
    })
}

//要在别的地方用，需要export
// !! refreshing page will reset routes
export const setRoutes=()=>{
    let storedMenus = localStorage.getItem('menus');
    if (storedMenus) {
        const menuRoutes={
                path: '/',
                name: 'Manage',
                component: () => import('../views/ManagerView'),
                redirect: "/home",
                children: [
                    {path: '/myInfo', name: 'MyInfo', component: () => import('../views/MyInfo')}
                ]
        }

        //防止重复添加
        const currentRoute=router.getRoutes().map(v=>v.name)
        if (!currentRoute.includes('Manage')) {
            let menus = JSON.parse(storedMenus);
            menus.forEach(item=>{
                if (item.path) {
                    let itemMenu={path: item.path.replace("/",""), name: item.name, component: () => import('../views/'+item.pagePath+'.vue')};
                    menuRoutes.children.push(itemMenu)
                }else if (item.children.length) {
                    item.children.forEach(item=>{
                        if (item.path) {
                            let itemMenu={path: item.path.replace("/",""), name: item.name, component: () => import('../views/'+item.pagePath+'.vue')};
                            menuRoutes.children.push(itemMenu)
                        }
                    })
                }
            })


            router.addRoute(menuRoutes);
        }

    }
}

setRoutes()


// 实现 header的多级path显示
// router.push时会进入 路由守卫，可以在这里拦截登录，而不是还没登陆就进入404
router.beforeEach((to, from, next) => {
    localStorage.setItem('currentPathName', to.name)  //设置当前路由名称，会在header.vue里的watch取到

    //未找到路由
    if (to.matched.length === 0) {
        const storeMenus = localStorage.getItem('menus');
        if (storeMenus) {
            next("/404");
        } else {
            next("/login");
        }
    } else {
        next()
    }

})

export default router
