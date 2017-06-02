package com.broker.scheduler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ShiftPlace {
    @NonNull
    private String shiftPlaceId;
    @NonNull
    private String managersName;
    @NonNull
    private String places;
}
