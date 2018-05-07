package com.broker.scheduler.repository;

import com.broker.scheduler.service.v2.model.ScheduleModelV2;
import com.broker.scheduler.service.v3.model.Schedule;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleV3Repository extends MongoRepository<Schedule, String> {

    List<Schedule> findAllByManagerName(String manager);

    List<Schedule> findByManagerNameOrderByCreatedTimestamp(String managerName, Pageable pageable);
}
