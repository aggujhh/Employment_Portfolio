/**
 * TokenData インターフェース
 * - ローカルストレージに保存するトークンデータの型を定義
 */
interface SessionData {
    currentUserId: string; // 当前用户的 ID
    token: string;         // 当前用户的 Token
    expiry: number;        // Token 的过期时间戳
}

/**
 * トークンとユーザー ID をローカルストレージに保存する
 * @param currentUserId 現在のユーザー ID
 * @param token 保存するトークン
 * @param expiresIn 有効時間（ミリ秒単位）
 */
export function setSession(currentUserId: string, token: string, expiresIn: number): void {
    const now = Date.now(); // 現在のタイムスタンプ（ミリ秒単位）
    const sessionData: SessionData = {
        currentUserId,
        token,
        expiry: now + expiresIn,
    };
    localStorage.setItem("session", JSON.stringify(sessionData)); // ローカルストレージに保存
}

/**
 * ローカルストレージからセッションデータを取得する
 * @returns SessionData または null
 */
export function getSession(): SessionData | null {
    const data = localStorage.getItem("session"); // データを取得
    if (!data) {
        return null; // データがない場合は null を返す
    }

    try {
        const parsedData: SessionData = JSON.parse(data); // データを解析
        const now = Date.now(); // 現在時刻のタイムスタンプを取得

        if (now > parsedData.expiry) {
            localStorage.removeItem("session"); // 有効期限切れの場合はセッションを削除
            return null;
        }

        return parsedData; // 有効なセッションデータを返す
    } catch (error) {
        console.error("セッションデータの解析中にエラーが発生しました:", error);
        localStorage.removeItem("session"); // 不正データの場合は削除
        return null;
    }
}


// クリーンアップ関数をエクスポート
export default { setSession, getSession };