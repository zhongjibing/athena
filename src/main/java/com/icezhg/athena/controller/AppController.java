package com.icezhg.athena.controller;

import io.netty.buffer.UnpooledByteBufAllocator;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.zip.GZIPOutputStream;

/**
 * Created by zhongjibing on 2021/10/27
 */
@Controller
public class AppController {
    private static final Logger log = LoggerFactory.getLogger(AppController.class);

    @GetMapping("/")
    public Mono<Void> index(ServerHttpRequest request, ServerHttpResponse response) {
        return Mono.fromRunnable(()->{
            response.setStatusCode(HttpStatus.FOUND);
            response.getHeaders().setLocation(URI.create("http://localhost:8090/login"));
        });
    }

    @GetMapping("/favicon.ico")
    public Mono<Void> favicon(ServerHttpResponse response) {
        return response.writeWith(Mono.create(sink -> {
            byte[] bytes = getGzipFileBytes("static/favicon.png");
            if (bytes.length > 0) {
                response.getHeaders().setContentType(MediaType.IMAGE_PNG);
                response.getHeaders().set(HttpHeaders.CONTENT_ENCODING, "gzip");
                NettyDataBufferFactory factory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
                DataBuffer dataBuffer = factory.wrap(bytes);
                sink.success(dataBuffer);
            } else {
                response.setStatusCode(HttpStatus.NOT_FOUND);
                sink.success();
            }
        }));
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
