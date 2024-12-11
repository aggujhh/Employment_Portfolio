import instance from './index';

/**
 * 料理のカテゴリーをフェッチ
 */
export function fetDishCategory() {
    return instance.get('/api/dish_category');
}

/**
 * 料理のカテゴリーを追加
 */
export function addDishCategory(data: { name: string }) {
    return instance.post('/api/dish_category', data);
}

/**
 * 料理のカテゴリーを削除
 */
export function deleteDishCategory(data: { id: number }) {
    return instance.delete('/api/dish_category', {
        data: data
    });
}

/**ut
 * 料理のカテゴリーを修正t
 */
export function updateDishCategory(data: { id: number, name: string }) {
    return instance.put('/api/dish_category', data);
}