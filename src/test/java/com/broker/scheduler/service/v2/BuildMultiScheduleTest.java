package com.broker.scheduler.service.v2;

import com.broker.scheduler.model.Broker;
import com.broker.scheduler.model.Preference;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.google.common.collect.Maps.newHashMap;
import static org.assertj.core.util.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

/**
 * Created by wahrons on 06/10/17.
 */
public class BuildMultiScheduleTest {

    @Test
    public void shouldBeAbleToScheduleOneShiftWithONeDayWithOneBroker() throws Exception {
        //Given
        //Given SUN with one position
        Map<String, Integer> days = newHashMap();
        days.put("SUN", 1);
        BuildMultiSchedule.Plantao plantao = BuildMultiSchedule.Plantao.builder().days(days).build();

        //Given One Broker with SUN as preference day
        List<Broker> brokers = newArrayList();
        Broker broker = buildBroker("William", "SUN");
        brokers.add(broker);

        BuildMultiSchedule buildMultiSchedule = new BuildMultiSchedule();
        List<BuildMultiSchedule.Plantao> schedule = buildMultiSchedule.build(newArrayList(plantao), brokers)
                .getPlantaos();

        assertEquals(1,schedule.size()); //Only one shift place
        assertEquals(7,schedule.get(0).getScheduled().keySet().size()); //Only has SUN
        assertEquals(1,schedule.get(0).getScheduled().get("SUN").size()); //Only has ONE Broker
        assertEquals(broker,schedule.get(0).getScheduled().get("SUN").get(0)); //Only has ONE Broker
    }

    @Test
    public void shouldBeAbleToScheduleOneShiftWithOneDayWithOneBrokerGivenTwoBrokers() throws Exception {
        //Given
        //Given SUN with one position
        Map<String, Integer> days = newHashMap();
        days.put("SUN", 1);
        BuildMultiSchedule.Plantao plantao = BuildMultiSchedule.Plantao.builder().days(days).build();

        //Given One Broker with SUN as preference day
        List<Broker> brokers = newArrayList();
        Broker broker = buildBroker("William", "SUN");
        brokers.add(broker);
        brokers.add(buildBroker("Ruth", "SUN"));

        BuildMultiSchedule buildMultiSchedule = new BuildMultiSchedule();
        List<BuildMultiSchedule.Plantao> schedule = buildMultiSchedule.build(newArrayList(plantao), brokers)
                .getPlantaos();

        assertEquals(1, schedule.size()); //Only one shift place
        assertEquals(7, schedule.get(0).getScheduled().keySet().size()); //Only has SUN
        assertEquals(1, schedule.get(0).getScheduled().get("SUN").size()); //Only has ONE Broker
        assertEquals(broker, schedule.get(0).getScheduled().get("SUN").get(0)); //Only has ONE Broker
    }

    //This test is without fulfilling brokers algorithm
    @Test
    public void shouldBeAbleToScheduleOneShiftWithTwoDaysWithOneBrokerOnSUNGivenTwoBrokers() throws Exception {
        //Given
        //Given SUN  with one position and MON with one
        Map<String, Integer> days = newHashMap();
        days.put("SUN", 1);
        days.put("MON", 1);
        BuildMultiSchedule.Plantao plantao = BuildMultiSchedule.Plantao.builder().days(days).build();

        //Given One Broker with SUN as preference day
        List<Broker> brokers = newArrayList();
        Broker broker = buildBroker("William", "SUN");
        brokers.add(broker);
        brokers.add(buildBroker("Ruth", "SUN"));

        BuildMultiSchedule buildMultiSchedule = new BuildMultiSchedule();
        List<BuildMultiSchedule.Plantao> schedule = buildMultiSchedule.build(newArrayList(plantao), brokers)
                .getPlantaos();

        assertEquals(1, schedule.size()); //Only one shift place
        assertEquals(7, schedule.get(0).getScheduled().keySet().size()); //Only has SUN
        assertEquals(1, schedule.get(0).getScheduled().get("SUN").size()); //Only has ONE Broker
        assertEquals(broker, schedule.get(0).getScheduled().get("SUN").get(0)); //Only has ONE Broker
    }


    @Test
    public void shouldBeAbleToScheduleOneShiftWithTwoDaysWithOneBrokerOnSUNAnotherOneOnMONGivenTwoBrokers() throws Exception {
        //Given
        //Given SUN  with one position and MON with one
        Map<String, Integer> days = newHashMap();
        days.put("SUN", 1);
        days.put("MON", 1);
        BuildMultiSchedule.Plantao plantao = BuildMultiSchedule.Plantao.builder().days(days).build();

        //Given One Broker with SUN as preference day
        List<Broker> brokers = newArrayList();
        Broker brokerSUN = buildBroker("William", "SUN");
        brokers.add(brokerSUN);
        Broker brokerMON = buildBroker("Ruth", "MON");
        brokers.add(brokerMON);

        BuildMultiSchedule buildMultiSchedule = new BuildMultiSchedule();
        List<BuildMultiSchedule.Plantao> schedule = buildMultiSchedule.build(newArrayList(plantao), brokers)
                .getPlantaos();

        assertEquals(1, schedule.size()); //Only one shift place
        assertEquals(7, schedule.get(0).getScheduled().keySet().size()); //Only has SUN
        assertEquals(1, schedule.get(0).getScheduled().get("SUN").size()); //Only has ONE Broker on SUN
        assertEquals(brokerSUN, schedule.get(0).getScheduled().get("SUN").get(0)); //Only has ONE Broker
        assertEquals(1, schedule.get(0).getScheduled().get("MON").size()); //Only has ONE Broker on MON
        assertEquals(brokerMON, schedule.get(0).getScheduled().get("MON").get(0)); //Only has ONE Broker
    }

    @Test
    public void shouldBeAbleToScheduleOneShiftWithTwoDaysWithTwoBrokersOnSUNanotherOneOnMONGivenTwoBrokers() throws Exception {
        //Given
        //Given SUN  with two positions and MON with one
        Map<String, Integer> days = newHashMap();
        days.put("SUN", 2);
        days.put("MON", 1);
        BuildMultiSchedule.Plantao plantao = BuildMultiSchedule.Plantao.builder().days(days).build();

        //Given One Broker with SUN as preference day
        List<Broker> brokers = newArrayList();
        Broker brokerSUN1 = buildBroker("William", "SUN");
        brokers.add(brokerSUN1);
        Broker brokerSUN2 = buildBroker("Rafael", "SUN");
        brokers.add(brokerSUN2);
        Broker brokerMON = buildBroker("Ruth", "MON");
        brokers.add(brokerMON);

        BuildMultiSchedule buildMultiSchedule = new BuildMultiSchedule();
        List<BuildMultiSchedule.Plantao> schedule = buildMultiSchedule.build(newArrayList(plantao), brokers)
                .getPlantaos();

        assertEquals(1, schedule.size()); //Only one shift place
        assertEquals(7, schedule.get(0).getScheduled().keySet().size()); //Only has SUN
        assertEquals(2, schedule.get(0).getScheduled().get("SUN").size()); //Only has ONE Broker on SUN
        assertEquals(brokerSUN1, schedule.get(0).getScheduled().get("SUN").get(0)); //Should be brokerSUN1
        assertEquals(brokerSUN2, schedule.get(0).getScheduled().get("SUN").get(1)); //Should be brokerSUN2
        assertEquals(1, schedule.get(0).getScheduled().get("MON").size()); //Only has ONE Broker on MON
        assertEquals(brokerMON, schedule.get(0).getScheduled().get("MON").get(0)); //Only has ONE Broker
    }

    @Test
    public void shouldBeAbleToScheduleOneShiftWithOneDayWithOneBrokerAndOtherShiftEmpty() throws Exception {
        //Given
        //Given SUN with one position
        Map<String, Integer> days = newHashMap();
        days.put("SUN", 1);
        BuildMultiSchedule.Plantao plantao1 = BuildMultiSchedule.Plantao.builder().days(days).build();
        BuildMultiSchedule.Plantao plantao2 = BuildMultiSchedule.Plantao.builder().days(days).build();
        //Given One Broker with SUN as preference day
        List<Broker> brokers = newArrayList();
        Broker brokerSUN1 = buildBroker("William", "SUN");
        brokers.add(brokerSUN1);

        BuildMultiSchedule buildMultiSchedule = new BuildMultiSchedule();
        List<BuildMultiSchedule.Plantao> schedule = buildMultiSchedule.build(newArrayList(plantao1,plantao2), brokers)
                .getPlantaos();

        assertEquals(2, schedule.size()); //Only one shift place
        assertEquals(7, schedule.get(0).getScheduled().keySet().size()); //Only has SUN
        assertEquals(1, schedule.get(0).getScheduled().get("SUN").size()); //Only has ONE Broker on SUN
        assertEquals(brokerSUN1, schedule.get(0).getScheduled().get("SUN").get(0)); //Should be brokerSUN1
        assertEquals(7, schedule.get(1).getScheduled().keySet().size()); //Could not schedule given no broker available
    }

    private Broker buildBroker(String name, String preferenceDay) {
        return Broker.builder()
                .brokerId(UUID.randomUUID().toString())
                .name(name)
                .preference(Preference.builder().weekDay(preferenceDay).build())
                .build();
    }
}