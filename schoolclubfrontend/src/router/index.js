import {createRouter, createWebHistory} from 'vue-router'
import {unauthorized} from "@/net";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name:"welcome",
            component: ()=> import("@/views/Welcome.vue"),
            children:[
                {
                    path:"",
                    name:"welcome-login",
                    component: ()=> import("@/views/welcome/LoginPage.vue")
                }
            ]
        },{
            path:'/index',
            name:"index",
            component:() => import('@/views/index.vue')
        }
    ]
})

router.beforeEach((to,form,next)=>{
    const isUnauthorized = unauthorized()
    console.log(to.name)
    if(to.name.startsWith("welcome-")&& !isUnauthorized){
        next("/index")
    }else if(to.fullPath.startsWith("/index") && isUnauthorized){
        next("/")
    }else {
        next()
    }
})

export default router