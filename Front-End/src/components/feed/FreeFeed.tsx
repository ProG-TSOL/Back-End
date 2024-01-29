import { FaMapPin, FaEllipsisVertical } from "react-icons/fa6";

const FreeFeed = () => {
  return (
    <div className="flex flex-col h-screen">
      <div className="p-4 w-auto mt-10 m-60 border-2 border-gray-200 shadow-lg rounded-lg flex-grow">
        <div className="mt-2 gap-4 border-2 border-main-color rounded-lg">
          <div className="flex">
            <div className="flex">
              <p>프사</p>
              <p>닉네임</p>
              <p>날짜</p>
            </div>
            <div className="flex">
              <FaMapPin className="size-10" />
              <FaEllipsisVertical className="size-10"/>
            </div>
          </div>
          <p>여기에 제목이 들어갑니다. map.filter로</p>
        </div>
      </div>
    </div>
  );
};

export default FreeFeed;
