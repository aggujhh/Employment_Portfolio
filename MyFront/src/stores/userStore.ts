// src/stores/user.ts
import { defineStore } from 'pinia';
import session from "@/utils/session.ts";

export const useUserStore = defineStore('user', {
    state: () => ({
        users: {} as Record<
            string, // ユーザー ID
            { id: string; name: string; type: string; job: string } // ユーザーデータ
        >,
    }),
    getters: {
        /**
         * 現在のユーザー情報を取得する
         * @param state ストアの状態
         * @returns 現在のユーザー情報または null
         */
        currentUser(state) {
            const currentUserId = session.getSession()?.currentUserId; // セッションから現在のユーザー ID を取得
            if (currentUserId && currentUserId in state.users) {
                return state.users[currentUserId]; // 対応するユーザーデータを返す
            }
            return null; // 見つからない場合は null を返す
        },
    },
    actions: {
        /**
         * ユーザーデータを追加または更新する
         * @param user ユーザー情報オブジェクト
         */
        upsertUser(user: { id: string; name: string; type: string; job: string }) {
            this.users[user.id] = user; // オブジェクトのキーを使用してユーザーを追加または更新
            console.log("存储成功: ",user);
        },

        /**
         * ユーザーデータを削除する
         * @param userId 削除対象のユーザー ID
         */
        removeUser(userId: string) {
            if (userId in this.users) {
                delete this.users[userId]; // 指定されたユーザーを削除
            } else {
                console.warn(`ユーザー ID ${userId} は存在しないため、削除できません`);
            }
        },
    },
});
