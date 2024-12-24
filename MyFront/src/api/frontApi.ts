import instance from './index';

/**
 * 調理済オーダーをすべてフェッチ
 */
export function fetchAllCompletedOrders() {
    return instance.get('/api/front');
}