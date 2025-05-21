import { useState } from "react";
import "./App.css";
import CategoryList from "../src/CategoryList.jsx";

const App = () => {
  const [count, setCount] = useState(0);

  const items = ["Water", "Bills", "Electricity"];

  return (
    <>
      <h2>React frontend</h2>
      <div className="card">
        <section>
          <p>Select a category to show its expenses and actions:</p>
          <CategoryList categories={items} />
        </section>
      </div>
    </>
  );
};

export default App;
