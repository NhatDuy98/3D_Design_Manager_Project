package org.design_manager_project.repositories;

import org.design_manager_project.filters.FileFilter;
import org.design_manager_project.models.entity.FileObject;
import org.design_manager_project.models.enums.FileStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface FileRepository extends BaseRepository<FileObject, FileFilter, UUID>{

    @Override
    @Query("""
        SELECT f FROM FileObject f 
        WHERE (:#{#filter.search == NULL || #filter.search.isEmpty()} = TRUE 
            OR LOWER(f.name) LIKE LOWER(CONCAT('%', :#{#filter.search}, '%')) 
            OR LOWER(f.url) LIKE LOWER(CONCAT('%', :#{#filter.search}, '%')))

""")
    Page<FileObject> findAllWithFilter(Pageable pageable, FileFilter filter);

    @Query("""
        SELECT f FROM FileObject f 
        WHERE LOWER(f.url) = LOWER(:name) 

""")
    FileObject findByFileUrl(String name);

    @Query("""
        SELECT f FROM FileObject f 
        WHERE f.status = :status 
         AND f.createdAt < :thresholdTime
""")
    List<FileObject> findAllByStatusAndUploadTime(FileStatus status, Instant thresholdTime);
}
