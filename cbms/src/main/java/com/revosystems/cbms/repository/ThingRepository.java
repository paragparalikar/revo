package com.revosystems.cbms.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.revosystems.cbms.domain.model.Thing;

@Repository
public interface ThingRepository extends CrudRepository<Thing, Long> {

}
