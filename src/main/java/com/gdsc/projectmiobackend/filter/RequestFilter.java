package com.gdsc.projectmiobackend.filter;

import com.gdsc.projectmiobackend.discord.MsgService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestFilter implements Filter {

    private final MsgService msgService;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // SSE 요청인지 확인하는 조건 추가
        if (isSseRequest(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();

        chain.doFilter(requestWrapper, responseWrapper);

        String requestMethod = ((HttpServletRequest) request).getMethod();
        String requestBody = getRequestBody(requestWrapper);
        String responseBody = getResponseBody(responseWrapper);
        String requestURI = ((HttpServletRequest) request).getRequestURI();
/*        log.info("\n" +
                        "[REQUEST] {} - {} {} - {}\n" +
                        "Headers : {}\n" +
                        "Request : {}\n" +
                        "Response : {}\n",
                ((HttpServletRequest) request).getMethod(),
                ((HttpServletRequest) request).getRequestURI(),
                responseWrapper.getStatus(),
                (end - start) / 1000.0,
                getHeaders((HttpServletRequest) request),
                getRequestBody(requestWrapper),
                getResponseBody(responseWrapper));*/

        responseWrapper.copyBodyToResponse();

        if(!(requestURI.contains("/auth/google"))) {
            if(requestMethod.equals("GET")){
                if(responseWrapper.getStatus() != 200){
                    msgService.sendMsg(
                            (requestMethod + " - " + requestURI + " - " + responseWrapper.getStatus() + " - " + (end - start) / 1000.0),
                            "Request : " + requestBody + "\n" +
                                    "Response : " + responseBody,
                            "실시간 API 로그"
                    );
                }
            }
            else{
                msgService.sendMsg(
                        (requestMethod + " - " + requestURI + " - " + responseWrapper.getStatus() + " - " + (end - start) / 1000.0),
                        "Request : " + requestBody + "\n" +
                                "Response : " + responseBody,
                        "실시간 API 로그"
                );
            }
        }
    }

    private boolean isSseRequest(HttpServletRequest request) {
        return request.getRequestURI().contains("/subscribe");
    }

    private Map getHeaders(HttpServletRequest request) {
        Map headerMap = new HashMap<>();

        Enumeration headerArray = request.getHeaderNames();
        while (headerArray.hasMoreElements()) {
            String headerName = (String) headerArray.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    return new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException e) {
                    return " - ";
                }
            }
        }
        return " - ";
    }

    private String getResponseBody(final HttpServletResponse response) throws IOException {
        String payload = null;
        ContentCachingResponseWrapper wrapper =
                WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                wrapper.copyBodyToResponse();
            }
        }
        return null == payload ? " - " : payload;
    }
}