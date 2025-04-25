package com.blck.demo_expenses.DB;

import com.blck.demo_expenses.ResponseDTOs.ExpenseSumByCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ExpenseRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private ExpenseRepository expenseRepository;

	@BeforeEach
	void setUp() throws ParseException {
		CategoryDTO categoryDTO = new CategoryDTO("Bills");
		Category category = new Category(categoryDTO);
		entityManager.persist(category);

		Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-1");
		ExpenseDTO expenseDTO = new ExpenseDTO("Shoes", 10.0, date, "Bills");
		entityManager.persist(new Expense(expenseDTO, category));

		categoryDTO = new CategoryDTO("Shopping");
		category = new Category(categoryDTO);
		entityManager.persist(category);

		date = new SimpleDateFormat("yyyy-MM-dd").parse("2024-03-1");
		expenseDTO = new ExpenseDTO("Shoes", 5.0, date, "Shopping");
		entityManager.persist(new Expense(expenseDTO, category));
	}
	
	@Test
	void findByName() {
		Optional<Expense> expense = expenseRepository.findByName("Shoes");

		assertTrue(expense.isPresent());
		assertEquals("Shoes", expense.get().getName());
	}

	@Test
	void findByNameNotFound() {
		Optional<Expense> expense = expenseRepository.findByName("");

		assertFalse(expense.isPresent());
	}

	@Test
	void getTotalSpent() {
		assertEquals(15.0, expenseRepository.getTotalSpent());
	}

	@Test
	void getSpentByCategory() {
		List<ExpenseSumByCategory> spentByCategory = expenseRepository.getSpentByCategory();

		assertAll(
				() -> assertEquals(2, spentByCategory.size()),
				() -> assertEquals(new ExpenseSumByCategory("Bills", 10.0), spentByCategory.getFirst()),
				() -> assertEquals(new ExpenseSumByCategory("Shopping", 5.0), spentByCategory.getLast())
		);
	}

}
