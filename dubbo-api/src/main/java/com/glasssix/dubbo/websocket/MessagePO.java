package com.glasssix.dubbo.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: dubbo
 * @description:
 * @author: wenjiang
 * @create: 2021-07-14 14:08
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessagePO implements Serializable {
    private String messageId;
    private String type;
    private Object data;
}

 