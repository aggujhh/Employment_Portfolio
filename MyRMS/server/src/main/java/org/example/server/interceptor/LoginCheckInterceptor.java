package org.example.server.interceptor;

import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.common.Result;
import org.example.common.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * ログインチェック用のインターセプタークラス
 * リクエストがコントローラーに渡る前に、トークンの検証を行い、ログイン状況をチェックします。
 */
@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        log.info("取得したリクエストURL:{}", url);

        // 登录请求放行
        if (url.contains("login")) {
            log.info("ログイン操作を許可する");
            return true;
        }

        // 获取 Authorization 头
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.info("トークンが空です");
            //エラーレスポンスを作成
            Result error = Result.error("NOT_LOGIN");
            // JSON形式に変換
            String notLogin = JSON.toJSONString(error);
            // レスポンスに書き込み
            response.getWriter().write(notLogin);
            return false;
        }

        // 提取 Token
        String jwt = authorizationHeader.substring(7);

        // 验证 Token
        try {
            Jwt.parseJWT(jwt); // トークンを解析
        } catch (ExpiredJwtException e) {
            log.info("トークンが期限切れです");
            //エラーレスポンスを作成
            Result error = Result.error("NOT_LOGIN");
            // JSON形式に変換
            String notLogin = JSON.toJSONString(error);
            // レスポンスに書き込み
            response.getWriter().write(notLogin);
            return false;
        } catch (Exception e) {
            log.error("トークン解析中にエラーが発生しました: {}", e.getMessage(), e);
            //エラーレスポンスを作成
            Result error = Result.error("NOT_LOGIN");
            // JSON形式に変換
            String notLogin = JSON.toJSONString(error);
            // レスポンスに書き込み
            response.getWriter().write(notLogin);
            return false;
        }
        return true;
    }
}