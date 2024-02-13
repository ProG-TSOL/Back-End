/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { useEffect, useState } from 'react';
import { FaGear } from 'react-icons/fa6';
import { useParams, useNavigate } from 'react-router-dom';
import { axiosInstance } from '../../../apis/lib/axios';
import { useRequireAuth } from '../../../hooks/useRequireAuth';
import { useUserStore } from '../../../stores/useUserStore';

interface Position {
	posName: string;
	posCode: number;
	posNowNumber: number;
	posNumber: number;
	members: string[];
}
interface MemberData {
	jobCode: {
		id: number; // Or string, based on your actual data model
	};
	member: {
		nickname: string;
	};
}

const IndexPage = () => {
	useRequireAuth();
	const { profile } = useUserStore();
	const memberId = profile?.id;
	console.log(memberId);
	//인덱스 페이지에서 세팅 페이지로 이동
	const navigate = useNavigate();
	const [isProjectStarted, setIsProjectStarted] = useState(false);
	const [startDay, setStartDay] = useState('');
	const [title, setTitle] = useState('');
	const [description, setDescription] = useState('');
	const [img, setImg] = useState('');
	const [mystack, setMyStack] = useState<string[]>([]);
	const [period, setPeriod] = useState('');
	const [positions, setPositions] = useState<Position[]>([]);
	const { projectId } = useParams();

	const MemberSetting = () => {
		navigate('./membersetting');
		window.scrollTo({ top: 0 });
	};

	const Setting = () => {
		navigate('./setting');
		window.scrollTo({ top: 0 });
	};

	const startProject = async () => {
		try {
			await axiosInstance.patch(`/projects/${projectId}/start/${memberId}`);
			getData(); // Refresh data after starting the project
		} catch (error) {
			console.error('Start failed:', error);
		}
	};

	const endProject = async () => {
		try {
			await axiosInstance.patch(`/projects/${projectId}/end/${memberId}`);
			getData(); // Refresh data after ending the project
		} catch (error) {
			console.error('End failed:', error);
		}
	};

	const calculatePeriod = () => {
		if (!startDay) return '0 일';

		const startDate = new Date(startDay);
		const currentDate = new Date();
		const differenceInTime = currentDate.getTime() - startDate.getTime(); // Correctly typed as number - number
		const differenceInDays = Math.ceil(differenceInTime / (1000 * 3600 * 24));

		return `${differenceInDays} 일`;
	};

	const getData = async () => {
		try {
			const response = await axiosInstance.get(`/projects/${projectId}/${memberId}`);
			const data = response.data.data;
			setTitle(data.title);
			setDescription(data.content);
			setImg(data.projectImgUrl);
			setMyStack(data.techCodes.map((tech: { detailName: any }) => tech.detailName));
			setPeriod(data.period);
			setIsProjectStarted(data.startDay !== null);
			setStartDay(data.startDay);

			const updatedPositions = data.projectTotals.map(
				(item: { jobCode: { detailDescription: any; id: any }; current: any; total: any }) => ({
					posName: item.jobCode.detailDescription,
					posCode: item.jobCode.id, // Ensure this is correctly populated
					posNowNumber: item.current,
					posNumber: item.total,
					members: [],
				}),
			);

			const membersResponse = await axiosInstance.get(`/projects/${projectId}/members`);
			const membersData = membersResponse.data.data;
			console.log(membersData);
			membersData.forEach((memberData: MemberData) => {
				console.log('Looking for position with posCode:', memberData.jobCode.id);
				const position = updatedPositions.find((pos: { posCode: any }) => pos.posCode === memberData.jobCode.id);
				if (position) {
					position.members.push(memberData.member.nickname);
				} else {
					console.log('No matching position found for jobCode.id:', memberData.jobCode.id);
				}
			});

			setPositions(updatedPositions);
		} catch (error) {
			console.error('Loading failed:', error);
		}
	};

	useEffect(() => {
		getData();
	}, [projectId]);

	return (
		<div>
			<div className='flex justify-between'>
				<div className='w-full h-screen flex p-10 flex-col'>
					<div>
						<div className='flex justify-between'>
							{isProjectStarted ? (
								<button className='ml-1 mb-2 bg-main-color text-white p-1 rounded' onClick={endProject}>
									프로젝트 종료
								</button>
							) : (
								<button className='ml-1 mb-2 bg-main-color text-white p-1 rounded' onClick={startProject}>
									프로젝트 시작
								</button>
							)}
							<span className='flex'>
								<span className='font-bold cursor-pointer' onClick={Setting}>
									프로젝트 설정
								</span>
								<FaGear className='mt-1 cursor-pointer' onClick={Setting} />
							</span>
						</div>
						<div className='flex h-100 bg-gray-100'>
							<div className='w-7/12'>
								<div className='border-black border-2 h-20 p-2'>
									<div className='text-2xl'>제목</div>
									<div className='text-3xl font-bold'>{title}</div>
								</div>
								<div className='border-black border-2 h-52 p-2'>
									<div className='text-2xl'>설명</div>
									<div className='font-bold overflow-y-scroll h-40'>
										{description.split('\n').map((line, index) => (
											<React.Fragment key={index}>
												{line}
												<br />
											</React.Fragment>
										))}
									</div>
								</div>
								<div className='border-black border-2 h-24 p-2'>
									<div className='text-2xl'>기술스택</div>
									<div className='flex flex-wrap'>
										{mystack.map((tech, index) => (
											<div key={index} className='bg-gray-200 p-1 m-1 rounded-full'>
												{tech}
											</div>
										))}
									</div>
								</div>
							</div>
							<div className='w-5/12'>
								<div className='border-black border-2 h-48 p-2'>
									<div className='text-2xl'>사진</div>
									<img src={img} className='w-full h-36' />
								</div>
								<div className='border-black border-2 h-24 p-2'>
									<div className='text-2xl'>예상 진행기간</div>
									<div className='text-center text-3xl'>{period} 주</div>
								</div>
								<div className='border-black border-2 h-24 p-2'>
									<div className='text-2xl'>{isProjectStarted ? '현재 진행기간' : '프로젝트 시작 전'}</div>
									<div className='text-center text-3xl'>
										{isProjectStarted ? (
											calculatePeriod()
										) : (
											<button className='ml-1 mb-2 bg-main-color text-white p-1 rounded' onClick={startProject}>
												프로젝트 시작
											</button>
										)}
									</div>
								</div>
							</div>
						</div>
					</div>
					<div className='border-black border-2 h-40 bg-gray-100 p-2'>
						<div className='flex'>
							<span className='text-2xl'>구성인원</span>
							<FaGear className='text-2xl ml-3 mt-1 cursor-pointer' onClick={MemberSetting} />
						</div>
						<div className='overflow-y-auto h-28'>
							{positions.map((position, index) => (
								<div key={index} className='h-8'>
									{`${position.posName}: ${position.posNumber}명 `}
									{position.members.map((nickname, memberIndex) => (
										<span key={memberIndex} className='bg-gray-200 p-1 m-1 rounded-full'>
											{nickname}
										</span>
									))}
								</div>
							))}
						</div>
					</div>
				</div>
			</div>
		</div>
	);
};

export default IndexPage;
