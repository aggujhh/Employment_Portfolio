// Vueアプリケーションを作成するための関数をインポート
import { createApp } from "vue";
import { createPinia } from 'pinia';


// メインコンポーネントをインポート
import App from "./App.vue";
import router from "./router";

// アプリケーションを作成
const app = createApp(App)
const pinia = createPinia();

// ルーターをアプリケーションに登録
app.use(router);
app.use(pinia);
localStorage.clear();

// アプリケーションを指定したDOM要素にマウント
app.mount("#app"); 