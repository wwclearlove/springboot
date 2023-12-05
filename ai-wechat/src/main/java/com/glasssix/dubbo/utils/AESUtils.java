package com.glasssix.dubbo.utils;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 *
 */
public class AESUtils {
    private static final String KEY = "6lh89gbi2bkgsnonu25xgy07dcdq6ifi";
    private static final String IV = "8x6akcl3naam6sqz";

    public static String getRandom(int length){
        char[] arr = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k',
                'l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
        String result = String.valueOf(arr[(int)Math.floor(Math.random()*36)]);
        for(int i = 1;i<length;i++){
            result+=arr[(int)Math.floor(Math.random()*36)];
        }
        return result;
    }
    public static void main(String[] args) {

        String s = encryptDataToString("k62dkgBaECSVjOSz2gBDbXxe2bovqasV");


        System.out.println(s);

        System.out.println(decryptDataToString("bTbGy0UQeg8X8AStx7ezTDDVXfp2KI3MB6YB8p3muWw="));


    }

    /**
     * 加密文件
     *
     * @param byteContent
     * @return
     */
    public static byte[] encryptData(byte[] byteContent, String key, String iv) {
        byte[] encryptedBytes = null;
        try {
            byte[] enCodeFormat = key.getBytes();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
            byte[] initParam = iv.getBytes();
            // 用于产生密文的第一个block，以使最终生成的密文产生差异（明文相同的情况下），
            // 使密码攻击变得更为困难，除此之外IvParameterSpec并无其它用途。
            // 为了方便也可以动态跟随key生成new IvParameterSpec(key.getBytes("utf-8"))
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            encryptedBytes = cipher.doFinal(byteContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedBytes;
    }

    /**
     * 解密文件
     *
     * @param encryptedBytes
     * @return
     */
    public static byte[] decryptData(byte[] encryptedBytes, String key, String iv) {
        byte[] result = null;
        try {
            byte[] sEnCodeFormat = key.getBytes();
            SecretKeySpec secretKey = new SecretKeySpec(sEnCodeFormat, "AES");
            byte[] initParam = iv.getBytes();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            result = cipher.doFinal(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 加密文件
     *
     * @param str
     * @param key
     * @param iv
     * @return
     */
    public static String encryptDataToString(String str, String key, String iv) {
        if (StringUtils.isNotBlank(str)) {
            return Base64.encodeBase64String(encryptData(str.getBytes(), key, iv));
        }
        return null;
    }


    /**
     * 解密文件
     *
     * @param str
     * @return
     */
    public static String decryptDataToString(String str, String key, String iv) {
        if (StringUtils.isNotBlank(str)) {
            return new String(decryptData(Base64.decodeBase64(str), key, iv), StandardCharsets.UTF_8);
        }
        return null;
    }

    /**
     * 加密字符串
     *
     * @param str
     * @return
     */
    public static String encryptDataToString(String str) {
        if (StringUtils.isNotBlank(str)) {
            return Base64.encodeBase64String(encryptData(str.getBytes(), KEY, IV));
        }
        return null;
    }


    /**
     * 解密字符串
     *
     * @param str
     * @return
     */
    public static String decryptDataToString(String str) {
        if (StringUtils.isNotBlank(str)) {
            return new String(decryptData(Base64.decodeBase64(str), KEY, IV), StandardCharsets.UTF_8);
        }
        return null;
    }

}
