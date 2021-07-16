package com.jmtc.file2chain.services.impl;

import com.jmtc.file2chain.antchain.File2Chain;
import com.jmtc.file2chain.domain.LogisticInfo;
import com.jmtc.file2chain.services.BigFile2ChainService;
import com.jmtc.file2chain.utils.Global;
import com.jmtc.file2chain.utils.UploadUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * @author Chris
 * @date 2021/6/1 21:19
 * @Email:gang.wu@nexgaming.com
 */

@Service("BigFile2ChainService")
public class BigFile2ChainServiceImpl implements BigFile2ChainService {

    File2Chain file2ChainApi ;

    @PostConstruct
    public void init(){
        try {
            file2ChainApi = new File2Chain();
            file2ChainApi.initAntChain();
        } catch (Exception e) {
            file2ChainApi = null;
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize()throws Throwable{
        if(file2ChainApi!= null){
            file2ChainApi.shutDown();
        }
    }

    @Override
    public String uploadFileContent2Chain(MultipartFile file) throws Exception {
        //保存目录
        String spath = "/mfile/material";
        //绝对路径
        String savePath = Global.getMaterialFile();

        String oFileName = file.getOriginalFilename();
        // 获取文件名
        String fileName = oFileName.substring(0, oFileName.lastIndexOf("."));
        String newFileName = fileName + new Date().getTime();
        // 获取文件的后缀名
        //String fileFormat = oFileName.substring(oFileName.lastIndexOf(".") + 1);
        String imgName = UploadUtils.fileUp(file, savePath, newFileName);
        savePath = String.format("%s/%s",savePath,imgName);

        return file2ChainApi.depositData(imgName,savePath);
    }

    @Override
    public boolean fileVerify(String txHash,MultipartFile file) throws Exception {
        //绝对路径
        String savePath = Global.getMaterialFile();

        String oFileName = file.getOriginalFilename();
        // 获取文件名
        String fileName = oFileName.substring(0, oFileName.lastIndexOf("."));
        String newFileName = fileName + new Date().getTime();
        // 获取文件的后缀名
        String imgName = UploadUtils.fileUp(file, savePath, newFileName);
        savePath = String.format("%s/%s",savePath,imgName);

        return file2ChainApi.fileVerify(txHash,savePath);
    }


}
