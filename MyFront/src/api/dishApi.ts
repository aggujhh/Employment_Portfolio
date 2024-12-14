import instance from './index';

/**
 * カテゴリー別に料理を取得する
 */
export function fetDishByCategoryId(data: { dishCategoryId: number }) {
    return instance.get('/api/dish', {
        params: data
    });
}

/**
 * 料理を取得追加する
 */
export function addDish(data: {
    dishCategoryId: number,
    name: string,
    state: string,
    price: number,
    image: string,
    description: string
}) {
    return instance.post('/api/dish', data);
}

/**
 * 料理の画像を変換するをする
 */
export function updateDishImage(data: {
    id: number,
    image: string,
    name: string,
    dishCategoryId: number,
}) {
    return instance.patch('/api/dish', data);
}