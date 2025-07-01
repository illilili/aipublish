import { createRouter, createWebHashHistory } from 'vue-router';

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/',
      component: () => import('../components/pages/Index.vue'),
    },
    {
      path: '/writes',
      component: () => import('../components/ui/WriteGrid.vue'),
    },
    {
      path: '/users',
      component: () => import('../components/ui/UserGrid.vue'),
    },
    {
      path: '/points',
      component: () => import('../components/ui/PointGrid.vue'),
    },
    {
      path: '/books',
      component: () => import('../components/ui/BookGrid.vue'),
    },
    {
      path: '/aiBookProcessors',
      component: () => import('../components/ui/AiBookProcessorGrid.vue'),
    },
    {
      path: '/login',
      component: () => import("../components/ui/LoginGrid.vue"),
    },
    {
      path: '/register',
      component: () => import("../components/ui/RegisterGrid.vue"),
    },
    {
      path: '/viewMyPages',
      component: () => import("../components/ui/ViewMyPagesGrid.vue"),
    }

  ],
})

export default router;
