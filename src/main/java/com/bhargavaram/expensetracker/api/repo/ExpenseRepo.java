package com.bhargavaram.expensetracker.api.repo;

import com.bhargavaram.expensetracker.api.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, Integer> {
    List<Expense> findByDateBetween(LocalDate start, LocalDate end);

}
