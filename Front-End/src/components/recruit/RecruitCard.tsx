import React from 'react';

const thumbnail = "src/assets/react.svg";
const title = "프로젝트 제목";
const tags = ["JAVA", "REACT", "VITE", "VUE", "TypeScript", "HTML", "CSS", "SASS", "SCSS", "JavaScript"];
const myproject = false;
const maxpeople = 6;
const nowpeople = 3;
const status = "모집중";

const RecruitCard = () => {
  return (
    <div className="relative bg-gray-100 w-64 h-80 grid place-items-center border-black border-2">
      <div className="absolute top-2 left-5 bg-yellow-300 text-black font-bold px-2 py-1 rounded-lg">
        {status}
      </div>
      <img src={thumbnail} alt="React logo" className="w-56 h-40 bg-sub-color" />
      <div className='font-bold'>
        {title}
      </div>
      <div className="flex flex-wrap">
        {tags.slice(0, 3).map((tag, index) => (
          <span key={index} className="mr-2 p-1 bg-black text-white rounded-lg mb-1 hover:bg-pink-600">
            #{tag}
          </span>
        ))}
      </div>
      <div className="flex flex-wrap">
        {tags.slice(3, 6).map((tag, index) => (
          <span key={index} className="mr-2 p-1 bg-black text-white rounded-lg mb-1 hover:bg-pink-600">
            #{tag}
          </span>
        ))}
      </div>
      <div>
        {myproject === false ? (
          <button className='bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-2 rounded-lg'>
            내 프로젝트로
          </button>
        ) : (
          <span className="text-gray-600 font-semibold">
            모집인원 {nowpeople}/{maxpeople}
          </span>
        )}
      </div>
    </div>
  );
};

export default RecruitCard;
