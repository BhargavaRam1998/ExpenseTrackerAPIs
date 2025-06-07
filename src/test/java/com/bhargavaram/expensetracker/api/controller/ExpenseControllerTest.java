package com.bhargavaram.expensetracker.api.controller;

import com.bhargavaram.expensetracker.api.config.JwtUtil;
import com.bhargavaram.expensetracker.api.model.Expense;
import com.bhargavaram.expensetracker.api.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static com.bhargavaram.expensetracker.api.model.Expense.Category.GROCERIES;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ExpenseControllerTest {

    @InjectMocks
    private ExpenseController expenseController;

    private MockMvc mockMvc;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ExpenseService expenseService;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(expenseController).build();
    }

    @Test
    public void addExpenseControllerShouldAddExpense() throws Exception {

        String token = "sample_token";

        Expense expense = new Expense();
        expense.setDescription("sample expense 1");
        expense.setAmount(200);
        expense.setCategory(GROCERIES);

        ArrayList<Expense> addedExpenses = new ArrayList<>();
        addedExpenses.add(expense);

        Mockito.when(jwtUtil.isAuthorized(Mockito.any())).thenReturn(true);
        Mockito.when(expenseService.addExpense(expense)).thenReturn(addedExpenses);


        mockMvc.perform(post("/expense/track/add")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"sample expense 1\", \"amount\": 200, \"category\": \"GROCERIES\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("sample expense 1"))
                .andExpect(jsonPath("$[0].amount").value(200))
                .andExpect(jsonPath("$[0].category").value("GROCERIES"));

    }

    @Test
    public void addExpenseShouldNotAddExpenseIfNotAuthorized() throws Exception {

        String token = "sample_token";

        Mockito.when(jwtUtil.isAuthorized(Mockito.any())).thenReturn(false);

        mockMvc.perform(post("/expense/track/add")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"sample expense 1\", \"amount\": 200, \"category\": \"GROCERIES\"}"))
                .andExpect(status().isUnauthorized());


    }

    @Test
    public void deleteExpenseShouldDeleteExpense() throws Exception {

        String token = "sample_token";

        Mockito.when(jwtUtil.isAuthorized(Mockito.any())).thenReturn(true);
        Mockito.when(expenseService.deleteExpense(Mockito.anyInt())).thenReturn(true);

        mockMvc.perform(put("/expense/track/delete/1")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted task with ID: 1 successfully!"));

    }

    @Test
    public void deleteExpenseShouldNotDeleteIfIdDoesNotExist() throws Exception {

        String token = "sample_token";

        Mockito.when(jwtUtil.isAuthorized(Mockito.any())).thenReturn(true);
        Mockito.when(expenseService.deleteExpense(Mockito.anyInt())).thenReturn(false);


        mockMvc.perform(put("/expense/track/delete/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task ID: 1 not found, please enter valid task ID"));

    }

    @Test
    public void deleteTaskShouldNotDeleteIfNotAuthorized() throws Exception {

        String token = "sample_token";

        Mockito.when(jwtUtil.isAuthorized(Mockito.any())).thenReturn(false);

        mockMvc.perform(put("/expense/track/delete/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());

    }

}