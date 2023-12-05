package com.glasssix.dubbo.service;

import com.glasssix.dubbo.utils.Result;

public interface WxService {
    /**
     * 生成微信二维码链接
     * @param redirectUrl
     * @return
     */
    String generateQrCode(String redirectUrl);

    /**
     * 通过扫码获取的code 获取openId
     * @param code
     * @param state
     * @return
     */
    Result callback(String code, String state);

    Result getUserInfo(String openid,String accessToken);

}
