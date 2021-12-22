package mzc.data.portal.controller;

import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.dto.*;
import mzc.data.portal.service.SandboxImageService;
import mzc.data.portal.service.SandboxModelService;
import mzc.data.portal.service.SandboxSpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/sandbox-model")
public class AdminSandboxModelController {

    private SandboxModelService sandboxModelService;
    private SandboxSpecService sandboxSpecService;
    private SandboxImageService sandboxImageService;

    /**
     * 생성자 주입
     * @param sandboxModelService
     * @param sandboxSpecService
     * @param sandboxImageService
     */
    @Autowired
    public AdminSandboxModelController(SandboxModelService sandboxModelService, SandboxSpecService sandboxSpecService, SandboxImageService sandboxImageService){
        this.sandboxModelService = sandboxModelService;
        this.sandboxSpecService = sandboxSpecService;
        this.sandboxImageService = sandboxImageService;
    }

    @GetMapping("")
    public String sandboxModelList(Model model) {

        List<SandboxModelData> sandboxModelList = sandboxModelService.sandboxModelList();

        model.addAttribute("sandboxModelList", sandboxModelList);

        return "admin/sandbox-model-list";
    }

    //샌드박스 모델 상세 페이지
    @GetMapping("/{sandboxModelIdx}")
    public String sandboxModelInfo(@PathVariable long sandboxModelIdx, Model model) {

        SandboxModelData.GetSandboxModelInfoParam sandboxModelInfo = sandboxModelService.getSandboxModelInfo(sandboxModelIdx); //모델 info
        SandboxSpecData.GetSandboxSpecInfoParam sandboxSpecInfo = sandboxSpecService.getSandboxSpecInfo(sandboxModelInfo.getSandboxSpecIdx()); //스펙 info
        SandboxImageData.GetSandboxImageInfoParam sandboxImageInfo = sandboxImageService.getSandboxImageInfo(sandboxModelInfo.getSandboxImageIdx());

        model.addAttribute("sandboxModelInfo", sandboxModelInfo);
        model.addAttribute("sandboxSpecInfo", sandboxSpecInfo);
        model.addAttribute("sandboxImageInfo", sandboxImageInfo);

        return "admin/sandbox-model-info";
    }

    //샌드박스 모델 생성폼으로 이동
    /*@GetMapping("/form/add")
    public String addSandboxModelForm(Model model) {

        List<SandboxSpecData> sandboxSpecsEC2 = sandboxSpecService.sandboxSpecs("EC2");
        List<SandboxSpecData> sandboxSpecsSAGEMAKER = sandboxSpecService.sandboxSpecs("SAGEMAKER");
        List<SandboxImageData> sandboxImages = sandboxImageService.sandboxImages();

        model.addAttribute("sandboxSpecsEC2", sandboxSpecsEC2);
        model.addAttribute("sandboxSpecsSAGEMAKER", sandboxSpecsSAGEMAKER);
        model.addAttribute("sandboxImages", sandboxImages);

        return "admin/sandbox-model-add";
    }*/

    //샌드박스 모델 ec2 생성폼으로 이동
    @GetMapping("/form/add/ec2")
    public String addSandboxModelEC2Form(Model model) {

        List<SandboxSpecData> sandboxSpecsEC2 = sandboxSpecService.sandboxSpecs("EC2");
        List<SandboxImageData> sandboxImages = sandboxImageService.sandboxImages();

        System.out.println("sandboxImages : " + sandboxImages);

        model.addAttribute("sandboxSpecsEC2", sandboxSpecsEC2);
        model.addAttribute("sandboxImages", sandboxImages);

        return "admin/sandbox-model-add-ec2";
    }

    //샌드박스 모델 sagemaker 생성폼으로 이동
    @GetMapping("/form/add/sagemaker")
    public String addSandboxModelSagemakerForm(Model model) {

        List<SandboxSpecData> sandboxSpecsSAGEMAKER = sandboxSpecService.sandboxSpecs("SAGEMAKER");
        List<SandboxImageData> sandboxImages = sandboxImageService.sandboxImages();

        model.addAttribute("sandboxSpecsSAGEMAKER", sandboxSpecsSAGEMAKER);
        model.addAttribute("sandboxImages", sandboxImages);

        return "admin/sandbox-model-add-sagemaker";
    }

    //샌드박스 모델 생성
    @PostMapping("/add")
    @ResponseBody
    public SandboxModelData.SandboxModelDataRes addSandboxModel(@Valid SandboxModelData.AddSandboxModelParam param) {

        sandboxModelService.addSandboxModel(param);

        return new SandboxModelData.SandboxModelDataRes(ApiResponse.CREATED, null, null);
    }

    //샌드박스 모델 업데이트
    @PostMapping("/update")
    @ResponseBody
    public SandboxModelData.SandboxModelDataRes updateSandboxModel(@Valid SandboxModelData.UpdateSandboxModelParam param) {

        sandboxModelService.updateSandboxModel(param);

        return new SandboxModelData.SandboxModelDataRes(ApiResponse.CREATED, null, null);
    }
}
