import React, { useState } from 'react';

export const techStack = {
  mystack: [] as { techCode: string }[]
};

const tags = ["선택", "JAVA", "REACT", "VITE", "VUE", "TypeScript", "HTML", "CSS", "SASS", "SCSS", "JavaScript"];

const TechStack = () => {
  const [selectedTags, setSelectedTags] = useState<{ techCode: string }[]>([]);
  const [selectedValue, setSelectedValue] = useState<string>('');

  const putTag = () => {
    if (selectedValue && !selectedTags.some(tag => tag.techCode === selectedValue)) {
      const newTag = { techCode: selectedValue };
      setSelectedTags((prevTags) => [...prevTags, newTag]);
      techStack.mystack = [...techStack.mystack, newTag];
      setSelectedValue('');
    }
  };

  const removeTag = (tagToRemove: string) => {
    setSelectedTags((prevTags) => {
      const updatedTags = prevTags.filter((tag) => tag.techCode !== tagToRemove);
      techStack.mystack = updatedTags;
      return updatedTags;
    });
  };

  return (
    <div>
      <div className='text-lg font-bold'>기술 스택</div>
      <div className='h-10 w-5/6 bg-gray-100'>
        {selectedTags.map((item, index) => (
          <span
            key={index}
            className="bg-gray-200 p-1 m-1 inline-block cursor-pointer"
            onClick={() => removeTag(item.techCode)}
          >
            {item.techCode} X
          </span>
        ))}
      </div>
      <div>
        <select
          id="techStack"
          className="mt-2 p-2"
          value={selectedValue}
          onChange={(e) => setSelectedValue(e.target.value)}
        >
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
