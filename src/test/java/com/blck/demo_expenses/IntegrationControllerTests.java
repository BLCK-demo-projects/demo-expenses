package com.blck.demo_expenses;

import com.blck.demo_expenses.DB.*;
import com.blck.demo_expenses.ResponseDTOs.ExpenseSumByCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationControllerTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private CategoryRepository categoryRepository;

	@BeforeEach
	void setUp() {
		categoryRepository.deleteAll();
	}

	@Test
	void addCategory() {
		CategoryDTO dto = new CategoryDTO("Bills");

		ResponseEntity<Category> response = restTemplate.postForEntity("/categories", dto, Category.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody())
				.isNotNull()
				.extracting(Category::getName)
				.isEqualTo("Bills");
	}

	@Test
	void addCategoryAlreadyExists() {
		CategoryDTO dto = new CategoryDTO("Bills");

		restTemplate.postForEntity("/categories", dto, String.class);
		ResponseEntity<String> response = restTemplate.postForEntity("/categories", dto, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
	}

	@Test
	void getAllCategories() {
		restTemplate.postForEntity("/categories", new CategoryDTO("Bills"), String.class);
		restTemplate.postForEntity("/categories", new CategoryDTO("Clothes"), String.class);

		ResponseEntity<CategoryDTO[]> response = restTemplate.getForEntity("/categories", CategoryDTO[].class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody())
				.isNotNull()
				.extracting(CategoryDTO::name)
				.containsExactlyInAnyOrder("Bills", "Clothes");
	}

	@Test
	void getAllCategoriesEmpty() {
		ResponseEntity<CategoryDTO[]> response = restTemplate.getForEntity("/categories", CategoryDTO[].class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody())
				.isNullOrEmpty();
	}

	@Test
	void deleteCategory() {
		restTemplate.postForEntity("/categories", new CategoryDTO("Bills"), String.class);

		ResponseEntity<Void> response = restTemplate.exchange("/categories/Bills", HttpMethod.DELETE, null, Void.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void deleteCategoryDeletesAssociatedExpenses() {
		restTemplate.postForEntity("/categories", new CategoryDTO("Bills"), String.class);
		restTemplate.postForEntity("/expenses", new ExpenseDTO("Water", 1.0, new Date(), "Bills"), String.class);
		restTemplate.postForEntity("/expenses", new ExpenseDTO("Heating", 1.0, new Date(), "Bills"), String.class);

		ResponseEntity<Void> response = restTemplate.exchange("/categories/Bills", HttpMethod.DELETE, null, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		ResponseEntity<ExpenseDTO[]> expenses = restTemplate.getForEntity("/expenses", ExpenseDTO[].class);
		assertThat(expenses.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(expenses.getBody())
				.isNullOrEmpty();
	}

	@Test
	void deleteCategoryNotFound() {
		ResponseEntity<Void> response = restTemplate.exchange("/categories/Bills", HttpMethod.DELETE, null, Void.class);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	void addExpense() {
		restTemplate.postForEntity("/categories", new CategoryDTO("Bills"), String.class);
		ExpenseDTO dto = new ExpenseDTO("Water", 1.0, new Date(), "Bills");

		ResponseEntity<Expense> response = restTemplate.postForEntity("/expenses", dto, Expense.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody())
				.isNotNull()
				.extracting(Expense::getName)
				.isEqualTo("Water");
	}

	@Test
	void addExpenseMissingCategory() {
		ExpenseDTO dto = new ExpenseDTO("Water", 1.0, new Date(), "Bills");

		ResponseEntity<String> response = restTemplate.postForEntity("/expenses", dto, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody())
				.isNotNull()
				.isEqualTo("Category not found: Bills");
	}

	@Test
	void addExpenseAlreadyExists() {
		restTemplate.postForEntity("/categories", new CategoryDTO("Bills"), String.class);
		ExpenseDTO dto = new ExpenseDTO("Water", 1.0, new Date(), "Bills");

		restTemplate.postForEntity("/expenses", dto, Expense.class);
		ResponseEntity<String> response = restTemplate.postForEntity("/expenses", dto, String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
	}

	@Test
	void getTotalSpentAmount() {
		restTemplate.postForEntity("/categories", new CategoryDTO("Bills"), String.class);
		restTemplate.postForEntity("/categories", new CategoryDTO("Clothes"), String.class);
		restTemplate.postForEntity("/expenses", new ExpenseDTO("Water", 10.0, new Date(), "Bills"), Expense.class);
		restTemplate.postForEntity("/expenses", new ExpenseDTO("Shows", 50.0, new Date(), "Clothes"), Expense.class);

		ResponseEntity<Double> response = restTemplate.getForEntity("/summary/total-spent", Double.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody())
				.isNotNull()
				.isEqualTo(60.0);
	}

	@Test
	void getSpentByCategory() {
		restTemplate.postForEntity("/categories", new CategoryDTO("Bills"), String.class);
		restTemplate.postForEntity("/categories", new CategoryDTO("Clothes"), String.class);
		restTemplate.postForEntity("/expenses", new ExpenseDTO("Water", 10.0, new Date(), "Bills"), Expense.class);
		restTemplate.postForEntity("/expenses", new ExpenseDTO("Shoes", 50.0, new Date(), "Clothes"), Expense.class);

		ResponseEntity<ExpenseSumByCategory[]> response = restTemplate.getForEntity("/summary/spent-by-category", ExpenseSumByCategory[].class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody())
				.isNotNull()
				.extracting(ExpenseSumByCategory::categoryFK, ExpenseSumByCategory::sumOfExpenses)
				.containsExactlyInAnyOrder(
						tuple("Bills", 10.0),
						tuple("Clothes", 50.0)
				);
	}

}
