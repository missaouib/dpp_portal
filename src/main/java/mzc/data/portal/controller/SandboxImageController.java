package mzc.data.portal.controller;

import mzc.data.portal.dto.SandboxImageData;
import mzc.data.portal.service.SandboxImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SandboxImageController {

    private SandboxImageService sandboxImageService;

    /**
     * 생성자 주입
     * @param sandboxImageService
     */
    @Autowired
    public SandboxImageController(SandboxImageService sandboxImageService){
        this.sandboxImageService = sandboxImageService;
    }

    @GetMapping("/SandboxImage")
    public String SandboxImage(Model model) {

        List<SandboxImageData> sandboxImages = sandboxImageService.sandboxImages();

        model.addAttribute("sandboxImages", sandboxImages);
        return ("user/sandbox-image-form");
    }
}
