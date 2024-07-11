package info.amytan.smartfile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.io.IOException;

@Component
@Slf4j
public class CustomExceptionHandlerResolver extends DefaultHandlerExceptionResolver {
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    protected ModelAndView handleErrorResponse(ErrorResponse errorResponse,
                                               HttpServletRequest request,
                                               HttpServletResponse response,
                                               @Nullable Object handler) throws IOException {
        //super.handleErrorResponse(errorResponse, request, response, handler);
        return new ModelAndView("redirect:/error");
    }
}
