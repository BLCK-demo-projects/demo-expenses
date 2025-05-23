import CategoryBubble from "../src/CategoryBubble.jsx";
import { useState, useEffect } from "react";
import ExpenseList from "../src/ExpenseList.jsx";

const CategoryList = () => {
  const [selected, setSelected] = useState("");
  const [categories, setCategories] = useState([]);
  const [categoryInput, setCategoryInput] = useState("");

  const style = {
    display: "flex",
    flexDirection: "column",
    justifyContent: "flex-start",
    alignItems: "center",
    backgroundColor: "#dedede",
    borderRadius: "5px",
    padding: "5px",
    width: "300px",
    margin: "25px auto",
  };

  useEffect(() => {
    fetch("http://localhost:8080/categories", {
      method: "GET",
    })
      .then((response) => response.json())
      .then((data) => setCategories(data));
  }, []);

  const addCategory = () => {
    if (!categoryInput || categoryInput.length > 30) return;
    fetch("http://localhost:8080/categories", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ name: categoryInput }),
    })
      .then((response) => response.json())
      .then((newCategory) => {
        setCategories((prev) => [...prev, newCategory]);
        setCategoryInput("");
        setSelected(newCategory.name);
      });
  };

  const deleteCategory = () => {
    if (!selected) return;
    fetch(`http://localhost:8080/categories/${encodeURIComponent(selected)}`, {
      method: "DELETE",
    })
      .then(() => {
        setCategories((prev) => prev.filter((category) => category.name !== selected));
      })
      .catch((error) => {
        console.error("Error deleting category:", error);
      });
  };

  // emitting/setter passing could be avoided by handling clicking in this component - this is intentional
  return (
    <>
      <div className="horizontalInputs">
        <input onChange={(e) => setCategoryInput(e.target.value)} type="text" placeholder="Category name" />
        <button onClick={addCategory} className="addButton">
          Add category
        </button>
        <button onClick={deleteCategory} disabled={!selected} className="deleteButton">
          Delete selected
        </button>
      </div>

      <ul style={style}>
        <p>Categories:</p>
        {categories
          .slice()
          .sort((a, b) => a.name.localeCompare(b.name))
          .map((category, index) => (
            <CategoryBubble
              key={index}
              categoryName={category.name}
              isSelected={category.name === selected}
              handleButtonClick={setSelected}
            />
          ))}
      </ul>
      <section className="expenseList">
        <p>
          Expenses of category <strong>{selected}</strong>:
        </p>
        <ExpenseList category={selected} />
      </section>
    </>
  );
};

export default CategoryList;
