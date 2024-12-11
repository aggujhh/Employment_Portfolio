// Vueアプリケーションを作成するための関数をインポート
import { createApp } from "vue";
import { createPinia } from 'pinia';


// メインコンポーネントをインポート
import App from "./App.vue";
import router from "./router";
import Header from '@/components/Header.vue';
import Nav from "./components/Nav.vue";
import DeleteDialog from "./components/DeleteDialog.vue";


// アプリケーションを作成
const app = createApp(App)
const pinia = createPinia();

// ルーターをアプリケーションに登録
app.use(router);
app.use(pinia);
// localStorage.clear();

// 全局组件注册（在 mount 之前）
app.component('HeaderView', Header);
app.component('NavView', Nav);
app.component('DeleteDialog', DeleteDialog);

// アプリケーションを指定したDOM要素にマウント
app.mount("#app"); 

