/* eslint-disable react-hooks/exhaustive-deps */
import { FC, useEffect, useState } from 'react';
import { FaArrowLeft } from 'react-icons/fa6';
import { useNavigate, useParams } from 'react-router-dom';
import { axiosInstance } from '../../apis/lib/axios';

interface SectionColors {
	[key: string]: string;
}

interface PrevRetrospectProps {
	onClick?: () => void; // 선택적 prop
}

interface RetrospectItem {
	content: string;
	category: string;
}

interface RetrospectsResponse {
	Keep: RetrospectItem[];
	Problem: RetrospectItem[];
	Try: RetrospectItem[];
}

const PrevRetrospect: FC<PrevRetrospectProps> = () => {
	const navigate = useNavigate();
	const [selectedWeek, setSelectedWeek] = useState<number>(1); // 타입 명시
	const [weeks, setWeeks] = useState<string[]>([]);
	const { projectId } = useParams<string>(); // useParams 타입 명시
	const [retrospects, setRetrospects] = useState<RetrospectItem[]>([]); // 타입 명시

	const getRetrospects = async () => {
		try {
			const response = await axiosInstance.get<RetrospectsResponse>(`/retrospects/${projectId}?week=${selectedWeek}`);
			const { Keep, Problem, Try } = response.data;

			const retrospectsData = [
				...(Keep ? Keep.map((item) => ({ ...item, category: 'Keep' })) : []),
				...(Problem ? Problem.map((item) => ({ ...item, category: 'Problem' })) : []),
				...(Try ? Try.map((item) => ({ ...item, category: 'Try' })) : []),
			];
			console.log(retrospects);
			setRetrospects(retrospectsData);
		} catch (error) {
			console.error(error);
			setRetrospects([]);
		}
	};

	useEffect(() => {
		// 프로젝트 기간동안의 주차 정보 설정
		const projectWeeks = Array.from({ length: 10 }, (_, i) => (i + 1).toString());
		setWeeks(projectWeeks);
		getRetrospects(); // 컴포넌트 마운트 시 초기 회고 정보 불러오기
	}, [projectId, selectedWeek]); // projectId 또는 selectedWeek 변경 시 재요청

	const sectionColors: SectionColors = {
		Keep: 'bg-yellow-300',
		Problem: 'bg-pink-300',
		Try: 'bg-green-300',
		Action: 'bg-blue-300',
	};

	return (
		<div className='p-4 m-10 max-w-2xl mx-auto border-2 border-gray-200 shadow-lg rounded-lg'>
			<div className='flex items-center mb-4'>
				<FaArrowLeft
					onClick={() => navigate(`/project/${projectId}/retrospect`)} // 후에 projectid params 받아서 유효한 링크로 나중에 라우팅 추가
					className='cursor-pointer text-main-color mr-4'
				/>
				<h1 className='text-xl font-bold'>이전 회고 보기</h1>
				<select
					className='ml-auto block appearance-none w-20 bg-white border border-gray-400 hover:border-gray-500 px-4 py-1 rounded shadow leading-tight focus:outline-none focus:shadow-outline'
					value={selectedWeek.toString()} // Ensure the value is a string
					onChange={(e) => setSelectedWeek(parseInt(e.target.value, 10))} // Convert the string to a number
				>
					{weeks.map((week) => (
						<option key={week} value={week}>{`${week} 주차`}</option>
					))}
				</select>
			</div>

			{retrospects.length === 0 ? (
				<div className='text-center mt-10'>
					<p>"이번 주차의 회고가 없습니다! 지금 회고를 추가해주세요!"</p>
					{/* 회고 추가 버튼 또는 링크가 필요한 경우 추가 */}
				</div>
			) : (
				['Keep', 'Problem', 'Try'].map((category) => (
					<div key={category} className='mb-6'>
						<h2 className='text-lg font-bold ml-4 mb-4'>{category}</h2>
						<ul className='list-disc list-inside'>
							{retrospects
								.filter((retro) => retro.category === category)
								.map((retro, idx) => (
									<li key={idx} className={`${sectionColors[category]} p-2 rounded-md shadow mb-2`}>
										{retro.content}
									</li>
								))}
						</ul>
					</div>
				))
			)}
		</div>
	);
};

export default PrevRetrospect;
