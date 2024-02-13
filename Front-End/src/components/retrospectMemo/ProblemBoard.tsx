import React, { useState } from "react";
import ProblemDetail from "./ProblemDetail";

interface Memo {
  retrospectId: number;
  content: string;
}

interface BoardProps {
  memos: Memo[];
}

const ProblemBoard: React.FC<BoardProps> = ({ memos }) => {
  const [showDetailModal, setShowDetailModal] = useState(false);
  // +더보기 버튼을 클릭할 때 호출될 핸들러
  const handleMoreClick = () => {
    setShowDetailModal(true); // 모달 열기
  };

  return (
    <div className="p-4 m-10 max-w-4xl mx-auto border-2 border-gray-200 shadow-lg rounded-lg">
      <div className="flex justify-between items-center">
        <h2 className="text-lg font-bold">Problem</h2>
        {memos?.length > 0 && ( // 메모가 6개보다 많을 때만 +더보기 버튼 표시
          <button
            onClick={handleMoreClick}
            className="px-4 py-1 bg-blue-500 text-white rounded hover:bg-blue-700 transition-colors"
          >
            + 보기
          </button>
        )}
      </div>
      <div className="mt-4 grid grid-cols-3 gap-4">
        {memos?.slice(0, 3).map(
          (
            memo // 최대 6개의 메모만 표시
          ) => (
            <div
              key={memo.retrospectId}
              className="bg-red-300 p-4 rounded-md shadow transform rotate-2 hover:rotate-0 transition-transform duration-200 ease-in-out"
            >
              <p className="break-words text-black font-bold text-sm">{memo.content}</p>
            </div>
          )
        )}
      </div>
      <ProblemDetail
        isOpen={showDetailModal}
        onClose={() => setShowDetailModal(false)}
        memos={memos}
      />
    </div>
  );
};

export default ProblemBoard;
