package kotlinholidaytracker.kotlinholidaytracker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinHolidayTrackerApplication

fun main(args: Array<String>) {
	runApplication<KotlinHolidayTrackerApplication>(*args)
	println("App has started!")
}
