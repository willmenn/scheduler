package com.broker.scheduler.service.v2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by wahrons on 22/04/18.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ShiftPlaceDay {
    private String plantaoName;
    private String plantaoId;
    private String day;
}
