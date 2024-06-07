package org.design_manager_project.services;

import jakarta.persistence.EntityNotFoundException;
import org.design_manager_project.dtos.label_print.LabelPrintDTO;
import org.design_manager_project.filters.LabelPrintFilter;
import org.design_manager_project.mappers.LabelMapper;
import org.design_manager_project.mappers.LabelPrintMapper;
import org.design_manager_project.models.entity.Label;
import org.design_manager_project.models.entity.LabelPrint;
import org.design_manager_project.repositories.LabelPrintRepository;
import org.design_manager_project.repositories.LabelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static org.design_manager_project.utils.Constants.DATA_NOT_FOUND;

@Service
public class LabelPrintService extends BaseService<LabelPrint, LabelPrintDTO, LabelPrintFilter, UUID>{

    private final LabelPrintRepository labelPrintRepository;
    private final LabelRepository labelRepository;
    private final LabelPrintMapper mapper = LabelPrintMapper.INSTANCE;
    private final LabelMapper labelMapper = LabelMapper.INSTANCE;

    protected LabelPrintService(LabelPrintRepository labelPrintRepository, LabelPrintMapper mapper, LabelRepository labelRepository) {
        super(labelPrintRepository, mapper);
        this.labelPrintRepository = labelPrintRepository;
        this.labelRepository = labelRepository;
    }

    private LabelPrintDTO createWithExistLabel(LabelPrintDTO dto){
        Label label = labelRepository.findById(dto.getLabel().getId()).orElseThrow(() -> new EntityNotFoundException(DATA_NOT_FOUND));

        dto.setLabel(labelMapper.convertToDTO(label));

        return super.create(dto);
    }

    private LabelPrintDTO createWithoutExistLabel(LabelPrintDTO dto){
        Label label = labelMapper.convertToEntity(dto.getLabel());

        labelRepository.save(label);

        LabelPrint labelPrint = mapper.convertToEntity(dto);
        labelPrint.setLabel(label);
        labelPrintRepository.save(labelPrint);

        return mapper.convertToDTO(labelPrint);
    }

    @Override
    public LabelPrintDTO create(LabelPrintDTO dto) {
        if (dto.getLabel().getId() != null){
            return createWithExistLabel(dto);
        }
        return createWithoutExistLabel(dto);
    }

    @Override
    public List<LabelPrintDTO> createAll(List<LabelPrintDTO> list) {
        return list.stream().map(e -> {
            if (e.getLabel().getId() != null){
                return createWithExistLabel(e);
            }
            return createWithoutExistLabel(e);
        }).toList();
    }
}
