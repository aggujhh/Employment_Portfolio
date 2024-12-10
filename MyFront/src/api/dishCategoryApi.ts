import instance from './index';

/**
 * 料理のカテゴリーをフェッチ
 */
export function fetDishCategory() {
    return instance.get('/api/dish_category');
}
