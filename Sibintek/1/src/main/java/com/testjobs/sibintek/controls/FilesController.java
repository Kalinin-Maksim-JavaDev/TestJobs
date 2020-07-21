package com.testjobs.sibintek.controls;

import com.testjobs.sibintek.DAO.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
import com.testjobs.sibintek.services.FilesService;

@Controller
public class FilesController {

    @Autowired
    FilesService filesService;

    //http://localhost:8080/newFile
    @RequestMapping(
            value = "/newFile",
            method = RequestMethod.GET
    )
    public String Index(Model model) {
        return "AddNewFile";
    }
    
    //http://localhost:8080/file?id=143 
    @RequestMapping(
            value = "/file",
            method = RequestMethod.GET
    )
    public String fileList(Model model,
                               @RequestParam(name = "id", required = true) String id) {


        List<FileEntity> files = filesService.findAllById(id);

        model.addAttribute("files", files);
        return "OneFileReport";
    }
    
    //http://localhost:8080/fileList?page=0&size=2&autor=admin&description=test
    @RequestMapping(
            value = "/fileList",
            //params = {"page", "size" ,"autor", "description"},
            method = RequestMethod.GET
    )
    public String fileList(Model model,
                               @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                               @RequestParam(name = "size", required = false, defaultValue = "20") int size,
                               @RequestParam(name = "autor", required = false, defaultValue = "") String autor,
                               @RequestParam(name = "description", required = false, defaultValue = "") String description) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("date"));

        Map<LocalDate, List<FileEntity>> map = filesService.findAllByAutorAndByDescriptionContaining(pageable, autor, description).stream().collect(groupingBy(FileEntity::localDate));

        LinkedHashMap<LocalDate, List<FileEntity>> sortedMap = map.entrySet().stream()
                .sorted(Map.Entry.<LocalDate, List<FileEntity>>comparingByKey())
                .collect(toMap(Map.Entry::getKey,
                        Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


        model.addAttribute("filesByDate", sortedMap);
        return "FilesReport";
    }


    //curl -i -X POST //http://localhost:8080/newFile?description=test&autor=user1
    @RequestMapping(
            value = "/newFile",
            params = {"description", "autor"},
            method = RequestMethod.POST
    )
    public String newFile(Model model,
                            @RequestParam("description") String description,
                            @RequestParam("autor") String autor) {

        return getSuccessResspond(model, description, autor);
    }  
 
    //curl -i -X POST http://localhost:8080/newLogFile
    @RequestMapping(
            value = "/newLogFile",
            params = {},
            method = RequestMethod.POST
    )
    public String newLogFile(Model model) {

        return getSuccessResspond(model, "Log", "Admin");
    }

    private String getSuccessResspond(Model model, String description, String autor) {
        
        FileEntity file = new FileEntity(description, autor);
        filesService.save(file);

        model.addAttribute("files", new ArrayList(Arrays.asList(file)));

        return "AddedSuccess";
    }
}
