package com.blck.demo_expenses.DB;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
public class Expense {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private float amount;

	private String description;

	private Date date;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;



	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}


}
