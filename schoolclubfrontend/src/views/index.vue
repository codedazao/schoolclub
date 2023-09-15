<script setup>
import {logout} from "@/net";
import router from "@/router";

function userLogout(){
  logout((data)=>{
    router.push('/')
    console.log(data)
  })
}
</script>

<template>
<div>
  <el-button type="danger" plain @click.prevent="userLogout()">退出登录</el-button>
</div>
</template>

<style scoped>

</style>