// Vue Routerをインポート
import { createRouter, createWebHistory } from 'vue-router';
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
        component: Home
    },
    {
        path: '/about',
        component: About
    },
    {
        path: '/login',
        component: Login
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
        path: '/:pathMatch(.*)*', // 匹配所有未定义路径
        redirect: '/404',
    },
    ], // ルートの設定を適用
});

// ルーターをエクスポート
export default router;