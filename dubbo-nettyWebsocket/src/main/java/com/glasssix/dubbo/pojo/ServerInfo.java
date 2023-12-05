package com.glasssix.dubbo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ServerInfo {

    //IP
    private String ip;

    //端口
    private int port;

    //启动时间
    private Date openDate;
}
