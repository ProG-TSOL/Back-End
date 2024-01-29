import React, { useState, ChangeEvent } from "react";
import TechStack, { techStack } from "../../components/techstack/TechStack";
import Position, { position } from "../../components/position/Position";

interface State {
  projectTitle: string;
  projectContent: string;
  projectImage: string | null;
  projectPeriodNum: number;
  projectPeriodUnit: string;
}

const RecruitWritePage: React.FC = () => {
  const [state, setState] = useState<State>({
    projectTitle: "",
    projectContent: "",
    projectImage: null,
    projectPeriodNum: 0,
    projectPeriodUnit: "",
  });

  const handleInputChange = (
    e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    if (name === "projectPeriodNum") {
      setState((prev) => ({
        ...prev,
        projectPeriodNum: parseInt(value, 10) || 0,
      }));
    } else {
      setState((prev) => ({
        ...prev,
        [name]: value,
      }));
    }
  };

  const handleImageChange = (e: ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];

    if (file) {
      const imageUrl = URL.createObjectURL(file);
      setState((prev) => ({
        ...prev,
        projectImage: imageUrl,
      }));
    }
  };

  const handleUnitClick = (unit: string) => {
    setState((prev) => ({
      ...prev,
      projectPeriodUnit: unit,
    }));
  };

  const handleSave = () => {
    const { projectTitle, projectContent } = state;

    if (projectTitle === "") {
      alert("제목을 입력해주세요.");
      return;
    } else if (projectContent === "") {
      alert("본문을 입력해주세요.");
      return;
    } else if (position.posName.length === 0) {
      alert("한 개 이상의 포지션을 입력해주세요.");
      return;
    } else {
      for (let i = 0; i < position.posName.length; i++) {
        if (position.posName[i] === "") {
          alert("모든 포지션을 입력해주세요.");
          return;
        }
      }

      console.log("프로젝트 제목:", projectTitle);
      console.log("프로젝트 내용:", projectContent);
      console.log("기술스택:", techStack.mystack);
      console.log("프로젝트 이미지:", state.projectImage);
      console.log(
        "프로젝트 기간:",
        state.projectPeriodNum + state.projectPeriodUnit
      );
      console.log("포지션:", position);
    }
  };

  return (
    <div className="bg-gray-100 w-screen h-max grid place-items-center">
      <div className="bg-sub-color w-screen h-20 justify-center flex items-center font-bold text-4xl">
        새 프로젝트 생성
      </div>
      <div className="bg-gray-300 w-9/12 h-auto p-16 m-5 border-black border-2 ">
        <div>
          <label htmlFor="projectTitle" className="font-bold text-lg my-3">
            프로젝트 제목
          </label>
          <div>
            <input
              type="text"
              id="projectTitle"
              name="projectTitle"
              className="w-5/6 h-10"
              placeholder="제목을 입력해 주세요"
              onChange={handleInputChange}
            />
          </div>
        </div>
        <div className="my-3">
          <label htmlFor="projectContent" className="font-bold text-lg ">
            프로젝트 내용
          </label>
          <div>
            <textarea
              id="projectContent"
              name="projectContent"
              className="w-5/6 h-40"
              placeholder="내용을 입력해 주세요"
              onChange={handleInputChange}
            />
          </div>

          <TechStack />

          <div className="my-3">
            <label htmlFor="projectImage" className="font-bold text-lg">
              프로젝트 이미지 업로드
            </label>
            <input
              type="file"
              id="projectImage"
              name="projectImage"
              accept="image/*"
              onChange={handleImageChange}
              className="w-5/6 mt-2"
            />
            {state.projectImage && (
              <img
                src={state.projectImage}
                alt="Uploaded"
                className="mt-2 max-h-40"
              />
            )}
          </div>

          <div className="my-3">
            <label htmlFor="projectPeriod" className="font-bold text-lg my-3">
              프로젝트 기간
            </label>
            <div>
              <span className="p-2">약</span>
              <input
                type="text"
                id="projectPeriodNum"
                name="projectPeriodNum"
                className="w-20 h-10 p-1 mr-2"
                onChange={handleInputChange}
              />
              <button
                className={`p-2 ${
                  state.projectPeriodUnit === "일"
                    ? "bg-main-color text-white"
                    : ""
                }`}
                onClick={() => handleUnitClick("일")}
              >
                일
              </button>
              <button
                className={`p-2 ${
                  state.projectPeriodUnit === "주"
                    ? "bg-main-color text-white"
                    : ""
                }`}
                onClick={() => handleUnitClick("주")}
              >
                주
              </button>
              <button
                className={`p-2 ${
                  state.projectPeriodUnit === "달"
                    ? "bg-main-color text-white"
                    : ""
                }`}
                onClick={() => handleUnitClick("달")}
              >
                달
              </button>
            </div>
          </div>

          <Position />
        </div>
        <button
          onClick={handleSave}
          className="mt-5 bg-main-color text-white p-3"
        >
          저장
        </button>
      </div>
    </div>
  );
};

export default RecruitWritePage;
