package com.leyou.upload.web;

import com.leyou.upload.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: 萧一旬
 * @Description:
 * @Date: Create in 10:45 2019/4/17
 */
@RestController
@RequestMapping("upload")
public class UploadCpntroller {

    @Autowired
    private UploadService uploadService;

    /**
     * 上传图片
     * @param multipartFile
     * @return
     */
    @PostMapping("image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile multipartFile){

        return ResponseEntity.ok(uploadService.uploadImage(multipartFile));
    }
}
