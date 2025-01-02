export default class SseService {
    private eventSource: EventSource | null = null; // EventSource インスタンス（初期値は null）
    private reconnectAttempts: number = 0; // 再接続の試行回数
    private readonly maxReconnectAttempts: number = 3; // 最大再接続試行回数

    /**
     * コンストラクタ
     * @param url SSE サーバーの URL
     * @param onMessage メッセージを受信したときに実行されるコールバック関数
     */
    constructor(private url: string, private onMessage: (message: string) => void) { }

    /**
     * SSE 接続を開始する
     */
    connect() {
        this.eventSource = new EventSource(this.url); // EventSource オブジェクトを初期化

        // メッセージを受信した場合の処理
        this.eventSource.onmessage = (event: MessageEvent) => {
            this.reconnectAttempts = 0; // メッセージを受信できれば試行回数をリセット
            this.onMessage(event.data); // コールバック関数を呼び出す
        };

        // 接続エラーが発生した場合の処理
        this.eventSource.onerror = () => {
            console.error("SSE 接続エラー"); // エラーメッセージを出力
            this.reconnectAttempts++; // 再接続の試行回数をインクリメント

            if (this.reconnectAttempts <= this.maxReconnectAttempts) {
                console.log(`再接続を試みています (${this.reconnectAttempts}/${this.maxReconnectAttempts})`);
                this.close(); // 現在の接続を閉じる
                setTimeout(() => this.connect(), 3000); // 1秒後に再接続を試みる
            } else {
                console.error("最大再接続試行回数に到達しました。接続を閉じます。");
                this.close(); // 最大試行回数を超えたら接続を閉じる
            }
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
