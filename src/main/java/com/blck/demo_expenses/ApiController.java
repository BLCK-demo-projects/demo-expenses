package com.blck.demo_expenses;

import com.blck.demo_expenses.DB.Category;
import com.blck.demo_expenses.DB.CategoryRepository;
import com.blck.demo_expenses.DB.Expense;
import com.blck.demo_expenses.DB.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class ApiController {

	private final CategoryRepository categoryRepository;
	private final ExpenseRepository expenseRepository;

	@Autowired
	public ApiController(CategoryRepository categoryRepository,
						 ExpenseRepository expenseRepository) {
		this.categoryRepository = categoryRepository;
		this.expenseRepository = expenseRepository;
	}

	@PostMapping("/categories")
	public ResponseEntity<Category> addCategory(@RequestBody Category category) {
		Category result = categoryRepository.save(category);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/expenses")
	public ResponseEntity<Expense> addExpense(@RequestBody Expense expense) {
		Expense result = expenseRepository.save(expense);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/expenses")
	public ResponseEntity<List<Expense>> getAllExpenses() {
		return ResponseEntity.ok(expenseRepository.findAll());
	}

	@GetMapping("/summary/monthly")
	public ResponseEntity<Float> getMonthlySpentAmount() {
		return ResponseEntity.ok(expenseRepository.getMonthlyTotal());
	}

	@GetMapping("/summary/by-category")
	public ResponseEntity<List<Object[]>> getSpentByCategory() {
		return ResponseEntity.ok(expenseRepository.getSpentByCategory());
	}

}
