package org.design_manager_project.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.design_manager_project.dtos.print.PrintDTO;
import org.design_manager_project.dtos.version.VersionDTO;
import org.design_manager_project.exeptions.BadRequestException;
import org.design_manager_project.filters.PrintFilter;
import org.design_manager_project.mappers.PrintMapper;
import org.design_manager_project.models.entity.Card;
import org.design_manager_project.models.entity.Print;
import org.design_manager_project.models.entity.Version;
import org.design_manager_project.repositories.CardRepository;
import org.design_manager_project.repositories.PrintRepository;
import org.design_manager_project.repositories.VersionRepository;
import org.design_manager_project.utils.Constants;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PrintService extends BaseService<Print, PrintDTO, PrintFilter, UUID> {

    private final EntityManager entityManager;
    private final PrintRepository printRepository;
    private final VersionRepository versionRepository;
    private final CardRepository cardRepository;
    private static final PrintMapper printMapper = PrintMapper.INSTANCE;

    protected PrintService(PrintRepository printRepository, PrintMapper printMapper, EntityManager entityManager, VersionRepository versionRepository, CardRepository cardRepository) {
        super(printRepository, printMapper);
        this.printRepository = printRepository;
        this.entityManager = entityManager;
        this.versionRepository = versionRepository;
        this.cardRepository = cardRepository;
    }

    private void validateExistPrint(UUID printID){
        printRepository.findById(printID).orElseThrow(() -> new EntityNotFoundException("Not found entity with id: " + printID));
    }

    private void validateCardDeleted(UUID cardID){
        Card card = cardRepository.findById(cardID).orElseThrow(() -> new EntityNotFoundException("Not found entity with id: " + cardID));

        if (card != null && card.getDeletedAt() != null){
            throw new BadRequestException(Constants.OBJECT_DELETED);
        }
    }

    private Print updateForCreate(PrintDTO dto){
        if (dto.getCard() != null){
            validateCardDeleted(dto.getCard().getId());
        }

        Print print = printMapper.convertToEntity(dto);

        print.getVersions().forEach(e -> e.setPrint(print));

        return print;
    }


    private Print updatePrint(UUID printID, PrintDTO dto){
        if (dto.getCard() != null){
            validateCardDeleted(dto.getCard().getId());
        }

        Print printRepo = printRepository.findById(printID).orElseThrow(() -> new EntityNotFoundException("Not found entity with id: " + printID));

        Print print = printMapper.convertToEntity(dto);

        Print newPrint = new Print();

        newPrint.setId(printID);

        List<Version> versions = print.getVersions().stream().map(e -> {
            e.setPrint(newPrint);

            return e;
        }).toList();

        versionRepository.saveAllAndFlush(versions);

        entityManager.refresh(printRepo);

        return printRepo;
    }

    @Override
    @Transactional
    public PrintDTO create(PrintDTO dto) {
        Print print = updateForCreate(dto);

        printRepository.saveAndFlush(print);

        entityManager.refresh(print);

        return printMapper.convertToDTO(print);
    }

    @Override
    @Transactional
    public List<PrintDTO> createAll(List<PrintDTO> list) {
        List<Print> prints = list.stream().map(e -> updateForCreate(e)).toList();

        printRepository.saveAllAndFlush(prints);

        prints.forEach(entityManager::refresh);

        return printMapper.convertListToDTO(prints);

    }

    @Override
    @Transactional
    public PrintDTO update(UUID uuid, PrintDTO dto) {
        Print print = updatePrint(uuid, dto);
        return printMapper.convertToDTO(print);
    }

    @Override
    @Transactional
    public List<PrintDTO> updateAll(List<PrintDTO> rqList) {
        List<Print> prints = rqList.stream().map(e -> updatePrint(e.getId(), e)).toList();
        return printMapper.convertListToDTO(prints);
    }

    public void deleteVersion(UUID printID, UUID versionID) {
        validateExistPrint(printID);

        versionRepository.deleteById(versionID);
    }

    public void deleteVersionBulk(UUID printID, List<VersionDTO> dto) {
        validateExistPrint(printID);

        versionRepository.deleteAllById(dto.stream().map(e -> e.getId()).toList());
    }

    public Page<PrintDTO> getAllPrintsWithCard(UUID cardID, PrintFilter filter) {
        filter.setCardId(cardID);

        return printMapper.convertPageToDTO(printRepository.findAllWithFilter(filter.getPageable(), filter));
    }

    public Page<PrintDTO> getAllPrintsWithProject(UUID projectId, PrintFilter filter) {
        filter.setProjectId(projectId);

        return printMapper.convertPageToDTO(printRepository.findAllWithFilter(filter.getPageable(), filter));
    }
}
