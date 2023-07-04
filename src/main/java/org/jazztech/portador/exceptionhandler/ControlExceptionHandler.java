package org.jazztech.portador.exceptionhandler;

import java.net.URI;
import java.time.LocalDateTime;
import org.jazztech.portador.exception.CardHolderAlreadyExistException;
import org.jazztech.portador.exception.IdsDoesntMatchException;
import org.jazztech.portador.exception.NonApprovedCreditAnalysisException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ControlExceptionHandler {
    public static final String TIMESTAMP = "timestamp";

    @ExceptionHandler
    public ProblemDetail idsDoesntMatchExceptionHandler(IdsDoesntMatchException e) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setType(URI.create(""));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setDetail(e.getMessage());
        return problemDetail;
    }

    @ExceptionHandler
    public ProblemDetail nonApprovedCreditAnalysisException(NonApprovedCreditAnalysisException e) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setType(URI.create(""));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setDetail(e.getMessage());
        return problemDetail;
    }

    @ExceptionHandler
    public ProblemDetail cardHolderAlreadyExistException(CardHolderAlreadyExistException e) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setType(URI.create(""));
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setDetail(e.getMessage());
        return problemDetail;
    }
}
