package org.design_manager_project.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;
import org.design_manager_project.dtos.print.PrintDTO;
import org.design_manager_project.dtos.version.VersionDTO;
import org.design_manager_project.exeptions.BadRequestException;
import org.design_manager_project.filters.PrintFilter;
import org.design_manager_project.mappers.PrintMapper;
import org.design_manager_project.models.entity.Card;
import org.design_manager_project.models.entity.FileObject;
import org.design_manager_project.models.entity.Print;
import org.design_manager_project.models.entity.Version;
import org.design_manager_project.repositories.CardRepository;
import org.design_manager_project.repositories.FileRepository;
import org.design_manager_project.repositories.PrintRepository;
import org.design_manager_project.repositories.VersionRepository;
import org.design_manager_project.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.design_manager_project.utils.Constants.*;

@Service
public class PrintService extends BaseService<Print, PrintDTO, PrintFilter, UUID> {
    private final PrintRepository printRepository;
    private final VersionRepository versionRepository;
    private final CardRepository cardRepository;
    private static final PrintMapper printMapper = PrintMapper.INSTANCE;
    private final FileService fileService;
    private final FileRepository fileRepository;

    @Autowired
    protected PrintService(PrintRepository printRepository, PrintMapper printMapper, VersionRepository versionRepository, CardRepository cardRepository, FileService fileService, FileRepository fileRepository) {
        super(printRepository, printMapper);
        this.printRepository = printRepository;
        this.versionRepository = versionRepository;
        this.cardRepository = cardRepository;
        this.fileService = fileService;
        this.fileRepository = fileRepository;
    }

    private Version saveVersion(Print print){
        Version version = new Version();
        version.setPrint(print);
        version.setImage(print.getImage());

        versionRepository.save(version);

        return version;
    }

    private List<Version> saveListVersions(Print print, PrintDTO dto){
        List<Version> versions = dto.getImages().stream().map(e -> {
            Version version = new Version();

            version.setPrint(print);
            version.setImage(e.getImage());

            return version;
        }).toList();

        versionRepository.saveAll(versions);

        return versions;
    }
    private void validateExistPrint(UUID printId){
        printRepository.findById(printId).orElseThrow(() -> new EntityNotFoundException("Not found entity with id: " + printId));
    }

    private void validateCardDeleted(UUID cardId){
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new EntityNotFoundException("Not found entity with id: " + cardId));

        if (card != null && card.getDeletedAt() != null){
            throw new BadRequestException(Constants.OBJECT_DELETED);
        }
    }

    private Print updateForCreate(PrintDTO dto){
        if (!dto.getImages().isEmpty() && dto.getImage() != null){
            throw new BadRequestException(BAD_REQUEST);
        }

        if (!dto.getImages().isEmpty()){
            fileService.updateFileStatusBulk(dto.getImages().stream().map(e -> e.getImage()).toList());

            Print print = printMapper.convertToEntity(dto);

            print.setImage(dto.getImages().get(dto.getImages().size() - 1).getImage());

            printRepository.save(print);

            List<Version> versions = saveListVersions(print, dto);

            print.setVersions(versions);

            return print;
        }
        fileService.updateFileActive(dto.getImage());

        Print print = printMapper.convertToEntity(dto);

        printRepository.save(print);

        Version version = saveVersion(print);

        print.getVersions().add(version);

        return print;
    }


    private Print updatePrint(UUID printId, PrintDTO dto){
        if (!dto.getImages().isEmpty() && dto.getImage() != null){
            throw new BadRequestException(BAD_REQUEST);
        }

        Print printRepo = printRepository.findById(printId).orElseThrow(() -> new EntityNotFoundException(DATA_NOT_FOUND));

        if (!dto.getImages().isEmpty()){
            fileService.updateFileStatusBulk(dto.getImages().stream().map(e -> e.getImage()).toList());

            Print print = printMapper.updateEntity(dto, printRepo);

            print.setImage(dto.getImages().get(dto.getImages().size() - 1).getImage());

            printRepository.save(print);

            List<Version> versions = saveListVersions(print, dto);

            Set<Version> uniqueVersions = new HashSet<>(print.getVersions());
            uniqueVersions.addAll(versions);

            print.setVersions(new ArrayList<>(uniqueVersions));

            return print;
        }
        fileService.updateFileActive(dto.getImage());

        var print = printMapper.updateEntity(dto, printRepo);

        printRepository.save(print);

        fileService.updateFileActive(print.getImage());

        var version = saveVersion(print);

        Set<Version> uniqueVersions = new HashSet<>(print.getVersions());
        uniqueVersions.add(version);

        print.setVersions(new ArrayList<>(uniqueVersions));

        return print;
    }

    @Override
    public PrintDTO create(PrintDTO dto) {
        if (dto.getCard() != null){
            validateCardDeleted(dto.getCard().getId());
        }

        var print = updateForCreate(dto);

        return printMapper.convertToDTO(print);
    }

    @Override
    public List<PrintDTO> createAll(List<PrintDTO> list) {
        List<Print> prints = list.stream().map(e -> {
            if (e.getCard() != null){
                validateCardDeleted(e.getCard().getId());
            }

            return updateForCreate(e);
        }).toList();

        return printMapper.convertListToDTO(prints);

    }

    @Override
    public PrintDTO update(UUID uuid, PrintDTO dto) {
        if (dto.getCard() != null){
            validateCardDeleted(dto.getCard().getId());
        }

        Print print = updatePrint(uuid, dto);
        return printMapper.convertToDTO(print);
    }

    @Override
    public List<PrintDTO> updateAll(List<PrintDTO> rqList) {
        List<Print> prints = rqList.stream().map(e -> {
            if (e.getCard() != null){
                validateCardDeleted(e.getCard().getId());
            }

            return updatePrint(e.getId(), e);
        }).toList();
        return printMapper.convertListToDTO(prints);
    }

    public void deleteVersion(UUID printId, UUID versionId) {
        validateExistPrint(printId);

        versionRepository.deleteById(versionId);

        List<Version> versions = versionRepository.findAllByPrint(printId);

        if (versions.isEmpty()){
            printRepository.deleteById(printId);
        }
    }

    public void deleteVersionBulk(UUID printId, List<VersionDTO> dto) {
        validateExistPrint(printId);

        versionRepository.deleteAllById(dto.stream().map(e -> e.getId()).toList());

        List<Version> versions = versionRepository.findAllByPrint(printId);

        if (versions.isEmpty()){
            printRepository.deleteById(printId);
        }
    }

    public Page<PrintDTO> getAllPrintsWithCard(UUID cardId, PrintFilter filter) {
        filter.setCardId(cardId);

        return printMapper.convertPageToDTO(printRepository.findAllWithFilter(filter.getPageable(), filter));
    }

    public Page<PrintDTO> getAllPrintsWithProject(UUID projectId, PrintFilter filter) {
        filter.setProjectId(projectId);

        return printMapper.convertPageToDTO(printRepository.findAllWithFilter(filter.getPageable(), filter));
    }

    public PrintDTO updateLatestVersion(UUID printId, UUID versionId) {
        var print = printRepository.findById(printId).orElseThrow(() -> new EntityNotFoundException(DATA_NOT_FOUND));

        boolean found = false;

        for (var e : print.getVersions()) {
            if (e.getId().equals(versionId)) {
                found = true;
                if (e.getImage().equals(print.getImage())) {
                    throw new BadRequestException(BAD_REQUEST);
                }
                print.setImage(e.getImage());
                break;
            }
        }

        if (!found) {
            throw new BadRequestException(BAD_REQUEST);
        }

        return printMapper.convertToDTO(print);
    }

    @SneakyThrows
    @Override
    public void deleteById(UUID uuid){
        var print = printRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException(OBJECT_DELETED));

        var file = fileRepository.findByFileUrl(print.getImage());

        fileService.delete(file.getId());

        super.deleteById(uuid);
    }

    @SneakyThrows
    @Override
    public void deleteAll(List<UUID> uuids) {
        List<Print> prints = printRepository.findAllById(uuids);

        List<FileObject> fileObjects = prints.stream().map(e -> fileRepository.findByFileUrl(e.getImage())).toList();

        fileService.deleteBulk(fileObjects.stream().map(e -> e.getId()).toList());

        super.deleteAll(uuids);
    }
}
