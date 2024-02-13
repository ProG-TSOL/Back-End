import { useState, FC } from "react";

type ModiDetailProps = {
  initialContent: string;
  onSave: (newContent: string) => void;
  onClose: () => void;
};

const ModiDetail: FC<ModiDetailProps> = ({ initialContent, onSave, onClose }) => {
  const [content, setContent] = useState(initialContent);

  const handleSave = () => {
    onSave(content);
    onClose();
  };

  return (
    <div className="fixed inset-0 bg-gray-200 bg-opacity-50 flex justify-center items-center z-50">
      <div className="bg-white p-6 rounded-lg shadow-xl max-w-md w-full">
        <textarea
          className="w-full h-64 p-2 border border-gray-300 rounded"
          value={content}
          onChange={(e) => setContent(e.target.value)}
        />
        <div className="flex justify-end mt-4 space-x-2">
          <button
            onClick={handleSave}
            className="bg-blue-500 text-white p-2 rounded hover:bg-blue-700 transition duration-300"
          >
            저장
          </button>
          <button
            onClick={onClose}
            className="bg-red-500 text-white p-2 rounded hover:bg-red-700 transition duration-300"
          >
            닫기
          </button>
        </div>
      </div>
    </div>
  );
};

export default ModiDetail;
