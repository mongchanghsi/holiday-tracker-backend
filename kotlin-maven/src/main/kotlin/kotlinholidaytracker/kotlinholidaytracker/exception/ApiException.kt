package kotlinholidaytracker.kotlinholidaytracker.exception

import org.springframework.http.HttpStatus

class ApiException (val message: String?, val httpStatus: HttpStatus)