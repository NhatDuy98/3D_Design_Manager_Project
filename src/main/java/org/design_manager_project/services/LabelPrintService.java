package org.design_manager_project.services;

import org.design_manager_project.dtos.label_print.LabelPrintDTO;
import org.design_manager_project.filters.LabelPrintFilter;
import org.design_manager_project.mappers.LabelPrintMapper;
import org.design_manager_project.models.entity.LabelPrint;
import org.design_manager_project.repositories.LabelPrintRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LabelPrintService extends BaseService<LabelPrint, LabelPrintDTO, LabelPrintFilter, UUID>{

    private final LabelPrintRepository labelPrintRepository;
    private final LabelPrintMapper mapper = LabelPrintMapper.INSTANCE;

    protected LabelPrintService(LabelPrintRepository labelPrintRepository, LabelPrintMapper mapper) {
        super(labelPrintRepository, mapper);
        this.labelPrintRepository = labelPrintRepository;
    }
}
