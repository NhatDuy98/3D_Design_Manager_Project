package org.design_manager_project.services;

import org.design_manager_project.dtos.label.LabelDTO;
import org.design_manager_project.filters.LabelFilter;
import org.design_manager_project.mappers.LabelMapper;
import org.design_manager_project.models.entity.Label;
import org.design_manager_project.repositories.LabelRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LabelService extends BaseService<Label, LabelDTO, LabelFilter, UUID> {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper = LabelMapper.INSTANCE;
    protected LabelService(LabelRepository labelRepository, LabelMapper labelMapper) {
        super(labelRepository, labelMapper);
        this.labelRepository = labelRepository;
    }

    public Page<LabelDTO> getAllLabelsWithProject(UUID projectId, LabelFilter filter) {
        filter.setProjectId(projectId);

        return labelMapper.convertPageToDTO(labelRepository.findAllWithFilter(filter.getPageable(), filter));
    }
}
