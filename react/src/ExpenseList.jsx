import { useState, useEffect, useRef } from "react";

const ExpenseList = ({ category }) => {
  const [allExpenses, setAllExpenses] = useState([]);

  const nameRef = useRef();
  const amountRef = useRef();
  const dateRef = useRef();

  const fetchExpenses = () => {
    fetch("http://localhost:8080/expenses", {
      method: "GET",
    })
      .then((response) => response.json())
      .then((data) => setAllExpenses(data))
      .catch((err) => console.error("Error fetching expenses:", err));
  };

  useEffect(() => {
    fetchExpenses();
  }, []);

  const expensesOfCategory = allExpenses.filter((expense) => expense.categoryFK === category);

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString(undefined, {
      year: "numeric",
      month: "long",
      day: "numeric",
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!category) {
      return;
    }

    const newExpense = {
      name: nameRef.current.value,
      amount: parseFloat(amountRef.current.value),
      date: new Date(dateRef.current.value).toISOString(),
      categoryFK: category,
    };

    fetch("http://localhost:8080/expenses", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(newExpense),
    })
      .then((res) => res.json())
      .then(() => {
        fetchExpenses();
        nameRef.current.value = "";
        amountRef.current.value = "";
        dateRef.current.value = "";
      })
      .catch((err) => console.error("Error posting expense:", err));
  };

  return (
    <>
      <form onSubmit={handleSubmit} className="formRow">
        <input type="text" placeholder="Expense" ref={nameRef} required />
        <input type="number" placeholder="Cost (€)" ref={amountRef} required />
        <input type="date" ref={dateRef} required />
        <button type="submit" className="addButton">
          Add Expense
        </button>
      </form>

      <ul>
        {expensesOfCategory.map((expense, index) => (
          <li className="expenseBubble" key={index}>
            {expense.name}, cost: {expense.amount} € ({formatDate(expense.date)})
          </li>
        ))}
      </ul>
    </>
  );
};

export default ExpenseList;
