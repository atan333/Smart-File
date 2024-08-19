package info.amytan.smartfile;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@Slf4j
public class ElasticsearchService implements StorageService {
    private final StorageProperties storageProperties;
    private RestClient restClient;
    private ElasticsearchTransport transport;
    private ElasticsearchClient esClient;

    @Autowired
    public ElasticsearchService(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    @PostConstruct
    public void init() {
        restClient = RestClient
                .builder(HttpHost.create(storageProperties.getHostUrl()))
                .build();

        transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        esClient = new ElasticsearchClient(transport);
        try {
            BooleanResponse resp = esClient.indices()
                    .exists(builder -> builder.index(storageProperties.getIndexName()));
            if (resp.value()) {
                log.info("Index already exists");
            } else {
                esClient.indices().create(c -> c
                        .index(storageProperties.getIndexName())
                );
                log.info("Index created.");
            }
        } catch (Exception e) {
            log.error("Creating ES index failed", e);
            throw new RuntimeException("Creating ES index failed", e);
        }
//        executeEsCommand(esClient -> {
//            BooleanResponse resp = esClient.indices()
//                    .exists(builder -> builder.index(storageProperties.getIndexName()));
//            if (resp.value()) {
//                log.info("Index already exists");
//            } else {
//                esClient.indices().create(c -> c
//                        .index(storageProperties.getIndexName())
//                );
//                log.info("Index created.");
//            }
//            return true;
//        }, "Creating index.");
    }

    @PreDestroy
    public void cleanUp() {
        try {
            transport.close();
        } catch (Exception e) {
            log.error("Error occurred while trying to close transport.");
        }
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
                    .id(UUID.randomUUID().toString())
                    .filename(filename)
                    .pageNum(i)
                    .content(strategy.getResultantText())
                    .build();
            save(page);
        }
        reader.close();
    }

    private void save(Page page) {
        try {
            esClient.index(i -> i
                    .index(storageProperties.getIndexName())
                    .id(page.getId())
                    .document(page)
            );
        } catch (Exception exp) {
            log.error("Failed to index page: " + page.getFilename() + "/" + page.getId(), exp);
            throw new RuntimeException("Failed to create page", exp);
        }
//        executeEsCommand(esClient -> {
//            esClient.index(i -> i
//                    .index(storageProperties.getIndexName())
//                    .id(page.getId())
//                    .document(page)
//            );
//            esClient.update(u -> u
//                            .index(storageProperties.getIndexName())
//                            .id(page.getId())
//                            .upsert(page), Page.class
//            );
//            log.info("Saving to Elastic Search");
//            return true;
//        }, "Saving page");
    }

    private <T> T executeEsCommand(CheckedFunction<ElasticsearchClient, T> func, String message) {
        try (ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper())) {
            ElasticsearchClient esClient = new ElasticsearchClient(transport);
            return func.apply(esClient);
        } catch (Exception e) {
            log.error("ES Execution failed", e);
            throw new RuntimeException("Error occurred during es command execution: " + message, e);
        }
    }

    @FunctionalInterface
    public interface CheckedFunction<T, R> {
        R apply(T t) throws IOException;
    }
}
