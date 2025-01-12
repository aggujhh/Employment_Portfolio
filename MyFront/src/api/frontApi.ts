import instance from './index';

/**
 * 調理済オーダーをすべてフェッチ
 */
export function fetchAllCompletedOrders() {
    return instance.get('/api/front');
}

/**
 * 料理状態を提供済みに変更
 */
export function changeOrderDishState(data: { deskId: string, orderId: string, dishId: number }) {
    return instance.patch('/api/front', data);
}

/**
 * 会計中のオーダーをすべてフェッチ
 */
export function fetchAllAccountingOrders(data: { deskId: string }) {
    return instance.get('/api/front/accounting', {
        params: data
    });
}

/**
 * 会計確認完了
 */
export function accountingConfirmationCompleted(data: any) {
    return instance.put('/api/front', data);
}

/**
 * 営業情報所得する
 */
export function fetchOperations() {
    return instance.get('/api/front/operations');
}

/**
 * 呼び出し確認完了
 */
export function checkedCall(data: { id: string }) {
    return instance.patch('/api/front/checkedCall', data);
}



