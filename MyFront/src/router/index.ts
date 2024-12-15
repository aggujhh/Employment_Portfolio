// Vue Routerをインポート
import { createRouter, createWebHistory } from 'vue-router';
import session from "@/utils/session"
// コンポーネントをインポートNews
import Home from '@/views/Home.vue';
import NotFound from '@/views/404.vue';
import Login from '@/views/Login.vue';
import FrontScreen from '@/views/FrontScreen.vue';
import KitchenScreen from '@/views/KitchenScreen.vue';
import Menu from '@/views/Menu.vue';
import Reservation from '@/views/reservation.vue';
import Shift from '@/views/Shift.vue';
import DataAnalysis from '@/views/DataAnalysis.vue';
import Staff from '@/views/Staff.vue';
import Permission from '@/views/Permission.vue';
import Dishes from '@/views/Dishes.vue';
import Order from '@/phoneViews/Index.vue';
import OrderDishes from '@/phoneViews/Dishes.vue';

// ルーターインスタンスを作成
const router = createRouter({
    history: createWebHistory(), // HTML5のヒストリーモードを使用
    routes: [
        { path: '/login', component: Login, meta: { requiresAuth: false } },
        { path: '/home', component: Home, },
        { path: '/front_screen', component: FrontScreen, },
        { path: '/kitchen_screen', component: KitchenScreen, },
        {
            path: '/menu', component: Menu, redirect: '/menu/1',
            children: [{ path: ':id', component: Dishes }]// 动态子路由],
        },
        { path: '/reservation', component: Reservation, },
        { path: '/shift', component: Shift, },
        { path: '/data_analysis', component: DataAnalysis, },
        { path: '/staff', component: Staff, },
        { path: '/permission', component: Permission, },
        {
            path: '/order', component: Order, meta: { requiresAuth: false }, redirect: '/order/1',
            children: [{
                path: ':id',
                component: OrderDishes,
                meta: { requiresAuth: false }
            }]
        },// 动态子路由]
        { path: '/404', component: NotFound, meta: { requiresAuth: false } },
        { path: '/', redirect: '/login' },
        { path: '/:pathMatch(.*)*', redirect: '/404' },
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