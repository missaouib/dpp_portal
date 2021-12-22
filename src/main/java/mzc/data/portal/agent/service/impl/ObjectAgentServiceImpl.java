package mzc.data.portal.agent.service.impl;

import io.minio.ListBucketsArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.agent.dto.BucketData;
import mzc.data.portal.agent.dto.ObjectData;
import mzc.data.portal.agent.service.ObjectAgentService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ObjectAgentServiceImpl implements ObjectAgentService {

    @Override
    public List<BucketData> listBuckets() {
        List<BucketData> result = new ArrayList<>();

        try {
            MinioClient minioClient = createMinioClient();


            // Lists objects information.
            List<Bucket> results =
                    minioClient.listBuckets(ListBucketsArgs.builder().build());


            for (Bucket bucket : results) {
                result.add(convert(bucket));
            }



            return result;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public List<ObjectData> listObjects(ObjectData.GetObjectsParam param) {
        String bucketName = param.getBucketName();
        String prefix = param.getPrefix();
        List<ObjectData> result = new ArrayList<>();
        ListObjectsArgs listObjectsArgs = null;

        try {
            MinioClient minioClient = createMinioClient();

            listObjectsArgs = ListObjectsArgs.builder().bucket(bucketName).prefix(prefix).build();

            if (StringUtils.equals(prefix, "")) {
                listObjectsArgs = ListObjectsArgs.builder().bucket(bucketName).build();
            }

            // Lists objects information.
            Iterable<Result<Item>> results =
                    minioClient.listObjects(listObjectsArgs);

            for (Result<Item> itemResult : results) {
                Item item = itemResult.get();
                if (!StringUtils.equals(item.objectName(), prefix)) {
                    result.add(convert(item));
                }

            }

            return result;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

    }

    @Override
    public byte[] downloadObject(ObjectData.GetObjectsParam param) {
        String bucketName = param.getBucketName();
        String prefix = param.getPrefix();

        byte[] content = null;

        try(S3Client s3Client = createS3Client()) {

            GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(prefix).build();

            ResponseBytes<GetObjectResponse> result = s3Client.getObjectAsBytes(getObjectRequest);

            content = result.asByteArray();

            return content;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

    }

    private MinioClient createMinioClient() {
        return MinioClient.builder().endpoint("https://s3.amazonaws.com").credentials("AKIASCNEQF2LAKROHHMD", "UXiWFU+zA6flRAoD/ZFoCGUzijntBNFcXdP9g0X5").build();
    }

    private S3Client createS3Client() {
        return S3Client.builder().region(Region.AP_NORTHEAST_2).build();
    }

    private BucketData convert(Bucket bucket) {
        BucketData bucketData = new BucketData();

        bucketData.setBucketName(bucket.name());
        bucketData.setCreationDate(bucket.creationDate());

        return bucketData;
    }

    private ObjectData convert(Item item) {
        ObjectData objectData = new ObjectData();

        objectData.setObjectName(item.objectName());
        objectData.setDir(item.isDir());

        if (!item.isDir()) {
            objectData.setSize(FileUtils.byteCountToDisplaySize(item.size()));
            objectData.setLastModified(item.lastModified());
        }


        return objectData;

    }
}
