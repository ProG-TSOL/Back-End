import { useEffect, useState } from "react";
import {
  FaAnglesRight,
  FaRegCircleUser,
  FaClockRotateLeft,
} from "react-icons/fa6";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css"; // ReactQuill 스타일시트 임포트
import { axiosInstance } from "../../apis/lib/axios";
import { useParams } from "react-router-dom";
import moment from "moment";
import TaskDatePicker from "../calendar/TaskDatePicker";

interface ChecklistItem {
  id: number;
  text: string;
  isChecked: boolean;
}

const DetailTask = () => {
  const { projectId } = useParams();

  const [editorContent, setEditorContent] = useState("");
  const [checklist, setChecklist] = useState<ChecklistItem[]>([]);
  const [newItemText, setNewItemText] = useState("");
  const [isOpen, setIsOpen] = useState(true);
  const [nickname, setNickname] = useState("");

  // const postTask = async () => {
  //   const taskInfo = {
  //     projectId: projectId, 
  //     producerId: nickname,
  //     statusCode: ,
  //     typeCode: ,
  //     priorityCode: ,
  //     consumerId: ,
  //     title: ,
  //     content: editorContent,
  //     startDay: now(),
  //     endDay: ,
  //   }

  //   try {
  //     await axiosInstance.post("/works")
  //   } catch (error) {
  //     console.log(error);
  //   }
  // }

  const editorChange = (content: string) => {
    setEditorContent(content);
  };

  const handleChecklistChange = (id: number) => {
    setChecklist(
      checklist.map((item) =>
        item.id === id ? { ...item, isChecked: !item.isChecked } : item
      )
    );
  };

  const addChecklistItem = () => {
    setChecklist([
      ...checklist,
      { id: Date.now(), text: newItemText, isChecked: false },
    ]);
    setNewItemText("");
  };

  const handleClose = () => {
    setIsOpen(!isOpen);
  };

  const calculateProgress = () => {
    const totalItems = checklist.length;
    const completedItems = checklist.filter((item) => item.isChecked).length;
    return totalItems > 0 ? (completedItems / totalItems) * 100 : 0;
  };

  const progressPercentage = calculateProgress();

  const removeChecklistItem = (id: number) => {
    setChecklist(checklist.filter((item) => item.id !== id));
  };

  useEffect(() => {
    // 로컬 스토리지에서 userProfile을 가져옴
    const userProfileString = localStorage.getItem("userProfile");
    if (userProfileString) {
      const userProfile = JSON.parse(userProfileString);
      setNickname(userProfile.nickname); // 닉네임 상태 업데이트
    }
  }, []);

  if (!isOpen) return null;

  return (
    <>
      <div
        className={`fixed inset-0 bg-black bg-opacity-50 z-40 ${
          !isOpen && "hidden"
        }`}
        onClick={handleClose}
      ></div>
      <div
        className={`fixed right-0 top-0 h-full bg-slate-50 p-4 rounded-md shadow-lg z-50 w-full max-w-2xl transition-transform transform ${
          isOpen ? "translate-x-0" : "translate-x-full"
        } ease-in-out duration-300`}
      >
        <div className="flex items-center mt-10 mb-4">
          <FaAnglesRight
            onClick={handleClose}
            className="text-lg text-gray-600 mr-2 cursor-pointer"
          />
          <h1 className="text-xl font-bold">상세 업무 등록</h1>
        </div>

        <div className="flex items-center mb-4">
          <FaRegCircleUser className="text-gray-600 mr-2" />
          <p className="text-md">업무 요청자: {nickname}</p>
          <p className="text-md ml-4">업무 등록 시작일: {moment().format('YYYY-MM-DD')}</p>
        </div>

        <p className="font-semibold mb-2">업무 제목</p>
        <input
          type="text"
          className="border border-gray-300 rounded p-2 mr-2 w-full"
        />

        <hr />

        <div className="my-4">
          <FaClockRotateLeft className="text-gray-600 mr-2" />
          {/* 상태코드 변경 로직 */}
        </div>

        <div className="mb-4">
          <FaRegCircleUser className="text-gray-600 mr-2" />
          {/* 담당자 변경 로직 */}
        </div>
        <TaskDatePicker/>
        <div className="flex flex-col">
          <div className="mb-4">
            <ReactQuill
              className="h-40"
              theme="snow"
              value={editorContent}
              onChange={editorChange}
            />
          </div>
          <div className="flex items-center mt-10 mb-4">
            <div className="w-full bg-gray-200 rounded h-4">
              <div
                className="bg-main-color h-4 rounded"
                style={{ width: `${progressPercentage}%` }}
              ></div>
            </div>
            <span className="ml-2">{progressPercentage.toFixed(0)}%</span>
          </div>
        </div>
        <div className="mb-4">
          <h2 className="font-semibold mb-2">하위업무</h2>
          <ul className="list-disc pl-6">
            {checklist.map((item) => (
              <li key={item.id} className="flex items-center mb-2">
                <input
                  type="checkbox"
                  checked={item.isChecked}
                  onChange={() => handleChecklistChange(item.id)}
                />
                <span className={item.isChecked ? "line-through" : ""}>
                  {item.text}
                </span>

                <button
                  className="ml-2 text-red-500"
                  onClick={() => removeChecklistItem(item.id)}
                >
                  X
                </button>
              </li>
            ))}
          </ul>

          <div className="flex mt-2">
            <input
              type="text"
              className="border border-gray-300 rounded p-2 mr-2 w-96"
              value={newItemText}
              onChange={(e) => setNewItemText(e.target.value)}
            />
            <button
              className="bg-blue-500 text-white p-2 rounded"
              onClick={addChecklistItem}
            >
              추가
            </button>
          </div>
          <button className="mt-2 bg-blue-500 text-white p-2 rounded">
            업무 등록
          </button>
        </div>
      </div>
    </>
  );
};

export default DetailTask;
