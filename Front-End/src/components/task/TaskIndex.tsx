import React, { useState } from "react";
import { FaCaretDown, FaCaretRight } from "react-icons/fa6";
import DetailTask from "./DetailTask"; // 업무 추가 모달 컴포넌트, 경로 확인 필요
import TaskOneDetail from "./TaskOneDetail"; // 업무 상세 정보 모달 컴포넌트, 경로 확인 필요

type TodoListProps = {
  title: string;
};

interface Task {
  id: number;
  task: string;
  state: string;
  asignee: string;
  startDate: string; // Date 타입이 아닌 string으로 수정, 실제 사용 시 Date 타입으로 조정 가능
  endDate: string; // 마찬가지로 string 타입 사용
}

const TaskIndex: React.FC<TodoListProps> = ({ title }) => {
  const [showButton, setShowButton] = useState<boolean>(false);
  const [indexListOn, setIndexListOn] = useState<boolean>(false);
  const [showDetailTaskModal, setShowDetailTaskModal] = useState<boolean>(false);
  const [selectedTaskDetail, setSelectedTaskDetail] = useState<any | null>(null);

  const list = [
    {
      id: 1,
      task: "첫 번째 메모",
      state: "진행 중",
      asignee: "박은수",
      startDate: "01-31",
      endDate: "02-16",
    },
    {
      id: 2,
      task: "집에 일찍 가기",
      state: "완료",
      asignee: "박은수",
      startDate: "01-31",
      endDate: "02-16",
    },
    {
      id: 3,
      task: "야근하기",
      state: "시작 전",
      asignee: "정지원",
      startDate: "01-31",
      endDate: "02-16",
    },
  ];

  const handleIndexListOn = () => {
    setIndexListOn(!indexListOn);
  };

  const handleShowDetailTaskModal = () => {
    setShowDetailTaskModal((prev) => !prev);
  };

  const handleTaskClick = (taskDetail: Task) => {
    setSelectedTaskDetail(taskDetail);
  };

  return (
    <div>
      <div
        className="flex border border-main-color p-2 items-center"
        onMouseEnter={() => setShowButton(true)}
        onMouseLeave={() => setShowButton(false)}
      >
        <div onClick={handleIndexListOn}>
          {indexListOn ? <FaCaretRight className="text-xl" /> : <FaCaretDown className="text-xl" />}
        </div>
        <span className="text-xl font-bold">{title} ({list.length})</span>
        <button
          className={`ml-2 border-2 px-2 ${showButton ? 'block' : 'hidden'} transition-all duration-300`}
          onClick={handleShowDetailTaskModal}
        >
          업무 추가
        </button>
      </div>
      {!indexListOn && (
        <ul>
          {list.map((item) => (
            <li key={item.id} className="flex border p-2 cursor-pointer hover:bg-gray-100" onClick={() => handleTaskClick(item)}>
              <div className="w-1/2 flex-grow p-2">{item.task}</div>
              <div className="w-1/6 p-2 text-center">{item.state}</div>
              <div className="w-1/6 p-2 text-center">{item.asignee}</div>
              <div className="w-1/12 p-2 text-center">{item.startDate}</div>
              <div className="w-1/12 p-2 text-center">{item.endDate}</div>
            </li>
          ))}
        </ul>
      )}
        {selectedTaskDetail && (
          <TaskOneDetail taskDetail={selectedTaskDetail} onClose={() => setSelectedTaskDetail(null)} />
        )}
      {showDetailTaskModal && (
        <DetailTask onClose={() => setShowDetailTaskModal((prev) => !prev)} />
        )}
    </div>
  );
};

export default TaskIndex;
