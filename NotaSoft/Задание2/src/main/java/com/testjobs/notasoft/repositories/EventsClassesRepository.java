package com.testjobs.notasoft.repositories;

import com.testjobs.notasoft.DAO.EventclassesEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventsClassesRepository extends PagingAndSortingRepository<EventclassesEntity, Integer> {

    List<EventclassesEntity> findByName(String name);
}
