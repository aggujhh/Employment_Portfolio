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
 * すべて料理の状態を最初の状態をリセットする
 */
export function resetAllOrderAmdDishState() {
    return instance.put('/api/kitchen/reset');
}

export function redoAllOrderAmdDishState(data: { version: string }) {
    return instance.put('/api/kitchen/redo', data);
}

export function undoAllOrderAmdDishState(data: { version: string }) {
    return instance.put('/api/kitchen/undo', data);
}

export function initializationVersion() {
    return instance.put('/api/kitchen/initialization');
}
