package kotlinholidaytracker.kotlinholidaytracker.repository

import kotlinholidaytracker.kotlinholidaytracker.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long>{
  fun findUserByEmail(email: String): Optional<User>;
}