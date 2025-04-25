package com.blck.demo_expenses.DB;

import com.blck.demo_expenses.ResponseDTOs.ExpenseSumByCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

	Optional<Expense> findByName(String name);

	@Query("SELECT SUM(e.amount) FROM Expense e")
	Double getTotalSpent();

	@Query("""
		SELECT new com.blck.demo_expenses.ResponseDTOs.ExpenseSumByCategory(c.name, SUM(e.amount))
		FROM Category c
		LEFT JOIN c.expenses e
		GROUP BY c.name
	""")
	List<ExpenseSumByCategory> getSpentByCategory();
}
