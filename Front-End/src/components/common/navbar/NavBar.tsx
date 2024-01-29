//react-modules
import { Link, useLocation } from "react-router-dom";

//react-icons
import { FaBell } from "react-icons/fa6";

interface Tab {
  label: string;
  path: string;
  logo?: React.ElementType;
  onClick?: () => void;
}

const Nav = () => {
  // const [activeTab, setActiveTab] = useState("");
  const location = useLocation();

  const tabs: Tab[] = [
    { label: "홈", path: "/", logo: FaBell },
    { label: "프로젝트 모집", path: "/recruit" },
    { label: "내 프로젝트", path: "/project" },
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
            //path를 포함하고 있다면 text 색깔을 main color로 바꿔줌
            className={
              location.pathname.startsWith(tab.path)
                ? "text-main-color"
                : "text-slate-500"
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
