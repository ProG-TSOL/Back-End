import { useState } from "react";
import { FaUser } from "react-icons/fa6";
import MyProfile from "../../components/mypage/MyProfile";
import MyProject from "../../components/mypage/MyProject";
import MyGit from "../../components/mypage/MyGit";


const MyPage = () => {
  const [isHovering, setIsHovering] = useState(false);

  const handleMouseEnter = () => {
    setIsHovering(true);
  };

  const handleMouseLeave = () => {
    setIsHovering(false);
  };

  return (
    <div className="p-4 pt-20">
      {/* 프로필 사진 부분 */}
      <div className="flex justify-center mb-4 relative">
        <div
          onMouseEnter={handleMouseEnter}
          onMouseLeave={handleMouseLeave}
          className="cursor-pointer"
        >
          <FaUser size="100" className="text-gray-500" />
          {isHovering && (
            <div className="absolute top-0 left-0 w-full h-full flex items-center justify-center bg-gray-700 bg-opacity-50">
              <div className="bg-white p-4 rounded">
                <p className="text-center text-gray-500">
                  클릭하여 프로필 사진을 변경해보세요
                </p>
              </div>
            </div>
          )}
        </div>
      </div>

      <p className="text-center mb-4 font-mainFont">
        환영합니다. {/* 나중에 UserName이 들어갑니다. */} 님
      </p>
      <MyProfile />
      <hr className="border-main-color border-1 mt-4 mb-5" />
      <MyProject />
      <hr className="border-main-color border-1 mt-4 mb-5" />
      <MyGit/>
    </div>
  );
};

export default MyPage;
