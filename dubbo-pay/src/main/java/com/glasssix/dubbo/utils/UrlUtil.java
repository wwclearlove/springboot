package com.glasssix.dubbo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtil {
    public static String getUrlparameter(String url, String name) {
        String result = "";
        Pattern pXM = Pattern.compile(name + "=([^&]*)");
        Matcher mXM = pXM.matcher(url);
        while (mXM.find()) {
            result += mXM.group(1);
        }
        return result;
    }
}

