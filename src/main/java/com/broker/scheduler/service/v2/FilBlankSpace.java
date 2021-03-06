package com.broker.scheduler.service.v2;

import com.broker.scheduler.model.Broker;
import com.broker.scheduler.service.v2.model.Plantao;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;

/**
 * Created by wahrons on 06/10/17.
 */
@Component
public class FilBlankSpace {

    //Precondiction the Plantao Scheduled must have all days already with empty Arays.
    public List<Plantao> toFilBlankSpace(List<Plantao> plantoes, Map<String, List<Broker>> alreadyScheduled,
                                         List<Broker> brokers) {

        plantoes.forEach(plantao -> plantao.getScheduled().keySet().stream()
                .filter(day -> plantao.getScheduled().get(day).size() < plantao.getDays().get(day))
                .forEach(day -> {
                    List<Broker> brokersNotScheduled = onlyBrokersNotScheduledForThisDay(day,
                            brokers, alreadyScheduled);
                    List<Broker> brokersAdded = newArrayList();

                    int positionsLeft = calculatePositionsLeft(plantao, day);

                    for (int i = 0; i < positionsLeft; i++) {
                        if (brokersNotScheduled.size() > i) {
                            plantao.getScheduled().get(day).add(brokersNotScheduled.get(i));
                            brokersAdded.add(brokersNotScheduled.get(i));
                        }
                    }

                    alreadyScheduled.get(day).addAll(brokersAdded);
                }));

        return plantoes;
    }

    private int calculatePositionsLeft(Plantao plantao, String day) {
        return plantao.getDays().get(day) - plantao.getScheduled().get(day).size();
    }

    private List<Broker> onlyBrokersNotScheduledForThisDay(String day, List<Broker> brokers,
                                                           Map<String, List<Broker>> alreadyScheduled) {
        Set<String> ids = alreadyScheduled.get(day)
                .stream()
                .map(Broker::getBrokerId)
                .collect(Collectors.toSet());

        return brokers.stream()
                .filter(broker -> !ids.contains(broker.getBrokerId()))
                .collect(toList());
    }

}
