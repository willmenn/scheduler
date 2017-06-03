package com.broker.scheduler.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class Broker {
    @NonNull
    private String brokerId;
    @NonNull
    private String name;
    @NonNull
    private Preference preference;
}
