/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gridnine.testing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        final boolean useStremAPI = (args.length > 0) && ("useStremAPI".equals(args[0]));

        ArrayList<Flight> flightsSet = new ArrayList(Flight.Builder.createFlights());

        UserFIlter[] userFilters = UserFIlter.createSet();

        for (UserFIlter<Flight> filter : userFilters) {

            List<Flight> list = useStremAPI
                    ? filterStremAPI(filter, flightsSet)
                    : filterOldStyle(filter, flightsSet);

            System.out.println(filter.getDescription());

            for (Flight flight : list) {
                System.out.println(flight);
            }
        }
    }

    static List<Flight> filterStremAPI(UserFIlter<Flight> testCase, ArrayList<Flight> flightsSet) {

        return flightsSet.stream()
                .filter(testCase.getPredicate().negate())
                .collect(Collectors.toList());
    }

    static List<Flight> filterOldStyle(UserFIlter<Flight> testCase, ArrayList<Flight> flightsSet) {

        ArrayList<Flight> result = (ArrayList<Flight>) flightsSet.clone();

        List<Flight> flightsEx = new ArrayList<Flight>();
        for (Flight flight : result) {
            if (testCase.getPredicate().test(flight)) {
                flightsEx.add(flight);
            }
        }

        result.removeAll(flightsEx);

        return result;
    }
}
