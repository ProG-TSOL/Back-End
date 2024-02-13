import { useEffect, useState } from "react";
import { FaPencil, FaUser } from "react-icons/fa6";
import ChangePw from "./ChangePw";
import TechStack from "../techstack/TechStack";
import { axiosInstance } from "../../apis/lib/axios";

interface ProfileData {
  email: string;
  name: string;
  nickname: string;
  description: string;
}

const MyProfile = () => {
  const [changeName, setchangeName] = useState<string>("");
  const [changeNickname, setChangeNickname] = useState<string>("");
  const [changeDescription, setChangeDescription] = useState<string>("");
  const [showModal, setShowModal] = useState<boolean>(false); // 모달 표시 상태
  const [isEditMode, setIsEditMode] = useState<boolean>(false);
  const [isHovering, setIsHovering] = useState<boolean>(false);
  const [profileData, setProfileData] = useState<ProfileData>({
    email: "",
    name: "",
    nickname: "",
    description: "",
  });
  const [loadProfile, setLoadProfile] = useState<boolean>(true); // 프로필 데이터를 불러올지 여부를 결정하는 상태
  const [id, setId] = useState<string>("");

	// 모달 표시/숨김 함수
	const toggleModal = () => {
		setShowModal(!showModal);
	};

	const toggleEditMode = () => {
		setIsEditMode(!isEditMode);
	};

  const profileGet = async () => {
    try {
      const response = await axiosInstance.get(
        `members/detail-profile/${id}`
      ); // 지금 여기 id로 바뀜
      const data = response.data.data;
      console.log(data);
      setProfileData({
        email: data.email,
        name: data.name,
        nickname: data.nickname,
        description: data.description,
      });
    } catch (error) {
      console.log(error);
    } finally {
      setLoadProfile(false);
    }
  };

  const changeProfile = async () => {
    const profileData = {
      name: changeName,
      nickname: changeNickname,
      description: changeDescription,
      memberTechDtoList: [],
    };

    const form = new FormData();
    const formData = JSON.stringify(profileData);
    form.append("member", new Blob([formData], { type: "application/json" }));
    try {
      await axiosInstance.patch("/members/update-profile", form, {});
      setIsEditMode(!isEditMode);
      setLoadProfile(true);
    } catch (error) {
      console.log(error);
    }
  };

  const chkNickNameDuplication = async () => {
    try {
      await axiosInstance.post("/members/nickname-validation-check", {
        nickname: changeNickname,
      });

      console.log("닉네임 사용 가능");
    } catch (error) {
      console.log("닉네임 중복 오류");
    }
  };

  const handleMouseEnter = () => {
    setIsHovering(true);
  };

  const handleMouseLeave = () => {
    setIsHovering(false);
  };

  // 컴포넌트 마운트 시 프로필 데이터 가져오기
  useEffect(() => {
    const userProfileString = localStorage.getItem("userProfile");
    if (userProfileString) {
      const userProfile = JSON.parse(userProfileString);
      setId(userProfile.id); // 닉네임 상태 업데이트
    }

    profileGet();
  }, [loadProfile]);

  return (
    <div>
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
        환영합니다. {profileData.nickname} 님
      </p>
      {/* 내용 부분 추후에 data. 정보 추가*/}
      <div className="flex">
        {/* My Profile과 Pencil 아이콘 부분 */}
        <div className="flex flex-col items-start mr-10">
          <div className="flex items-center mb-4">
            <p className="font-mainFont font-semibold mr-2">My Profile</p>
            <FaPencil
              size="28"
              onClick={toggleEditMode}
              className="cursor-pointer text-gray-500"
            />
          </div>
          {isEditMode && (
            <button
              className="ml-10 bg-main-color hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
              onClick={changeProfile}
            >
              수정
            </button>
          )}
        </div>
        {/* 입력 필드 부분 */}
        <div className="flex-1">
          {/* 이메일 필드 */}
          <div className="mb-4">
            <p className="mb-2 font-mainFont">이메일</p>
            <p>{profileData.email}</p>
          </div>
          {/* 이름 필드 */}
          <div className="mb-4">
            <p className="mb-2 font-mainFont">이름</p>
            {isEditMode ? (
              <input
                value={changeName}
                onChange={(e) => setchangeName(e.target.value)}
                className="w-96 p-2 border border-gray-300 rounded"
              />
            ) : (
              <p>{profileData.name}</p> // axios.get을 통해 받아온 데이터 표시
            )}
          </div>
          {/* 닉네임 필드 */}
          <div className="mb-4">
            <p className="mb-2 font-mainFont">닉네임</p>
            {isEditMode ? (
              <>
                <input
                  value={changeNickname}
                  onChange={(e) => setChangeNickname(e.target.value)}
                  className="w-80 p-2 border border-gray-300 rounded"
                />
                <button
                  onClick={chkNickNameDuplication}
                  className="ml-1 mt-8 bg-main-color hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                >
                  검사
                </button>
              </>
            ) : (
              <p>{profileData.nickname}</p> // axios.get을 통해 받아온 데이터 표시
            )}
          </div>
          {/* 비밀번호 변경 필드 */}
          <div className="flex items-center mb-4">
            <div>
              <p className="flex mb-2 font-mainFont">비밀번호 변경</p>
              <input
                type="password"
                placeholder="●●●●●●●●●●●●●●●"
                className="w-80 p-2 border border-gray-300 rounded"
                disabled
              />
            </div>
            <button
              onClick={toggleModal}
              className="ml-1 mt-8 bg-main-color hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
            >
              변경
            </button>
          </div>
        </div>
        <div className="flex-1 flex-col">
          <div className="mb-4">
            <p className="mb-2">나의 소개</p>
            {isEditMode ? (
              <textarea
                value={changeDescription}
                onChange={(e) => setChangeDescription(e.target.value)}
                className="w-96 h-32 p-2 border border-gray-300 rounded"
              />
            ) : (
              <p>{profileData.description}</p>
            )}
          </div>
          <div>
            <p className="font-mainFont text-lg mb-2">관심있는 기술 태그</p>
            <p className="font-mainFont mb-4">
              사용 중인 기술이나 관심있는 기술을 선택해주세요
            </p>
            <TechStack />
          </div>
        </div>
        {showModal && <ChangePw onClose={toggleModal} />}
      </div>
    </div>
  );
};

export default MyProfile;
