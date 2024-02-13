import { useState } from "react";

const SignUpInfo = () => {
  const [isHovered, setIsHovered] = useState(false);

  return (
    <div className="relative flex justify-center items-center mb-3">
      <div
        className="bg-gray-200 rounded-full w-4 h-4 flex justify-center items-center cursor-pointer hover:bg-gray-300"
        onMouseEnter={() => setIsHovered(true)}
        onMouseLeave={() => setIsHovered(false)}
      >
        ?
      </div>
      {isHovered && (
        <div className="absolute left-full ml-2 bottom-1/2 transform translate-y-1/2 p-4 bg-white text-sm text-gray-800 shadow-lg rounded-md w-96">
          인증 번호 : 사용자의 이메일에서 인증번호를 확인
          <hr />
          <p>비밀번호 : 영문,숫자,특수기호로 구성</p>
          <hr />
          이름 : 영어와 한글로 구성
          <hr />
          닉네임 : 영어, 한글, 숫자로 구성
        </div>
      )}
    </div>
  );
};

export default SignUpInfo;
