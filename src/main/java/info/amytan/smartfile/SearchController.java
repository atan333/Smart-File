package info.amytan.smartfile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
public class SearchController {
    private final StorageService storageService;

    @Autowired
    public SearchController(StorageService storageService) {
        this.storageService = storageService;
    }
    @GetMapping("/")
    public String listUploadFiles(Model model) throws IOException {
        return "searchForm";
    }

    @PostMapping("/")
    public ModelAndView handleSearch() {
        List<String> data = List.of("This is a test");
        return new ModelAndView("searchForm", "searchItems", data);
    }
}
