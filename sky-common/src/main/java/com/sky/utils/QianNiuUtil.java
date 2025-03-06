package com.sky.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class QianNiuUtil {


    public static String uploadToken() {
        String accessKey = "bPSUVpopKxqjyARH5iiXnOECgpEzTTKc07lA7vhi";
        String secretKey = "zcjR0reZosUCEqvFvqqG_QBf9e9rLW244W2jeG0y";
        String bucket = "blog255";
        String key = "takeout";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket, key);
        System.out.println(upToken);
        return upToken;
    }

    public static void uploadFile() {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String key = "takeout";

        String upToken = uploadToken();

        String localFilePath = "C:\\Users\\HwH\\Desktop\\其他\\一览.jpg";

        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            ex.printStackTrace();
            if (ex.response != null) {
                System.err.println(ex.response);

                try {
                    String body = ex.response.toString();
                    System.err.println(body);
                } catch (Exception ignored) {
                }
            }
        }

    }

    public static void main(String[] args) {
        uploadFile();
    }

}
