package org.example.server.config;

import lombok.extern.slf4j.Slf4j;
import org.example.common.Result;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.stream.Collectors;

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
     * バリデーションエラーを処理します。
     * RequestBodyのバリデーションに失敗した場合に発生する例外を捕捉し、エラーメッセージを返します。
     *
     * @param e MethodArgumentNotValidException 発生したバリデーション例外
     * @return Result バリデーションエラーのメッセージを含むレスポンス
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleValidationException(MethodArgumentNotValidException e) {
        // 将所有验证错误信息合并为一个字符串
        String errorMsg = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.error("バリデーションエラー: {}", errorMsg);
        return Result.error(errorMsg);
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
        // 记录完整堆栈信息
        log.error("システム例外が発生しました。詳細：", e);

        // 返回统一的错误信息
        return Result.error("システムエラーが発生しました。管理者に連絡してください。");
    }
}