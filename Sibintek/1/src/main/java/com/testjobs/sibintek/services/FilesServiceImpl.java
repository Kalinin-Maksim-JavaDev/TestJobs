package com.testjobs.sibintek.services;

import com.testjobs.sibintek.DAO.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.testjobs.sibintek.repositories.FilesRepository;
import java.util.ArrayList;
import java.util.List;

@Service
public class FilesServiceImpl implements FilesService {

    @Autowired
    private FilesRepository repository;

    public FileEntity save(FileEntity file) {

        return repository.save(file);
    }

    public Page<FileEntity> findAll(Pageable pageable) {

        return repository.findAll(pageable);
    }

    @Override
    public List<FileEntity> findAllById(String id) {

        List<Integer> ids = new ArrayList();
        ids.add(Integer.valueOf(id));
        return repository.findAllById(ids);
    }

    @Override
    public Page<FileEntity> findAllByAutor(Pageable pageable, String autor) {

        return "".equals(autor) ? repository.findAll(pageable) : repository.findAllByAutor(pageable, autor);
    }

    @Override
    public Page<FileEntity> findAllByAutorAndByDescriptionContaining(Pageable pageable, String autor, String description) {

        if ("".equals(autor) && ("".equals(description))) {
            return repository.findAll(pageable);
        }

        if ("".equals(description)) {
            return findAllByAutor(pageable, autor);
        }
        
        if ("".equals(autor)) {
            return repository.findAllByDescriptionContaining(pageable, description);
        }

        return repository.findAllByAutorAndDescriptionContaining(pageable, autor, description);
    }

}
