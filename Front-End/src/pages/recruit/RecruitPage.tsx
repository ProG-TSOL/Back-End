import React from 'react';
import RecruitCard from '../../components/recruit/RecruitCard.tsx';
import RecruitSearchBar from '../../components/recruit/RecruitSearchBar.tsx';
import Pagination from '@mui/material/Pagination';
import useRecruitStore from '../../stores/useRecruitStore.ts';
import { useRequireAuth } from '../../hooks/useRequireAuth';

const RecruitPage: React.FC = () => {
	useRequireAuth();
	const { currentPage, setCurrentPage, totalPages } = useRecruitStore();

	const handlePageChange = (event: React.ChangeEvent<unknown>, page: number) => {
		setCurrentPage(page);
	};

	return (
		<React.StrictMode>
			<div className='grid text-center'>
				<div className='font-bold text-6xl'>프로젝트 찾기</div>
				<RecruitSearchBar currentPage={currentPage} />
			</div>
			<span className='p-1 m-1 flex justify-center items-center'>
				<RecruitCard />
			</span>
			<div className='flex justify-center mb-10'>
				<Pagination
					count={totalPages}
					variant='outlined'
					shape='rounded'
					page={currentPage}
					onChange={handlePageChange}
					onClick={() => window.scrollTo({ top: 0 })}
				/>
			</div>
		</React.StrictMode>
	);
};

export default RecruitPage;
