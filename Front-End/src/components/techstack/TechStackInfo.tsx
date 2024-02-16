import { useState } from 'react';

const TechStackInfo = () => {
  const [isHovered, setIsHovered] = useState(false);

  return (
    <div className="relative flex mb-2 justify-center items-center">
      <div
        className="bg-gray-200 rounded-full w-8 h-8 flex justify-center items-center cursor-pointer hover:bg-gray-300"
        onMouseEnter={() => setIsHovered(true)}
        onMouseLeave={() => setIsHovered(false)}
      >
        ?
      </div>
      {isHovered && (
        <div className="absolute left-full ml-2 bottom-1/2 transform translate-y-1/2 p-4 bg-white text-sm text-gray-800 shadow-lg rounded-md w-64">
          Lv1 : 프로젝트의 기본 구조와 원리를 배우기 시작한 단계
          <hr/>
          Lv2 : 프로젝트에 기능을 추가할 수 있는 단계
          <hr/>
          Lv3 : 다양한 기술을 학습, 프로젝트의 중요한 부분을 설계하는 단계
          <hr/>
          Lv4 : 프로젝트의 핵심 기능과 아키텍쳐 설계의 기여하는 단계
          <hr/>
          Lv5 : 여러 프로젝트를 만들어내고 영향력을 펼치는 단계
        </div>
      )}
    </div>
  );
};

export default TechStackInfo;
