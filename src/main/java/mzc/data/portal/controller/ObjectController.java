package mzc.data.portal.controller;

import mzc.data.portal.agent.dto.BucketData;
import mzc.data.portal.agent.dto.ObjectData;
import mzc.data.portal.agent.service.ObjectAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ObjectController {

    private ObjectAgentService objectAgentService;

    /**
     * 생성자 주입
     * @param objectAgentService
     */
    @Autowired
    public ObjectController(ObjectAgentService objectAgentService){
        this.objectAgentService = objectAgentService;
    }

    @GetMapping("/buckets")
    public String getBuckets(Model model) {

        List<BucketData> buckets = objectAgentService.listBuckets();

        model.addAttribute("buckets", buckets);

        return "user/bucket";
    }


    @GetMapping("/objects")
    public String getObjects(@RequestParam String bucket, @RequestParam String prefix, Model model) {

        ObjectData.GetObjectsParam objectData = ObjectData.GetObjectsParam.builder().bucketName(bucket).prefix(prefix).build();

        List<ObjectData> objects = objectAgentService.listObjects(objectData);


        model.addAttribute("prefix", prefix);
        model.addAttribute("bucket", bucket);
        model.addAttribute("objects", objects);

        return "user/object";
    }

    @GetMapping("/objects/download")
    public ResponseEntity<ByteArrayResource> downloadObject(@RequestParam String bucket, @RequestParam String prefix) {

        ObjectData.GetObjectsParam objectData = ObjectData.GetObjectsParam.builder().bucketName(bucket).prefix(prefix).build();

        final byte[] data = objectAgentService.downloadObject(objectData);
        final ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + prefix + "\"")
                .body(resource);

    }

}
