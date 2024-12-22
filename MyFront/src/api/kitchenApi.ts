import instance from './index';

/**
 * 注文のオーダーをすべてフェッチ
 */
export function fetAllOrders() {
    return instance.get('/api/kitchen');
}