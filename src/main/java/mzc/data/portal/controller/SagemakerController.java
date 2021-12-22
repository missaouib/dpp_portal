package mzc.data.portal.controller;

import mzc.data.portal.dto.ApiResponse;
import mzc.data.portal.dto.SagemakerData;
import mzc.data.portal.agent.service.SagemakerService;
import mzc.data.portal.dto.SandboxData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import software.amazon.awssdk.services.sagemaker.model.NotebookInstanceSummary;

import javax.validation.Valid;
import java.util.List;

@Controller
public class SagemakerController {

    private SagemakerService sagemakerService;
    private SandboxData sandboxData;

    @Autowired
    public SagemakerController(SagemakerService sagemakerService){
        this.sagemakerService = sagemakerService;
    }

    @GetMapping("/sagemaker/form")
    public String sagemakerAddForm() {
        System.out.println("hello");

        return "sagemaker/sagemaker-add";
    }

    @PostMapping("/sagemaker/add")
    public SagemakerData.SagemakerDataCustomRes addSagemaker (@Valid SagemakerData.CreateSagemakerInstanceParam param) {

        sagemakerService.create(param);

        return new SagemakerData.SagemakerDataCustomRes<>(ApiResponse.CREATED, null, null);
    }


    @GetMapping("/sagemaker/list")
    @ResponseBody
    public SagemakerData.SagemakerDataCustomRes sagemakerList(Model model) {
        //List<NotebookInstanceSummary> list = sagemakerService.sagemakerBookList();


        return new SagemakerData.SagemakerDataCustomRes<>(ApiResponse.CREATED, null, model);
    }


    @GetMapping("/sagemaker/presign-url")
    @ResponseBody
    public SagemakerData.SagemakerDataCustomRes sagemakerUrl(Model model) {
        //String url = sagemakerService.createNotebookPresignedUrl(sandboxData.getCloudIdx());
        //model.addAttribute("url", url);
        return new SagemakerData.SagemakerDataCustomRes<>(ApiResponse.CREATED, null, model);
    }

    @GetMapping("/sagemaker/update")
    @ResponseBody
    public SagemakerData.SagemakerDataCustomRes updateSagemaker(@Valid SagemakerData.UpdateSagemakerInstanceParam param) {

        sagemakerService.update(param);
        return new SagemakerData.SagemakerDataCustomRes<>(ApiResponse.CREATED, null, null);
    }



}
