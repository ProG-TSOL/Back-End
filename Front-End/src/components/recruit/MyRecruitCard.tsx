// MyRecruitCard.tsx
import React from 'react';
import { Link } from 'react-router-dom';
import useMyRecruitStore from '../../stores/useMyRecruitStore';

const MyRecruitCard = () => {
  const mySearchResults = useMyRecruitStore(state => state.mySearchResults);

  return (
    console.log(mySearchResults),
    <div className='flex flex-wrap justify-center'>
      {mySearchResults.map((result) => (
        <div key={result.id} className='relative bg-gray-100 w-64 h-auto grid place-items-center border-black border-2 m-3'>
          <div className='absolute top-2 left-5 bg-yellow-300 text-black font-bold px-2 py-1 rounded-lg'>
            {result.statusCode.detailDescription}
          </div>
          <img src={result.projectImgUrl || 'default-thumbnail-url'} alt='Project Thumbnail' className='w-56 h-40 bg-sub-color' />
          <div className='font-bold'>{result.title}</div>
          <div className='flex flex-wrap'>
            {result.techCodes.map((tag) => (
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

export default MyRecruitCard;