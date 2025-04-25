package com.blck.demo_expenses;

import com.blck.demo_expenses.DB.*;
import com.blck.demo_expenses.ResponseDTOs.ExpenseSumByCategory;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	public ResponseEntity<?> addCategory(@RequestBody CategoryDTO dto) {
		if (categoryRepository.findByName(dto.name()).isPresent()) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Category already exists: " + dto.name());
		}
		Category category = new Category(dto);
		Category saved = categoryRepository.save(category);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@GetMapping("/categories")
	public ResponseEntity<List<CategoryDTO>> getAllCategories() {
		List<CategoryDTO> allCategories = categoryRepository.findAll().stream()
				.map(category -> new CategoryDTO(category.getName()))
				.toList();
		return ResponseEntity.ok(allCategories);
	}

	@DeleteMapping("/categories/{name}")
	public ResponseEntity<Void> deleteCategory(@PathVariable String name) {
		Optional<Category> category = categoryRepository.findByName(name);
		if (category.isEmpty())
			return ResponseEntity.notFound().build();
		categoryRepository.delete(category.get());
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/expenses")
	public ResponseEntity<?> addExpense(@RequestBody ExpenseDTO expenseDTO) {
		Category category = categoryRepository.findByName(expenseDTO.categoryFK())
				.orElseThrow(() -> new EntityNotFoundException("Category not found: " + expenseDTO.categoryFK()));
		boolean expenseExists = category.getExpenses().stream()
				.anyMatch(e -> expenseDTO.name().equalsIgnoreCase(e.getName()));
		if (expenseExists) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Expense already exists: " + expenseDTO.name());
		}
		Expense expense = new Expense(expenseDTO, category);
		Expense result = expenseRepository.save(expense);
		return ResponseEntity.ok(result);
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
				.toList();
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
