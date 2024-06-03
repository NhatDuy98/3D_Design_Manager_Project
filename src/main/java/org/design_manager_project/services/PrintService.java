package org.design_manager_project.services;

import org.design_manager_project.dtos.print.PrintDTO;
import org.design_manager_project.filters.PrintFilter;
import org.design_manager_project.mappers.PrintMapper;
import org.design_manager_project.models.entity.Print;
import org.design_manager_project.repositories.PrintRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PrintService extends BaseService<Print, PrintDTO, PrintFilter, UUID> {
    private final PrintRepository printRepository;
    private final PrintMapper printMapper = PrintMapper.INSTANCE;

    protected PrintService(PrintRepository printRepository, PrintMapper printMapper) {
        super(printRepository, printMapper);
        this.printRepository = printRepository;
    }
}
