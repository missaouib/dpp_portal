package mzc.data.portal.controller;

import mzc.data.portal.dto.ApiResponse;
import mzc.data.portal.dto.SandboxImageData;
import mzc.data.portal.dto.SandboxModelData;
import mzc.data.portal.dto.SandboxSpecData;
import mzc.data.portal.service.SandboxImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AdminSandboxImageController {

    private SandboxImageService sandboxImageService;

    /**
     * 생성자 주입
     * @param sandboxImageService
     */
    @Autowired
    public AdminSandboxImageController(SandboxImageService sandboxImageService){
        this.sandboxImageService = sandboxImageService;
    }

    @GetMapping("/admin/sandbox-image")//("/SandboxImage")
    public String SandboxImage(Model model) {

        List<SandboxImageData> sandboxImages = sandboxImageService.sandboxImages();

        model.addAttribute("sandboxImages", sandboxImages);
        return ("admin/sandbox-image-list");
    }

    //샌드박스 이미지 생성폼으로 이동
    @GetMapping("/admin/form/image/add")
    public String addSandboxImageForm(Model model) {

        return "admin/sandbox-image-add";
    }

    //샌드박스 이미지 생성
    @PostMapping("/admin/image/add")
    @ResponseBody
    public SandboxImageData.SandboxImageDataRes addSandboxImage(@Valid SandboxImageData.AddSandboxImageParam param) {

        sandboxImageService.addSandboxImage(param);

        return new SandboxImageData.SandboxImageDataRes(ApiResponse.CREATED, null, null);
    }

    //샌드박스 이미지 업데이트
    @PostMapping("/update")
    @ResponseBody
    public SandboxImageData.SandboxImageDataRes updateSandboxImage(@Valid SandboxImageData.UpdateSandboxImageParam param) {

        sandboxImageService.updateSandboxImage(param);

        return new SandboxImageData.SandboxImageDataRes(ApiResponse.CREATED, null, null);
    }




}
