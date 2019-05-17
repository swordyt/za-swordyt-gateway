package com.swordyt.MockGateway.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.swordyt.MockGateway.dto.response.ResultModel;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Enumeration;


/**
 * 通用异常处理类
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ResultModel businessErrorHandler(BusinessException e)  {
        return ResultModel.failure(e.getErrCode()==null?ResultModel.ERR_CODE.FAIL.getCode():e.getErrCode()
                ,e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultModel defaultErrorHandler(HttpServletRequest req, Exception e)  {
        String errorMsg = e.getMessage();
        if(StringUtils.isNotBlank(errorMsg)&& errorMsg.contains("\n")){
            errorMsg = errorMsg.substring(0,errorMsg.indexOf("\n"));
        }
        log.error("系统异常{}",e);
        return ResultModel.failure(ResultModel.ERR_CODE.FAIL.getCode(),errorMsg);
    }
}
