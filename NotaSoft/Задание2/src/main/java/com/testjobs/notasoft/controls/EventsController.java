package com.testjobs.notasoft.controls;

import com.testjobs.notasoft.DAO.EventclassesEntity;
import com.testjobs.notasoft.DAO.EventsEntity;
import com.testjobs.notasoft.services.EventsService;
import com.testjobs.notasoft.services.EventsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Controller
public class EventsController {

    @Autowired
    EventsService eventsService;

    //http://localhost:8080/index
    @RequestMapping(
            value = "/",
            method = RequestMethod.GET
    )
    public String Index(Model model) {
        return "EnterEventSuccess";
    }

    //http://localhost:8080/eventsList?page=0&size=2&className=Success
    @RequestMapping(
            value = "/eventsList",
            //params = {"page", "size" ,"className"},
            method = RequestMethod.GET
    )
    public String EventsReport(Model model,
                               @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                               @RequestParam(name = "size", required = false, defaultValue = "20") int size,
                               @RequestParam(name = "className", required = false, defaultValue = "") String className) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("date"));

        List<EventsEntity> collect = eventsService.findAll(pageable).stream().collect(Collectors.toList());

        EventclassesEntity clazz = eventsService.getClassByName(className);
        Map<LocalDate, List<EventsEntity>> map = eventsService.findAllByClass(pageable, clazz).stream().collect(groupingBy(EventsEntity::localDate));

        LinkedHashMap<LocalDate, List<EventsEntity>> sortedMap = map.entrySet().stream()
                .sorted(Map.Entry.<LocalDate, List<EventsEntity>>comparingByKey())
                .collect(toMap(Map.Entry::getKey,
                        Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


        model.addAttribute("eventsByDate", sortedMap);
        return "EventsReport";
    }


    //http://localhost:8080/eventsList/newCustom?name=new&class=Unespected
    @RequestMapping(
            value = "/eventsList/newCustom",
            params = {"name", "class"},
            method = RequestMethod.POST
    )
    public String newCustom(Model model,
                            @RequestParam("name") String name,
                            @RequestParam("class") String clazz) {

        EventclassesEntity eventClass = eventsService.getClassByName(clazz, true);

        return getResspond(model, name, eventClass);
    }

    //curl -i -X POST http://localhost:8080/eventsList/newSuccess?name=allRight
    @RequestMapping(
            value = "/eventsList/newSuccess",
            params = {"name"},
            method = RequestMethod.POST
    )
    public String newSuccess(Model model,
                             @RequestParam("name") String name) {

        EventclassesEntity eventClass = eventsService.getClassSuccess();

        return getResspond(model, name, eventClass);
    }

    //curl -i -X POST http://localhost:8080/eventsList/newError?name=ithappenes
    @RequestMapping(
            value = "/eventsList/newError",
            params = {"name"},
            method = RequestMethod.POST
    )
    public String newError(Model model,
                           @RequestParam("name") String name) {

        EventclassesEntity eventClass = eventsService.getClassError();

        return getResspond(model, name, eventClass);
    }

    private String getResspond(Model model, @RequestParam("name") String name, EventclassesEntity clazz) {
        EventsEntity event = new EventsEntity(name);
        event.setEventclass(clazz);
        eventsService.save(event);

        model.addAttribute("events", new ArrayList(Arrays.asList(event)));

        return "newEventSuccess";
    }
}
