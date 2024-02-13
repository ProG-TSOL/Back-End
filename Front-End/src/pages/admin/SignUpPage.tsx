import React, { ChangeEvent, FormEvent, useState } from "react";
import ProgImage from "../../assets/logo.png";
import { axiosInstance } from "../../apis/lib/axios";
import Timer from "../../components/alarm/Timer";
import SignUpInfo from "../../components/alarm/SignUpInfo";

// interface UserData {
//   email: string;
//   certificationNo: string;
//   password: string;
//   name: string;
//   nickname: string;
// }

export const SignUpForm: React.FC = () => {
  const [userEmail, setUserEmail] = useState<string>("");
  const [emailValid, setEmailValid] = useState(true);
  const [certificationNo, setCertificationNo] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [passwordChk, setPasswordChk] = useState<string>("");
  const [name, setName] = useState<string>("");
  const [nickname, setNickname] = useState<string>("");
  const [isTimerActive, setIsTimerActive] = useState<boolean>(false);

  const signUpSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    const userData = {
      email: userEmail,
      password: password,
      passwordCheck: passwordChk,
      authCode: certificationNo,
      name: name,
      nickname: nickname,
    };

    try {
      const response = await axiosInstance.post("/members/sign-up", userData);
      console.log(response.data);
    } catch (error) {
      console.error("회원가입 오류", error);
    }

    // 폼 제출 후 입력 필드 초기화
    setUserEmail("");
    setCertificationNo("");
    setPassword("");
    setPasswordChk("");
    setName("");
    setNickname("");
  };

  const emailVerification = async () => {
    console.log(userEmail);
    const veriEmail = {
      email: userEmail,
    }
    try {
      startTimer();
      await axiosInstance.post(
        "/members/email-verification",
        veriEmail
        );
      console.log("이메일 사용 가능");
    } catch (error) {
      console.log("이메일 오류");
    }
  };
  
  const emailVerificationConfirm = async () => {
    const veridata = {
      email: userEmail,
      authCode: certificationNo,
    }
    console.log(veridata)
    try {
      await axiosInstance.post(
        "/members/email-verification-confirm",
        veridata
        );
        
// 인증 콘솔 지우고, Modal창으로 OK 해주기

        console.log("인증 완료");
      } catch (error) {
        console.log("인증 안됨 ㅠ");
      }
    };
    
    const chkNickNameDuplication = async () => {
      try {
        await axiosInstance.post(
          "/members/nickname-validation-check",
          nickname
        );
  
        console.log("닉네임 사용 가능");
      } catch (error) {
        console.log("닉네임 중복 오류");
      }
    };

    // TailWind는 따로 클래스로 빼서 사용할 수 있음..
    const inputClass = "border-solid border-2 border-gray-300 p-2 rounded";
    const buttonClass =
    "bg-main-color hover:bg-purple-400 text-white font-bold p-2 rounded";

  const validateEmail = (email: string) => {
    const re = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    return re.test(email);
  };

  // TypeScript에서 사용되는 타입으로, React의 이벤트 타입 중 하나입니다. 이 타입은 HTML의 <input> 요소에서 발생하는 변경 이벤트를 나타냅니다. ChangeEvent는 변경 이벤트에 대한 정보를 포함하고 있으며, <input>에 특화된 세부 정보를 포함하는 제네릭 타입
  const handleEmailChange = (e: ChangeEvent<HTMLInputElement>) => {
    const email = e.target.value;
    setUserEmail(email);
    setEmailValid(validateEmail(email) || email === "");
  };

  const startTimer = () => {
    setIsTimerActive(true);
  };

  const handleTimerComplete = () => {
    console.log("Timer Completed!");
    setIsTimerActive(false);
  };

  const getEmailValidationMessage = () => {
    if (userEmail === "") {
      return (
        <p className="text-blue-500 text-xs mr-64">이메일을 입력해주세요!</p>
      );
    } else if (!emailValid) {
      return (
        <p className="text-red-500 text-xs mr-48 pr-5">
          유효한 이메일 형식이 아닙니다!
        </p>
      );
    } else {
      return (
        <p className="text-green-500 text-xs mr-60 pr-9">멋진 이메일이네요!</p>
      );
    }
  };

  const validatePassword = (password: string): boolean => {
    const re =
      /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,16}$/;
    return re.test(password);
  };

  const validateName = (name: string): boolean => {
    const re = /^[A-Za-z가-힣]+$/;
    return re.test(name);
  };

  const validateNickName = (nickname: string): boolean => {
    const re = /^[A-Za-z0-9ㄱ-ㅎㅏ-ㅣ가-힣]+$/;
    return re.test(nickname);
  };

  const isPasswordMatching = (): boolean => {
    return password === passwordChk && password !== "";
  };

  const isFormValid = () => {
    return (
      userEmail !== "" &&
      emailValid &&
      certificationNo !== "" &&
      password !== "" &&
      validatePassword(password) &&
      password === passwordChk &&
      name !== "" &&
      validateName(name) &&
      nickname !== "" &&
      validateNickName(nickname)
    );
  };

  const getButtonClass = () => {
    if (isFormValid()) {
      return `w-40 mt-8 bg-main-color hover:bg-purple-400 text-white font-bold p-2 rounded`; // 활성화 상태
    } else {
      return `w-40 mt-8 bg-gray-400 text-white font-bold p-2 rounded`; // 비활성화 상태
    }
  };

  return (
    <div className="mt-20 mb-20">
      <div className="flex items-center justify-center h-screen">
        <div className="w-1/2 border-solid border-2 p-10 rounded-lg bg-white shadow-md">
          <div className="flex flex-col items-center justify-center">
            <div className="flex items-center justify-center mb-10">
              <img src={ProgImage} alt="로고 이미지" className="h-16" />
              <p className="text-2xl font-bold">ProG</p>
            </div>
            <div className="flex">
            <p className="m-2 mb-10 text-center text-3xl font-bold">회원가입</p>
            <SignUpInfo/>
            </div>
            <form
              onSubmit={signUpSubmit}
              className="w-full flex flex-col items-center"
            >
              <p className="font-bold mr-80 pr-5 mb-3">이메일</p>
              <div className="w-full flex justify-center">
                <input
                  id="email-input"
                  type="text"
                  value={userEmail}
                  onChange={handleEmailChange}
                  placeholder="id@example.com"
                  className={`w-72 ${inputClass}`}
                />
                <button
                  type="button"
                  onClick={emailVerification}
                  disabled={!emailValid || userEmail === ""}
                  className={`w-20 ml-5 ${
                    emailValid && userEmail !== ""
                      ? buttonClass
                      : "bg-gray-400 text-white font-bold p-2 rounded"
                  }`}
                >
                  인증
                </button>
              </div>
              {getEmailValidationMessage()}
              {isTimerActive && (
                <Timer
                  initialSeconds={300}
                  isActive={isTimerActive}
                  onComplete={handleTimerComplete}
                />
              )}
              <p className="font-bold mr-80 pr-1 mt-3 mb-3">인증번호</p>
              <div className="w-full flex justify-center">
                <input
                  id="certi-input"
                  type="text"
                  value={certificationNo}
                  onChange={(e) => setCertificationNo(e.target.value)}
                  placeholder="6자리 인증번호 입력"
                  className={`w-72 ${inputClass}`}
                />
                <button
                  id="chk-button"
                  type="button"
                  onClick={emailVerificationConfirm}
                  className={`w-20 ml-5 ${buttonClass}`}
                >
                  확인
                </button>
              </div>
              <p className="font-bold mr-80 pr-1 mt-3 mb-3">비밀번호</p>
              <div className="w-full flex justify-center ml-7 mr-32 space-y-3">
                <input
                  id="pw-input"
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="8~16자리 비밀번호를 입력하세요"
                  className={`w-72 ${inputClass}`}
                />
              </div>
              {password !== "" && (
                <p
                  className={`text-${
                    validatePassword(password) ? "black" : "red"
                  }-500 text-xs ${
                    validatePassword(password) ? "mr-60 ml-8" : "mr-40 ml-16"
                  }`}
                >
                  {!validatePassword(password)
                    ? "비밀번호는 8~16자리의 특수문자를 포함해야 합니다."
                    : "훔쳐봐도 모를 것 같은 비밀번호!!"}
                </p>
              )}
              <p className="font-bold mr-80 ml-8 mt-3 mb-3">비밀번호 확인</p>
              <div className="w-full flex justify-center ml-7 mr-32 space-y-3">
                <input
                  id="pwChk-input"
                  type="password"
                  value={passwordChk}
                  onChange={(e) => setPasswordChk(e.target.value)}
                  placeholder="위와 동일하게 입력해주세요"
                  className={`w-72 ${inputClass}`}
                />
              </div>
              {password !== "" && passwordChk !== "" && (
                <p
                  className={`text-${
                    isPasswordMatching() ? "blue" : "red"
                  }-500 text-xs mr-56`}
                >
                  {isPasswordMatching()
                    ? "비밀번호가 일치합니다!"
                    : "비밀번호가 일치하지 않습니다!"}
                </p>
              )}
              <p className="font-bold mr-80 pr-8 mb-3">이름</p>
              <div className="w-full flex justify-center ml-7 mr-32 space-y-3">
                <input
                  type="text"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  placeholder="박개굴"
                  className={`${inputClass} w-72`}
                />
              </div>
              <p className="font-bold mr-80 pr-5 mt-3 mb-3">닉네임</p>
              <div className="w-full flex justify-center">
                <input
                  type="text"
                  value={nickname}
                  onChange={(e) => setNickname(e.target.value)}
                  placeholder="프로그박"
                  className={`w-72 ${inputClass}`}
                />
                <button
                  id="nickname-check-button"
                  type="button"
                  onClick={chkNickNameDuplication} // 닉네임 중복 검사 함수를 onClick 이벤트에 연결합니다.
                  className={`w-20 ml-5 ${buttonClass}`}
                >
                  검사
                </button>
              </div>

              <div className="flex justify-center">
                <button
                  id="sign-up-button"
                  type="submit"
                  className={getButtonClass()}
                  disabled={!isFormValid}
                >
                  회원가입
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SignUpForm;
