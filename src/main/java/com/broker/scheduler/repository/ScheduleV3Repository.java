package com.broker.scheduler.repository;

import com.broker.scheduler.service.v3.model.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleV3Repository extends MongoRepository<Schedule, String> {
}
