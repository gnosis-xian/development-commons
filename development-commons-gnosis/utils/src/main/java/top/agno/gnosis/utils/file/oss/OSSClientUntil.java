package top.agno.gnosis.utils.file.oss;

import com.aliyun.oss.OSSClient;

/**
 * <pre>
 * 名称: OSSClientUntil
 * 描述: 阿里云OSS客户端工具类
 * </pre>
 *
 * @author gnosis
 * @since 1.0.0
 */
public class OSSClientUntil {

    private static OSSClientUntil instance = null;

    private OSSClient ossClient = null;


    private OSSClientUntil(String endpoint, String accessKeyId, String accessKeySecret,
                           String bucket) {
        if (null == ossClient) {
            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            if (!ossClient.doesBucketExist(bucket)) {
                ossClient.createBucket(bucket);
            }
        }
    }

    public static OSSClientUntil getInstance(String endpoint, String accessKeyId,
                                             String accessKeySecret, String bucket) {
        if (instance == null) {
            instance = new OSSClientUntil(endpoint, accessKeyId, accessKeySecret, bucket);
        }
        return instance;
    }

    public OSSClient getOssClient() {
        return ossClient;
    }
}
