package com.testjobs.notasoft.services;

import com.testjobs.notasoft.DAO.EventclassesEntity;
import com.testjobs.notasoft.DAO.EventsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

public interface EventsService {

    EventclassesEntity getClassByName(String clazz);

    EventclassesEntity getClassByName(String clazz, boolean createIsNotExcist);

    EventclassesEntity getClassSuccess();

    EventclassesEntity getClassError();

    EventsEntity save(EventsEntity event);

    Page<EventsEntity> findAll(Pageable pageable);

    Page<EventsEntity> findAllByClass(Pageable pageable, EventclassesEntity clazz);
}
