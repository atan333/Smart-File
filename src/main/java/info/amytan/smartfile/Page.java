package info.amytan.smartfile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page {
    private String id;
    private String filename;
    private int pageNum;
    private String content;


}
