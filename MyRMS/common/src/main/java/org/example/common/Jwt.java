package org.example.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;
@Slf4j
public class Jwt {
    private static String signKey = "my-sign-key";//暗号キー
    private static Long expire = 2 *1000* 1000L;//有効期限

    //JWTトークンの生成
    public static String generateJet(Map<String, Object> claims) {
        String jwt = Jwts.builder()//JWTビルダーインスタンスを取得し
                .addClaims(claims)//クレームを追加する
                .signWith(SignatureAlgorithm.HS256, signKey)// デジタル署名を追加
                .setExpiration(new Date(System.currentTimeMillis() + expire))// 有効期限を設定
                .compact();// JWTトークンを構築して文字列として取得
        return jwt;
    }

    //JWTトークンの解析
    public static Claims parseJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(signKey)// 署名キーを設定します。
                .parseClaimsJws(jwt)// 渡されたJWTを解析します。
                .getBody();// JWTのペイロード（クレームセット）を取得します。
        log.info( "claimsの結果:{}",claims);
        return claims;// 解析されたクレームを返します。
    }
}
