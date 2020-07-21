package com.testjobs.sibintek.repositories;

import com.testjobs.sibintek.DAO.FileEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesRepository extends PagingAndSortingRepository<FileEntity, Integer> {

    @Override
    List<FileEntity> findAllById(Iterable<Integer> ids);
    
    Page<FileEntity> findAllByAutor(Pageable pageable, String autor);
    
    Page<FileEntity> findAllByDescriptionContaining(Pageable pageable, String description);
    
    Page<FileEntity> findAllByAutorAndDescriptionContaining(Pageable pageable, String autor, String description);
    
}
