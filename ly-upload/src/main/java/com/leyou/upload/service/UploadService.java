package com.leyou.upload.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 10:48 2019/4/17
 */
@Service
@Slf4j
public class UploadService {

    private static final List<String> ALLOW_TYPES = Arrays.asList("image/jpeg", "image/png");

    public String uploadImage(MultipartFile multipartFile) {
        try {
            //校验文件类型
            String contentType = multipartFile.getContentType();
            if (!ALLOW_TYPES.contains(contentType)) {
                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
            }

            //校验文件内容
            BufferedImage read = ImageIO.read(multipartFile.getInputStream());
            if (read == null) {
                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
            }

            //准备目标路径
            File file = new File("C:\\APP\\JetBrains", multipartFile.getOriginalFilename());

            //保存文件到本地
            multipartFile.transferTo(file);

            //返回路径
            return "http://127.0.0.1:8082/" + multipartFile.getOriginalFilename();
        } catch (IOException e) {
            //上传失败
            log.error("上传文件失败", e);
            throw new LyException(ExceptionEnum.UPLOAD_FILE_ERROR);
        }


    }
}
