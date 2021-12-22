package mzc.data.portal.mapper;

import mzc.data.portal.dto.SagemakerData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SagemakerMapper {

    List<SagemakerData> sagemakerList();

    void addSagemaker(SagemakerData.CreateSagemakerInstanceParam param);

}
