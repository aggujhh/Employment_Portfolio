import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import vueSetupExtend from 'vite-plugin-vue-setup-extend';


export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
    vueSetupExtend(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    host: '0.0.0.0',
    port: 5173,
    proxy: {
      '/api': {
        target: "http://192.168.1.10:8080", //跨域地址
        changeOrigin: true, //支持跨域
      }
    }
  },
  assetsInclude: ['**/*.ttf'],
})
