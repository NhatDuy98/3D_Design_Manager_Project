package org.design_manager_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface BaseRepository<E, ID extends String> extends JpaRepository<E, ID> {

}
