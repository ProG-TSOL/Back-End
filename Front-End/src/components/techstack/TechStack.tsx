import React from 'react';

export const techStack = {
  mystack: [] as string[],
};

const tags = ["JAVA", "REACT", "VITE", "VUE", "TypeScript", "HTML", "CSS", "SASS", "SCSS", "JavaScript"];

const TechStack = () => {
  const putTag = () => {
    const selectElement = document.getElementById("techStack") as HTMLSelectElement;
    const selectedValue = selectElement.value;

    if (selectedValue && techStack.mystack.indexOf(selectedValue) === -1) {
      techStack.mystack.push(selectedValue);
      forceUpdate();
    }
  };

  const removeTag = (tagToRemove: string) => {
    const index = techStack.mystack.indexOf(tagToRemove);

    if (index !== -1) {
      techStack.mystack.splice(index, 1);
      forceUpdate();
    }
  };

  const forceUpdate = () => {
    // A dummy function for forceUpdate in functional component
  };

  return (
    <div>
      <div className='text-lg font-bold'>기술 스택</div>
      <div className='h-10 w-5/6 bg-gray-100'>
        {techStack.mystack.map((item, index) => (
          <span
            key={index}
            className="bg-gray-200 p-1 m-1 inline-block cursor-pointer"
            onClick={() => removeTag(item)}
          >
            {item} X
          </span>
        ))}
      </div>
      <div>
        <select id="techStack" className="mt-2 p-2">
          {tags.map((tag, index) => (
            <option key={index} value={tag}>
              {tag}
            </option>
          ))}
        </select>
        <button onClick={putTag} className="mt-5 bg-main-color text-white p-2 ml-2">
          등록
        </button>
      </div>
    </div>
  );
};

export default TechStack;
