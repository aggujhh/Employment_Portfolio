package org.example.server.interceptor;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.common.Result;
import org.example.common.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * ログインチェック用のインターセプタークラス
 * リクエストがコントローラーに渡る前に、トークンの検証を行い、ログイン状況をチェックします。
 */
@Slf4j // ログ出力用の注釈
@Component // Springコンテナにこのクラスを登録
public class LoginCheckInterceptor implements HandlerInterceptor {

    /**
     * コントローラーにリクエストが渡る前に実行されるメソッド
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // リクエストURLを取得
        String url = request.getRequestURL().toString();
        log.info("取得したリクエストURL:{}", url);

        // ログインリクエストの場合は処理を許可
        if (url.contains("login")) {
            log.info("ログイン操作を許可する");
            return true;
        }

        // リクエストヘッダーからトークンを取得
        String jwt = request.getHeader("token");
        log.info("jwtの名前:{}", jwt);

        // トークンが空の場合
        if (!StringUtils.hasLength(jwt)) {
            log.info("リクエストヘッダーのトークンが空の場合、'未ログイン' を返します");
            // エラーレスポンスを作成
            Result error = Result.error("NOT_LOGIN");
            // JSON形式に変換
            String notLogin = JSON.toJSONString(error);
            // レスポンスに書き込み
            response.getWriter().write(notLogin);
            return false;
        }

        // トークンの検証を試みる
        try {
            Jwt.parseJWT(jwt); // トークンを解析
        } catch (Exception e) {
            // ログレベルをERRORに設定して例外の情報を出力
            log.error("トークン解析中にエラーが発生しました: {}", e.getMessage(), e);
            log.info("解析に失敗した場合、'未ログイン' を返します");
            // エラーレスポンスを作成
            Result error = Result.error("NOT_LOGIN");
            // JSON形式に変換
            String notLogin = JSON.toJSONString(error);
            // レスポンスに書き込み
            response.getWriter().write(notLogin);
            return false;
        }

        // トークンが合法な場合
        log.info("トークンが合法");
        return true;
    }
}
