import CategoryBubble from "../src/CategoryBubble.jsx";
import { useState, useEffect } from "react";
import ExpenseList from "../src/ExpenseList.jsx";

const CategoryList = () => {
  const [selected, setSelected] = useState("");
  const [categories, setCategories] = useState([]);

  const style = {
    display: "flex",
    flexDirection: "column",
    justifyContent: "flex-start",
    backgroundColor: "grey",
    width: "300px",
  };

  useEffect(() => {
    fetch("http://localhost:8080/categories", {
      method: "GET",
    })
      .then((response) => response.json())
      .then((data) => setCategories(data));
  }, []);

  // emitting/setter passing could be avoided by handling clicking in this component - this is intentional
  return (
    <>
      <ul style={style}>
        {categories.map((category, index) => (
          <CategoryBubble
            key={index}
            categoryName={category.name}
            isSelected={category.name === selected}
            handleButtonClick={setSelected}
          />
        ))}
      </ul>
      <section>
        <p>Expenses of category:</p>
        <ExpenseList category={selected} />
      </section>
    </>
  );
};

export default CategoryList;
