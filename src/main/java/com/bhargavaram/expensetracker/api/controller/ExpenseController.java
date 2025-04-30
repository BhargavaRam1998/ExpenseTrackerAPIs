package com.bhargavaram.expensetracker.api.controller;

import com.bhargavaram.expensetracker.api.model.Expense;
import com.bhargavaram.expensetracker.api.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expense/track")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;


    @PostMapping("/add")
    public ResponseEntity<List<Expense>> addExpense(@RequestBody Expense expense){

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


}
