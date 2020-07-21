/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gridnine.testing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MainTest {

    @Parameter(0)
    public JUnitUserFIlter filter;

    List<Flight> result;
    static ArrayList<JUnitUserFIlter> filters = new ArrayList<>();

    public MainTest() {
    }

    @Parameters
    public static Iterable<Object[]> parameters() {

        Flight expcetedfight = Flight.Builder.createFlight("2101-01-01T12:30:00", "2101-01-01T14:30:00");
        JUnitUserFIlter filter1 = new JUnitUserFIlter(Arrays.asList(
                Flight.Builder.createFlight("2001-01-01T12:30:00", "2001-01-01T12:29:00"),
                expcetedfight),
                Arrays.asList(expcetedfight),
                flight -> !((Flight) flight).getWrongSegments().isEmpty(), "Перелеты, исключая вылеты с датой прилёта раньше даты вылета:");

        LocalDateTime now = LocalDateTime.now();
        JUnitUserFIlter filter2 = new JUnitUserFIlter(Arrays.asList(
                Flight.Builder.createFlight("2001-01-01T12:30:00", "2001-01-01T12:29:00"),
                expcetedfight),
                Arrays.asList(expcetedfight),
                flight -> !((Flight) flight).getSegmentBefore(now).isEmpty(), "Перелеты, исключая вылеты до текущего момента времени:");

        Flight expcetedfight2 = Flight.Builder.createFlight("2001-01-01T12:30:00", "2001-01-01T14:30:00",
                "2001-01-01T16:30:00", "2001-01-01T18:30:00");
        JUnitUserFIlter filter3 = new JUnitUserFIlter(Arrays.asList(
                Flight.Builder.createFlight("2001-01-01T12:30:00", "2001-01-01T14:30:00",
                        "2001-01-01T16:31:00", "2001-01-01T17:30:00"),
                expcetedfight2),
                Arrays.asList(expcetedfight2),
                flight -> ((Flight) flight).getHours() > 2, "Перелеты, исключая более 2-х часовое нахождение на земле:");

        return Arrays.asList(new Object[][]{{filter1}, {filter2}, {filter3}});
    }

    @Test
    public void testFilterStremAPI() {

        System.out.println("testFilterStremAPI");
        result = Main.filterStremAPI(filter, new ArrayList<Flight>(filter.input));
        assertArrayEquals(filter.expResult.toArray(), result.toArray());
    }

    public void testFilterOldStyle() {
        System.out.println("testFilterOldStyle");
        result = Main.filterOldStyle(filter, new ArrayList<Flight>(filter.input));
        assertArrayEquals(filter.expResult.toArray(), result.toArray());
    }

    @After
    public void SayCorrectAnswer() {

        System.out.println(filter.getDescription());
        for (Flight flight : result) {

            System.out.println(flight);
        }
        
        System.out.println("    Исключены");
        
        filter.input.stream().filter(flight -> !result.contains(flight)).forEach(System.out::println);        
    }

    static class JUnitUserFIlter extends UserFIlter {

        private List<Flight> input;
        private List<Flight> expResult;

        public JUnitUserFIlter(List<Flight> input, List<Flight> expResult, Predicate p, String description) {
            super(p, description);
            this.input = input;
            this.expResult = expResult;
        }
    }
}
