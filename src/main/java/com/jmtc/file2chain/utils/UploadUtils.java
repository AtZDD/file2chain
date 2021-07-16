package com.jmtc.file2chain.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Chris
 * @date 2021/6/7 7:21
 * @Email:gang.wu@nexgaming.com
 */
public class UploadUtils {
    public static String fileUp(MultipartFile file, String filePath, String fileName){
        String extName = ""; // 扩展名格式：
        try {
            if (file.getOriginalFilename().lastIndexOf(".") >= 0){
                extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            }

            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            copyFile(file.getInputStream(), filePath, fileName+extName).replaceAll("-", "");
        } catch (IOException e) {
            System.out.println(e);
        }
        return fileName+extName;
    }

    /**
     * 写文件到当前目录的upload目录中
     *
     * @param in
     * @param
     * @throws IOException
     */
    public static String copyFile(InputStream in, String dir, String realName)
            throws IOException {
        File file = new File(dir, realName);

        if (file.exists()) {
            FileUtils.delFile(dir+realName);
        }

        file.createNewFile();
        FileUtils.copyInputStreamToFile(in, file);
        System.out.println(file.getAbsolutePath());
        System.out.println(file.getCanonicalPath());
        return realName;
    }

}
