package javamaven.javamaven.controller;

import javamaven.javamaven.entity.Leave;
import javamaven.javamaven.exception.BadRequestException;
import javamaven.javamaven.exception.NotFoundException;
import javamaven.javamaven.service.LeaveService;
import javamaven.javamaven.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path="api/leave")
public class LeaveController {
    private LeaveService leaveService;
    private UserService userService;

    @GetMapping
    public List<Leave> getLeaves() { return leaveService.getAllLeaves(); }

    @GetMapping("/count")
    public Integer getLeaveCount(@RequestParam(defaultValue = "") String year) {
        if (year.isEmpty()) {
            return leaveService.getAllLeavesCount();
        }
        return leaveService.getAllLeavesCountByYear(year);
    }

    @GetMapping("/{userId")
    public void applyLeave(@PathVariable Long userId, @RequestBody Leave leave) {
        if (leave.getDate().isEmpty()) {
            throw new BadRequestException("Please provide the date in YYYY-MM-DD format");
        }

        boolean doesUserExist = userService.doesUserIdExist(userId);

        if (!doesUserExist) {
            throw new NotFoundException("No such user found");
        }

        // Must create a leave object before adding into user object
        Leave createdLeave = leaveService.createLeave(leave.getDate());

        userService.addLeave(userId, createdLeave);
    }
}
