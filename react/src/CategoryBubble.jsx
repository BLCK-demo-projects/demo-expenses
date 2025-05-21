const CategoryBubble = ({ categoryName, isSelected, handleButtonClick }) => {
  const style = {
    backgroundColor: isSelected ? "green" : "white",
    color: isSelected ? "white" : "black",
    border: "2px solid black",
    margin: "3px",
  };

  return (
    <>
      <button onClick={() => handleButtonClick(categoryName)} style={style}>
        {categoryName}
      </button>
    </>
  );
};

export default CategoryBubble;
