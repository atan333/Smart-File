package info.amytan.smartfile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

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
        return new ModelAndView("searchForm", "searchItems", storageService.search(query));
    }
}
