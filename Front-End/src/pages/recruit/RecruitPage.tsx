import React, { useEffect } from 'react';
import RecruitCard from '../../components/recruit/RecruitCard.tsx';
import RecruitSearchBar from '../../components/recruit/RecruitSearchBar.tsx';
import Pagination from '@mui/material/Pagination';
import useRecruitStore from '../../stores/useRecruitStore.ts';
import { useRequireAuth } from '../../hooks/useRequireAuth';

import '../../styles/page/recruit/recruit-page.scss';

const RecruitPage: React.FC = () => {
	useRequireAuth();
	const { currentPage, setCurrentPage, totalPages } = useRecruitStore();

	const handlePageChange = (_event: React.ChangeEvent<unknown>, page: number) => {
		setCurrentPage(page);
	};
	useEffect(() => {
		setCurrentPage(1);
		<RecruitSearchBar currentPage={1} />;
		console.log(currentPage);
	}, []);
	return (
		<React.StrictMode>
			<div className={'recruit-container'}>
				<div className='text-center w-full'>
					<div className='font-bold text-6xl'>프로젝트 찾기</div>
					<RecruitSearchBar currentPage={currentPage} />
				</div>
				<div className='w-full'>
					<div className='m-1'>
						<RecruitCard />
					</div>
				</div>
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
			</div>
		</React.StrictMode>
	);
};

export default RecruitPage;
