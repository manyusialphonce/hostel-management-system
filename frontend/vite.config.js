import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    // Only used when running "npm run dev" for hot-reload during
    // development. Forwards /api/* calls to the Spring Boot backend
    // running on :8080, so the frontend code can always call a plain
    // relative "/api" path - no separate URL needed for dev vs the
    // final build (which is served from the same origin as the API).
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
