package com.mjrj.lzh.pms.util;

import com.mjrj.lzh.pms.dto.response.ResponseResult;
import com.mjrj.lzh.pms.dto.response.ResponseTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.util
 * @Author: lzh
 * @CreateTime: 2020-04-06 20:36
 * @Description: ${Description}
 */
@Component
@Slf4j
public class UploadFileUtil {

    @Value("${uploadFile.resourceHandler}")
    private String resourceHandler;

    @Value("${uploadFile.location}")
    private String uploadFileLocation;

    @Value("${uploadFile.leave-location}")
    private String leaveDirectory;


    //    上传图片
    public ResponseResult uploadFile(MultipartFile file, HttpServletRequest request) throws IOException {
        if (!file.isEmpty()) {
            String basePath = request.getScheme() + "://" + request.getServerName()
                    + ":" + request.getServerPort() + request.getContextPath();

            String fileName = file.getOriginalFilename();
            String leaveDirectoryPath = leaveDirectory + "/" + new SimpleDateFormat("yyyyMMdd").format(new Date());
            File locationDirectory = new File(uploadFileLocation + leaveDirectoryPath);

            if (!locationDirectory.exists()) {
                locationDirectory.mkdirs();
            }
            fileName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));

            file.transferTo(new File(locationDirectory.getPath(),fileName));
            basePath += resourceHandler.substring(0, resourceHandler.lastIndexOf("/") + 1) +
                    leaveDirectoryPath + "/" + fileName;

            return ResponseTool.success(basePath);
        } else {
            return ResponseTool.fail();
        }
    }

    //    删除图片
    public ResponseResult deleteFile(List <String> img, HttpServletRequest request) {
        if (img.size() > 0) {
            String basePath = request.getScheme() + "://" + request.getServerName()
                    + ":" + request.getServerPort() + request.getContextPath();
            for (String s : img) {
                String v1 = uploadFileLocation.substring(0, uploadFileLocation.lastIndexOf("/"));
                v1 = v1.substring(0, v1.lastIndexOf("/"));
                v1 = v1 + s.substring(basePath.length());
                File f = new File(v1);
                f.delete();
            }
            return ResponseTool.success();
        }
        return new ResponseResult(false, 300, "请选择需要上传的图片！");
    }


    public ResponseResult deleteFileByPath(String relativePath, HttpServletRequest request) {
        String basePath = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + request.getContextPath();
        relativePath = relativePath.substring(basePath.length());
        log.info(" relativePath :" + relativePath );
        String path = uploadFileLocation;
        path = path.substring(0, path.lastIndexOf("/"));
        path = path.substring(0, path.lastIndexOf("/"));
        path = path + relativePath;
        log.info(" upl oad file :" + path );
        File f = new File(path);
        f.delete();
        return ResponseTool.success();


    }
}
