package info.amytan.smartfile;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {
    void store(MultipartFile file);

    List<Page> search(String query);
}
