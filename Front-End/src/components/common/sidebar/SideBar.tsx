import { Link, useLocation } from "react-router-dom";
// import { useAttendanceStartMutation } from "../../../apis/useAttendanceStartMutation";

interface SideTab {
  label: string;
  path: string;
  onClick?: () => void;
}

// const { mutate: startAttendance } = useAttendanceStartMutation();

const SideBar = () => {
  const location = useLocation();
  const sideTabs: SideTab[] = [
    { label: "홈", path: "/project" },
    { label: "근태", path: "/project/commute" },
    { label: "업무", path: "/project/task" },
    { label: "피드", path: "/project/feed" },
    { label: "회고", path: "/project/retrospect" },
  ];

  return (
    <div className="flex flex-col">
      <div
        className="flex flex-col bg-sub-color w-52 items-center justify-evenly"
        style={{ height: "calc(100vh - 64px)" }}
      >
        {/* 출퇴근 */}
        <div className="flex justify-center space-x-2">
          <button
            className="flex w-20 h-20 bg-white rounded-2xl"
            // onClick={() => startAttendance("나의 프로젝트id")}
          >
            출근
          </button>
          <button className="flex w-20 h-20 bg-white rounded-2xl">퇴근</button>
        </div>

        {/* 메뉴 */}
        <div className="flex flex-col justify-center items-center">
          {sideTabs.map((tab, index) => (
            <Link
              key={index}
              to={tab.path}
              className={`my-2 p-2 text-lg ${
                location.pathname === tab.path
                  ? "text-main-color underline underline-offset-4 hover:text-main-color hover:underline-offset-4"
                  : "text-slate-500"
              }`}
              onClick={tab.onClick}
            >
              {tab.label}
            </Link>
          ))}
        </div>
      </div>
    </div>
  );
};

export default SideBar;
