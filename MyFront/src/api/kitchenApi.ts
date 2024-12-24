import instance from './index';

/**
 * 注文のオーダーをすべてフェッチ
 */
export function fetAllOrders() {
    return instance.get('/api/kitchen');
}

/**
 * 調理済み料理の状態を変更する
 */
export function changeOrderDishState(data: { orderId: string, dishId: number }) {
    return instance.patch('/api/kitchen', data);
}

/**
 * このオーダー内のすべての料理を調理済み状態に変更します。
 */
export function changeOrderState(data: { id: string }) {
    return instance.put('/api/kitchen', data);
}

/**
 * すべて料理の状態を最初の状態をリセットする
 */
export function resetAllOrderAmdDishState() {
    return instance.put('/api/kitchen/reset');
}

/**
 * すべての料理とオーダーの状態をやり直し（redo）する
 * @param data バージョン情報を含むデータ）
 */
export function redoAllOrderAmdDishState(data: { version: string }) {
    return instance.put('/api/kitchen/redo', data);
}

/**
 * すべての料理とオーダーの状態を元に戻す（undo）
 * @param data バージョン情報を含むデータ）
 */
export function undoAllOrderAmdDishState(data: { version: string }) {
    return instance.put('/api/kitchen/undo', data);
}

/**
 * バージョン情報を初期化する
 */
export function initializationVersion() {
    return instance.put('/api/kitchen/initialization');
}


/**
 * すべてオーダー履歴をフェッチ
 */
export function fetchAllOrderHistory() {
    return instance.get('/api/kitchen/history');
}