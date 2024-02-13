// RecruitCard.tsx
import React from 'react';
import { Link } from 'react-router-dom';
import useRecruitStore from '../../stores/useRecruitStore';
import { FaEye, FaHeart } from "react-icons/fa6";

const RecruitCard = () => {
  const { searchResults } = useRecruitStore();

  return (
    <div className='flex flex-wrap justify-center'>
      {searchResults.map((result) => (
        <div key={result.id} className='relative bg-gray-100 w-64 h-auto grid place-items-center border-black border-2 m-3'>
          <div className='flex justify-between w-auto mt-2'>
            <span className={`bg-yellow-300 text-black font-bold px-2 py-1 rounded-lg ${
                result.statusCode.detailDescription === '모집중' ? 'bg-yellow-200' :
                result.statusCode.detailDescription === '진행중' ? 'bg-green-200' :
                result.statusCode.detailDescription === '완료' ? 'bg-red-200' : ''}`}>
              {result.statusCode.detailDescription}
            </span>
            <div className='mx-3 bg-sub-color text-black font-bold px-2 py-1 rounded-lg flex h-8'>
              <span className='mx-1 pt-1'><FaEye /></span> <span>{result.viewCnt}</span>
            </div>
            <div className={`mx-1 text-black font-bold px-2 py-1 rounded-lg flex h-8 bg-sub-color`}>
              <span className='mx-1 pt-1'><FaHeart /></span> <span>{result.likeCnt}</span>
            </div>
          </div>

          <img src={result.projectImgUrl || 'default-thumbnail-url'} alt='Project Thumbnail' className='w-56 h-40 mt-2 bg-sub-color' />
          <div className='font-bold'>{truncate(result.title, 17)}</div>
          <div className='flex flex-wrap justify-center'>
            {result.techCodes.slice(0, 6).map((tag) => (
              <span key={tag.id} className='mr-2 p-1 bg-black text-white rounded-lg mb-1 hover:bg-pink-600'>
                #{tag.detailName}
              </span>
            ))}
          </div>
          <div className='flex-col items-center m-3'>
            <Link
              to={`/recruit/project/${result.id}`}
              className='bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-2 m-1 rounded-lg'
              onClick={() => window.scrollTo({ top: 0 })}
            >
              프로젝트 개요 보기
            </Link>
            <div className='text-gray-600 font-semibold ml-7 mt-5'>
              모집인원 {result.current}/{result.total}
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};

export default RecruitCard;

const truncate = (str: string, n: number) => {
  return str?.length > n ? str.substr(0, n - 1) + "..." : str;
};
