import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name:"welcome",
            component: ()=> import("@/views/Welcome.vue"),
            children:[

            ]
        }
    ]
})

export default router