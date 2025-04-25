package com.blck.demo_expenses.DB;

import java.util.Date;

public record ExpenseDTO(
		String name,
		double amount,
		Date date,
		String categoryFK
) {}
