package com.blck.demo_expenses;

import com.blck.demo_expenses.DB.*;
import com.blck.demo_expenses.Exceptions.CategoryNotFoundException;
import com.blck.demo_expenses.ResponseDTOs.ExpenseSumByCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

	@Operation(summary = "Create a new category")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Success",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = Category.class))),
			@ApiResponse(responseCode = "409", description = "Category already exists", content = @Content)
	})
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

	@Operation(summary = "Get all category names")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success")
	})
	@GetMapping("/categories")
	public ResponseEntity<List<CategoryDTO>> getAllCategories() {
		List<CategoryDTO> allCategories = categoryRepository.findAll().stream()
				.map(category -> new CategoryDTO(category.getName()))
				.toList();
		return ResponseEntity.ok(allCategories);
	}

	@Operation(summary = "Delete a category and all associated expenses")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success"),
			@ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
	})
	@DeleteMapping("/categories/{name}")
	public ResponseEntity<Void> deleteCategory(@PathVariable String name) {
		Optional<Category> category = categoryRepository.findByName(name);
		if (category.isEmpty())
			return ResponseEntity.notFound().build();
		categoryRepository.delete(category.get());
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Add a new expense to a category")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = Expense.class))),
			@ApiResponse(responseCode = "409", description = "Expense already exists", content = @Content),
			@ApiResponse(responseCode = "404", description = "Category not found", content = @Content)
	})
	@PostMapping("/expenses")
	public ResponseEntity<?> addExpense(@RequestBody ExpenseDTO expenseDTO) {
		Category category = categoryRepository.findByName(expenseDTO.categoryFK())
				.orElseThrow(() -> new CategoryNotFoundException(expenseDTO.categoryFK()));

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

	@Operation(summary = "Get all expenses and their categories")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success")
	})
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

	@Operation(summary = "Get total amount ever spent")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success")
	})
	@GetMapping("/summary/total-spent")
	public ResponseEntity<Double> getTotalSpentAmount() {
		return ResponseEntity.ok(expenseRepository.getTotalSpent());
	}

	@Operation(summary = "Get a summary of spending per category")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Success")
	})
	@GetMapping("/summary/spent-by-category")
	public ResponseEntity<List<ExpenseSumByCategory>> getSpentByCategory() {
		return ResponseEntity.ok(expenseRepository.getSpentByCategory());
	}

}
