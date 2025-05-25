package com.bhargavaram.expensetracker.api.service;

import com.bhargavaram.expensetracker.api.model.Expense;
import com.bhargavaram.expensetracker.api.repo.ExpenseRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.bhargavaram.expensetracker.api.model.Expense.Category.GROCERIES;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {


    @InjectMocks
    private ExpenseService expenseService;

    @Mock
    private ExpenseRepo expenseRepo;

    @Test
    public void addExpenseShouldAddExpense(){

        Expense expense = new Expense();
        expense.setID(1);
        expense.setDescription("Grocery shopping");
        expense.setAmount(500);
        expense.setCategory(GROCERIES);
        expense.setDate(LocalDate.now());

        ArrayList<Expense> mock_list = new ArrayList<>();
        mock_list.add(expense);

        Mockito.when(expenseRepo.save(expense)).thenReturn(expense);
        Mockito.when(expenseRepo.findAll()).thenReturn(mock_list);

        List<Expense> expenses = expenseService.addExpense(expense);

        Expense addedExpense = expenses.get(0);

        assertNotNull(expenses);
        assertEquals(expense.getID(), addedExpense.getID());
        assertEquals(expense.getDescription(), addedExpense.getDescription());
        assertEquals(expense.getAmount(), addedExpense.getAmount());
        assertEquals(expense.getCategory(), addedExpense.getCategory());
        assertEquals(expense.getDate(), addedExpense.getDate());

    }

    @Test
    public void addExpenseShouldAddExpense_IfDateIsNull(){

        Expense expense = new Expense();
        expense.setID(1);
        expense.setDescription("Grocery shopping");
        expense.setAmount(500);
        expense.setCategory(GROCERIES);

        ArrayList<Expense> mock_list = new ArrayList<>();
        mock_list.add(expense);

        Mockito.when(expenseRepo.save(expense)).thenReturn(expense);
        Mockito.when(expenseRepo.findAll()).thenReturn(mock_list);

        List<Expense> expenses = expenseService.addExpense(expense);

        Expense addedExpense = expenses.get(0);

        assertNotNull(expenses);
        assertEquals(expense.getID(), addedExpense.getID());
        assertEquals(expense.getDescription(), addedExpense.getDescription());
        assertEquals(expense.getAmount(), addedExpense.getAmount());
        assertEquals(expense.getCategory(), addedExpense.getCategory());
        assertEquals(expense.getDate(), addedExpense.getDate());

    }

}