import React from 'react';

interface TaskOneDetailProps {
  taskDetail: {
    id: number;
    task: string;
    state: string;
    asignee: string;
    startDate: string;
    endDate: string;
  };
  onClose: () => void; // 모달을 닫는 함수
}

const TaskOneDetail: React.FC<TaskOneDetailProps> = ({ taskDetail, onClose }) => {
  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
      <div className="bg-white p-6 rounded-lg shadow-xl max-w-md w-full">
        <h2 className="text-xl font-bold mb-4">{taskDetail.task}</h2>
        <p className="mb-2"><strong>상태:</strong> {taskDetail.state}</p>
        <p className="mb-2"><strong>담당자:</strong> {taskDetail.asignee}</p>
        <p className="mb-2"><strong>시작 일자:</strong> {taskDetail.startDate}</p>
        <p className="mb-2"><strong>종료 일자:</strong> {taskDetail.endDate}</p>
        <div className="text-right mt-4">
          <button
            className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-700"
            onClick={onClose}
          >
            닫기
          </button>
        </div>
      </div>
    </div>
  );
};

export default TaskOneDetail;
