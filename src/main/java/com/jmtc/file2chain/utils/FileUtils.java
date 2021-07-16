package com.jmtc.file2chain.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author Chris
 * @date 2021/6/7 7:23
 * @Email:gang.wu@nexgaming.com
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
    private static Logger log = LoggerFactory.getLogger(FileUtils.class);

    public static boolean delFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            log.error(fileName + " not exist.");
            return true;
        } else {
            if (file.isFile()) {
                return FileUtils.deleteFile(fileName);
            } else {
                return FileUtils.deleteDirectory(fileName);
            }
        }
    }

    public static boolean deleteDirectory(String dirName) {
        String dirNames = dirName;
        if (!dirNames.endsWith(File.separator)) {
            dirNames = dirNames + File.separator;
        }
        File dirFile = new File(dirNames);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            log.error(dirNames + " not exist.");
            return true;
        }
        boolean flag = true;
        // 列出全部文件及子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = FileUtils.deleteFile(files[i].getAbsolutePath());
                // 如果删除文件失败，则退出循环
                if (!flag) {
                    break;
                }
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = FileUtils.deleteDirectory(files[i]
                        .getAbsolutePath());
                // 如果删除子目录失败，则退出循环
                if (!flag) {
                    break;
                }
            }
        }

        if (!flag) {
            log.error("delete folder failed.");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            log.debug("delete " + dirName + " success.");
            return true;
        } else {
            log.error("delete " + dirName + " failed.");
            return false;
        }

    }

    /**
     *
     * 删除单个文件
     *
     * @param fileName 被删除的文件名
     * @return 如果删除成功，则返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                log.debug("delete file " + fileName + " success.");
                return true;
            } else {
                log.error("delete file " + fileName + " failed.");
                return false;
            }
        } else {
            log.error(fileName + " not exist.");
            return true;
        }
    }
}
