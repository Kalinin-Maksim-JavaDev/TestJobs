package com.testjobs.notasoft.services;

import com.testjobs.notasoft.DAO.EventclassesEntity;
import com.testjobs.notasoft.DAO.EventsEntity;
import com.testjobs.notasoft.repositories.EventsClassesRepository;
import com.testjobs.notasoft.repositories.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventsServiceImpl implements EventsService {

    @Autowired
    private EventsClassesRepository classeRepository;

    @Autowired
    private EventsRepository repository;

    @Override
    public EventclassesEntity getClassSuccess() {

        return getClassByName("Success", true);
    }

    public EventclassesEntity getClassError() {

        return getClassByName("Error", true);
    }

    public EventsEntity save(EventsEntity event) {

        return repository.save(event);
    }

    public EventclassesEntity getClassByName(String clazzName) {

        return classeRepository.findByName(clazzName).stream().findFirst().orElse(null);
    }

    public EventclassesEntity getClassByName(String clazzName, boolean createIsNotExcist) {

        if (!createIsNotExcist) {
            return getClassByName(clazzName);
        }
        return classeRepository.findByName(clazzName).stream().findFirst().orElseGet(() -> classeRepository.save(new EventclassesEntity(clazzName)));
    }

    public Page<EventsEntity> findAll(Pageable pageable) {

        return repository.findAll(pageable);
    }

    @Override
    public Page<EventsEntity> findAllByClass(Pageable pageable, EventclassesEntity clazz) {

        return clazz == null ? repository.findAll(pageable) : repository.findAllByEventclass(clazz, pageable);
    }
}
