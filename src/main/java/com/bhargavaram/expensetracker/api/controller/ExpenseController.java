package com.bhargavaram.expensetracker.api.controller;

import com.bhargavaram.expensetracker.api.model.Expense;
import com.bhargavaram.expensetracker.api.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expense/track")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;


    @PostMapping("/add")
    public void addExpense(@RequestBody Expense expense){
        expenseService.addExpense(expense);
    }

    @PutMapping("/delete/{id}")
    public void deleteExpense(@PathVariable int id){
        expenseService.deleteExpense(id);
    }

    @PutMapping("update/{id}")
    public void updateExpense(@PathVariable int id, @RequestBody Expense expense){
        expenseService.updateExpense(id, expense);
    }
}
