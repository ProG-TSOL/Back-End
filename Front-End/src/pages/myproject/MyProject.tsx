// MyProject.tsx
import React, { useEffect } from 'react';
import MyRecruitCard from '../../components/recruit/MyRecruitCard';
import { axiosInstance } from '../../apis/lib/axios';
import useMyRecruitStore from '../../stores/useMyRecruitStore'; // Corrected import
import { useRequireAuth } from '../../hooks/useRequireAuth';
import { useUserStore } from '../../stores/useUserStore';

const MyProject = () => {
	useRequireAuth();
	const { profile } = useUserStore();
	const memberId = profile?.id;
	const updateMySearchResults = useMyRecruitStore((state) => state.updateMySearchResults);

	useEffect(() => {
		const getMyProject = async () => {
			try {
				const response = await axiosInstance.get(`/projects/myProject/${memberId}`);
				const projects = response.data.data;
				if (projects.length > 0) {
					updateMySearchResults(projects);
				} else {
					console.log('검색 결과가 없습니다.');
				}
			} catch (error) {
				console.error('Search failed:', error);
			}
		};
		getMyProject();
	}, []);

	return (
		<React.StrictMode>
			<div className='grid text-center'>
				<div className='font-bold text-6xl'>내가 참여중인 프로젝트</div>
			</div>
			<div className='grid grid-rows-4 gap-4 p-1 m-1'>
				<MyRecruitCard />
			</div>
		</React.StrictMode>
	);
};

export default MyProject;
