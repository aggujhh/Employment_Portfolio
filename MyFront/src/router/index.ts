// Vue Routerをインポート
import { createRouter, createWebHistory } from 'vue-router';
import session from "@/utils/session"
// コンポーネントをインポートNews
import Home from '@/views/Home.vue';
import News from '@/views/News.vue';
import About from '@/views/About.vue';
import NotFound from '@/views/404.vue';
import Login from '@/views/Login.vue';


// ルーターインスタンスを作成
const router = createRouter({
    history: createWebHistory(), // HTML5のヒストリーモードを使用
    routes: [{
        path: '/home',
        component: Home,
    },
    {
        path: '/about',
        component: About
    },
    {
        path: '/login',
        component: Login,
        meta: { requiresAuth: false }
    },
    {
        path: '/news',
        component: News
    },
    {
        path: '/404',
        component: NotFound
    },
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/:pathMatch(.*)*', 
        redirect: '/404',
    },
    ], // ルートの設定を適用
});


// ナビゲーションガード（拦截器）を追加
router.beforeEach((to, from, next) => {
    // 例: ユーザーの認証状態を確認
    const isAuthenticated = session.getSession(); // トークンがある場合はログイン状態とみなす
    const requiresAuth = to.meta.requiresAuth !== false; // デフォルトでは認証が必要


    if (requiresAuth && !isAuthenticated) {
        // 認証が必要なルートにアクセスする場合、ログインページにリダイレクト
        next('/login');
    } else {
        // 通常のナビゲーション
        next();
    }
});

// ルーターをエクスポート
export default router;