package com.broker.scheduler.repository;

import com.broker.scheduler.service.v2.model.ScheduleModelV2;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by wahrons on 15/01/18.
 */
public interface ScheduleV2Repository extends MongoRepository<ScheduleModelV2, String> {
}
