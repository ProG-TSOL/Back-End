import { useState } from "react";
import { axiosInstance } from "../../apis/lib/axios";
import { FiLogOut } from "react-icons/fi"; // react-icons 라이브러리에서 아이콘 사용

const WithdrawalMember = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const withdrawMember = async () => {
    try {
      await axiosInstance.delete("/members/withdrawal-member");
      // 성공적으로 탈퇴 처리가 완료된 후의 로직 추가 (예: 홈페이지로 리다이렉트)
    } catch (error) {
      console.error(error);
      // 에러 처리 로직 추가
    }
  };

  const handleOpenModal = () => {
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
  };

  return (
    <div>
      <button
        onClick={handleOpenModal}
        title="회원 탈퇴"
        className="right-4 text-gray-600 hover:text-gray-800 font-bold py-2 px-4 rounded inline-flex items-center justify-center"
      >
        <FiLogOut size={24} /> {/* 아이콘 사용 */}
      </button>

      {isModalOpen && (
        <div
          className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full"
          id="my-modal"
        >
          <div className="relative top-80 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
            <div className="mt-3 text-center">
              <h3 className="text-lg leading-6 font-medium text-gray-900">
                정말로 탈퇴하시겠습니까?
              </h3>
              <div className="mt-2 px-7 py-3">
                <p className="text-sm text-gray-500">
                  이 작업은 되돌릴 수 없습니다.
                </p>
              </div>
              <div className="items-center px-4 py-3">
                <button
                  onClick={withdrawMember}
                  className="mt-3 mr-2 px-4 py-2 bg-red-600 text-white text-base font-medium rounded-md w-30 shadow-sm hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500"
                >
                  탈퇴하기
                </button>
                <button
                  onClick={handleCloseModal}
                  className="px-4 py-2 bg-gray-500 text-white text-base font-medium rounded-md w-20 shadow-sm hover:bg-gray-400 focus:outline-none focus:ring-2 focus:ring-gray-300"
                >
                  취소
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default WithdrawalMember;
