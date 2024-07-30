package info.amytan.smartfile;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

@Component
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public ModelAndView handleMaxSizeException(MaxUploadSizeExceededException ex) {
        ModelAndView modelAndView = new ModelAndView("uploadForm");
        modelAndView.addObject("message", "File too large");
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        return modelAndView;
    }
}
