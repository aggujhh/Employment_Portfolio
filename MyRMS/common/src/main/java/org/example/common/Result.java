package org.example.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;//レスポンスコード、1 は成功を表し、0 は失敗を表します
    private String msg;  //レスポンスメッセージ、説明の文字列です
    private Object data; //返されるデータ

    //追加、削除、更新 成功レスポンス
    public static Result success() {
        return new Result(1, "success", null);
    }

    //クエリ 成功レスポンス
    public static Result success(Object data) {
        return new Result(1, "success", data);
    }

    //失敗レスポンス
    public static Result error(String msg) {
        return new Result(0, msg, null);
    }
}
