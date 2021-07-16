package com.jmtc.file2chain.antchain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.mychain.sdk.api.MychainClient;
import com.alipay.mychain.sdk.api.env.ClientEnv;
import com.alipay.mychain.sdk.api.env.ISslOption;
import com.alipay.mychain.sdk.api.env.SignerOption;
import com.alipay.mychain.sdk.api.env.SslBytesOption;
import com.alipay.mychain.sdk.api.logging.AbstractLoggerFactory;
import com.alipay.mychain.sdk.api.logging.ILogger;
import com.alipay.mychain.sdk.api.utils.Utils;
import com.alipay.mychain.sdk.crypto.MyCrypto;
import com.alipay.mychain.sdk.crypto.hash.Hash;
import com.alipay.mychain.sdk.crypto.keyoperator.Pkcs8KeyOperator;
import com.alipay.mychain.sdk.crypto.keypair.Keypair;
import com.alipay.mychain.sdk.crypto.signer.SignerBase;
import com.alipay.mychain.sdk.domain.account.Identity;
import com.alipay.mychain.sdk.message.query.QueryTransactionResponse;
import com.alipay.mychain.sdk.message.transaction.account.DepositDataRequest;
import com.alipay.mychain.sdk.message.transaction.account.DepositDataResponse;
import com.alipay.mychain.sdk.utils.IOUtil;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chris
 * @date 2021/6/7 16:19
 * @Email:gang.wu@nexgaming.com
 */
public class File2Chain {
    private static final String account = "chrisblocktest";
    private static Identity userIdentity;
    private static Keypair userKeypair;

    /**
     * sdk client
     */
    private static MychainClient sdk;
    /**
     * client key password
     */
    private static String keyPassword = "Local#123";
    /**
     * user password
     */
    private static String userPassword = "Local#123";
    /**
     * host ip
     */
    private static String host = "47.103.163.48";

    /**
     * server port
     */
    private static int port = 18130;
    /**
     * trustCa password.
     */
    private static String trustStorePassword = "mychain";
    /**
     * mychain environment
     */
    private static ClientEnv env;
    /**
     * mychain is tee Chain
     */
    private static boolean isTeeChain = false;
    /**
     * tee chain publicKeys
     */
    private static List<byte[]> publicKeys = new ArrayList<byte[]>();
    /**
     * tee chain secretKey
     */
    private static String secretKey = "123456";

    private static void exit(String tag, String msg) {
        exit(String.format("%s error : %s ", tag, msg));
    }

    private static void exit(String msg) {
        System.out.println(msg);
        System.exit(0);
    }

    private void initMychainEnv() throws IOException {
        // any user key for sign message
        String userPrivateKeyFile = "user.key";
        userIdentity = Utils.getIdentityByName(account);
        Pkcs8KeyOperator pkcs8KeyOperator = new Pkcs8KeyOperator();
        userKeypair = pkcs8KeyOperator.load(IOUtil.inputStreamToByte(File2Chain.class.getClassLoader().getResourceAsStream(userPrivateKeyFile)), userPassword);

        // use publicKeys by tee
        if(isTeeChain) {
            Keypair keypair = new Pkcs8KeyOperator()
                    .loadPubkey(
                            IOUtil.inputStreamToByte(File2Chain.class.getClassLoader().getResourceAsStream("test_seal_pubkey.pem")));
            byte[] publicKeyDer = keypair.getPubkeyEncoded();
            publicKeys.add(publicKeyDer);
        }

        env = buildMychainEnv();

        ILogger logger = AbstractLoggerFactory.getInstance(File2Chain.class);
        env.setLogger(logger);
    }

    private static ClientEnv buildMychainEnv() throws IOException {
        InetSocketAddress inetSocketAddress = InetSocketAddress.createUnresolved(host, port);
        String keyFilePath = "client.key";
        String certFilePath = "client.crt";
        String trustStoreFilePath = "trustCa";

        // build ssl option
        ISslOption sslOption = new SslBytesOption.Builder()
                .keyBytes(IOUtil.inputStreamToByte(File2Chain.class.getClassLoader().getResourceAsStream(keyFilePath)))
                .certBytes(IOUtil.inputStreamToByte(File2Chain.class.getClassLoader().getResourceAsStream(certFilePath)))
                .keyPassword(keyPassword)
                .trustStorePassword(trustStorePassword)
                .trustStoreBytes(
                        IOUtil.inputStreamToByte(File2Chain.class.getClassLoader().getResourceAsStream(trustStoreFilePath)))
                .build();

        List<InetSocketAddress> socketAddressArrayList = new ArrayList<InetSocketAddress>();
        socketAddressArrayList.add(inetSocketAddress);

        List<SignerBase> signerBaseList = new ArrayList<SignerBase>();
        SignerBase signerBase = MyCrypto.getInstance().createSigner(userKeypair);
        signerBaseList.add(signerBase);
        SignerOption signerOption = new SignerOption();
        signerOption.setSigners(signerBaseList);

        return ClientEnv.build(socketAddressArrayList, sslOption, signerOption);
    }

    private void initSdk() {
        sdk = new MychainClient();
        boolean initResult = sdk.init(env);
        if (!initResult) {
            exit("initSdk", "sdk init failed.");
        }
    }

    public void initAntChain() throws Exception {
        //step 1:init mychain env.
        initMychainEnv();

        //step 2: init sdk client
        initSdk();
    }

    public void shutDown(){
        sdk.shutDown();
    }

    public String  depositData(String fileName,String filePath) throws Exception {
        long startTime = System.currentTimeMillis();
        // 获取文件的内容
        byte[] fileBytes = IOUtil.readFileToByteArray(filePath);
        // 获取文件内容的hash
        String fileContentHash = DigestUtils.sha256Hex(fileBytes);

        // 文件内容上链
        FileToChain fileToChain = new FileToChain(fileName, String.valueOf(System.currentTimeMillis()), fileBytes, fileContentHash);
        DepositDataRequest request = new DepositDataRequest(Utils.getIdentityByName(account), Utils.getIdentityByName(account), JSON.toJSONBytes(fileToChain), new BigInteger("0"));
        DepositDataResponse response = sdk.getAccountService().depositData(request);
        System.out.println("DepositData cost time is:" + (System.currentTimeMillis() - startTime));
        if (!response.isSuccess() || response.getTransactionReceipt().getResult() != 0) {
            System.out.println("depositData failure.");
            return null;
        } else {
            System.out.println("depositData success.");
            fileToChain.setFileToChainHash(response.getTxHash().hexStrValue());
            System.out.println("txHash is：" + fileToChain.getFileToChainHash());
//            System.out.println("fileToChain is：" + JSON.toJSONString(fileToChain) + ", response is:"  + JSON.toJSONString(response));
            return fileToChain.getFileToChainHash();
        }
    }

    public boolean fileVerify(String txHash,String filePath) throws Exception{
        // 对比文件内容hash是否一致
        Hash hash = new Hash(txHash);
        QueryTransactionResponse queryTransactionResponse = sdk.getQueryService().queryTransaction(hash);
        // 链上交易的文件内容hash
        String txData = new String(queryTransactionResponse.getTransaction().getData());
        JSONObject fileToChain = JSONObject.parseObject(txData);

        // 获取文件内容的hash
        byte[] fileBytes = IOUtil.readFileToByteArray(filePath);
        String fileContentHash = DigestUtils.sha256Hex(fileBytes);
        if (!fileContentHash.equals(fileToChain.getString("fileContentHash"))) {
            System.out.println("fileVerify failure, fileContentHashOnChain is: " + fileToChain.getString("fileContentHash") + "fileContentHashOss is: " + DigestUtils.sha256Hex(fileBytes));
            return false ;
        } else {
            System.out.println("fileVerify success, fileContentHash is: " + fileContentHash);
            return true ;
        }
    }

    private static class FileToChain {
        private String fileName;
        private String fileTime;
        private byte[] fileContent;
        private String fileContentHash;
        private String fileToChainHash;

        FileToChain(String fileName, String fileTime, byte[] fileContent, String fileContentHash) {
            this.fileName = fileName;
            this.fileTime = fileTime;
            this.fileContent = fileContent;
            this.fileContentHash = fileContentHash;
        }

        public void setFileToChainHash(String fileToChainHash){
            this.fileToChainHash = fileToChainHash;
        }

        public String getFileName(){
            return this.fileName;
        }
        public String getFileTime(){
            return this.fileTime;
        }
        public byte[]  getFileContent(){
            return this.fileContent;
        }
        public String getFileContentHash(){
            return this.fileContentHash;
        }
        public String getFileToChainHash(){
            return this.fileToChainHash;
        }
    }


}
