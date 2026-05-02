package com.hospital.exception;

import com.hospital.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResultVo<?> handleException(Exception e) {
        log.error("系统异常", e);
        return ResultVo.error("系统繁忙，请稍后再试");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResultVo<?> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常", e);
        return ResultVo.error(e.getMessage());
    }
}
