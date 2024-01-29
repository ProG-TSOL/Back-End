import { FaPencil } from "react-icons/fa6";

const MyProfile = () => {

  return (
    <div>
      {/* 내용 부분 추후에 data. 정보 추가*/}
      <div className="flex">
        {/* My Profile과 Pencil 아이콘 부분 */}
        <div className="flex flex-col items-start mr-10">
          <div className="flex items-center mb-4">
            <p className="font-mainFont font-semibold mr-2">My Profile</p>
            <FaPencil size="28" className="cursor-pointer text-gray-500" />
          </div>
        </div>

        {/* 입력 필드 부분 */}
        <div className="flex-1">
          {/* 이메일 필드 */}
          <div className="mb-4">
            <p className="mb-2 font-mainFont">이메일</p>
            <input className="w-96 p-2 border border-gray-300 rounded" />
          </div>
          {/* 이름 필드 */}
          <div className="mb-4">
            <p className="mb-2 font-mainFont">이름</p>
            <input className="w-96 p-2 border border-gray-300 rounded" />
          </div>
          {/* 닉네임 필드 */}
          <div className="mb-4">
            <p className="mb-2 font-mainFont">닉네임</p>
            <input className="w-96 p-2 border border-gray-300 rounded" />
          </div>
          {/* 비밀번호 변경 필드 */}
          <div className="flex items-center mb-4">
            <div>
              <p className="flex mb-2 font-mainFont">비밀번호 변경</p>
              <input
                type="password"
                className="w-80 p-2 border border-gray-300 rounded"
              />
            </div>
            <button className="ml-1 mt-8 bg-main-color hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
              변경
            </button>
          </div>
        </div>
        <div className="flex-1 flex-col">
          <div className="mb-4">
            <p className="mb-2">나의 소개</p>
            <textarea className="w-96 h-32 p-2 border border-gray-300 rounded" />
          </div>
          <div>
            <p className="font-mainFont text-lg mb-2">관심있는 기술 태그</p>
            <p className="font-mainFont mb-4">
              사용 중인 기술이나 관심있는 기술을 선택해주세요
            </p>
          </div>
        </div>
      </div>

    </div>
  );
};

export default MyProfile;
