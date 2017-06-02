package com.broker.scheduler.repository;

import com.broker.scheduler.model.ScheduleModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends MongoRepository<ScheduleModel, String> {
}
