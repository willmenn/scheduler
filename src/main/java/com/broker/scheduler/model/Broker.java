package com.broker.scheduler.model;

import com.broker.scheduler.service.v3.score.ScoreFunction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class Broker {
    @NonNull
    private String brokerId;
    @NonNull
    private String name;
    @NonNull
    private Preference preference;

    private String manager;

    private List<String> daysScheduled;

    private Map<ScoreFunction, List<String>> constraints;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Broker broker = (Broker) o;

        return name != null ? name.equals(broker.name) : broker.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
