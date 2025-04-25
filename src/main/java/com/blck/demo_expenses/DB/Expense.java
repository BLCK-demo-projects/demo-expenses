package com.blck.demo_expenses.DB;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
public class Expense {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private double amount;

	private String name;

	private Date date;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "category_id")
	private Category categoryFK;

	public Expense() {}

	public Expense(ExpenseDTO expenseDTO, Category category) {
		setAmount(expenseDTO.amount());
		setName(expenseDTO.name());
		setDate(expenseDTO.date());
		setCategoryFK(category);
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String description) {
		this.name = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Category getCategoryFK() {
		return categoryFK;
	}

	public void setCategoryFK(Category category) {
		this.categoryFK = category;
	}


}
