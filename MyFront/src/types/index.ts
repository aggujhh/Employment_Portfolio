// インターフェースを定義
// ユーザーオブジェクトの具体的な構造を制約するために使用
export interface UserInter {
    id: string,
    password: string,
    type?: number//オプション
}

// 型エイリアスを定義
// UserInterの配列型を定義するために使用
export type UserList = Array<UserInter>;