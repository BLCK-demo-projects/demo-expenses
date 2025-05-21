const CategoryBubble = ({ categoryName, isSelected, handleButtonClick }) => {
  const buttonStyle = {
    backgroundColor: isSelected ? "green" : "white",
    color: isSelected ? "white" : "black",
    border: "2px solid black",
    margin: "3px",
  };

  const flex = {
    display: "flex",
    flexDirection: "column",
  };

  return (
    <>
      <div style={flex}>
        <button onClick={() => handleButtonClick(categoryName)} style={buttonStyle}>
          {categoryName}
        </button>
      </div>
    </>
  );
};

export default CategoryBubble;
