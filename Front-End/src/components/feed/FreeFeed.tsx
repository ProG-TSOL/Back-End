import { useState } from "react";
import { FaPlus, FaMapPin, FaEllipsisVertical } from "react-icons/fa6";
import ModalEditor from "./ModalEditor";
import FeedDetail from "./FeedDetail";
import ModiDetail from "./ModiDetail";
import { axiosInstance } from "../../apis/lib/axios";

const FreeFeed = () => {
  const [showDetail, setShowDetail] = useState(false);
  const [rotateIcon, setRotateIcon] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editorContent, setEditorContent] = useState("");
  const [showModiDelete, setShowModiDelete] = useState(false);
  const [showModiDetail, setShowModiDetail] = useState(false);

  const handleEllipsisClick = (event: React.MouseEvent) => {
    event.stopPropagation(); // 상위 버전으로 이벤트 전파 중단
    setShowModiDelete((prev) => !prev);
  };

  const handleIconClick = () => {
    setRotateIcon(!rotateIcon);
    setIsModalOpen(true);
  };

  const handleDetailClick = () => {
    setShowDetail(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setRotateIcon(false); // rotateIcon 상태를 직접 false로 설정
  };

  const editorChange = (content: string) => {
    setEditorContent(content);
  };

  const handleModifyClick = (event: React.MouseEvent) => {
    event.stopPropagation();
    setShowModiDetail(true);
  };

  // ModiDetail 저장 버튼 핸들러
  const handleSaveModiDetail = (newContent: string) => {
    console.log("새로운 내용:", newContent);
    // 여기에 API 요청 로직 또는 다른 상태 업데이트 로직을 추가할 수 있습니다.
    setShowModiDetail(false); // 모달 닫기
  };

  const handleCloseModiDetail = () => {
    setShowModiDetail(false);
  };

  const handleDelete = (event: React.MouseEvent) => {
    // 삭제하기 버튼 눌렀을 때 요청.
    event.stopPropagation();
  };

  const handleModalSubmit = (title: string, content: string) => {
    // POST 요청 로직
    axiosInstance
      .post("여기 내일 확인", {
        title,
        content,
      })
      .then(() => {
        // 성공 처리
      })
      .catch(() => {
        // 에러 처리
      });
  };

  return (
    <div className="flex flex-col h-screen">
      <div className="p-4 w-auto mt-10 m-60 border-2 border-gray-200 shadow-lg rounded-lg flex-grow">
        <div
          onClick={handleDetailClick}
          className="mt-2 gap-4 border-2 border-main-color rounded-lg p-4"
        >
          {/* flex를 사용하여 주요 컨텐츠를 양쪽으로 정렬 */}
          <div className="flex justify-between items-center">
            {/* 프사, 닉네임, 날짜 */}
            <div className="flex gap-4">
              <p>프사</p>
              <p>닉네임</p>
              <p>날짜</p>
            </div>
            <div className="flex gap-2 relative">
              <FaMapPin className="text-xl" />
              <FaEllipsisVertical
                className="text-xl"
                color="#4B33E3"
                onClick={handleEllipsisClick}
              />
              {showModiDelete && (
                <div className="absolute w-20 right-0 top-full bg-white shadow-md rounded-md mt-1">
                  <button onClick={handleModifyClick}>수정하기</button>
                  <button onClick={handleDelete}>삭제하기</button>
                </div>
              )}
              {showModiDetail && (
                <ModiDetail
                  initialContent="초기 내용"
                  onSave={handleSaveModiDetail}
                  onClose={handleCloseModiDetail}
                />
              )}{" "}
            </div>
          </div>
          <p className="mt-2">여기에 제목이 들어갑니다. map.filter로</p>
        </div>
        {showDetail && <FeedDetail onClose={() => setShowDetail(false)} />}{" "}
      </div>
      <button
        onClick={handleIconClick}
        className={`fixed bottom-20 right-96 transform transition-transform duration-500 ${
          rotateIcon ? "rotate-45" : "rotate-0"
        }`}
      >
        <FaPlus className="size-10" />
      </button>
      <ModalEditor
        isOpen={isModalOpen}
        onClose={closeModal}
        value={editorContent}
        onChange={editorChange}
        onSubmit={handleModalSubmit}
      />
    </div>
  );
};

export default FreeFeed;
