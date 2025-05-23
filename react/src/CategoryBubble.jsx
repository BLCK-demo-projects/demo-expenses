const CategoryBubble = ({ categoryName, isSelected, handleButtonClick }) => {
  const buttonStyle = {
    backgroundColor: isSelected ? "#ffa925" : "white",
    color: "black",
    border: "2px solid black",
    margin: "3px",
    width: "250px",
  };

  return (
    <>
      <button onClick={() => handleButtonClick(categoryName)} style={buttonStyle}>
        {categoryName}
      </button>
    </>
  );
};

export default CategoryBubble;
