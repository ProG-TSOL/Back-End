import React, { useState, useEffect } from 'react';
import { FaSearch } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import { axiosInstance } from "../../apis/lib/axios";
import useRecruitStore from '../../stores/useRecruitStore';
import { useRequireAuth } from '../../hooks/useRequireAuth';

interface RecruitSearchBarProps {
  currentPage: number;
}

const RecruitSearchBar: React.FC<RecruitSearchBarProps> = ({ currentPage }) => {
  useRequireAuth();
  // const searchResults = useRecruitStore(state => state.searchResults); // Subscribe to searchResults
  const updateSearchResults = useRecruitStore(state => state.updateSearchResults);
  let test = [];


  const [tags, setTags] = useState<{ id: number; detailName: string }[]>([]);
  const [statuses, setStatuses] = useState<{ id: number; detailDescription: string }[]>([]);
  const [selectedTechId, setSelectedTechId] = useState<number | null>(null);
  const [selectedStatusesId, setSelectedStatusesId] = useState<number | null>(null);
  const [searchInput, setSearchInput] = useState('');



  const handleTechStackChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    const selectedId = event.target.value === 'default' ? null : parseInt(event.target.value, 10);
    setSelectedTechId(selectedId);
  };

  const handleStatusesChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    const selectedId = event.target.value === 'default' ? null : parseInt(event.target.value, 10);
    setSelectedStatusesId(selectedId);
  };

  const search = async () => {
    try {
      const response = await axiosInstance.get('/projects', {
        params: {
          keyword: searchInput,
          techCodes: selectedTechId,
          statusCode: selectedStatusesId,
          page: currentPage - 1,
          size: 10,
        },
      });
      test = response.data.data.content;
      console.log(test);
      const totalPages = response.data.data.totalPages;
      updateSearchResults(response.data.data.content);
      useRecruitStore.getState().setTotalPages(totalPages);
      if (test.length > 0) {
        updateSearchResults(test);
      } else {
        console.log('검색 결과가 없습니다.');
      }
    } catch (error) {
      console.error("Search failed:", error);
    }
  };


  useEffect(() => {

    const getTags = async () => {
      try {
        const response = await axiosInstance.get('/codes/details/Tech');
        if (response.data.status === 'OK') {
          setTags(response.data.data.map(({ id, detailName }: { id: number; detailName: string }) => ({ id, detailName })));
        }
      } catch (error) {
        console.error("tag failed:", error);
      }
    };

    const getStatuses = async () => {
      try {
        const response = await axiosInstance.get('/codes/details/ProjectStatus');
        if (response.data.status === 'OK') {
          setStatuses(response.data.data.map(({ id, detailDescription }: { id: number; detailDescription: string }) => ({ id, detailDescription })));
        }
      } catch (error) {
        console.error("status failed:", error);
      }
    };
    search();
    getTags();
    getStatuses();
    console.log(currentPage);
  }, []);
  useEffect(() => {
    search();
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentPage, selectedTechId, selectedStatusesId, searchInput]);


  return (
    <div>
      <div className='flex justify-end'>
        <Link to='../myproject' className='bg-main-color text-white p-3 mt-5 mr-5 font-bold'>
          내 프로젝트로
        </Link>
        <Link to='/recruit/write' className='bg-orange-300 p-3 mt-5 mr-5 font-bold'>
          직접 모집하기
        </Link>
      </div>

      <div className='flex justify-between mx-5 my-3 bg-sub-color p-5 items-center'>
        <div>
          <label htmlFor='techStack'>기술 스택</label>
          <select id='techStack' className='ml-2 p-2' onChange={handleTechStackChange}>
            <option value='default'>기술 스택 선택</option>
            {tags.map((tag) => (
              <option key={tag.id} value={tag.id}>
                {tag.detailName}
              </option>
            ))}
          </select>
        </div>
        <div>
          <label htmlFor='recruitmentStatus'>모집 상태</label>
          <select id='recruitmentStatus' className='ml-2 p-2' onChange={handleStatusesChange}>
            <option value='default'>모집 선택</option>
            {statuses.map((stat) => (
              <option key={stat.id} value={stat.id}>
                {stat.detailDescription}
              </option>
            ))}
          </select>
        </div>
        <div>
          <label htmlFor='searchInput'>검색창</label>
          <input
            id='searchInput'
            className='ml-2 p-2'
            type='text'
            placeholder='검색어를 입력하세요.'
            value={searchInput}
            onChange={(e) => setSearchInput(e.target.value)}
          />

          <button className='ml-2 p-3 bg-main-color text-white' onClick={search}>
            <FaSearch />
          </button>
        </div>
      </div>
    </div>
  );
};

export default RecruitSearchBar;