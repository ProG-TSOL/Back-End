import { useState } from "react";

const ChangePwInfo = () => {
  const [isHovered, setIsHovered] = useState(false);

  return (
    <div className="relative flex justify-center items-center ml-1 mb-3">
      <div
        className="bg-gray-200 rounded-full w-4 h-4 flex justify-center items-center cursor-pointer hover:bg-gray-300"
        onMouseEnter={() => setIsHovered(true)}
        onMouseLeave={() => setIsHovered(false)}
      >
        ?
      </div>
      {isHovered && (
        <div className="absolute left-full ml-2 bottom-1/2 transform translate-y-1/2 p-4 bg-white text-sm text-gray-800 shadow-lg rounded-md w-96">
          1. 영어, 숫자, 특수문자로 8~16자로 구성해주세요
          <br/>
          2. 현재 비밀번호와 변경할 비밀번호가 달라야 해요
          <br/>
          3. 변경 비밀번호 확인은 변경 비밀번호와 동일하게!
        </div>
      )}
    </div>
  );
};

export default ChangePwInfo;
