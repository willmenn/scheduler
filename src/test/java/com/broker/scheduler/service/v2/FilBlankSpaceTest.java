package com.broker.scheduler.service.v2;

import com.broker.scheduler.model.Broker;
import com.broker.scheduler.model.Preference;
import com.broker.scheduler.service.v2.BuildMultiSchedule;
import com.broker.scheduler.service.v2.FilBlankSpace;
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
    public void shouldFilBlankSpaceForSUNForOneBrokerWithNoScheduled() throws Exception {
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

        assertEquals(1, plantaos.get(0).getScheduled().size());
    }

    @Test
    public void shouldFilBlankSpaceForSUNForOneBrokerWithAlreadyOneBroker() throws Exception {
        //Given
        //Given Two Brokers with MON And SUN as preference day
        List<Broker> brokers = newArrayList();
        Broker broker1 = buildBroker("William", "MON");
        brokers.add(broker1);
        Broker broker2 = buildBroker("Ruth", "SUN");
        brokers.add(broker2);

        //Given SUN with one position With none brokers scheduled
        Map<String, Integer> days = newHashMap();
        days.put("SUN", 2);
        Map<String, List<Broker>> scheduled = newHashMap();
        scheduled.put("SUN", newArrayList(broker2));
        BuildMultiSchedule.Plantao plantao = BuildMultiSchedule.Plantao.builder()
                .days(days)
                .scheduled(scheduled).build();
        ArrayList<BuildMultiSchedule.Plantao> plantoes = newArrayList(plantao);

        //Given broker2 is alreadyScheduled
        Map<String, List<Broker>> alreadyScheduled = buildEmptyAlreadyScheduled();
        alreadyScheduled.put("SUN", newArrayList(broker2));

        FilBlankSpace filBlankSpace = new FilBlankSpace();
        List<BuildMultiSchedule.Plantao> plantaos = filBlankSpace.toFilBlankSpace(plantoes,
                alreadyScheduled, brokers);

        assertEquals(1, plantaos.get(0).getScheduled().size());
        assertEquals(2, plantaos.get(0).getScheduled().get("SUN").size());
    }

    @Test
    public void shouldFilBlankSpaceForSUNForOneBrokerWithAlreadyOneBrokerAnd2BrokersAvailable() throws Exception {
        //Given
        //Given Two Brokers with MON And SUN as preference day
        List<Broker> brokers = newArrayList();
        Broker broker1 = buildBroker("William", "MON");
        brokers.add(broker1);
        Broker broker2 = buildBroker("Ruth", "SUN");
        brokers.add(broker2);
        Broker broker3 = buildBroker("Rafael", "TUE");
        brokers.add(broker3);

        //Given SUN with one position With none brokers scheduled
        Map<String, Integer> days = newHashMap();
        days.put("SUN", 2);
        Map<String, List<Broker>> scheduled = newHashMap();
        scheduled.put("SUN", newArrayList(broker2));
        BuildMultiSchedule.Plantao plantao = BuildMultiSchedule.Plantao.builder()
                .days(days)
                .scheduled(scheduled).build();
        ArrayList<BuildMultiSchedule.Plantao> plantoes = newArrayList(plantao);

        //Given broker2 is alreadyScheduled
        Map<String, List<Broker>> alreadyScheduled = buildEmptyAlreadyScheduled();
        alreadyScheduled.put("SUN", newArrayList(broker2));

        FilBlankSpace filBlankSpace = new FilBlankSpace();
        List<BuildMultiSchedule.Plantao> plantaos = filBlankSpace.toFilBlankSpace(plantoes,
                alreadyScheduled, brokers);

        assertEquals(1, plantaos.get(0).getScheduled().size());
        assertEquals(2, plantaos.get(0).getScheduled().get("SUN").size());
    }

    @Test
    public void shouldFilBlankSpaceForSUNForOneBrokerWithAlreadyOneBrokerAnd2BrokersAvailableWithTwodays() throws Exception {
        //Given
        //Given Two Brokers with MON And SUN as preference day
        List<Broker> brokers = newArrayList();
        Broker broker1 = buildBroker("William", "MON");
        brokers.add(broker1);
        Broker broker2 = buildBroker("Ruth", "SUN");
        brokers.add(broker2);
        Broker broker3 = buildBroker("Rafael", "TUE");
        brokers.add(broker3);

        //Given SUN with one position With none brokers scheduled
        Map<String, Integer> days = newHashMap();
        days.put("SUN", 2);
        days.put("MON", 2);
        Map<String, List<Broker>> scheduled = newHashMap();
        scheduled.put("SUN", newArrayList(broker2));
        scheduled.put("MON", newArrayList());
        BuildMultiSchedule.Plantao plantao = BuildMultiSchedule.Plantao.builder()
                .days(days)
                .scheduled(scheduled).build();
        ArrayList<BuildMultiSchedule.Plantao> plantoes = newArrayList(plantao);

        //Given broker2 is alreadyScheduled
        Map<String, List<Broker>> alreadyScheduled = buildEmptyAlreadyScheduled();
        alreadyScheduled.put("SUN", newArrayList(broker2));

        FilBlankSpace filBlankSpace = new FilBlankSpace();
        List<BuildMultiSchedule.Plantao> plantaos = filBlankSpace.toFilBlankSpace(plantoes,
                alreadyScheduled, brokers);

        assertEquals(2, plantaos.get(0).getScheduled().size());
        assertEquals(2, plantaos.get(0).getScheduled().get("SUN").size());
        assertEquals(2, plantaos.get(0).getScheduled().get("MON").size());
    }

    @Test
    public void shouldFilBlankSpaceForSUNForOneBrokerWithAlreadyOneBrokerAnd2BrokersAvailableWithTwoShiftPlaceWithSameDay() throws Exception {
        //Given
        //Given Two Brokers with MON And SUN as preference day
        List<Broker> brokers = newArrayList();
        Broker broker1 = buildBroker("William", "MON");
        brokers.add(broker1);
        Broker broker2 = buildBroker("Ruth", "SUN");
        brokers.add(broker2);
        Broker broker3 = buildBroker("Rafael", "TUE");
        brokers.add(broker3);

        //Given SUN with one position With none brokers scheduled
        Map<String, Integer> days = newHashMap();
        days.put("SUN", 2);
        Map<String, List<Broker>> scheduled = newHashMap();
        scheduled.put("SUN", newArrayList(broker2));
        BuildMultiSchedule.Plantao plantao1 = BuildMultiSchedule.Plantao.builder()
                .days(days)
                .scheduled(scheduled).build();

        Map<String, List<Broker>> scheduled2 = newHashMap();
        scheduled2.put("SUN", newArrayList());
        BuildMultiSchedule.Plantao plantao2 = BuildMultiSchedule.Plantao.builder()
                .days(days)
                .scheduled(scheduled2).build();
        ArrayList<BuildMultiSchedule.Plantao> plantoes = newArrayList(plantao1,plantao2);

        //Given broker2 is alreadyScheduled
        Map<String, List<Broker>> alreadyScheduled = buildEmptyAlreadyScheduled();
        alreadyScheduled.put("SUN", newArrayList(broker2));

        FilBlankSpace filBlankSpace = new FilBlankSpace();
        List<BuildMultiSchedule.Plantao> plantaos = filBlankSpace.toFilBlankSpace(plantoes,
                alreadyScheduled, brokers);

        assertEquals(1, plantaos.get(0).getScheduled().size());
        assertEquals(2, plantaos.get(0).getScheduled().get("SUN").size());
        assertEquals(1, plantaos.get(1).getScheduled().size());
        assertEquals(1, plantaos.get(1).getScheduled().get("SUN").size());
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