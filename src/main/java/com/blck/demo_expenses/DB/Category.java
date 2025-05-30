package com.blck.demo_expenses.DB;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private String name;

	@OneToMany(mappedBy = "categoryFK", cascade = CascadeType.REMOVE)
	@JsonManagedReference
	private Set<Expense> expenses;

	public Category() {}

	public Category(CategoryDTO categoryDTO) {
		setName(categoryDTO.name());
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Expense> getExpenses() {
		return expenses;
	}

	public void setExpenses(Set<Expense> expenses) {
		this.expenses = expenses;
	}
}
