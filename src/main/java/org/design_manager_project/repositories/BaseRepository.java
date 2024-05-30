package org.design_manager_project.repositories;

import org.design_manager_project.filters.BaseFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface
BaseRepository<E, FT extends BaseFilter, ID extends UUID> extends JpaRepository<E, ID>, JpaSpecificationExecutor<E> {

    Page<E> findAllWithFilter(Pageable pageable, FT ft);
}
