package mzc.data.portal.service.impl;

import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.dto.SandboxSpecData;
import mzc.data.portal.mapper.SandboxSpecMapper;
import mzc.data.portal.service.SandboxSpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@ComponentScan
public class SandboxSpecServiceImpl implements SandboxSpecService {

    private SandboxSpecMapper sandboxSpecMapper;

    /**
     * 생성자 주입
     * @param sandboxSpecMapper
     */
    @Autowired
    public SandboxSpecServiceImpl(SandboxSpecMapper sandboxSpecMapper){
        this.sandboxSpecMapper = sandboxSpecMapper;
    }

    //샌드박스 스펙들 가져오기
    @Override
    public List<SandboxSpecData> sandboxSpecs(String type) {
        List<SandboxSpecData> sandboxSpecs = new ArrayList<>();
        try {
            sandboxSpecs = sandboxSpecMapper.findSandboxSpecs(type);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다.관리자에게 문의하세요.");
        } return sandboxSpecs;
    }

    @Override
    public SandboxSpecData.GetSandboxSpecInfoParam getSandboxSpecInfo(long sandboxSpecIdx) {

        SandboxSpecData.GetSandboxSpecInfoParam sandboxSpecInfo = null;

        try {
            sandboxSpecInfo = sandboxSpecMapper.getSandboxSpecInfo(sandboxSpecIdx);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다. 관리자에게 문의하세요.");
        }
        return sandboxSpecInfo;
    }

}
