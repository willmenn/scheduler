package com.broker.scheduler.service.v2.model;

import com.broker.scheduler.model.Broker;
import com.broker.scheduler.service.v3.model.DayEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    String address;
    String places;

    //Preciso destes atributo para a v3
    @NotNull
    Map<DayEnum, Shift> daysV3;

    @NotBlank
    String shiftPlaceId;
    @NotBlank
    String managersName;
    String name;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Shift {
        private Integer morning;
        private Integer afternoon;
        private Integer night;
    }
}
