import { useState } from "react";
import { FaPencil } from "react-icons/fa6";

{/* 지금은 Type을 이렇게 지정했지만, 나중에 API 입력값에 맞게 변경해야함, 단순 에러 지우는 용도로 지정*/}
type ProjectType = "ongoing" | "completed";

const MyProject = () => {
  const [selectedProjectType, setSelectedProjectType] = useState("ongoing");

  const handleProjectTypeChange = (type: ProjectType) => {
    setSelectedProjectType(type);
  };

  return (
    <div className="flex items-center mb-4">
      {/* My Profile과 Pencil 아이콘 부분 */}
      <div className="flex flex-col items-start mr-10">
        <div className="flex items-center">
          <p className="font-mainFont font-semibold mr-2">My Project</p>
          <FaPencil
            size="28"
            className="cursor-pointer text-gray-500 mr-40"
            onClick={() => handleProjectTypeChange("ongoing")}
          />
        </div>
      </div>
      <p
        className={`ml-40 mr-40 ${
          selectedProjectType === "ongoing"
            ? "font-semibold font-mainFont text-main-color"
            : ""
        }`}
        onClick={() => handleProjectTypeChange("ongoing")}
      >
        나의 프로젝트
      </p>
      <p
        className={`ml-40 mr-40 ${
          selectedProjectType === "completed"
            ? "font-semibold font-mainFont text-main-color"
            : ""
        }`}
        onClick={() => handleProjectTypeChange("completed")}
      >
        완료한 프로젝트
      </p>


    </div>
  );
};

export default MyProject;
