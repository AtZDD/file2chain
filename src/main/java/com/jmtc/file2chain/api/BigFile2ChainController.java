package com.jmtc.file2chain.api;

import com.jmtc.file2chain.comm.TmException;
import com.jmtc.file2chain.services.BigFile2ChainService;
import com.jmtc.file2chain.utils.R;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Chris
 * @date 2021/6/7 7:26
 * @Email:gang.wu@nexgaming.com
 */

@RestController
@RequestMapping("/api/bigfile")
@Api(value = "Big file 2 chain APIs", description = "big file 2 chain interface")
public class BigFile2ChainController extends BaseController{
    @Autowired
    private BigFile2ChainService bigFile2ChainService ;

    @RequestMapping(value="/upload/filecontent",method= RequestMethod.POST)
    public R uploadFileContent2Chain(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        if (file.isEmpty()) {
            throw new TmException("File can not empty");
        }

        String hashValue = bigFile2ChainService.uploadFileContent2Chain(file);

        return R.ok().put("fileHash", hashValue);
    }

    @RequestMapping(value="/verify",method= RequestMethod.GET)
    public R verifyFile(@RequestParam("transactionHash") String transactionHash,@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        if (file.isEmpty()) {
            throw new TmException("File can not empty");
        }

        Boolean verifyRes = bigFile2ChainService.fileVerify(transactionHash,file);

        return R.ok().put("verifyRes", verifyRes);
    }
}
