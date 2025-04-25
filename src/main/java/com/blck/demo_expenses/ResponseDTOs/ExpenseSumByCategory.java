package com.blck.demo_expenses.ResponseDTOs;

public record ExpenseSumByCategory(
		String categoryFK,
		Double sumOfExpenses
) {}
