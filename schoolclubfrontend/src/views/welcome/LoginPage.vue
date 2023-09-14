<script setup>
import {reactive, ref, onMounted} from "vue";
import {Lock, User} from "@element-plus/icons-vue"
import {login} from "@/net";


const formRef = ref();


const form = reactive({
  username: "",
  password: "",
  remember: ""
})

const rule = {
  username: {
    required: true,
    message: "请输入用户名"
  },
  password: {
    required: true,
    message: "请输入密码"
  }
}

function userLogin() {
  formRef.value.validate((valid)=>{
    if (valid){
      login(form.username,form.password,form.remember,()=>{
        console.log("登录操作")
      })
    }
  })
}
</script>

<template>
  <div style="text-align: center; margin: 0 20px">
    <div style="margin-top: 150px">
      <h2>
        登录
      </h2>
      <div style="font-size: 14px; color: grey;margin-top: 9px;">
        在进入系统之前，请先登录
      </div>
    </div>
    <div style="margin-top: 50px; ">
      <el-form :model="form" :rules="rule" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username" maxlength="10" placeholder="用户名" type="text">
            <template #prefix>
              <el-icon>
                <User></User>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" maxlength="20" placeholder="密码" type="password">
            <template #prefix>
              <el-icon>
                <Lock></Lock>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-row>
          <el-col :span="12" style="text-align: left">
            <el-form-item prop="remember">
              <el-checkbox v-model="form.remember">记住我</el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="12" style="text-align: right">
            <el-link>忘记密码</el-link>
          </el-col>
        </el-row>
      </el-form>
      <div style="margin-top: 20px; padding: 0">
        <el-button type="success" plain style="width: 270px;" @click="userLogin()">立即登录</el-button>
        <el-divider>没有账号？</el-divider>
        <el-button type="warning" plain style="width: 270px;">立即注册</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>