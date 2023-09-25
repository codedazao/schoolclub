<script setup>
import '@/assets/main.css'
</script>

<template>
  <div style="width:100vw; height: 100vh; display: flex;overflow: hidden">
    <div style="flex:1; background-color: black">
      <el-image style="width: 100%;
                height: 100%;"
                fit="cover"
                src="https://itbaima.net/static/image/background/light/1.webp">
      </el-image>
    </div>
    <div class="right-card">
      <router-view v-slot="{Component}" >
        <transition name="el-fade-in-linear" mode="out-in">
          <component :is="Component"></component>
        </transition>
      </router-view>
    </div>
  </div>
</template>

<style scoped>
.right-card{
  width: 400px;
  z-index: 1;
  background-color: white;
}
</style>