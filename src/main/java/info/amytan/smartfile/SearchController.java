package info.amytan.smartfile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/")
public class SearchController {
    private final StorageService storageService;

    @Autowired
    public SearchController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping
    public String listUploadFiles(Model model) throws IOException {
        return "searchForm";
    }

    @PostMapping
    public ModelAndView handleSearch(@RequestParam("query") String query) {
        Map<String, Object> model = new HashMap<>();
        model.put("query", query);
        model.put("searchItems", storageService.search(query));
        return new ModelAndView("searchForm", model);
    }
}
