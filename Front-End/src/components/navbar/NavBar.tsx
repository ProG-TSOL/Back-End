//react-modules
import React, { useEffect } from "react";
import { Link, useLocation } from "react-router-dom";
import { useState } from "react";

//react-icons
import { FaBell } from "react-icons/fa6";

interface Tab {
  label: string;
  path: string;
  logo?: React.ElementType;
  onClick?: () => void;
}

const Nav = () => {
  const [activeTab, setActiveTab] = useState("");
  const location = useLocation();

  useEffect(() => {
    //컴포넌트가 마운트 or 경로 변경 시마다 activeTab을 업데이트
    const currentTab = tabs.find((tab) => tab.path === location.pathname);
    setActiveTab(currentTab ? currentTab.label : "");
  }, [location]);
  //의존성 배열에 location 추가, 경로가 변경될 때마다 이 효과 다시 실행

  const tabs: Tab[] = [
    { label: "홈", path: "/", logo: FaBell },
    { label: "프로젝트 모집", path: "/recruit" },
    { label: "내 프로젝트", path: "/myproject" },
    { label: "마이페이지", path: "/mypage", logo: FaBell },
    { label: "로그인", path: "/login" },
  ];

  return (
    <div className="fixed flex bg-slate-200 w-full h-16">
      <div className="flex space-x-2 items-center">
        {tabs.map((tab, index) => (
          <Link
            key={index}
            to={tab.path}
            className={
              activeTab === tab.label ? "text-main-color" : "text-slate-500"
            }
          >
            {tab.label}
          </Link>
        ))}
      </div>
    </div>
  );
};

export default Nav;
