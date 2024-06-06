package org.design_manager_project.repositories;

import org.design_manager_project.filters.VersionFilter;
import org.design_manager_project.models.entity.Version;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VersionRepository extends BaseRepository<Version, VersionFilter, UUID> {

    @Override
    @Query("""
        SELECT v FROM Version v 

""")
    Page<Version> findAllWithFilter(Pageable pageable, VersionFilter versionFilter);

    @Query("""
        SELECT v FROM Version v 
        WHERE v.print.id = :printId

""")
    List<Version> findAllByPrint(UUID printId);

}
