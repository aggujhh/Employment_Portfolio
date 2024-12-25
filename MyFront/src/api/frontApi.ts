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