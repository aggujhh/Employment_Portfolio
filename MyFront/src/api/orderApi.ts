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