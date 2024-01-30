import React from "react";
import RecruitCard from "../../components/recruit/RecruitCard.tsx";
import RecruitSearchBar from "../../components/recruit/RecruitSearchBar.tsx";
import Pagination from "@mui/material/Pagination";

const RecruitPage: React.FC = () => {
  return (
    <React.StrictMode>
      <div className="grid text-center">
        <div className="font-bold text-6xl">프로젝트 찾기</div>
        <RecruitSearchBar />
      </div>
      <div className="grid grid-cols-4 gap-4 p-10 m-10">
        <RecruitCard />
        <RecruitCard />
        <RecruitCard />
        <RecruitCard />
        <RecruitCard />
        <RecruitCard />
        <RecruitCard />
        <RecruitCard />
        <RecruitCard />
        <RecruitCard />
      </div>
      <div className="flex justify-center mb-10">
        <Pagination count={30} variant="outlined" shape="rounded" />
      </div>
    </React.StrictMode>
  );
};
export default RecruitPage;

