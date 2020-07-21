package com.testjobs.notasoft.repositories;

import com.testjobs.notasoft.DAO.EventclassesEntity;
import com.testjobs.notasoft.DAO.EventsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRepository extends PagingAndSortingRepository<EventsEntity, Long> {

    Page<EventsEntity> findAllByEventclass(EventclassesEntity clazz, Pageable pageable);
}
