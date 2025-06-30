// vite.config.js
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import DefineOptions from 'unplugin-vue-define-options/vite'
import { fileURLToPath } from 'url'
import { defineConfig } from 'vite'
import Pages from 'vite-plugin-pages'
import Layouts from 'vite-plugin-vue-layouts'
import vuetify from 'vite-plugin-vuetify'
import ViteYaml from '@modyfi/vite-plugin-yaml'

export default defineConfig({
  plugins: [
    vue(),
    vueJsx(),
    vuetify({
      styles: {
        configFile: 'src/styles/variables/_vuetify.scss',
      },
    }),
    Pages({}),
    Layouts(),
    Components({
      dirs: ['src/@core/components', 'src/components'],
      extensions: ['vue'],
      dts: true,
    }),
    AutoImport({
      eslintrc: {
        enabled: true,
        filepath: './.eslintrc-auto-import.json',
      },
      imports: ['vue', 'vue-router', '@vueuse/core', 'vue-i18n', 'pinia'],
      vueTemplate: true,
    }),
    DefineOptions(),
    ViteYaml(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
      '@core': fileURLToPath(new URL('./src/@core', import.meta.url)),
      '@layouts': fileURLToPath(new URL('./src/@layouts', import.meta.url)),
      '@configured-variables': fileURLToPath(new URL('./src/styles/variables/_template.scss', import.meta.url)),
      '@axios': fileURLToPath(new URL('./src/plugins/axios', import.meta.url)),
      'apexcharts': fileURLToPath(new URL('node_modules/apexcharts-clevision', import.meta.url)),
    },
  },
  build: {
    chunkSizeWarningLimit: 5000,
  },
  optimizeDeps: {
    exclude: ['vuetify'],
    entries: [
      './src/**/*.vue',
    ],
  },
  server: {
    host: '0.0.0.0',
    port: 8080,
    // --- 이 부분이 핵심적으로 추가되어야 합니다 ---
    proxy: {
      '/writes': { // 프론트엔드에서 /writes 로 시작하는 모든 요청을 프록시합니다.
        // 백엔드 서버의 실제 주소 (이 URL은 백엔드 Gitpod 주소와 정확히 일치해야 합니다)
        target: 'https://8082-meritending-aipublish1-t2dc620809q.ws-us120.gitpod.io',
        changeOrigin: true, // 대상 서버의 출처를 변경하여 CORS 문제 회피
        secure: false, // Gitpod은 HTTPS를 사용하므로 true로 설정해도 되지만, 개발 편의상 false로 두는 경우도 있습니다.
        // rewrite: (path) => path.replace(/^\/writes/, '/writes'), // 경로를 다시 작성할 필요가 없다면 생략 가능
      },
    },
    // ------------------------------------------------
  },
  css: {
    preprocessorOptions: {
      scss: {
        api: 'modern'
      }
    }
  }
})
