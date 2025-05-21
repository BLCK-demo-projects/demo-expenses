import CategoryBubble from "../src/CategoryBubble.jsx";
import { useState } from "react";

const CategoryList = ({ categories }) => {
  const [selected, setSelected] = useState("Bills");

  const style = {
    display: "flex",
    flexDirection: "column",
    justifyContent: "flex-start",
    backgroundColor: "grey",
    width: "200px",
  };

  // emitting/setter passing could be avoided by handling clicking in this component - this is intentional
  return (
    <>
      <ul style={style}>
        {categories.map((name, index) => (
          <CategoryBubble key={index} categoryName={name} isSelected={name === selected} handleButtonClick={setSelected} />
        ))}
      </ul>
    </>
  );
};

export default CategoryList;
