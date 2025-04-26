package com.blck.demo_expenses.Exceptions;

public class CategoryNotFoundException extends RuntimeException {
	public CategoryNotFoundException(String categoryName) {
		super("Category not found: " + categoryName);
	}
}
