package com.testjobs.sibintek.services;

import com.testjobs.sibintek.DAO.FileEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface FilesService {

    FileEntity save(FileEntity file);

    Page<FileEntity> findAll(Pageable pageable);

    List<FileEntity> findAllById(String id);
    
    Page<FileEntity> findAllByAutor(Pageable pageable, String autor);
    
    Page<FileEntity> findAllByAutorAndByDescriptionContaining(Pageable pageable, String autor, String description);
}
