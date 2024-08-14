package info.amytan.smartfile;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
@Slf4j
public class ElasticsearchService implements StorageService {
    private final StorageProperties storageProperties;

    @Autowired
    public ElasticsearchService(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    @PostConstruct
    public void init() {
        // we need establish the connection
        // we also need create a index to hold the pages if there is no such index
    }

    @Override
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store an empty file.");
            }

            if (!isPdf(file)) {
                throw new StorageException("Your upload must be a PDF.");
            }
            else {
                InputStream pdf = file.getInputStream();
                parsePdfAndSave(file.getOriginalFilename(), pdf);
            }

//            Path destinationFile = this.rootLocation.resolve(
//                    Paths.get(file.getOriginalFilename()))
//                    .normalize().toAbsolutePath();
//
//            if (!destinationFile.getParent().equals
//                    (this.rootLocation.toAbsolutePath())) {
//                throw new StorageException("Cannot store file outside of current directory.");
//            }
//
//            try (InputStream inputStream = file.getInputStream()) {
//                Files.copy(inputStream, destinationFile,
//                        StandardCopyOption.REPLACE_EXISTING);
//            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store exception", e);
        }
    }

    private boolean isPdf(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType.equals("application/pdf")) {
            return true;
        }
        else {
            return false;
        }
    }

    private void parsePdfAndSave(String filename, InputStream pdf) throws IOException {
        PdfReader reader = new PdfReader(pdf);
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        TextExtractionStrategy strategy;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
            Page page = Page.builder()
                    .filename(filename)
                    .pageNum(i)
                    .content(strategy.getResultantText())
                    .build();
            save(page);
        }
        reader.close();
    }

    private void save(Page page) {
        // TODO: implement this method
        log.info("Saving to Elastic Search");
    }
}
