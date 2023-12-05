package com.glasssix.dubbo.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * Created by Love丶TG on 2019/9/24 11:41.
 */
public class MD5Util {
    public final static String calc(String ss) {//MD5加密算法
        String s = ss == null ? "" : ss;//如果为空，则返回""
        char hexDigists[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
                'e', 'f'};//字典

        try {
            byte[] strTemp = s.getBytes();//获取二进制
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);//执行加密
            byte[] md = mdTemp.digest();//加密结果
            int j = md.length;//结果长度
            char str[] = new char[j * 2];//字符数组
            int k = 0;
            for (int i = 0; i < j; i++) { //将二进制加密结果转化为字符
                byte byte0 = md[i];
                str[k++] = hexDigists[byte0 >>> 4 & 0xf];
                str[k++] = hexDigists[byte0 & 0xf];

            }
            return new String(str).toUpperCase();//输出加密后的字符
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }

    }

    private static String byteArrayToHexString(byte b[]) {
        StringBuilder resultSb = new StringBuilder();
        for (byte aB : b) {
            resultSb.append(byteToHexString(aB));
        }
        return resultSb.toString();
    }

    /**
     * @param origin      加密字段
     * @param charsetname 加密编码
     * @author ph
     */
    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname)) {
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charsetname)));
            }
        } catch (Exception ignored) {
        }
        return resultString;
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String encrypt(String data) {
        //结果字符串
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 16位小写
     *
     * @param data
     * @return
     */
    public static String to16LowerCase(String data) {
        return encrypt(data).substring(8, 24).toLowerCase(Locale.ROOT);
    }

    /**
     * 16位大写
     *
     * @param data
     * @return
     */
    public static String to16UpperCase(String data) {
        return encrypt(data).substring(8, 24).toUpperCase(Locale.ROOT);
    }

    /**
     * 32位小写
     *
     * @param data
     * @return
     */
    public static String to32LowerCase(String data) {
        return encrypt(data).toLowerCase(Locale.ROOT);
    }

    /**
     * 32位大写
     *
     * @param data
     * @return
     */
    public static String to32UpperCase(String data) {
        return encrypt(data).toUpperCase(Locale.ROOT);
    }

    public static void main(String[] args) {
        String paramStr = "xKey=" + 1 + "&secret=" + "XDLY3ceVPJkE6A3vyq6JVQq5hVKdtdeJlxydZhmVcy3";
        System.out.println(to32UpperCase(paramStr));
    }
}
