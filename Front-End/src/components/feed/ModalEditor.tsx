// ModalEditor.jsx
import { useRef, FC, useState } from "react";
import ReactQuill from "react-quill";

type ModalEditorProps = {
  isOpen?: boolean; // ?를 붙이면, 선택 가능한 Type이 됨.
  onClose?: () => void;
  value: string;
  onChange: (content: string) => void;
  onSubmit: (title: string, content: string) => void;
};

const ModalEditor: FC<ModalEditorProps> = ({
  isOpen = true,
  onClose = () => {}, //기본값을 빈 함수로 설정
  value,
  onChange,
  onSubmit,
}) => {
  const quillRef = useRef<ReactQuill>(null);
  const [title, setTitle] = useState("");
  if (!isOpen) return null;

  const handleTitleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setTitle(e.target.value);
  };

  const handleSubmit = () => {
    onSubmit(title, value); // 제목과 내용을 백엔드로 보냄
    onClose(); // 모달 닫기
  };

  const modules = {
    toolbar: [
      [{ header: [1, 2, false] }],
      ["bold", "italic", "underline", "strike", "blockquote"],
      [
        { list: "ordered" },
        { list: "bullet" },
        { indent: "-1" },
        { indent: "+1" },
      ],
      ["link", "image"],
      [{ align: [] }, { color: [] }, { background: [] }],
      ["clean"],
    ],
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
      <div className="bg-white p-6 rounded-lg shadow-xl max-w-md w-full">
        <div className="mb-4">
          <p className="text-lg font-bold mb-2">제목</p>
          <input
            className="w-full p-2 border border-gray-300 rounded"
            placeholder="제목을 입력하세요"
            value={title}
            onChange={handleTitleChange}
          />
        </div>
        <p className="mb-2">내용</p>
        <div className="mb-20">
          <ReactQuill
            ref={quillRef}
            className="h-80"
            modules={modules}
            value={value}
            onChange={onChange}
          />
        </div>
        <div className="flex justify-end space-x-4 mt-4">
          <button
            className="bg-main-color text-white p-2 rounded hover:bg-blue-700 transition duration-300"
            onClick={handleSubmit}
          >
            저장
          </button>
          <button
            className="bg-red-500 text-white p-2 rounded hover:bg-red-700 transition duration-300"
            onClick={onClose}
          >
            닫기
          </button>
        </div>
      </div>
    </div>
  );
};

export default ModalEditor;
