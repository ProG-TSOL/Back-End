import { useState, useEffect } from "react";
import { axiosInstance } from "../../apis/lib/axios";
import { useRequireAuth } from "../../hooks/useRequireAuth";

export const techStack = {
  mystack: [] as { techCode: number }[],
};

const TechStack = () => {
  useRequireAuth()
  const [tags, setTags] = useState<{ id: number; detailName: string }[]>([]);
  // Updated to include both id and techCode (detailName)
  const [selectedTags, setSelectedTags] = useState<{ id: number; techCode: string }[]>([]);
  const [selectedValue, setSelectedValue] = useState<string>("");

  useEffect(() => {
    const getTags = async () => {
      try {
        const response = await axiosInstance.get("/codes/details/Tech");
        if (response.data.status === "OK") {
          setTags(response.data.data.map(({ id, detailName }: { id: number; detailName: string }) => ({ id, detailName })));
        }
      } catch (error) {
        console.error("tag failed:", error);
      }
    };

    getTags();
  }, []);

  const putTag = () => {
    const selectedTag = tags.find(tag => tag.detailName === selectedValue);
    if (selectedTag && !selectedTags.some(tag => tag.id === selectedTag.id)) {
      const newTag = { id: selectedTag.id, techCode: selectedTag.detailName };
      setSelectedTags((prevTags) => [...prevTags, newTag]);
      techStack.mystack.push({ techCode: selectedTag.id });
      setSelectedValue("");
    }
  };

  const removeTag = (idToRemove: number) => {
    setSelectedTags((prevTags) => {
      const updatedTags = prevTags.filter(tag => tag.id !== idToRemove);
      techStack.mystack = updatedTags.map(tag => ({ techCode: tag.id }));
      return updatedTags;
    });
  };

  return (
    <div>
      <div className="text-lg font-bold">기술 스택</div>
      <div className="h-10 w-full bg-gray-100">
        {selectedTags.map((item, index) => (
          <span
            key={index}
            className="bg-gray-200 p-1 m-1 inline-block cursor-pointer"
            onClick={() => removeTag(item.id)}
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
          <option value='default'>기술 스택 선택</option>
          {tags.map((tag, index) => (
            <option key={index} value={tag.detailName}>
              {tag.detailName}
            </option>
          ))}
        </select>

        <button
          onClick={putTag}
          className="mt-5 bg-main-color text-white p-2 ml-2"
        >
          등록
        </button>
      </div>
    </div>
  );
};

export default TechStack;
