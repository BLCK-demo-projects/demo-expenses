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
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class CategoryRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private CategoryRepository categoryRepository;

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
		Optional<Category> category = categoryRepository.findByName("Bills");

		assertTrue(category.isPresent());
		assertEquals("Bills", category.get().getName());
	}

	@Test
	void findByNameNotFound() {
		Optional<Category> category = categoryRepository.findByName("");

		assertFalse(category.isPresent());
	}

}
