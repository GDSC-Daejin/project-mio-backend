package com.gdsc.projectmiobackend.log;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.net.InetAddress;
import java.nio.file.AccessDeniedException;

@Aspect
@Component
public class AopLogging {

    private final ApiLogRepository apiLogRepository;

    public AopLogging(ApiLogRepository apiLogRepository) {
        this.apiLogRepository = apiLogRepository;
    }

    @Around("execution(public * com.gdsc.projectmiobackend.controller.*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpRequest = requestAttributes.getRequest();
        ApiLog log = apiLogRepository.save(
                new ApiLog(
                        InetAddress.getLocalHost().getHostAddress(),
                        httpRequest.getRequestURL().toString(),
                        httpRequest.getMethod(),
                        httpRequest.getHeader("X-FORWARDED-FOR") != null && !httpRequest.getHeader("X-FORWARDED-FOR").isEmpty() ?
                                httpRequest.getHeader("X-FORWARDED-FOR") : httpRequest.getRemoteAddr(),
                        getRequestString(joinPoint)
                )
        );
        try {
            ResponseEntity<?> response = (ResponseEntity<?>) joinPoint.proceed();
            apiLogRepository.updateResponse(log.getSeq(), response.getStatusCodeValue(), response.getBody().toString());
            return response;
        } catch (Exception e) {
            apiLogRepository.updateResponse(log.getSeq(), exceptionToStatus(e).value(), e.getMessage() != null ? e.getMessage() : "error");
            throw e;
        }
    }

    private String getRequestString(ProceedingJoinPoint joinPoint) {
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < parameterNames.length; i++) {
            stringBuilder.append(parameterNames[i])
                    .append("=")
                    .append(parameterValues[i]);
            if (i < parameterNames.length - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    public HttpStatus exceptionToStatus(Exception e) {
        if (e instanceof IllegalArgumentException) {
            return HttpStatus.BAD_REQUEST;
        } else if (e instanceof AccessDeniedException) {
            return HttpStatus.FORBIDDEN;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}