package com.broker.scheduler.service.v2.model;

import com.broker.scheduler.model.Broker;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Created by wahrons on 15/01/18.
 */
@Data
@Builder(toBuilder = true)
@ToString
public class Plantao {
    @NotNull
    Map<String, Integer> days;
    Map<String, List<Broker>> scheduled;
    @NotBlank
    String shiftPlaceId;
    @NotBlank
    String managersName;
}
