package org.design_manager_project.repositories;

import org.design_manager_project.filters.LabelPrintFilter;
import org.design_manager_project.models.entity.LabelPrint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LabelPrintRepository extends BaseRepository<LabelPrint, LabelPrintFilter, UUID> {

    @Override
    @Query("""
        SELECT lp FROM LabelPrint lp 
        

""")
    Page<LabelPrint> findAllWithFilter(Pageable pageable, LabelPrintFilter filter);
}
