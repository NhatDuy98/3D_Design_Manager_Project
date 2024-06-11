package org.design_manager_project.repositories;

import org.design_manager_project.filters.FileFilter;
import org.design_manager_project.models.entity.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface FileRepository extends BaseRepository<File, FileFilter, UUID>{

    @Override
    @Query("""
        SELECT f FROM File f 
        WHERE (:#{#filter.search == NULL || #filter.search.isEmpty()} = TRUE 
            OR LOWER(f.fileName) LIKE LOWER(CONCAT('%', :#{#filter.search}, '%')) 
            OR LOWER(f.fileUrl) LIKE LOWER(CONCAT('%', :#{#filter.search}, '%')))

""")
    Page<File> findAllWithFilter(Pageable pageable, FileFilter filter);

    @Query("""
        SELECT f FROM File f 
        WHERE LOWER(f.fileUrl) = LOWER(:name) 

""")
    File findByFileUrl(String name);

    @Query("""
        SELECT f FROM File f 
        WHERE f.status = :status 
         AND f.uploadTime < :thresholdTime
""")
    List<File> findAllByStatusAndUploadTime(String status, Instant thresholdTime);
}
