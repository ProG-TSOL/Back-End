import React, { FormEvent, useState } from "react";
import { Link } from "react-router-dom";
import ProgImage from "../../assets/logo.png";

export const LoginForm: React.FC = () => {
  const [username, setUserName] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    // API 호출을 통한 로그인 처리
    try {
      const response = await fetch("YOUR_API_ENDPOINT", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }),
      });

      if (!response.ok) {
        throw new Error("로그인 실패");
      }

      const data = await response.json();
      // 토큰 처리나 로그인 후의 로직을 여기에 구현합니다.
      console.log(data);
    } catch (error) {
      console.error(error);
    }

    setUserName("");
    setPassword("");
  };

  return (
    <div className="flex items-center justify-center h-screen">
      <div className="border-solid border-2 p-4 rounded-lg">
        <div className="flex items-center justify-center">
          <img src={ProgImage} alt="로고 이미지" className="h-16" />
          <span className="text-3xl font-bold">Prog</span>
        </div>
        <p className="m-2 text-center">환영합니다!</p>
        <p className="text-center">로그인을 통해</p>
        <p className="mb-4 text-center">
          당신의 프로젝트와 꿈으로 나아가보세요
        </p>
        <form onSubmit={handleSubmit} className="m-2">
          <div>
            <label htmlFor="id-input" />
            <input
              id="id-input"
              type="text"
              value={username}
              onChange={(e) => setUserName(e.target.value)}
              placeholder="이메일을 입력하세요"
              className="border-solid border-2 border-gray-300 w-full p-2 m-1"
            />
          </div>
          <div>
            <label htmlFor="pw-input" />
            <input
              id="pw-input"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="비밀번호를 입력하세요"
              className="border-solid border-2 border-gray-300 w-full p-2 m-1"
            />
          </div>
          <button
            id="login-button"
            type="submit"
            className="w-full bg-main-color hover:bg-purple-400 text-white font-bold py-2 px-4 rounded m-1"
          >
            Login
          </button>
        </form>
        <p className="text-center">계정이 없으신가요?</p>
        <Link to="/signup" className="text-center block underline">
          회원가입
        </Link>
        <hr className="my-4" />
        <a className="inline-block w-full bg-white text-gray-800 py-2 px-4 rounded hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-800 mb-2">
          GitHub으로 로그인하기
        </a>
        <a className="inline-block w-full bg-white text-gray-800 py-2 px-4 rounded hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-800">
          Google로 로그인하기
        </a>
      </div>
    </div>
  );
};

export default LoginForm;
