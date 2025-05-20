package com.bhargavaram.expensetracker.api.controller;

import com.bhargavaram.expensetracker.api.config.JwtUtil;
import com.bhargavaram.expensetracker.api.model.Expense;
import com.bhargavaram.expensetracker.api.repo.ExpenseRepo;
import com.bhargavaram.expensetracker.api.service.ExpenseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/expense/track")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    private final ExpenseRepo expenseRepo;

    private final JwtUtil jwtUtil;


    @PostMapping("/add")
    public ResponseEntity<List<Expense>> addExpense(@RequestBody Expense expense, HttpServletRequest request){

        if(!jwtUtil.isAuthorized(request)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<Expense> expenses = expenseService.addExpense(expense);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable int id, HttpServletRequest request){

        if(!jwtUtil.isAuthorized(request)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if(expenseService.deleteExpense(id)){
            return new ResponseEntity<>("Deleted task with ID: " + id + " successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Task ID: " + id + " not found, please enter valid task ID",HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable int id, @RequestBody Expense expense, HttpServletRequest request){

        if(!jwtUtil.isAuthorized(request)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Expense updatedExpense = expenseService.updateExpense(id, expense);

        if(updatedExpense != null){
            return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Expense>> getExpenses(@RequestParam String filter,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate, HttpServletRequest request) {

        if(!jwtUtil.isAuthorized(request)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }


        LocalDate today = LocalDate.now();
        LocalDate start;

        switch (filter.toLowerCase()) {

            case "pastweek":
                start = today.minusDays(7);
                return new ResponseEntity<>(expenseRepo.findByDateBetween(start, today), HttpStatus.OK);

            case "pastmonth":
                start = today.minusMonths(1);
                return new ResponseEntity<>(expenseRepo.findByDateBetween(start, today), HttpStatus.OK);

            case "past3months":
                start = today.minusMonths(3);
                return new ResponseEntity<>(expenseRepo.findByDateBetween(start, today), HttpStatus.OK);

            case "custom":
                if (startDate != null && endDate != null) {
                    return new ResponseEntity<>(expenseRepo.findByDateBetween(startDate, endDate), HttpStatus.OK);
                } else {
                    throw new IllegalArgumentException("custom filter requires start and end date");
                }

            default:
                throw new IllegalArgumentException("Invalid filter type");
        }

    }
}
