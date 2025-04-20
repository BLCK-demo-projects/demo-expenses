package com.blck.demo_expenses.DB;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

	@Query("SELECT SUM(e.amount) FROM Expense e")
	Float getMonthlyTotal();

	@Query(
			"SELECT c.name, SUM(e.amount) " +
			"FROM Category c " +
			"LEFT OUTER JOIN Expense e ON c.id = e.category.id " +
			"GROUP BY c.name"
	)
	List<Object[]> getSpentByCategory();
}
