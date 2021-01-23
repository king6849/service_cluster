package utils;


import Enties.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * @Author king
 * @date 2020/11/19
 */
public class TokenUtil {

    public static final String SECRET = "king";
    /**
     * 签发地
     */
    public static final String issuer = "FaRuan";
    /**
     * 过期时间
     */
    public static final long expire = 3600 * 1000 * 60;


    public static String getToken(User user) {
        return createJwtToken(user);
    }


    public static String getToken(String openId, String sessionKey) {
        return createJwtToken(openId, sessionKey);
    }

    /**
     * @Describe 手机号，账号密码登录的Token
     * @Author king
     * @Date 2020/11/20
     */
    private static String createJwtToken(User user) {
        Key key = generateKey();
        JwtBuilder builder = tokenDeclaration(user, algorithm(), key);
        return generateToken(builder, expire);
    }

    /**
     * @Describe 用于微信号码登录的Token
     * @Author king
     * @Date 2020/11/20
     */
    private static String createJwtToken(String openId, String sessionKey) {
        Key key = generateKey();
        JwtBuilder builder = tokenDeclaration(openId, sessionKey, algorithm(), key);
        return generateToken(builder, expire);
    }


    private static SignatureAlgorithm algorithm() {
        // 签名算法 ，将对token进行签名
        return SignatureAlgorithm.HS256;
    }

    private static Key generateKey() {
        // 通过秘钥签名JWT
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
        String str = algorithm().getJcaName();
        return new SecretKeySpec(apiKeySecretBytes, str);
    }

    /**
     * @Describe token声明方式一，用于手机号，账号登录
     * @Author king
     * @Date 2020/11/20
     */
    private static JwtBuilder tokenDeclaration(User user, SignatureAlgorithm signatureAlgorithm, Key signingKey) {
        // 让我们设置JWT声明
        return Jwts.builder()
                .claim("phone", user.getPhone())
                .claim("id", user.getId())
                //签发人
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);
    }

    /**
     * @Describe token声明方式二，用于微信扫码登录
     * @Author king
     * @Date 2020/11/20
     */
    private static JwtBuilder tokenDeclaration(String openId, String session_key, SignatureAlgorithm signatureAlgorithm, Key signingKey) {
        // 让我们设置JWT声明
        JwtBuilder builder = Jwts.builder()
                .claim("openId", openId)
                .claim("sessionKey", session_key)
                //签发人
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);
        return builder;
    }


    /**
     * @Describe : 用于生成token 字符串
     * @Author : king
     * @Date : 2021/1/20 - 16:10
     * @Params : [builder, ttlMillis]
     */
    public static String generateToken(JwtBuilder builder, long ttlMillis) {

        // 生成签发时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        builder.setIssuedAt(now);
        // if it has been specified, let's add the expiration
        if (ttlMillis >= 0) {
            //过期时间
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        // 构建JWT并将其序列化为一个紧凑的url安全字符串
        return builder.compact();
    }


    /**
     * @Describe Token解析方法
     * @Author king
     * @Date 2021/1/20 - 16:11
     * @Params [jwt]
     */
    public static Claims parseJWT(String jwt) {
        // 如果这行代码不是签名的JWS(如预期)，那么它将抛出异常
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET))
                .parseClaimsJws(jwt).getBody();
    }

    /**
     * @Describe 解析token，并获取指定key的值
     * @Author king
     * @Date 2021/1/20 - 16:11
     * @Params [jwt, key]
     */
    public static long parseJWTForKey(String jwt, String key) {
        Claims claims = parseJWT(jwt);
        return Long.parseLong(claims.get(key).toString());
    }

}
