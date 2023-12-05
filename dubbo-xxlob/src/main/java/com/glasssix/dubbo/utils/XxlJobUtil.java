package com.glasssix.dubbo.utils;

import cn.hutool.http.Header;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.glasssix.dubbo.constants.XxlJobConstants;
import com.glasssix.dubbo.constants.XxlReturnT;

import java.io.IOException;
import java.util.HashMap;

public class XxlJobUtil {


    /**
     * 查询现有的任务（可以关注这个整个调用链，可以自己模仿着写其他的拓展接口）
     *
     * @param url
     * @param xxlJobInfo
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static String pageList(String url, HashMap<String, Object> xxlJobInfo) throws HttpException, IOException {
        //发送get请求并接收响应数据
        String result = HttpUtil.createGet(url + XxlJobConstants.PAGELISTAPI).addHeaders(getHeader()).form(xxlJobInfo).execute().body();
        return result;
    }

    public static HashMap<String, String> getHeader() {
        HashMap<String, String> headers = new HashMap<>();//存放请求头，可以存放多个请求头
        long timeMillis = System.currentTimeMillis();
        String paramStr = "xxl-job-Key=" + XxlJobConstants.XXLJOBKEY + "&timestamp=" + timeMillis;
        String checkSign = MD5Util.to32UpperCase(paramStr);
        headers.put("x-sign", checkSign);
        headers.put("x-timestamp", Long.toString(timeMillis));
        return headers;
    }

    /**
     * 新增/编辑任务
     *
     * @param url
     * @param xxlJobInfo
     * @return
     * @throws HttpException
     * @throws IOException
     */

    public static XxlReturnT addJob(String url, String xxlJobInfo) throws HttpException, IOException {
        String request = HttpUtil.createPost(url + XxlJobConstants.ADDJOB).addHeaders(getHeader()).body(xxlJobInfo).execute().body();
        XxlReturnT result = JSON.parseObject(request, XxlReturnT.class);
        return result;
    }

    /**
     * 删除任务
     *
     * @param url
     * @param id
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static XxlReturnT deleteJob(String url, int id) throws HttpException, IOException {
        String request= HttpUtil.createRequest(Method.DELETE, url+ XxlJobConstants.DELETEJOB + id).addHeaders(getHeader()).execute().body();
        XxlReturnT result = JSON.parseObject(request, XxlReturnT.class);
        return result;
    }

    /**
     * 开始任务
     *
     * @param url
     * @param id
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static XxlReturnT startJob(String url, int id) throws HttpException, IOException {
        String request= HttpUtil.createRequest(Method.PUT, url+ XxlJobConstants.STARJOB + id).addHeaders(getHeader()).execute().body();
        XxlReturnT result = JSON.parseObject(request, XxlReturnT.class);
        return result;
    }

    /**
     * 停止任务
     *
     * @param url
     * @param id
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static XxlReturnT stopJob(String url, int id) throws HttpException, IOException {
        String request= HttpUtil.createRequest(Method.PUT, url+ XxlJobConstants.STOPJOB + id).addHeaders(getHeader()).execute().body();
        XxlReturnT result = JSON.parseObject(request, XxlReturnT.class);
        return result;
    }


//    /**
//     * 登录
//     * @param url
//     * @param userName
//     * @param password
//     * @return
//     * @throws HttpException
//     * @throws IOException
//     */
//
//    public static String login(String url, String userName, String password) throws HttpException, IOException {
//        String path = "/atp/jobinfo/login?userName="+userName+"&password="+password;
//        String targetUrl = url + path;
//        HttpClient httpClient = new HttpClient();
//        HttpMethod get = new GetMethod((targetUrl));
//        httpClient.executeMethod(get);
//        if (get.getStatusCode() == 200) {
//            Cookie[] cookies = httpClient.getState().getCookies();
//            StringBuffer tmpcookies = new StringBuffer();
//            for (Cookie c : cookies) {
//                tmpcookies.append(c.toString() + ";");
//            }
//            cookie = tmpcookies.toString();
//        } else {
//            try {
//                cookie = "";
//            } catch (Exception e) {
//                cookie="";
//            }
//        }
//        return cookie;
//    }
}