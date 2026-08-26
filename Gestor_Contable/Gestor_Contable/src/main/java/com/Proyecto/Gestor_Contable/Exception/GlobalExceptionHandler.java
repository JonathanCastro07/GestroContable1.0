package com.Proyecto.Gestor_Contable.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ErrorResponse(String mensaje, int status, LocalDateTime fecha) {
    }

    @ExceptionHandler(NegocioNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleNogocioNoEncontado(NegocioNoEncontradoException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), 404, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MovimientoFinancieroNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleMoovimientoNoEncontrado(MovimientoFinancieroNoEncontradoException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), 404, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(TipoMovimientoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleTipoMovimientoNoEncontado(TipoMovimientoNoEncontradoException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), 404, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(OrigenNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleOrigenNoEncontado(OrigenNoEncontradoException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), 404, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(PeriodoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handlePeriodoNoEncontrando(PagoPeriodicoNoEncontradoException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), 404, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ErrorResponse> handleCrendencialesInvalidas(CredencialesInvalidasException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), 401, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(EmailYaRegistradoException.class)
    public ResponseEntity<ErrorResponse> handleEmailDuplicado(EmailYaRegistradoException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), 409, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(AccesoNoAutorizadoException.class)
    public ResponseEntity<ErrorResponse> handleAccesoNoAutorizado(AccesoNoAutorizadoException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), 403, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return build("Ocurrio un error inesperado ", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> build(String mensaje, HttpStatus status) {
        ErrorResponse error = new ErrorResponse(mensaje, status.value(), LocalDateTime.now());
        return ResponseEntity.status(status).body(error);
    }

}
