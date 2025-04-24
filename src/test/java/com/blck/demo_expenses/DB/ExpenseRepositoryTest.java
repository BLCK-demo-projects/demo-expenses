package com.blck.demo_expenses.DB;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
		ExpenseDTO expenseDTO = new ExpenseDTO("Heating", 10.f, date, "Bills");
		entityManager.persist(new Expense(expenseDTO, category));

		categoryDTO = new CategoryDTO("Shopping");
		category = new Category(categoryDTO);
		entityManager.persist(category);

		date = new SimpleDateFormat("yyyy-MM-dd").parse("2024-03-1");
		expenseDTO = new ExpenseDTO("Heating", 10.f, date, "Shopping");
		entityManager.persist(new Expense(expenseDTO, category));
	}
	
	@Test
	void findByName() {
		Optional<Expense> expense = expenseRepository.findByName("Heating");

		assertTrue(expense.isPresent());
		assertEquals("Heating", expense.get().getName());
	}

	@Test
	void findByNameNotFound() {
		Optional<Expense> expense = expenseRepository.findByName("");

		assertFalse(expense.isPresent());
	}

	@Test
	void getMonthlyTotal() {
		assertEquals(20.f, expenseRepository.getMonthlyTotal());
	}

	@Test
	void getSpentByCategory() {
		List<Object[]> spentByCategory = expenseRepository.getSpentByCategory();

		assertAll(
				() -> assertTrue(Arrays.toString(spentByCategory.getFirst()).contains("10.0")),
				() -> assertTrue(Arrays.toString(spentByCategory.getLast()).contains("10.0"))
		);
	}

}
