package javamaven.javamaven.repository;

import javamaven.javamaven.entity.Leave;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LeaveRepository extends CrudRepository<Leave, Long> {
    Leave findByDate(String date);
}
