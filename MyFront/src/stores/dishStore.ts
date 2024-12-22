import { defineStore } from 'pinia';

export const useDishStore = defineStore('dishCount', {
    state: () => ({
        // テーブルごとに料理情報（料理ID、数量、タイトル、価格）を管理するオブジェクト
        dishes: {} as Record<string, Record<number, { count: number; title: string; price: number }>>,
    }),
    actions: {

        /**
         * 特定のテーブルと料理の情報を設定します。
         * @param desk_id テーブルID（文字列）
         * @param dish_id 料理ID（数値）
         * @param title 料理のタイトル（文字列）
         * @param price 料理の価格（数値）
         */
        setDish(desk_id: string, dish_id: number, title: string, price: number) {
            // テーブルの初期化
            if (!this.dishes[desk_id]) {
                this.dishes[desk_id] = {};
            }
            // 料理の初期化または更新
            this.dishes[desk_id][dish_id] = {
                count: this.dishes[desk_id][dish_id]?.count || 0, // デフォルトは0
                title,
                price,
            };
        },

        /**
         * 特定のテーブルと料理の数量を取得します。
         * @param desk_id テーブルID（文字列）
         * @param dish_id 料理ID（数値）
         * @returns 指定した料理の数量（存在しない場合は0）
         */
        getDishCount(desk_id: string, dish_id: number): number {
            return this.dishes[desk_id]?.[dish_id]?.count || 0;
        },

        /**
         * 特定のテーブルと料理の数量を1つ増やします。
         * @param desk_id テーブルID（文字列）
         * @param dish_id 料理ID（数値）
         */
        plusDishCount(desk_id: string, dish_id: number) {
            if (!this.dishes[desk_id]?.[dish_id]) {
                console.warn(`Dish with ID ${dish_id} is not set for desk ${desk_id}.`);
                return;
            }
            this.dishes[desk_id][dish_id].count++;
        },

        /**
         * 特定のテーブルと料理の数量を1つ減らします。
         * @param desk_id テーブルID（文字列）
         * @param dish_id 料理ID（数値）
         */
        minusDishCount(desk_id: string, dish_id: number) {
            if (this.dishes[desk_id]?.[dish_id]?.count > 0) {
                this.dishes[desk_id][dish_id].count--;
            }
        },

        /**
         * 特定のテーブルにおけるすべての料理の合計数量を計算します。
         * @param desk_id テーブルID（文字列）
         * @returns 料理の数量の合計値
         */
        sumDishCount(desk_id: string): number {
            const deskDishes = this.dishes[desk_id];
            if (!deskDishes) {
                return 0; // テーブルが存在しない場合は0を返します
            }
            // すべての料理の数量を合計
            return Object.values(deskDishes).reduce((sum, dish) => sum + dish.count, 0);
        },

        sumDishPrice(desk_id: string): number {
            const deskDishes = this.dishes[desk_id];
            if (!deskDishes) {
                return 0; // テーブルが存在しない場合は0を返します
            }
            // すべての料理の数量を合計
            return Object.values(deskDishes).reduce((sum, dish) => sum + (dish.count * dish.price), 0);
        },

        getInfo(desk_id: string): Array<{ id: string; name: string; count: number }> {
            const deskDishes = this.dishes[desk_id];
            if (!deskDishes) return []; // 如果没有菜品，返回空数组
            return Object.entries(deskDishes) // 获取键值对数组 [id, dish]
                .filter(([_, dish]) => dish.count !== 0) // 过滤 count 为 0 的菜品
                .map(([id, dish]) => ({ id, name: dish.title, count: dish.count })); // 返回新数组，包含 id
        },

        resetDishes(desk_id: string) {
            // 检查 desk_id 是否存在
            if (!this.dishes[desk_id]) {
                return;
            }
            // 重置该 desk_id 下所有 dish 的 count 为 0
            Object.values(this.dishes[desk_id]).forEach(dish => {
                dish.count = 0;
            });
        }
    },
});
