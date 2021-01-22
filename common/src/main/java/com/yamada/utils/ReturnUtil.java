package com.yamada.utils;

import com.yamada.vo.ReturnVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ReturnUtil {

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
}