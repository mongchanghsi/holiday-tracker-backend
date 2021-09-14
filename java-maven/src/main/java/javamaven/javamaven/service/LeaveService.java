package javamaven.javamaven.service;

import javamaven.javamaven.entity.Leave;
import javamaven.javamaven.repository.LeaveRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LeaveService {
    private final LeaveRepository leaveRepository;

    public List<Leave> getAllLeaves() { return (List<Leave>) leaveRepository.findAll(); }

    public Integer getAllLeavesCount() {
        return Math.toIntExact(leaveRepository.count());
    }

    public Integer getAllLeavesCountByYear(String year) {
        // Need to fix this service
        List<Leave> leavesByYear = (List<Leave>) leaveRepository.findAll();
        return leavesByYear.size();
    }

    public Leave getLeaveByDate(String date) {
        return leaveRepository.findByDate(date);
    }

    public Integer getLeaveCountByDate(String date) {
        Leave leavesByDate = leaveRepository.findByDate(date);
        // Need to handle if there is no such date
        return leavesByDate.getUsers().size();
    }

    public Leave createLeave(String date) {
        Leave leaveResult = leaveRepository.findByDate(date);
        if (leaveResult == null) {
            leaveResult = leaveRepository.save(new Leave(date));
        }
        return leaveResult;
    }

}
