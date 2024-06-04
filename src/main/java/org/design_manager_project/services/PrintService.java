package org.design_manager_project.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.design_manager_project.dtos.print.PrintDTO;
import org.design_manager_project.filters.PrintFilter;
import org.design_manager_project.mappers.PrintMapper;
import org.design_manager_project.models.entity.Print;
import org.design_manager_project.models.entity.Version;
import org.design_manager_project.repositories.PrintRepository;
import org.design_manager_project.repositories.VersionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PrintService extends BaseService<Print, PrintDTO, PrintFilter, UUID> {

    private final EntityManager entityManager;
    private final PrintRepository printRepository;
    private final VersionRepository versionRepository;
    private static final PrintMapper printMapper = PrintMapper.INSTANCE;

    protected PrintService(PrintRepository printRepository, PrintMapper printMapper, EntityManager entityManager, VersionRepository versionRepository) {
        super(printRepository, printMapper);
        this.printRepository = printRepository;
        this.entityManager = entityManager;
        this.versionRepository = versionRepository;
    }

    private void validateExistPrint(UUID printID){
        printRepository.findById(printID).orElseThrow(() -> new EntityNotFoundException("Not found entity with id: " + printID));
    }

    private Print updateForCreate(PrintDTO dto){
        Print print = printMapper.convertToEntity(dto);

        print.getVersions().forEach(e -> e.setPrint(print));

        return print;
    }


    private Print updatePrint(UUID printID, PrintDTO dto){
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

    public void deleteVersionBulk(PrintDTO dto) {
        validateExistPrint(dto.getId());

        versionRepository.deleteAllById(dto.getImages().stream().map(e -> e.getId()).toList());
    }
}
