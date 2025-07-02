import { createRouter, createWebHashHistory } from 'vue-router';
import { useUserStore } from '@/store/user'; // ✅ 라우터 가드에서 사용할 userStore import

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/',
      component: () => import('../components/pages/Index.vue'),
    },
    // ✅ [수정] 중복된 /login 경로를 하나로 정리했습니다.
    {
      path: '/login',
      component: () => import("../components/ui/LoginGrid.vue"),
    },
    // ✅ [수정] 중복된 /register 경로를 하나로 정리했습니다.
    {
      path: '/register',
      component: () => import("../components/ui/RegisterGrid.vue"),
    },
    // ✅ [수정] 중복된 /viewMyPages 경로를 하나로 정리했습니다.
    {
      path: '/viewMyPages',
      component: () => import("../components/ui/ViewMyPagesGrid.vue"),
      meta: { requiresAuth: true } // ✅ 이 페이지는 로그인이 필요하다고 명시
    },
    {
      path: '/writer_register',
      component: () => import("../components/ui/WriterRegisterGrid.vue"),
      meta: { requiresAuth: true } // ✅ 이 페이지는 로그인이 필요하다고 명시
    },
    {
      path: '/book_mangement',
      component: () => import("../components/ui/BookManagement.vue"),
      meta: { requiresAuth: true } // ✅ 이 페이지는 로그인이 필요하다고 명시
    },
    {
      path: '/write',
      component: () => import("../components/ui/WriteBookPage.vue")
    },
    {
      path: '/admin/writer_management',
      component: () => import("../components/ui/WriterManagementPageGrid.vue"),
      // ✅ [관리자 전용 가드] 이 페이지는 관리자만 접근 가능
      beforeEnter: (to, from, next) => {
        const userStore = useUserStore();
        if (userStore.isLoggedIn && userStore.currentUser?.isAdmin) {
          next(); // 관리자면 통과
        } else {
          alert("접근 권한이 없습니다.");
          next('/'); // 관리자가 아니면 메인 페이지로 리디렉션
        }
      }
    },
    {
      // ✅ [수정] 이 경로는 어떤 가드도 없어야 합니다. (중복 제거)
      path: '/create-super-user-account',
      component: () => import("../components/ui/AdminSignUpPageGrid.vue"),
    },
    // --- 아래는 병합 과정에서 추가된 다른 경로들입니다. ---
    {
      path: '/writes_register',
      component: () => import('../components/ui/WriteRegister.vue'),
    },
    {
      path: '/manuscript_create',
      component: () => import("../components/ui/Manuscript_create.vue"),
    },
    {
      path: '/publish-dashboard',
      component: () => import('../components/PublishDashboard.vue'),
    },
    {
      path: '/writerLists',
      component: () => import('../components/WriterList.vue'),
    },
    {
      path: '/writerDetails',
      component: () => import('../components/WriterDetails.vue'),
    },
    // ... 다른 라우트 ...
  ],
});


/**
 * ✅ [전역 가드] 모든 페이지 이동이 일어나기 전에 실행됩니다.
 */
router.beforeEach((to, from, next) => {
  const userStore = useUserStore();
  
  // 이동하려는 페이지가 로그인을 필요로 하고(meta.requiresAuth), 현재 로그인 상태가 아니라면
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    alert("로그인이 필요한 서비스입니다.");
    next('/login'); // 로그인 페이지로 강제 이동
  } else {
    next(); // 그 외의 경우는 모두 통과
  }
});

export default router;