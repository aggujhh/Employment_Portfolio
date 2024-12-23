// src/services/sseService.ts

// SseService クラスをエクスポート
export default class SseService {
    private eventSource: EventSource | null = null; // EventSource インスタンス（初期値は null）

    /**
     * コンストラクタ
     * @param url SSE サーバーの URL
     * @param onMessage メッセージを受信したときに実行されるコールバック関数
     */
    constructor(private url: string, private onMessage: () => void) {}

    /**
     * SSE 接続を開始する
     */
    connect() {
        this.eventSource = new EventSource(this.url); // EventSource オブジェクトを初期化

        // メッセージを受信した場合の処理
        this.eventSource.onmessage = () => {
            this.onMessage(); // コールバック関数を呼び出す
        };

        // 接続エラーが発生した場合の処理
        this.eventSource.onerror = () => {
            console.error("SSE 接続エラー"); // エラーメッセージを出力
            this.close(); // 接続を閉じる
        };
    }

    /**
     * SSE 接続を閉じる
     */
    close() {
        if (this.eventSource) {
            this.eventSource.close(); // 接続を閉じる
            this.eventSource = null; // インスタンスをリセット
        }
    }
}
