package org.example.server.config;

import lombok.extern.slf4j.Slf4j;
import org.example.common.Result;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 外部キー制約違反の例外を処理します。
     * 例えば、削除しようとしたカテゴリが他のテーブル（例: dish）で参照されている場合にこの例外が発生します。
     *
     * @param e SQLIntegrityConstraintViolationException 外部キー制約違反の例外
     * @return Result エラーメッセージを含むレスポンス
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        log.error("外部キー制約違反：{}", e.getMessage());
        // 外部キー制約違反が発生した場合のエラーメッセージを返します。
        return Result.error("このカテゴリは既存の料理と関連付けられているため、削除できません。関連付けられている料理を先に削除してください。");
    }

    /**
     * 一般的なシステム例外を処理します。
     * 想定外の例外が発生した場合にこのハンドラが呼び出されます。
     *
     * @param e Exception 発生した例外
     * @return Result システムエラーを示すレスポンス
     */
    @ExceptionHandler(Exception.class)
    public Result handleGeneralException(Exception e) {
        log.error("システム例外：{}", e.getMessage());
        // システムエラーが発生した場合のエラーメッセージを返します。
        return Result.error("システムエラーが発生しました。管理者に連絡してください。");
    }
}