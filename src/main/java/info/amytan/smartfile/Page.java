package info.amytan.smartfile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Page {
    private String id;
    private String filename;
    private int pageNum;
    private String content;


}
