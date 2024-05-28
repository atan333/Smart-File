package info.amytan.smartfile;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

public class SmartFile {

    public static void main(String[] args) {
        String path = "/Users/amytan/Downloads/PracticeProblemsExam1.pdf";
        try {
            PdfReader reader = new PdfReader(path);
            PdfDocument pdfDoc = new PdfDocument(reader);
            for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
                String text = PdfTextExtractor.getTextFromPage(pdfDoc.getPage(i));
                System.out.println("Page " + i + ": ");
                System.out.println(text);
                System.out.println();
            }
            pdfDoc.close();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
