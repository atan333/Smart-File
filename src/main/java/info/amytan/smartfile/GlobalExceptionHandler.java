package info.amytan.smartfile;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

@Component
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return new ResponseEntity<>("File size exceeds the maximum limit!", HttpStatus.PAYLOAD_TOO_LARGE);
    }

//    public String handleMaxSizeException(MaxUploadSizeExceededException exception) {
//        Object status = exception.getStatusCode();
//        System.out.println("ControllerAdvice is being called");
//
//        if (status != null) {
//            int statusCode = Integer.parseInt(status.toString());
//            System.out.println(statusCode + " is the status code");
//
//
//            if (statusCode == HttpStatus.PAYLOAD_TOO_LARGE.value()) {
//                return "error-413";
//            }
//
//            else if (statusCode == HttpStatus.NOT_FOUND.value()) {
//                return "error-404";
//            }
//        }
//
//        return "error";
//    }
}
