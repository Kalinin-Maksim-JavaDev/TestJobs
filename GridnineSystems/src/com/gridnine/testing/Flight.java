/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

public interface Flight {

    interface ISegment {

    }

    public List<ISegment> getSegmentBefore(final LocalDateTime date);

    public List<ISegment> getWrongSegments();

    public long getHours();

    static class Builder {

        public static List<FlightImpl> createFlights() {
            LocalDateTime threeDaysFromNow = LocalDateTime.now().plusDays(3);
            return Arrays.asList(
                    //A normal flight with two hour duration
                    createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2)),
                    //A normal multi segment flight
                    createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2),
                            threeDaysFromNow.plusHours(3), threeDaysFromNow.plusHours(5)),
                    //A flight departing in the past
                    createFlight(threeDaysFromNow.minusDays(6), threeDaysFromNow),
                    //A flight that departs before it arrives
                    createFlight(threeDaysFromNow, threeDaysFromNow.minusHours(6)),
                    //A flight with more than two hours ground time
                    createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2),
                            threeDaysFromNow.plusHours(5), threeDaysFromNow.plusHours(6)),
                    //Another flight with more than two hours ground time
                    createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2),
                            threeDaysFromNow.plusHours(3), threeDaysFromNow.plusHours(4),
                            threeDaysFromNow.plusHours(6), threeDaysFromNow.plusHours(7)));
        }

        public static FlightImpl createFlight(final LocalDateTime... dates) {
            if ((dates.length % 2) != 0) {
                throw new IllegalArgumentException(
                        "you must pass an even number of dates");
            }
            List<FlightImpl.Segment> segments = new ArrayList<>(dates.length / 2);
            for (int i = 0; i < (dates.length - 1); i += 2) {
                segments.add(new FlightImpl.Segment(dates[i], dates[i + 1]));
            }
            return new FlightImpl(segments);
        }

        public static FlightImpl createFlight(final String... datesAsString) {

            if ((datesAsString.length % 2) != 0) {
                throw new IllegalArgumentException(
                        "you must pass an even number of dates");
            }
            List<FlightImpl.Segment> segments = new ArrayList<>(datesAsString.length / 2);
            for (int i = 0; i < (datesAsString.length - 1); i += 2) {
                segments.add(new FlightImpl.Segment(LocalDateTime.parse(datesAsString[i]), LocalDateTime.parse(datesAsString[i + 1])));
            }
            return new FlightImpl(segments);
        }
    }
}

/**
 * Factory class to get sample list of flights.
 */
/**
 * Bean that represents a flight.
 */
class FlightImpl implements Flight {

    private final List<Segment> segments;

    private final TreeMap<LocalDateTime, Segment> departureIndex = new TreeMap<LocalDateTime, Segment>();
    private final TreeMap<LocalDateTime, Segment> arrivalIndex = new TreeMap<LocalDateTime, Segment>();

    FlightImpl(final List<Segment> segs) {
        segments = segs;

        for (Segment segment : segments) {
            departureIndex.put(segment.getDepartureDate(), segment);
            arrivalIndex.put(segment.getArrivalDate(), segment);
        }
    }

    List<Segment> getSegments() {
        return segments;
    }

    @Override
    public String toString() {
        return segments.stream().map(Object::toString)
                .collect(Collectors.joining(" "));
    }

    public List<ISegment> getSegmentBefore(final LocalDateTime date) {

        return new ArrayList<>(departureIndex.headMap(date, false).values());
    }

    public List<ISegment> getWrongSegments() {

        List<ISegment> result = new ArrayList<ISegment>();
        for (Segment segment : segments) {

            if (segment.getArrivalDate().isBefore(segment.getDepartureDate())) {
                result.add(segment);
            }
        }

        return result;
    }

    //Находим как разность длительностей всего перелета и суммы сегментов 
    public long getHours() {

        long totalSegmentsTime = 0;

        for (Segment segment : segments) {

            totalSegmentsTime += Math.max(0, segment.getHours());
        }

        Segment firstSegment = departureIndex.firstEntry().getValue();

        Segment lastSegment = departureIndex.lastEntry().getValue();

        return Math.max(0, Duration.between(firstSegment.getDepartureDate(), lastSegment.getArrivalDate()).toHours()) - totalSegmentsTime;
    }

    /**
     * Bean that represents a flight segment.
     */
    static class Segment implements ISegment {

        private final LocalDateTime departureDate;

        private final LocalDateTime arrivalDate;

        private final Duration duration;

        Segment(final LocalDateTime dep, final LocalDateTime arr) {
            departureDate = Objects.requireNonNull(dep);
            arrivalDate = Objects.requireNonNull(arr);
            duration = Duration.between(departureDate, arrivalDate);

            if (duration.isNegative()) {

                System.err.println(toString() + " has negative duration!");
            }
        }

        LocalDateTime getDepartureDate() {
            return departureDate;
        }

        LocalDateTime getArrivalDate() {
            return arrivalDate;
        }

        long getHours(){
            return duration.toHours();
        }
        
         long getMinutes() {
            return duration.toMinutes();
        }

        @Override
        public String toString() {
            DateTimeFormatter fmt
                    = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

            return '[' + departureDate.format(fmt) + '|' + arrivalDate.format(fmt)
                    + ']' + ' ' + getHours() + "h. "+ ' ' + (getMinutes() - getHours() *60) + "m.";
        }
    }
}
