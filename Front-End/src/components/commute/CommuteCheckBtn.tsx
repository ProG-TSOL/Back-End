import { useEffect, useState } from 'react';
import {
	useAttendanceEndMutation,
	useAttendanceStartMutation,
	useAttendanceStartQuery,
} from '../../apis/useAttendance';
import { useNavigate } from 'react-router-dom';

export interface CommuteCheckBtnProps {
	projectId: number;
	memberId: number;
}

const CommuteCheckBtn = ({ projectId, memberId }: CommuteCheckBtnProps) => {
	const { mutate: startAttendance } = useAttendanceStartMutation();
	const { data: attendance, isLoading, error } = useAttendanceStartQuery(projectId, memberId);
	const { mutate: endAttendance } = useAttendanceEndMutation();
	const [isWorking, setIsWorking] = useState(false); //출근 상태 관리
	const [showModal, setShowModal] = useState(false);
	const navigate = useNavigate();

	useEffect(() => {
		if (attendance?.data) {
			setIsWorking(attendance.data.isWorking); //출근 상태 초기화(true로)
		}
	}, [attendance]);

	//출근 버튼 클릭 시
	const handleStartAttendance = async () => {
		await startAttendance(
			{ projectId, memberId },
			// {
			// 	//성공 콜백에서 상태 업데이트
			// 	onSuccess: () => {
			// 		setIsWorking(true);
			// 	},
			// },
		);
		setIsWorking(true);
		// console.log(projectId, memberId, 'success');
	};

	//퇴근 버튼 클릭 시
	const handleEndAttendance = async () => {
		await endAttendance(
			{ projectId, memberId },
			// {
			// 	onSuccess: () => {
			// 		setIsWorking(false);
			//    setShowModal(true);
			// 	},
			// },
		);
		setIsWorking(false);
		setShowModal(true);
	};

	const closeModal = () => setShowModal(false);

	const confirmWork = () => {
		closeModal();
		navigate(`/project/${projectId}/commute`);
	};

	//startAt시간 -> 한국시간으로 포맷팅
	const convertToKST = (dateString: string) => {
		const serverDateTime = new Date(dateString);
		const koreaDateTime = new Date(serverDateTime.getTime() + 9 * 60 * 60 * 1000);

		//날짜 형식 포맷팅
		const date = `${koreaDateTime.getMonth() + 1}. ${koreaDateTime.getDate()}`;

		//시간 형식 포맷팅
		const hours = koreaDateTime.getHours().toString().padStart(2, '0');
		const minutes = koreaDateTime.getMinutes().toString().padStart(2, '0');
		const time = `${hours}:${minutes}`;
		// koreaDateTime.toLocaleString('ko-KR');

		return { date, time };
	};

	const { date, time } = attendance?.data ? convertToKST(attendance.data.startAt) : { date: '', time: '' };
	// const time = attendance?.data ? convertToKST(attendance.data.startAt) : '';
	console.log(time);

	console.log(attendance);
	console.log(isWorking);

	return (
		<div className='flex flex-col justify-center space-y-2'>
			<div className='flex flex-col items-center justify-center'>
				<div className={`text-lg font-bold ${!isWorking ? 'invisible' : ''}`}>{date}</div>
				<div className={`text-2xl font-bold ${!isWorking ? 'invisible' : ''}`}>{time}</div>
			</div>
			<div className='flex space-x-2'>
				<button
					disabled={isWorking}
					className={`flex w-20 h-20 rounded-2xl items-center justify-center  ${
						isWorking ? 'bg-gray-300' : 'bg-sub-color hover:bg-main-color'
					}`}
					onClick={handleStartAttendance}
				>
					출근
				</button>
				<button
					disabled={!isWorking}
					className={`flex w-20 h-20 rounded-2xl items-center justify-center ${
						!isWorking ? 'bg-gray-300' : 'bg-sub-color hover:bg-main-color'
					}`}
					onClick={handleEndAttendance}
				>
					퇴근
				</button>
			</div>
			{showModal && (
				<div
					className='fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full flex items-center justify-center'
					id='my-modal'
				>
					<div className='relative mx-auto p-5 border w-96 shadow-lg rounded-md bg-white'>
						<div className='mt-3 text-center'>
							<h3 className='text-lg leading-6 font-medium text-gray-900'>오늘 근무기록을 확인하시겠습니까?</h3>
							<div className='mt-2 px-7 py-3'>
								<p className='text-sm text-gray-500'>
									근무 기록 페이지로 이동하여 오늘의 근무 시간을 확인할 수 있습니다.
								</p>
							</div>
							<div className='items-center px-4 py-3'>
								<button
									id='ok-btn'
									onClick={confirmWork}
									className='px-4 py-2 bg-main-color text-white text-base font-medium rounded-md w-full shadow-sm hover:bg-blue-400 focus:outline-none focus:ring-2 focus:ring-green-300'
								>
									확인
								</button>
								<button
									id='cancel-btn'
									onClick={closeModal}
									className='mt-3 px-4 py-2 bg-gray-500 text-white text-base font-medium rounded-md w-full shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-300'
								>
									취소
								</button>
							</div>
						</div>
					</div>
				</div>
			)}
		</div>
	);
};

export default CommuteCheckBtn;
