import { FaMapPin, FaEllipsisVertical } from "react-icons/fa6";

const FreeFeed = () => {
  return (
    <div className="flex flex-col h-screen">
      <div className="p-4 w-auto mt-10 m-60 border-2 border-gray-200 shadow-lg rounded-lg flex-grow">
        <div className="mt-2 gap-4 border-2 border-main-color rounded-lg p-4">
          {/* flex를 사용하여 주요 컨텐츠를 양쪽으로 정렬 */}
          <div className="flex justify-between items-center">
            {/* 프사, 닉네임, 날짜 */}
            <div className="flex gap-4">
              <p>프사</p>
              <p>닉네임</p>
              <p>날짜</p>
            </div>
            {/* 아이콘 */}
            <div className="flex gap-2">
              <FaMapPin className="text-xl" />
              <FaEllipsisVertical className="text-xl"/>
            </div>
          </div>
          <p className="mt-2">여기에 제목이 들어갑니다. map.filter로</p>
        </div>
      </div>
    </div>
  );
};

export default FreeFeed;
