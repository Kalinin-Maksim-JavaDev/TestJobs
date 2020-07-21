/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gridnine.testing;

import com.gridnine.testing.Flight;
import com.gridnine.testing.Flight;
import java.time.LocalDateTime;
import java.util.function.Predicate;

public class UserFIlter<T> {

    private Predicate<T> predicate;
    private String description;

    public UserFIlter(Predicate<T> p, String description) {
        this.predicate = p;
        this.description = description;
    }

    public Predicate<T> getPredicate() {
        return predicate;
    }

    public String getDescription() {
        return description;
    }

    public static UserFIlter[] createSet() {

        LocalDateTime now = LocalDateTime.now();

        return new UserFIlter[]{
            new UserFIlter<Flight>(flight -> !flight.getSegmentBefore(now).isEmpty(), "Перелеты, исключая вылеты до текущего момента времени:"),
            new UserFIlter<Flight>(flight -> !flight.getWrongSegments().isEmpty(), "Перелеты, исключая вылеты с датой прилёта раньше даты вылета:"),
            new UserFIlter<Flight>(flight -> flight.getHours() > 2, "Перелеты, исключая более 2-х часовое нахождение на земле:")};
    }
}
