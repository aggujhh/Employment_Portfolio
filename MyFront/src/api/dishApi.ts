import instance from './index';

/**
 * カテゴリー別に料理を取得する
 */
export function fetDishByCategoryId(data: { dishCategoryId: number }) {
    return instance.get('/api/dish', {
        params: data
    });
}