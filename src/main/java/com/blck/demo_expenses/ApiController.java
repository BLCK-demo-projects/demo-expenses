package com.blck.demo_expenses;

import com.blck.demo_expenses.DB.*;
import com.blck.demo_expenses.ResponseDTOs.ExpenseSumByCategory;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
	public ResponseEntity<Category> addCategory(@RequestBody CategoryDTO dto) {
		Category category = new Category(dto);
		Category result = categoryRepository.save(category);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/expenses")
	public ResponseEntity<Expense> addExpense(@RequestBody ExpenseDTO expenseDTO) {
		Category category = categoryRepository.findByName(expenseDTO.categoryFK())
				.orElseThrow(() -> new EntityNotFoundException("Category not found: " + expenseDTO.categoryFK()));
		Expense expense = new Expense(expenseDTO, category);
		Expense result = expenseRepository.save(expense);
		return ResponseEntity.ok(result);
	}

	@DeleteMapping("/expenses/{name}")
	public ResponseEntity<Void> deleteExpense(@PathVariable String name) {
		Optional<Expense> expenseOpt = expenseRepository.findByName(name);
		if (expenseOpt.isEmpty())
			return ResponseEntity.notFound().build();
		expenseRepository.delete(expenseOpt.get());
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/expenses")
	public ResponseEntity<List<ExpenseDTO>> getAllExpenses() {
		List<ExpenseDTO> allExpenses = expenseRepository.findAll().stream()
				.map(expense -> new ExpenseDTO(
						expense.getName(),
						expense.getAmount(),
						expense.getDate(),
						expense.getCategoryFK().getName()
				))
				.collect(Collectors.toList());
		return ResponseEntity.ok(allExpenses);
	}

	@GetMapping("/summary/monthly")
	public ResponseEntity<Double> getMonthlySpentAmount() {
		return ResponseEntity.ok(expenseRepository.getTotalSpent());
	}

	@GetMapping("/summary/by-category")
	public ResponseEntity<List<ExpenseSumByCategory>> getSpentByCategory() {
		return ResponseEntity.ok(expenseRepository.getSpentByCategory());
	}

}
