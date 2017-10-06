package com.broker.scheduler.service;

import com.broker.scheduler.model.Broker;
import com.broker.scheduler.model.Preference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.google.common.collect.Maps.newHashMap;
import static org.assertj.core.util.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

/**
 * Created by wahrons on 06/10/17.
 */
public class FilBlankSpaceTest {


    @Test
    public void shouldFilBlankSpaceForSUNForOneBrokerWithNoPreferences() throws Exception {
        //Given
        //Given SUN with one position With none brokers scheduled
        Map<String, Integer> days = newHashMap();
        days.put("SUN", 1);
        Map<String, List<Broker>> scheduled = newHashMap();
        scheduled.put("SUN", newArrayList());
        BuildMultiSchedule.Plantao plantao = BuildMultiSchedule.Plantao.builder()
                .days(days)
                .scheduled(scheduled).build();
        ArrayList<BuildMultiSchedule.Plantao> plantoes = newArrayList(plantao);

        //Given One Broker with MON as preference day
        List<Broker> brokers = newArrayList();
        Broker broker = buildBroker("William", "MON");
        brokers.add(broker);

        FilBlankSpace filBlankSpace = new FilBlankSpace();
        List<BuildMultiSchedule.Plantao> plantaos = filBlankSpace.toFilBlankSpace(plantoes,
                buildEmptyAlreadyScheduled(), brokers);
        assertEquals(plantaos.get(0).getScheduled().size(), 1);
    }

    private Map<String, List<Broker>> buildEmptyAlreadyScheduled() {
        Map<String, List<Broker>> alreadyScheduled = Maps.newHashMap();
        alreadyScheduled.put("SUN", Lists.newArrayList());
        alreadyScheduled.put("MON", Lists.newArrayList());
        alreadyScheduled.put("TUE", Lists.newArrayList());
        alreadyScheduled.put("WED", Lists.newArrayList());
        alreadyScheduled.put("THU", Lists.newArrayList());
        alreadyScheduled.put("FRI", Lists.newArrayList());
        alreadyScheduled.put("SAT", Lists.newArrayList());
        return alreadyScheduled;
    }

    private Broker buildBroker(String name, String preferenceDay) {
        return Broker.builder()
                .brokerId(UUID.randomUUID().toString())
                .name(name)
                .preference(Preference.builder().weekDay(preferenceDay).build())
                .build();
    }
}