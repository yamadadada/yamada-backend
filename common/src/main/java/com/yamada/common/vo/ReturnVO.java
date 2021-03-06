package com.yamada.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ReturnVO {

    private int code;

    private String msg;

    private Object data;
}
