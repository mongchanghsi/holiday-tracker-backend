package kotlinholidaytracker.kotlinholidaytracker.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ApiExceptionHandler {

  @ExceptionHandler(value = [BadRequestException::class])
  fun handleBadRequestException(e: BadRequestException): ResponseEntity<ApiException> {
    val badRequest = HttpStatus.BAD_REQUEST;
    val apiException = ApiException(e.message, badRequest);
    return ResponseEntity(apiException, badRequest);
  }

  @ExceptionHandler(value = [NotFoundException::class])
  fun handleBadRequestException(e: NotFoundException): ResponseEntity<ApiException> {
    val notFound = HttpStatus.NOT_FOUND;
    val apiException = ApiException(e.message, notFound);
    return ResponseEntity(apiException, notFound);
  }
}