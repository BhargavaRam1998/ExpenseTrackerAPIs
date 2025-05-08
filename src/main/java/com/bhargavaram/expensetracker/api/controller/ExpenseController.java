package com.bhargavaram.expensetracker.api.controller;

import com.bhargavaram.expensetracker.api.config.JwtUtil;
import com.bhargavaram.expensetracker.api.model.Expense;
import com.bhargavaram.expensetracker.api.repo.ExpenseRepo;
import com.bhargavaram.expensetracker.api.service.ExpenseService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/expense/track")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExpenseRepo expenseRepo;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/add")
    public ResponseEntity<List<Expense>> addExpense(@RequestBody Expense expense, HttpServletRequest request){

        String token = request.getHeader("Authorization");

        if(token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if(!jwtUtil.validateToken(token)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<Expense> expenses = expenseService.addExpense(expense);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable int id){
        if(expenseService.deleteExpense(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable int id, @RequestBody Expense expense){
        Expense updatedExpense = expenseService.updateExpense(id, expense);

        if(updatedExpense != null){
            return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/list")
    public List<Expense> getExpenses(@RequestParam String filter,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        LocalDate today = LocalDate.now();
        LocalDate start;

        switch (filter.toLowerCase()) {

            case "pastweek":
                start = today.minusDays(7);
                return expenseRepo.findByDateBetween(start, today);

            case "pastmonth":
                start = today.minusMonths(1);
                return expenseRepo.findByDateBetween(start, today);

            case "past3months":
                start = today.minusMonths(3);
                return expenseRepo.findByDateBetween(start, today);

            case "custom":
                if (startDate != null && endDate != null) {
                    return expenseRepo.findByDateBetween(startDate, endDate);
                } else {
                    throw new IllegalArgumentException("custom filter requires start and end date");
                }

            default:
                throw new IllegalArgumentException("Invalid filter type");
        }

    }
}
