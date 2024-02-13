// 필요한 아이콘 또는 컴포넌트를 import 합니다.
import { FC, useEffect, useState } from "react";
import { FaArrowLeft } from "react-icons/fa6";
import { useNavigate, useParams } from "react-router-dom";

interface SectionColors {
  [key: string]: string;
}

interface PrevRetrospectProps {
  onClick?: () => void; // 선택적 prop
}

const PrevRetrospect: FC<PrevRetrospectProps> = ( ) => {
  const navigate = useNavigate();
  const [selectedWeek, setSelectedWeek] = useState('1'); // 초기 선택 주차
  const [weeks, setWeeks] = useState<string[]>([]); // 주차 목록
  const { projectId } = useParams();
  // const getThisWeekKPT = async() => {
  //   try {
  //     await axiosInstance.get(`/retrospects/${}`)
  //   }
  // }

  useEffect(() => {
    // 예시: 프로젝트 기간이 10주라고 가정
    const projectWeeks = Array.from({ length: 10 }, (_, i) => (i + 1).toString());
    setWeeks(projectWeeks);
  }, []);

  const retrospects = [
    {
      section: "Keep",
      summaries: ["지속적으로 잘한 점 1", "지속적으로 잘한 점 2"],
    },
    {
      section: "Problem",
      summaries: ["개선이 필요한 점 1", "개선이 필요한 점 2"],
    },
    {
      section: "Try",
      summaries: ["시도해볼 액션 후보들 1", "시도해볼 액션 후보들 2"],
    },{
      section: "Action",
      summaries: ["시도해볼 액션 아이템 1", "시도해볼 액션 아이템 2"],
    },
  ];

  // 섹션 이름에 따른 배경색 클래스 매핑
  const sectionColors: SectionColors = {
    Keep: "bg-yellow-300",
    Problem: "bg-pink-300",
    Try: "bg-green-300",
    Action: "bg-blue-300", // Action에 대한 색상으로 blue-300을 선택
  };

  return (
    <div className="p-4 m-10 max-w-2xl mx-auto border-2 border-gray-200 shadow-lg rounded-lg">
      <div className="flex items-center mb-4">
        <FaArrowLeft
          onClick={() => navigate(`/project/${ projectId }/retrospect`)} // 후에 projectid params 받아서 유효한 링크로 나중에 라우팅 추가
          className="cursor-pointer text-main-color mr-4"
        />
        <h1 className="text-xl font-bold">이전 회고 보기</h1>
        <select
          className="ml-2 block appearance-none w-20 bg-white border border-gray-400 hover:border-gray-500 px-4 py-1 rounded shadow leading-tight focus:outline-none focus:shadow-outline"         value={selectedWeek}
          onChange={(e) => setSelectedWeek(e.target.value)}
        >
          {weeks.map(week => (
            <option key={week} value={week}>{`${week} 주차`}</option>
          ))}
        </select>
      </div>
      {retrospects.map((retro, index) => (
        <div key={index} className="mb-6">
          <h2 className="text-lg font-bold ml-4 mb-4">{retro.section}</h2>
          <ul className="list-disc list-inside">
            {retro.summaries.map((summary, idx) => (
              <li key={idx} className={`${sectionColors[retro.section]} p-2 rounded-md shadow mb-2`}>
                {summary}
              </li>
            ))}
          </ul>
        </div>
      ))}
    </div>
  );
};

export default PrevRetrospect;

