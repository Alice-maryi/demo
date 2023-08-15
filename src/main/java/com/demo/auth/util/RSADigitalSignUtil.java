package com.demo.auth.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lkx
 * @date 2023/1/13 17:38
 */
@Component
public class RSADigitalSignUtil {

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 245;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 256;

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA 位数 如果采用2048 上面最大加密和最大解密则须填写:  245 256
     */
    private static final int INITIALIZE_LENGTH = 2048;

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    private static Logger logger = LoggerFactory.getLogger( "RSADigitalSignUtil" );


    public static void main(String[] args) throws Exception {
       /* Map<String, Object> stringObjectMap = RSADigitalSignUtil.genKeyPair();
        final RSAPublicKey rsaPublicKey = (RSAPublicKey) stringObjectMap.get("RSAPublicKey");
        final RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) stringObjectMap.get("RSAPrivateKey");
        final String publicKeyString = Base64.encodeBase64String(rsaPublicKey.getEncoded());
        System.out.println("publicKey----------------------------->\n" + publicKeyString);
        final String privateKeyString = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
        System.out.println("privateKey----------------------------->\n" + privateKeyString);*/
        // 服务端
        String spub = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiakFViGd+TkSa4y8iqZ/DvbFshtAa4mruSCkn79olqhotTqoSukTgscqKXjtY1OYJk3xQSk8s8IblJcdjG0FU3NjpAZ08DycKtnKGwlY3z5MQ4hnaeHlWRSVKGIv4rY48X2+2o/IMkgifW+eAuJoUpjFpoO+E0MUQMAAuTzsy3p9b3vDZ02xg7wihK8fjjSY9Qm/65n2tm1jkQ464l53bxL4mMrQx6njn8D62RadqFV+mTwLtw7hskTM+30gKcXEd4TOXXCBu/e6C3HtlUqyoxs6EnPH4CEuCFArsZKGcwYtla1D+zs9zgw0x2KWzF3rCEn1NI1lyXn2M3bGNYj5ewIDAQAB";
        String spri = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCJqQVWIZ35ORJrjLyKpn8O9sWyG0Briau5IKSfv2iWqGi1OqhK6ROCxyopeO1jU5gmTfFBKTyzwhuUlx2MbQVTc2OkBnTwPJwq2cobCVjfPkxDiGdp4eVZFJUoYi/itjjxfb7aj8gySCJ9b54C4mhSmMWmg74TQxRAwAC5POzLen1ve8NnTbGDvCKErx+ONJj1Cb/rmfa2bWORDjriXndvEviYytDHqeOfwPrZFp2oVX6ZPAu3DuGyRMz7fSApxcR3hM5dcIG797oLce2VSrKjGzoSc8fgIS4IUCuxkoZzBi2VrUP7Oz3ODDTHYpbMXesISfU0jWXJefYzdsY1iPl7AgMBAAECggEAVpuQT/A2g2X0wNV6iYDWz4NSPgwHK6Eh+Qcgi6DRHHg36E1PNFIPhGfpjs4WDDe7sd23u1dGaZHl6EZqwL6WazpdpgSp7bs0RYFgNkJbN5jqQlK/PU5yDOl2LkIX/nrGuKh0Ou3a9keL638d2pK3L8AOGgUowNWjGFm0GtbJ4/nuf0/XlRdA7ixjqjL9PyrT+omT0AS6Mk4Xo7OECdz2kgQ5/d9zlr1sLIV8k0+FIqe0GxObjO+/+MFxbKlKW985bh9f3EQA5r4fsAZiNXF330RiRQLmBapY2G21/Ok4KbROHCg19dhO68FChJy4nMVq9O/yJIpETsYANUEbH+aXgQKBgQDUwxNUVdXq9qSsdOE2QVNEB5PGHvnhqu+xMaFxEGLFCdhxMbtvKAPFMR2ceLH1aiAMDXJGssnfA5BfiEoibP9cgAJeoCP44Q0zQBVVkFfq+GMuAf/SWeGmodm289t8joWy8Y+GL23KNVcAJCNd++Zx5CKiIsLQRkIai5DnYk6uBQKBgQCloshBJHL4SXUgr/+7424T04xWbJwLkFtc1GDrNwGOJH21L3hf/uj08g4aX4X7rOS9urpuFyxkrbPeHRqb9FiaJnFcyuJ065yBJLr0CMy/0H3S/A5dYZbbuRK6aXq1IxuzEEIvpvVB5rBZQf9dA70QEv/XqTVR56/Eo2thpmohfwKBgQDBiwdtY3vqdx2L5/LrKPe23fG2e30ypQ3QMea7dH104qTrK6Nm1Y7AdhKKwVyZdEXc0OqQl5l97r8JuHA/6slNewEs1R6ECy233mbJ5Dml/Pgz5QuzbaIQtBDaFfEm2HuVMZsvJLwKAwW2kik+GKUkG7TJqzduwUrm3OkfI1WRhQKBgCNiEsVXs/e2OX8yGb9ZZjp2BpSvvuX5gIrkE5Arv/lNck0yRtS676jjzH91yHlrBbJCIXFDIgif0rywGu8V05eafr0D2jx/2H7+CbH370VKlJZtoePw5PkrIAUSY1Qum8w0vO7RC7N4RFPM9XA5TDoS1PSNAQbuJ24qOtYxZpqhAoGBAKKI4E1SW5IegfAB4TzLMcHFUjS1YHIJaDVoTIeFoIL7CIWi/4dULpAunrh7b5OyeBVt00ylroK8Q+cRcAgibTUTcprlAuPX9kHPmybEuKTpLd2JCRM6UbPEtQWskSQzznl+aB8SZBMX5uVGrmCz2K5A0EIyPcEC+BjasgK03E/B";
        // 客户端
        String cpub = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgT0ZyTEC5Vmv2pv6kz9jckGKRCQighd8E4Gy8CtKCc+Snk2yHRQhVo87hISvKTYgAoK/JONAJEyU05YhsvQGWfBzz5S5cyH9JxcylMKmWnER7Mf7MMf1g9YL65Z/0CAWK0Mhyyigs2WRh1m+PnDvqg/21Wjd16NTOy+3k6RRc6/f4uv14IOgSwYj/1lnQkf70XbbjJY2TKjqbxz9X2/U4VgDXUCeIXrNm7twZMICoDX52HUYwiZRk6DKwqD9oBiqaZriJ4jCsn8xfBIygbUcnz+KZa3eC/79N9s2E+H6h6LI4VtPWi96uggrjxzJztCHM9sHm77AY6xSycGktxxlAwIDAQAB";
        String cpri = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCBPRnJMQLlWa/am/qTP2NyQYpEJCKCF3wTgbLwK0oJz5KeTbIdFCFWjzuEhK8pNiACgr8k40AkTJTTliGy9AZZ8HPPlLlzIf0nFzKUwqZacRHsx/swx/WD1gvrln/QIBYrQyHLKKCzZZGHWb4+cO+qD/bVaN3Xo1M7L7eTpFFzr9/i6/Xgg6BLBiP/WWdCR/vRdtuMljZMqOpvHP1fb9ThWANdQJ4hes2bu3BkwgKgNfnYdRjCJlGToMrCoP2gGKppmuIniMKyfzF8EjKBtRyfP4plrd4L/v032zYT4fqHosjhW09aL3q6CCuPHMnO0Icz2webvsBjrFLJwaS3HGUDAgMBAAECggEAa479eesXXOeyqn7sIUY9LqcLOsDUE28WLWnhEW8FtHqTrLo/BWJuiIq2XwjKesL4Cv7jtACJrmwp5JprO3NKIeoziExft0lPVgxBZXtGVbkK0QRystuIIouu6ggcIYvCx4ichGcwr31Vi88NvQvi+qdEp8IZ91CPgY+p+e9kdqFlwN96lb8IIeDkzvAkVtl2eE163zFVvwwOT3KrvW7J4Nzv5vaqN/RRyny0wIOS+lC+V8MkCj+lM8sZyAVlWcRiHn0WR1HiylTtgYQWpmh9bNL/k/k7sav1Olj8r0QWnhxU35CjB+ZVfBv6ZnTmuOHEQl1M0VDxAGtMzeLEIJRoyQKBgQD+++P8ZXRcbS1p+bqK0zTWdhwBY46lwJinNXsS352wq2J8zkak5Ic7wWDpoh1IzJKMad28FmwmcaQcdU85EozQZTqQWcPjiD2osWqrnH+LY8S0nhXzyaYjehszqI8kzQxRZfo3yvz29BJiUcbHLhR4aVupx8eaFUnomOE9/bCbVQKBgQCBwO/vzmZmvXOddmga2UMxNr7Xt2qo5Afm1vg17JAoe8krBB2DIt8gN+v6bVAzxSyX3SxwdPuymIVlfF3dsgozz1ViemDICMTVEt8FytmCSCyDEykOG+Fi4DHoVNGwFyPXT382X+mwGgrzmy6WncjmDhwvtgWRE/P9HeSogxhu9wKBgQCaOxBvBYr5sQwsMuKzwAcQxcYwzOBmv/VWBpJYGyxNxYzAAyFV8ekeqtyM1QMzmpH2Hr8BWuq14j0q/YYa9m54SxK+Z52GTk1FcXMdNH8td/5uZcTPfiRhByXW/FkBIIoX3kAYaU7TrVRFZkN/DQ9Suh+4tV+1grT+3SI5Fe8yNQKBgCPihTkwPxfY+3mn/qC2R6P7vQoR2vi8oAbSmzDmrPPVUEyKRdM3uLfDX4sR4mV++ZdCfwDXCalaD0KDA77Fd5bel4G10MBh1HYNUwraEA+9ADp7+RXC1jGvSX40vw1RSQNKJCeYnRusCC1ZVKgpGR+u9VKop595AjiVpOCJ6R7/AoGBAJYV0dV1XBfuOZFmWXU1nUdJGS8ClZTxpR/5yvQigdhch0CndwxZ3/sgbpMAGSfSCnPe4xk7DvmXrGY+IZ+BSU7Vy2oTddWbojcf9jNIpDiFHvLo2Xz/vH+swx8n7cnxpXQ3sKxz59aFtc14iaSw41S0u0A+6AyBqmc13/0X+SvP";

//        String abc = "123456";
        String abc = "{\n" +
                "    \"carbonReductionId\":1612377018857086977,\n" +
                "    \"status\":2,\n" +
                "    \"userId\":1234445\n" +
                "}";
//        String abc = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCBPRnJMQLlWa/am/qTP2NyQYpEJCKCF3wTgbLwK0oJz5KeTbIdFCFWjzuEhK8pNiACgr8k40AkTJTTliGy9AZZ8HPPlLlzIf0nFzKUwqZacRHsx/swx/WD1gvrln/QIBYrQyHLKKCzZZGHWb4+cO+qD/bVaN3Xo1M7L7eTpFFzr9/i6/Xgg6BLBiP/WWdCR/vRdtuMljZMqOpvHP1fb9ThWANdQJ4hes2bu3BkwgKgNfnYdRjCJlGToMrCoP2gGKppmuIniMKyfzF8EjKBtRyfP4plrd4MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCBPRnJMQLlWa/am/qTP2NyQYpEJCKCF3wTgbLwK0oJz5KeTbIdFCFWjzuEhK8pNiACgr8k40AkTJTTliGy9AZZ8HPPlLlzIf0nFzKUwqZacRHsx/swx/WD1gvrln/QIBYrQyHLKKCzZZGHWb4+cO+qD/bVaN3Xo1M7L7eTpFFzr9/i6/Xgg6BLBiP/WWdCR/vRdtuMljZMqOpvHP1fb9ThWANdQJ4hes2bu3BkwgKgNfnYdRjCJlGToMrCoP2gGKppmuIniMKyfzF8EjKBtRyfP4plrd4MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCBPRnJMQLlWa/am/qTP2NyQYpEJCKCF3wTgbLwK0oJz5KeTbIdFCFWjzuEhK8pNiACgr8k40AkTJTTliGy9AZZ8HPPlLlzIf0nFzKUwqZacRHsx/swx/WD1gvrln/QIBYrQyHLKKCzZZGHWb4+cO+qD/bVaN3Xo1M7L7eTpFFzr9/i6/Xgg6BLBiP/WWdCR/vRdtuMljZMqOpvHP1fb9ThWANdQJ4hes2bu3BkwgKgNfnYdRjCJlGToMrCoP2gGKppmuIniMKyfzF8EjKBtRyfP4plrd4MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCBPRnJMQLlWa/am/qTP2NyQYpEJCKCF3wTgbLwK0oJz5KeTbIdFCFWjzuEhK8pNiACgr8k40AkTJTTliGy9AZZ8HPPlLlzIf0nFzKUwqZacRHsx/swx/WD1gvrln/QIBYrQyHLKKCzZZGHWb4+cO+qD/bVaN3Xo1M7L7eTpFFzr9/i6/Xgg6BLBiP/WWdCR/vRdtuMljZMqOpvHP1fb9ThWANdQJ4hes2bu3BkwgKgNfnYdRjCJlGToMrCoP2gGKppmuIniMKyfzF8EjKBtRyfP4plrd4L/v032zYT4";
//        String abc = "{\"action\":0,\"declineReason\":\"\",\"documentInfoList\":[{\"createUserId\":0,\"description\":\"\",\"documentTypeId\":0,\"fileId\":0,\"id\":0,\"name\":\"\",\"projectHash\":\"\",\"projectId\":0,\"projectStatus\":0,\"status\":0,\"tokenUserId\":0,\"updateUserId\":0}],\"id\":0,\"tokenUserId\":0}";
        //加密
        String encryptStr = encryptByPublicKey(abc, spub);
        System.out.println("加密数据：");
        System.out.println(encryptStr);
        String signature = signByPrivateKey(encryptStr, cpri);
        System.out.println("签名：");
        System.out.println(signature);
        //解密
        String s1 = decryptByPrivateKey(encryptStr, spri);
        System.out.println("解密数据：");
        System.out.println(s1);
        boolean verify = verify(encryptStr.getBytes(StandardCharsets.UTF_8), cpub, signature);
        System.out.println(verify);


    }

    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(INITIALIZE_LENGTH);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * <p>公钥加密(服务端公钥)</p>
     * @param data  源数据
     * @param publicKey  公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        byte[] dataBytes = data.getBytes();
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return Base64.encodeBase64String(encryptedData);
    }

    /**
     * <p>私钥解密(服务端私钥)<p/>
     * @param encryptedData 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String encryptedData, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        byte[] encryptBytes = Base64.decodeBase64(encryptedData);
        int inputLen = encryptBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptBytes, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptBytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData, "utf-8");
    }

    /**
     * <p> 公钥解密 </p>
     *
     * @param encryptedData  已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String encryptedData, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        byte[] dataBytes = Base64.decodeBase64(encryptedData);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData, "utf-8");
    }

    /**
     * <p> 私钥加密 </p>
     * 暂时没用
     * @param data 源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);

        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        byte[] encryptBytes = data.getBytes();
        int inputLen = encryptBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(encryptBytes, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptBytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return Base64.encodeBase64String(encryptedData);
    }

    /**
     * <p> 私钥加密签名 </p>
     *
     * @param content
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String signByPrivateKey(String content, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey  privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);

        signature.initSign(privateK);
        signature.update( content.getBytes());
        byte[] signed = signature.sign();
        return Base64.encodeBase64String(signed);
    }

    /**
     * <p> 校验数字签名</p>
     *
     * @param data 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign 数字签名
     *
     * @return
     * @throws Exception
     *
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.decodeBase64(sign));
    }

}