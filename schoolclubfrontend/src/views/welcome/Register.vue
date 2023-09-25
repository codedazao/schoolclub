<script setup>
import {reactive, ref, onMounted} from "vue";
import {Lock, Message, User} from "@element-plus/icons-vue";
import router from "@/router";
  const form = reactive({
    username:"",
    password:"",
    password_repeat:"",
    email:"",
    code:""
  })
const rule = {}
</script>

<template>
<div style="margin: 0 20px; text-align: center">
  <div style="margin-top: 100px">
    <div style="font-size: 25px; font-weight: bold">注册新用户</div>
    <div style="font-size: 14px;color: grey">欢迎注册我们的学习平台，请在下方填写相关信息</div>
  </div>
  <div style="margin-top: 50px">
    <el-form :model="form">
      <el-form-item>
        <el-input v-model="form.username" maxlength="10" type="text" placeholder="请输入用户名/邮箱账号">
          <template #prefix>
            <el-icon><User/></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item >
        <el-input v-model="form.password" maxlength="20" type="password" placeholder="密码">
            <template #prefix>
              <el-icon>
                <Lock></Lock>
              </el-icon>
            </template>
        </el-input>
      </el-form-item>
      <el-form-item>
      <el-input v-model="form.password_repeat" maxlength="20" type="password" placeholder="重复密码">
        <template #prefix>
          <el-icon>
            <Lock></Lock>
          </el-icon>
        </template>
      </el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="form.email" maxlength="10" type="email" placeholder="电子邮件地址">
          <template #prefix>
            <el-icon>
              <Message></Message>
            </el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item >
        <el-row  style="width: 100%"  gutter="10  ">
          <el-col :span="17">
            <el-input v-model="form.code">请输入验证码</el-input>
          </el-col>
          <el-col :span="5">
            <el-button type="success">获取验证码</el-button>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>
    <el-button type="warning" style="margin-top: 20px;width: 270px">立即注册</el-button>
    <el-divider></el-divider>
    <el-link @click="router.push('/')">已有账户？立即登录</el-link>
  </div>
</div>
</template>

<style scoped>

</style>