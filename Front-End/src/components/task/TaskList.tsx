import TaskIndex from "./TaskIndex";

const Test = () => {
  return (
    <div className="flex justify-center mt-5">
      <div className="w-3/4 bg-white shadow-lg rounded-lg overflow-hidden">
        <div className="flex">
          <div className="border-main-color border-2 w-1/2 flex-grow p-4">
            <span className="font-bold text-xl">업무명</span>
          </div>
          <div className="border-main-color border-2 w-1/6 p-4 text-center">
            <span className="font-bold text-xl">상태</span>
          </div>
          <div className="border-main-color border-2 w-1/6 p-4 text-center">
            <span className="font-bold text-xl">담당자</span>
          </div>
          <div className="border-main-color border-2 w-1/12 p-4 text-center">
            <span className="font-bold text-xl">시작</span>
          </div>
          <div className="border-main-color border-2 w-1/12 p-4 text-center">
            <span className="font-bold text-xl">마감</span>
          </div>
        </div>
        <TaskIndex title="1. 분석 🔮" />
        <TaskIndex title="2. 설계 🎯" />
        <TaskIndex title="3. 개발 👩‍💻" />
        <TaskIndex title="4. 테스트 🕵️‍♀️" />
        <TaskIndex title="5. 기타 🎸" />
      </div>
    </div>
  );
};

export default Test;
