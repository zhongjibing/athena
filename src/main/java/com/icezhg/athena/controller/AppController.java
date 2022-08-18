package com.icezhg.athena.controller;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by zhongjibing on 2021/10/27
 */
@Controller
public class AppController {
    private static final Logger log = LoggerFactory.getLogger(AppController.class);

    @GetMapping("/")
    public String index() {
        return "redirect:http://127.0.0.1:8090/login";
    }

    @GetMapping("/favicon.ico")
    public void favicon(HttpServletResponse response) {
        byte[] bytes = getGzipFileBytes("static/favicon.png");
        if (bytes.length > 0) {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            response.setHeader(HttpHeaders.CONTENT_ENCODING, "gzip");
            try (ServletOutputStream out = response.getOutputStream()) {
                out.write(bytes);
                out.flush();
            } catch (IOException e) {
                log.error("send favicon data error: {}", e.getMessage());
            }
        } else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }

    private byte[] getGzipFileBytes(String path) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             GZIPOutputStream gout = new GZIPOutputStream(bos);
             InputStream resource = getClass().getClassLoader().getResourceAsStream(path)) {
            if (resource != null) {
                IOUtils.copy(resource, gout);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            log.error("read file error: {}", e.getMessage());
        }
        return new byte[0];
    }
}
