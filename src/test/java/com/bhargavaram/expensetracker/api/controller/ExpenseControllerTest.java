package com.bhargavaram.expensetracker.api.controller;

import com.bhargavaram.expensetracker.api.config.JwtUtil;
import com.bhargavaram.expensetracker.api.exception.GlobalExceptionHandler;
import com.bhargavaram.expensetracker.api.model.Expense;
import com.bhargavaram.expensetracker.api.repo.ExpenseRepo;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.bhargavaram.expensetracker.api.model.Expense.Category.GROCERIES;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Mock
    private ExpenseRepo expenseRepo;


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(expenseController)
                .setControllerAdvice(new GlobalExceptionHandler()) // Register exception handler here
                .build();
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
    public void deleteExpenseShouldNotDeleteIfNotAuthorized() throws Exception {

        String token = "sample_token";

        Mockito.when(jwtUtil.isAuthorized(Mockito.any())).thenReturn(false);

        mockMvc.perform(put("/expense/track/delete/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void updateExpenseShouldUpdateExpense() throws Exception {

         String token = "sample_token";

        Expense expense = new Expense();
        expense.setDescription("sample expense 1");
        expense.setAmount(200);
        expense.setCategory(GROCERIES);


        Mockito.when(jwtUtil.isAuthorized(Mockito.any())).thenReturn(true);
        Mockito.when(expenseService.updateExpense(Mockito.anyInt(), Mockito.any(Expense.class))).thenReturn(expense);

        mockMvc.perform(put("/expense/track/update/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"sample expense 1\", \"amount\": 200, \"category\": \"GROCERIES\"}"))
                .andExpect(status().isOk());

    }

    @Test
    public void updateExpenseShouldNotUpdateExpenseIfUnauthorized() throws Exception {

        String token = "sample_token";

        Mockito.when(jwtUtil.isAuthorized(Mockito.any())).thenReturn(false);


        mockMvc.perform(put("/expense/track/update/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"sample expense 1\", \"amount\": 200, \"category\": \"GROCERIES\"}"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void updateExpenseResponseWhenExpenseNotFound() throws Exception {

        String token = "sample_token";

        Mockito.when(jwtUtil.isAuthorized(Mockito.any())).thenReturn(true);
        Mockito.when(expenseService.updateExpense(Mockito.anyInt(), Mockito.any(Expense.class))).thenReturn(null);


        mockMvc.perform(put("/expense/track/update/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"sample expense 1\", \"amount\": 200, \"category\": \"GROCERIES\"}"))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testGetExpenses_PastWeekFilter_ShouldReturnExpenses() throws Exception {
        String token = "sample_token";

        Mockito.when(jwtUtil.isAuthorized(Mockito.any())).thenReturn(true);

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(7);

        List<Expense> mockExpenses = Arrays.asList(
                new Expense(1, "Grocery shopping", 100, Expense.Category.GROCERIES, today.minusDays(3)),
                new Expense(2, "Netflix", 15, Expense.Category.LEISURE, today.minusDays(6))
        );

        Mockito.when(expenseRepo.findByDateBetween(startDate, today)).thenReturn(mockExpenses);

        mockMvc.perform(get("/expense/track/list")
                        .param("filter", "pastweek")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].description").value("Grocery shopping"))
                .andExpect(jsonPath("$[1].description").value("Netflix"));
    }

    @Test
    public void testGetExpenses_PastMonthFilter_ShouldReturnExpenses() throws Exception {
        String token = "sample_token";

        Mockito.when(jwtUtil.isAuthorized(Mockito.any())).thenReturn(true);

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusMonths(1);

        List<Expense> mockExpenses = Arrays.asList(
                new Expense(1, "Grocery shopping", 100, Expense.Category.GROCERIES, today.minusDays(20)),
                new Expense(2, "Netflix", 15, Expense.Category.LEISURE, today.minusDays(25))
        );

        Mockito.when(expenseRepo.findByDateBetween(startDate, today)).thenReturn(mockExpenses);

        mockMvc.perform(get("/expense/track/list")
                        .param("filter", "pastmonth")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].description").value("Grocery shopping"))
                .andExpect(jsonPath("$[1].description").value("Netflix"));

    }

    @Test
    public void testGetExpenses_Past3MonthsFilter_ShouldReturnExpenses() throws Exception {
        String token = "sample_token";

        Mockito.when(jwtUtil.isAuthorized(Mockito.any())).thenReturn(true);

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusMonths(3);

        List<Expense> mockExpenses = Arrays.asList(
                new Expense(1, "Grocery shopping", 100, Expense.Category.GROCERIES, today.minusMonths(1)),
                new Expense(2, "Netflix", 15, Expense.Category.LEISURE, today.minusMonths(2))
        );

        Mockito.when(expenseRepo.findByDateBetween(startDate, today)).thenReturn(mockExpenses);

        mockMvc.perform(get("/expense/track/list")
                        .param("filter", "past3months")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].description").value("Grocery shopping"))
                .andExpect(jsonPath("$[1].description").value("Netflix"));

    }

    @Test
    public void testGetExpenses_CustomFilter_ShouldReturnExpenses() throws Exception {
        String token = "sample_token";

        Mockito.when(jwtUtil.isAuthorized(Mockito.any())).thenReturn(true);

        LocalDate startDate = LocalDate.now().minusDays(15);
        LocalDate endDate = LocalDate.now().minusDays(5);

        List<Expense> mockExpenses = Arrays.asList(
                new Expense(1, "Medical Bill", 200, Expense.Category.HEALTH, LocalDate.now().minusDays(10)),
                new Expense(2, "Clothing", 80, Expense.Category.CLOTHING, LocalDate.now().minusDays(12))
        );

        Mockito.when(expenseRepo.findByDateBetween(startDate, endDate)).thenReturn(mockExpenses);

        mockMvc.perform(get("/expense/track/list")
                        .param("filter", "custom")
                        .param("startDate", startDate.toString()) // YYYY-MM-DD
                        .param("endDate", endDate.toString())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].description").value("Medical Bill"))
                .andExpect(jsonPath("$[1].description").value("Clothing"));
    }

    @Test
    public void testGetExpenses_CustomFilter_MissingEndDate_ShouldThrowException() throws Exception {
        String token = "sample_token";

        Mockito.when(jwtUtil.isAuthorized(Mockito.any())).thenReturn(true);

        LocalDate startDate = LocalDate.now().minusDays(10);

        mockMvc.perform(get("/expense/track/list")
                        .param("filter", "custom")
                        .param("startDate", startDate.toString())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("custom filter requires start and end date"));
    }

    @Test
    public void testGetExpense_Default_ShouldThrowException() throws Exception {
        String token = "sample_token";

        Mockito.when(jwtUtil.isAuthorized(Mockito.any())).thenReturn(true);

        mockMvc.perform(get("/expense/track/list")
                        .param("filter", "invalidfilter")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid filter type"));
    }

    @Test
    public void testGetExpense_ShouldNotReturnAnything_IfNotAuthorized() throws Exception {

        String token = "sample_token";

        Mockito.when(jwtUtil.isAuthorized(Mockito.any())).thenReturn(false);

        mockMvc.perform(get("/expense/track/list")
                        .param("filter", "pastmonth")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

}