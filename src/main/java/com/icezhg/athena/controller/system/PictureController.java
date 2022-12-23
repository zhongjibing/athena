package com.icezhg.athena.controller.system;

import com.icezhg.athena.annotation.Operation;
import com.icezhg.athena.domain.Picture;
import com.icezhg.athena.enums.OperationType;
import com.icezhg.athena.service.system.PictureService;
import com.icezhg.commons.exception.ErrorCodeException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * Created by zhongjibing on 2022/09/07.
 */
@Slf4j
@RestController
@RequestMapping("/picture")
public class PictureController {

    private final PictureService pictureService;

    public PictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @PostMapping("/upload")
    @Operation(title = "pictures uploading", type = OperationType.UPLOAD)
    public Picture upload(MultipartFile file) {
        return pictureService.save(file);
    }

    @GetMapping("/{id}")
    @Operation(title = "pictures viewing", type = OperationType.VIEW)
    public void readImage(@PathVariable String id, HttpServletResponse response) {
        Picture picture = pictureService.findById(id);
        if (picture == null) {
            picture = pictureService.findByAvatar(id);
        }
        if (picture != null) {
            byte[] bytes = getGzipBytes(picture.getData());
            response.setContentType(picture.getType());
            response.setHeader(HttpHeaders.CONTENT_ENCODING, "gzip");
            try (ServletOutputStream out = response.getOutputStream()) {
                out.write(bytes);
                out.flush();
            } catch (IOException e) {
                throw new ErrorCodeException("", "send image error", e);
            }
        } else {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }

    private byte[] getGzipBytes(byte[] bytes) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             GZIPOutputStream gout = new GZIPOutputStream(bos)) {
            gout.write(bytes);
            gout.close();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new ErrorCodeException("", "gzip image error", e);
        }
    }
}
