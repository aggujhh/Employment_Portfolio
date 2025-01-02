import instance from './index';

/**
 * 料理のカテゴリーをフェッチ
 */
export function fetDishCategory() {
    return instance.get('/api/order');
}

/**
 * カテゴリー別に料理を取得する
 */
export function fetchDishByCategoryId(data: { dishCategoryId: number }) {
    return instance.get(`/api/order/${data.dishCategoryId}`);
}

/**
 * テーブルをすべてフェッチ
 */
export function fetchAllTables() {
    return instance.get('/api/order/desk/all');
}

/**
 * テーブルによりデータをフェッチ
 */
export function fetchDateByTableId(data: { id: string }) {
    return instance.get('/api/order/desk', {
        params: data
    });
}

/**
 * 人数を設定する
 */
export function setCustomerCount(data: { id: string, guestCount: number }) {
    return instance.patch('/api/order/desk', data);
}


/**
 * 新しいオーダーを追加
 */
export function addOrder(data: {
    deskId: string,
    dishes: {
        dishId: number,
        count: string
    }
}) {
    return instance.put('/api/order', data);
}

/**
 * オーダーの総値段を取得する
 */
export function getOrderTotalPrice(data: { deskId: string }) {
    return instance.get('/api/order/price', {
        params: data
    });
}

/**
 * すべて注文完了オーダーを取得する
 */
export function fetchAllCompletedOrders(data: { deskId: string }) {
    return instance.get('/api/order/completed', {
        params: data
    });
}


/**
 * 会計完了
 */
export function finishOrder(data: {
    deskId: string,
    payMethod: string,
    amount: number
 }) {
    return instance.patch('/api/order/finish', data);
}
