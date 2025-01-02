package org.example.server.config;

import org.example.server.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration // 設定クラスとしてSpringコンテナに登録
@EnableTransactionManagement//トランザクション管理を有効化するためのアノテーションです。
public class WebConfig implements WebMvcConfigurer {
    // LoginCheckInterceptorをフィールドとして保持
    private final LoginCheckInterceptor loginCheckInterceptor;

    /**
     * コンストラクタでインターセプターを注入する
     */
    @Autowired
    public WebConfig(LoginCheckInterceptor loginCheckInterceptor) {
        // フィールドにインターセプターを設定
        this.loginCheckInterceptor = loginCheckInterceptor;
    }

    /**
     * インターセプターの登録とルールの設定
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor) // カスタムインターセプターを登録
                .addPathPatterns("/**") // すべてのURLパターンを対象にする
                .excludePathPatterns( // 以下のパスはインターセプターの対象外とする
                        "/order",       // 注文ページ
                        "/order/*",  // カートページ
                        "/order/*/*",
                        "/login",// ログインページ,
                        "/kitchen/front",
                        "/front/order"
                );
    }

    /**
     * CORS（クロスオリジンリクエスト）の設定
     *
     * @param registry CORS設定用のレジストリ
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // すべてのパスを対象とする
                .allowedOrigins("http://localhost:5173", "http://127.0.0.1:5173") // 許可するオリジン
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH") // 許可するHTTPメソッド
                .allowedHeaders("*") // 許可するヘッダー
                .allowCredentials(true); // クッキー情報を許可
    }
}
