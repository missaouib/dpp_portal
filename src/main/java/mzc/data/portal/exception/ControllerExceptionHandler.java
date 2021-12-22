package mzc.data.portal.exception;

import mzc.data.portal.dto.ApiResponse;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice(basePackages = {"mzc.data.portal.controller"})
public class ControllerExceptionHandler {
    /**
     * 에러처리를 위한 응답 데이터
     */
    @NoArgsConstructor
    public static class ErrResponse extends ApiResponse<Object> {
        public ErrResponse(int status, String error) {
            super(status, error, null);
        }
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, ValidationException.class, BadSqlGrammarException.class, ValidCustomException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrResponse handleBadRequest(RuntimeException e) {
        return getErrResponse(ApiResponse.BAD_REQUEST, e);
    }

    @ExceptionHandler(value = {IllegalStateException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrResponse handleConflict(RuntimeException e) {
        return getErrResponse(ApiResponse.CONFLICT, e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrResponse handleInternalServerError(Exception e) {
        return getErrResponse(ApiResponse.INTERNAL_SERVER_ERROR, e);
    }

    /**
     * Validation 에러는 400 으로 리턴
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleValidationError(MethodArgumentNotValidException e) {

        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        String error = errors.stream().map(o -> {
            FieldError fe = (FieldError) o;
            return fe.getField() + " " + fe.getDefaultMessage();
        }).collect(Collectors.joining("| "));
        return getErrResponse(ApiResponse.BAD_REQUEST, error);
    }

    /**
     * 메시지와 코드로 에러응답을 만든다.
     *
     * @param status
     * @param error
     * @return
     */
    private ErrResponse getErrResponse(int status, String error) {
        return new ErrResponse(status, error);
    }

    /**
     * 예외와 코드로 에러응답을 만든다.
     *
     * @param status
     * @param e
     * @return
     */
    private ErrResponse getErrResponse(int status, Exception e) {

        return getErrResponse(status, e.getMessage());
    }
}