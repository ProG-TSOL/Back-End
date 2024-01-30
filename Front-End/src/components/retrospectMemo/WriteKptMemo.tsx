import React, { useRef, useState } from "react";

interface KPTMemoProps {
  modalOpen: boolean;
  setModalOpen: (value: boolean) => void;
}

const KPTMemo: React.FC<KPTMemoProps> = ({ modalOpen, setModalOpen }) => {
  const modalBackground = useRef<HTMLDivElement>(null);
  const [selectedSection, setSelectedSection] = useState<string | null>("Keep");
  const [textareaValue, setTextareaValue] = useState<string>("");
  const [showConfirmationModal, setShowConfirmationModal] =
    useState<boolean>(false);
  const [nextSection, setNextSection] = useState<string | null>(null);

  const handleSectionClick = (section: string) => {
    if (textareaValue) {
      setShowConfirmationModal(true);
      setNextSection(section);
    } else {
      setSelectedSection(section);
    }
  };

  const confirmSectionChange = () => {
    setSelectedSection(nextSection);
    setTextareaValue("");
    setShowConfirmationModal(false);
  };

  const getBorderColorClass = () => {
    switch (selectedSection) {
      case "Keep":
        return "border-green-500"; // 초록색 테두리
      case "Problem":
        return "border-red-500"; // 빨간색 테두리
      case "Try":
        return "border-main-color"; // 메인 색상 테두리
      default:
        return "border-main-color"; // 기본 테두리 색상
    }
  };
  
  return (
    <div>
      {modalOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-10"
          ref={modalBackground}
          onClick={(e) => {
            if (e.target === modalBackground.current) {
              setModalOpen(false);
            }
          }}
        >
          <div className="bg-white p-5 rounded-lg shadow-lg w-11/12 max-w-2xl">
            <div className="flex">
              <div
                className="flex-1 p-4"
                onClick={() => handleSectionClick("Keep")}
              >
                <h2 className="text-center">Keep</h2>
              </div>
              <div className="border-main-color border-r
              "></div>
              <div
                className="flex-1 p-4"
                onClick={() => handleSectionClick("Problem")}
              >
                <h2 className="text-center">Problem</h2>
              </div>
              <div className="border-main-color border-r"></div>
              <div
                className="flex-1 p-4"
                onClick={() => handleSectionClick("Try")}
              >
                <h2 className="text-center">Try</h2>
              </div>
            </div>
            {selectedSection && (
              <div className="mt-4">
                <textarea
                  className={`w-full h-32 p-2 rounded border-2 ${getBorderColorClass()}`}
                  value={textareaValue}
                  onChange={(e) => setTextareaValue(e.target.value)}
                ></textarea>
              </div>
            )}
            <div className="flex justify-center mt-4">
              <button
                className="modal-close-btn border-2 mr-2 border-main-color"
                onClick={() => setModalOpen(false)}
              >
                회고 등록
              </button>
              <button
                className="modal-close-btn border-2 border-main-color"
                onClick={() => setModalOpen(false)}
              >
                닫기
              </button>
            </div>
          </div>
        </div>
      )}
      {showConfirmationModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-20">
          <div className="bg-white p-5 rounded-lg shadow-lg">
            <p>진짜 초기화할까요?</p>
            <div className="flex mt-4">
              <button
                className="flex-1 border-2 border-indigo-700 mr-2"
                onClick={confirmSectionChange}
              >
                확인
              </button>
              <button
                className="flex-1 border-2 border-red-600"
                onClick={() => setShowConfirmationModal(false)}
              >
                취소
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default KPTMemo;
