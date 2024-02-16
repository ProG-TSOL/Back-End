/* eslint-disable @typescript-eslint/no-explicit-any */
import { useEffect, useState } from 'react';
// import {FaGear, FaCircleUser} from 'react-icons/fa6';
import { useParams, useNavigate, NavigateFunction } from 'react-router-dom';
import { axiosInstance } from '../../../apis/lib/axios';
import { useRequireAuth } from '../../../hooks/useRequireAuth';
import { useUserStore } from '../../../stores/useUserStore';
// import logo from '../../../assets/logo.png';
// import ImageWithFallback from "../../../utils/DefaultImgage.tsx";
import '../../../styles/page/project-index-page.scss';
import { LineProgressBar } from '@frogress/line';
import MemberSettingPage from '../setting/MemberSettingPage.tsx';

// interface Position {
//     posName: string;
//     posCode: number;
//     posNowNumber: number;
//     posNumber: number;
//     members: string[];
// }

// interface MemberData {
// 	jobCode: {
// 		id: number; // Or string, based on your actual data model
// 	};
// 	member: {
// 		nickname: string;
// 	};
// }

interface HomeData {
	projectId: string;
	title: string;
	startDay: string;
	endDay: string;
	progress: number;
	myWorkCnt: number;
}

const CustomLabelComponent = ({ percent }: { percent: number }) => {
	return (
		<div className='custom-label'>
			<span>{percent}%</span>
		</div>
	);
};

const useHomeInfo = (projectId: string, trigger:number) => {
	let memberId = 0;

	// 로컬 스토리지에서 userProfile을 가져옴
	const userProfileKey = 'userProfile';
	const userProfileString = localStorage.getItem(userProfileKey);
	if (userProfileString) {
		const userProfile = JSON.parse(userProfileString);
		memberId = userProfile.id;
	}

	const [homeData, setHomeData] = useState<HomeData>();
	const getHomdeInfo = async () => {
		try {
			const response = await axiosInstance.get(`/projects/home/${projectId}/${memberId}`);
			const data = response.data.data;
			console.log(`Data loaded : ${JSON.stringify(data)}`);
			setHomeData(data);

		} catch (error) {
			console.error('Loading failed:', error);
		}
	};

	useEffect(() => {
		console.log(`useEffect 홈데이터 get`);
		if (projectId && projectId !== '') {
			console.log(`데이터 있음`);
			getHomdeInfo();
		}
	}, [projectId,trigger]);

	return homeData;
};

// 내 업무 클릭시 페이지 이동
const onHandleClick = (navigate: NavigateFunction, projectId: string) => {
	navigate(`/project/${projectId}/task`);
};

const IndexPage = () => {
	useRequireAuth();
	const { profile } = useUserStore();
	const memberId = profile?.id;
	console.log(memberId);
	//인덱스 페이지에서 세팅 페이지로 이동
	const navigate = useNavigate();
	const [isProjectStarted, setIsProjectStarted] = useState(false);
	// const [startDay, setStartDay] = useState('');
	// const [title, setTitle] = useState('');
	// const [description, setDescription] = useState('');
	// const [img, setImg] = useState('');
	// const [mystack, setMyStack] = useState<string[]>([]);
	// const [period, setPeriod] = useState<number>(0);
	// const [positions, setPositions] = useState<Position[]>([]);
	const { projectId } = useParams();

	// const [test, setTest] = useState<useHomeInfo(projectId || '')>;

	const [trigger, setTrigger] = useState(0);
	const myHomeInfo = useHomeInfo(projectId || '', trigger);
	// if (projectId && projectId !== '') {
	//     // const numericProjectId = parseInt(projectId);
	// }

	// const MemberSetting = () => {
	//     navigate('./membersetting');
	//     window.scrollTo({top: 0});
	// };

	const Setting = () => {
		navigate('./setting');
		window.scrollTo({ top: 0 });
	};

	const startProject = async () => {
		try {
			console.log(`startProject`)
			await axiosInstance.patch(`/projects/${projectId}/start/${memberId}`);
			// getData(); // Refresh data after starting the project
			setIsProjectStarted(true)
			setTrigger(prev => prev + 1);
		} catch (error) {
			console.error('Start failed:', error);
		}
	};

	const endProject = async () => {
		try {
			console.log(`endProject`)
			await axiosInstance.patch(`/projects/${projectId}/end/${memberId}`);
			// getData(); // Refresh data after ending the project
			setTrigger(prev => prev + 1);
		} catch (error) {
			console.error('End failed:', error);
		}
	};

	// const calculatePeriod = () => {
	//     if (!startDay) return '0 일';
	//
	//     const startDate = new Date(startDay);
	//     const currentDate = new Date();
	//     const differenceInTime = currentDate.getTime() - startDate.getTime(); // Correctly typed as number - number
	//     const differenceInDays = Math.ceil(differenceInTime / (1000 * 3600 * 24));
	//
	//     return `${differenceInDays} 일`;
	// };

	// const getData = async () => {
	// 	try {
	// 		const response = await axiosInstance.get(`/projects/${projectId}/${memberId}`);
	// 		const data = response.data.data;
	// 		// setTitle(data.title);
	// 		// setDescription(data.content);
	// 		// setImg(data.projectImgUrl);
	// 		// setMyStack(data.techCodes.map((tech: { detailName: any }) => tech.detailName));
	// 		// setPeriod(data.period);
	// 		setIsProjectStarted(data.startDay !== null);
	// 		// setStartDay(data.startDay);
	//
	// 		const updatedPositions = data.projectTotals.map(
	// 			(item: { jobCode: { detailDescription: any; id: any }; current: any; total: any }) => ({
	// 				posName: item.jobCode.detailDescription,
	// 				posCode: item.jobCode.id, // Ensure this is correctly populated
	// 				posNowNumber: item.current,
	// 				posNumber: item.total,
	// 				members: [],
	// 			}),
	// 		);
	//
	// 		const membersResponse = await axiosInstance.get(`/projects/${projectId}/members`);
	// 		const membersData = membersResponse.data.data;
	// 		console.log(membersData);
	// 		membersData.forEach((memberData: MemberData) => {
	// 			console.log('Looking for position with posCode:', memberData.jobCode.id);
	// 			const position = updatedPositions.find((pos: { posCode: any }) => pos.posCode === memberData.jobCode.id);
	// 			if (position) {
	// 				position.members.push(memberData.member.nickname);
	// 			} else {
	// 				console.log('No matching position found for jobCode.id:', memberData.jobCode.id);
	// 			}
	// 		});
	//
	// 		// setPositions(updatedPositions);
	// 	} catch (error) {
	// 		console.error('Loading failed:', error);
	// 	}
	// };

	useEffect(() => {
		console.log(`useEffect : ${isProjectStarted}`);
		// 시작일이 있으면 프로젝트 시작 상태 true
		// if (myHomeInfo?.startDay !== null && myHomeInfo?.startDay !== 'null') {
		if (myHomeInfo?.startDay) {
			console.log('여기타나? true만듦')
			console.log(`값 : :${isProjectStarted}`)
			setIsProjectStarted(true);
		}

	}, [projectId, myHomeInfo]);

	return (
		<main>
			{/*제목*/}
			<div className={'title-box under-line'}>
				<h2>{myHomeInfo?.title}</h2>
			</div>
			{/*프로젝트 정보*/}
			<div className={'project-box'}>
				<div className={'flex justify-between under-line'}>
					<h2>프로젝트 정보</h2>
					<div className={'grid grid-cols-2 gap-x-5'}>
						{isProjectStarted ? (
							<button className={'project-home-btn end'} onClick={endProject}>
								프로젝트 종료
							</button>
						) : (
							<button className={'project-home-btn'} onClick={startProject}>
								프로젝트 시작
							</button>
						)}
						{/*<p>참여인원</p> /!*참여인원 => 컴포넌트 제작하기*!/*/}
						<button className={'project-home-btn'} onClick={Setting}>
							설정
						</button>
					</div>
				</div>
				<div className={'p-3'}>
					<button className={'my-work-btn'} onClick={() => onHandleClick(navigate, projectId || '')}>
						🔎 내 업무 {myHomeInfo?.myWorkCnt}
					</button>
					<div>
						<div className={'flex py-5 text-xl justify-evenly'}>
							{/*시작일, 종료일*/}
							<div>
								<p>시작일 {myHomeInfo?.startDay}</p>
							</div>
							<div>
								<p>종료일 {myHomeInfo?.endDay}</p>
							</div>
							{/*진척도*/}
						</div>
						<div>
							{/*프로그레스바*/}
							<div>
								<LineProgressBar
									label={(value: number) => <CustomLabelComponent percent={value} />}
									percent={myHomeInfo?.progress || 0}
									progressColor='linear-gradient(to right, rgb(18, 216, 250) 25%, rgb(67, 164, 255) 85%, rgb(49, 121, 255) 98%)'
									containerColor='#e9ecef'
									height={24}
								/>
							</div>
						</div>
					</div>
				</div>
			</div>
			{/*참여멤버*/}
			<MemberSettingPage projectId={myHomeInfo?.projectId} />
		</main>
	);
};

export default IndexPage;
