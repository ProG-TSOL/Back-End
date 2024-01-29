import axios from "axios";
import React, { ChangeEvent, FormEvent, useState } from "react";
import ProgImage from "../../assets/logo.png";

interface UserData {
  email: string;
  certificationNo: string;
  password: string;
  name: string;
  nickname: string;
}

export const SignUpForm: React.FC = () => {
  const [userEmail, setUserEmail] = useState<string>("");
  const [emailValid, setEmailValid] = useState(true);
  const [certificationNo, setCertificationNo] = useState("");
  const [password, setPassword] = useState<string>("");
  const [passwordChk, setPasswordChk] = useState<string>("");
  const [name, setName] = useState("");
  const [nickname, setNickname] = useState("");

  const signUpSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    // API 통신 로직 (현재는 예시로만 존재)

    const userData = {
      email: userEmail,
      certificationNo: certificationNo,
      password: password,
      name: name,
      nickname: nickname,
    };

    try {
      const response = await axios.post("백엔드 URL", userData);
      console.log(response.data);
      // 성공적인 응답 처리
    } catch (error) {
      console.error("회원가입 오류", error);
      // 에러 처리
    }

    // 폼 제출 후 입력 필드 초기화
    setUserEmail("");
    setCertificationNo("");
    setPassword("");
    setPasswordChk("");
    setName("");
    setNickname("");
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

  const getEmailValidationMessage = () => {
    if (userEmail === "") {
      return (
        <p className="text-blue-500 text-xs ml-60">이메일을 입력해주세요!</p>
      );
    } else if (!emailValid) {
      return (
        <p className="text-red-500 text-xs ml-60">
          유효한 이메일 형식이 아닙니다!
        </p>
      );
    } else {
      return <p className="text-green-500 text-xs ml-60">멋진 이메일이네요!</p>;
    }
  };

  const validatePassword = (password: string): boolean => {
    const re = /^(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,16}$/;
    return re.test(password);
  };

  const isPasswordMatching = (): boolean => {
    return password === passwordChk && password !== "";
  };

  return (
    <div className="flex items-center justify-center h-screen">
      <div className="w-1/2 border-solid border-2 p-10 rounded-lg bg-white shadow-md">
        <div className="flex items-center justify-center">
          <img src={ProgImage} alt="로고 이미지" className="h-16" />
          <p className="text-2xl font-bold">ProG</p>
        </div>
        <p className="m-2 mb-10 text-center text-3xl font-bold">회원가입</p>
        <form onSubmit={signUpSubmit} className="space-y-3">
          <p className="font-bold ml-60">이메일</p>
          <div>
            <div className="flex justify-center items-center">
              <input
                id="email-input"
                type="text"
                value={userEmail}
                onChange={handleEmailChange}
                placeholder="id@example.com"
                className={`w-72 ${inputClass}`}
              />
              <button
                id="certi-button"
                type="button"
                className={`w-20 ml-5 ${buttonClass}`}
              >
                인증
              </button>
            </div>
          </div>
          {getEmailValidationMessage()}
          <p className="font-bold ml-60">이름</p>
          <div className="ml-60">
            <input
              id="name-input"
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="박개굴"
              className={`${inputClass} w-72`}
            />
          </div>
          <p className="font-bold ml-60">닉네임</p>
          <div className="ml-60">
            <input
              id="nickname-input"
              type="text"
              value={nickname}
              onChange={(e) => setNickname(e.target.value)}
              placeholder="프로그박"
              className={`w-72 ${inputClass}`}
            />
            <button
              id="certi-button"
              type="button"
              className={`w-20 ml-5 ${buttonClass}`}
            >
              검사
            </button>
          </div>
          <p className="font-bold ml-60">인증번호</p>
          <div className="ml-60">
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
              className={`w-20 ml-5 ${buttonClass}`}
            >
              확인
            </button>
          </div>
          <p className="font-bold ml-60">비밀번호</p>
          <div className="ml-60">
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
              }-500 text-xs`}
            >
              {!validatePassword(password)
                ? "비밀번호는 8~16자리의 특수문자를 포함해야 합니다."
                : "훔쳐봐도 모를 것 같은 비밀번호!!"}
            </p>
          )}
          <p className="font-bold ml-60">비밀번호 확인</p>
          <div className="ml-60">
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
              }-500 text-xs`}
            >
              {isPasswordMatching()
                ? "비밀번호가 일치합니다!"
                : "비밀번호가 일치하지 않습니다!"}
            </p>
          )}
          <div className="flex justify-center">
            <button
              id="sign-up-button"
              type="submit"
              className={`w-40 mt-8 ${buttonClass}`}
            >
              회원가입
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default SignUpForm;
