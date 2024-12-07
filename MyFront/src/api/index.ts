// api/axiosInstance.js
import axios from 'axios';
// Vue 3 のルーターAPIをインポート
import { useRouter } from 'vue-router';


const instance = axios.create({
    baseURL: 'https://api.example.com', // 基本URL
    timeout: 10000, // タイムアウト時間（ミリ秒）
    headers: { 'Content-Type': 'application/json' }, // デフォルトのヘッダー
});

// リクエストインターセプター
instance.interceptors.request.use(
    config => {
        // リクエストを送信する前に行う処理
        const token = localStorage.getItem('token'); // ローカルストレージからトークンを取得
        if (token) {
            config.headers.Authorization = `Bearer ${token}`; // ヘッダーにトークンを追加
        }
        return config;
    },
    error => {
        // リクエストエラー時の処理
        return Promise.reject(error);
    }
);

// レスポンスインターセプター
instance.interceptors.response.use(
    response => {
        // レスポンスデータの処理
        if (
            response &&
            response.data.code === 0 &&
            response.data.msg === 'NOT_LOGIN'
        ) {           
            const router = useRouter();// ルーターインスタンスを作成
            const currentRoute = router.currentRoute.value; // Vue 3ではrouter.currentRouteはrefであるためvalueが必要
            if (currentRoute.path !== '/login') {
                // 未ログインの場合、ログインページにリダイレクト
                router.push('/login');
                alert('ログインしてください。'); // ユーザーにログインを促す
            }
        }
        return response;
    },
    error => {
        // レスポンスエラー時の処理
        if (error.response) {
            // リクエストは送信されたが、ステータスコードが2xx範囲外の場合
            console.error('Error:', error.response.data);
            console.error('Status:', error.response.status);
        } else {
            // リクエスト設定時またはリクエストのキャンセル時のエラー
            console.error('Error:', error.message);
        }
        return Promise.reject(error);
    }
);

export default instance;
