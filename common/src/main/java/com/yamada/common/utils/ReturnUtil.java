package com.yamada.common.utils;

import com.yamada.common.vo.ReturnVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ReturnUtil {

    /**
     * 普通成功返回
     * @param data
     * @return
     */
    public static ResponseEntity<ReturnVO> success(Object data) {
        return new ResponseEntity<>(
                ReturnVO.builder()
                        .code(HttpStatus.OK.value())
                        .msg("成功")
                        .data(data)
                        .build(),
                HttpStatus.OK
        );
    }

    /**
     * 自定义返回msg 成功返回
     * @param message
     * @param data
     * @return
     */
    public static ResponseEntity<ReturnVO> success(String message, Object data) {
        return new ResponseEntity<>(
                ReturnVO.builder()
                        .code(HttpStatus.OK.value())
                        .msg(message)
                        .data(data)
                        .build(),
                HttpStatus.OK
        );
    }
}
