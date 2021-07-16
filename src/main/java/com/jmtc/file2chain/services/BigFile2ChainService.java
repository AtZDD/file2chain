package com.jmtc.file2chain.services;

import com.jmtc.file2chain.domain.LogisticInfo;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Chris
 * @date 2021/6/1 21:18
 * @Email:gang.wu@nexgaming.com
 */
public interface BigFile2ChainService {
    String uploadFileContent2Chain(MultipartFile file) throws Exception;
    boolean fileVerify(String txHash,MultipartFile file) throws Exception;

}
