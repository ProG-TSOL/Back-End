import React from "react";

// 메모 객체에 대한 타입을 정의합니다.
interface Memo {
  id: number;
  text: string;
}

const ProblemBoard: React.FC = () => {
  const memos: Memo[] = [
    { id: 1, text: "첫 번째 메모" },
    { id: 2, text: "두 번째 메모" },
    { id: 3, text: "세 번째 메모" },
  ];

  return (
    <div className="p-4 max-w-4xl mx-auto border-2 border-gray-200 shadow-lg rounded-lg">
      <h2 className="text-lg font-bold ml-4 mb-4">Problem</h2>
      <div className="p-4 max-w-4xl mx-auto">
        <div className="grid grid-cols-3 gap-4">
          {memos.map((memo: Memo) => (
            <div
              key={memo.id}
              className="bg-pink-300 p-4 rounded-md shadow-lg transform rotate-2 hover:rotate-0 transition-transform duration-200 ease-in-out"
            >
              <p className="text-black font-bold text-sm">{memo.text}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default ProblemBoard;
