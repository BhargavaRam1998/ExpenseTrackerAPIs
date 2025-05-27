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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @Test
    public void deleteExpenseShouldDeleteExpense(){

        Mockito.when(expenseRepo.existsById(Mockito.anyInt())).thenReturn(true);
        Mockito.doNothing().when(expenseRepo).deleteById(Mockito.anyInt());

        boolean result = expenseService.deleteExpense(Mockito.anyInt());

        assertTrue(result);
        verify(expenseRepo, times(1)).deleteById(Mockito.anyInt());
    }

    @Test
    public void deleteExpenseShouldNotDeleteIfIdDidNotExist(){

        Mockito.when(expenseRepo.existsById(Mockito.anyInt())).thenReturn(false);

        boolean result = expenseService.deleteExpense(Mockito.anyInt());

        assertFalse(result);
        verify(expenseRepo, times(0)).deleteById(Mockito.anyInt());
    }

    @Test
    public void updateExpenseShouldUpdateExpense(){

        Expense expense = new Expense();
        expense.setDescription("Grocery shopping");
        expense.setAmount(500);
        expense.setCategory(GROCERIES);
        expense.setDate(LocalDate.now());

        int id = 3;

        Mockito.when(expenseRepo.existsById(Mockito.anyInt())).thenReturn(true);
        Mockito.when(expenseRepo.save(expense)).thenReturn(expense);

        Expense updatedExpense = expenseService.updateExpense(id, expense);

        assertNotNull(updatedExpense);
        assertEquals(expense.getID(), updatedExpense.getID());
        assertEquals(expense.getDescription(), updatedExpense.getDescription());
        assertEquals(expense.getAmount(), updatedExpense.getAmount());
        assertEquals(expense.getCategory(), updatedExpense.getCategory());
        assertEquals(expense.getDate(), updatedExpense.getDate());
    }

    @Test
    public void updateExpenseShouldAddDateIfNull(){

        Expense expense = new Expense();
        expense.setDescription("Grocery shopping");
        expense.setAmount(500);
        expense.setCategory(GROCERIES);

        int id = 3;

        Mockito.when(expenseRepo.existsById(Mockito.anyInt())).thenReturn(true);
        Mockito.when(expenseRepo.save(expense)).thenReturn(expense);

        Expense updatedExpense = expenseService.updateExpense(id, expense);

        assertNotNull(updatedExpense);
        assertNotNull(updatedExpense.getDate());

    }

    @Test
    public void updateExpenseShouldNotUpdateIfIdNotExist(){

        Expense expense = new Expense();
        expense.setDescription("Grocery shopping");
        expense.setAmount(500);
        expense.setCategory(GROCERIES);
        expense.setDate(LocalDate.now());

        int id = 3;

        Mockito.when(expenseRepo.existsById(Mockito.anyInt())).thenReturn(false);

        Expense updatedExpense = expenseService.updateExpense(id, expense);

        assertNull(updatedExpense);
    }

}