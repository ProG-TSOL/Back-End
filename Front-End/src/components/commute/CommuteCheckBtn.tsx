import { useEffect, useState } from 'react';
import { useAttendanceEndMutation, useAttendanceStartMutation } from '../../apis/useAttendance';
import { useNavigate } from 'react-router-dom';
import '../../styles/component/navbar.scss';

export interface CommuteCheckBtnProps {
	projectId: number;
	memberId: number;
}

const CommuteCheckBtn = ({ projectId, memberId }: CommuteCheckBtnProps) => {
	const [isWorking, setIsWorking] = useState(false); //출근 상태 관리
	const [showModal, setShowModal] = useState(false);
	const [startDateTime, setStartDateTime] = useState<{ date: string; time: string } | null>(null);
	const navigate = useNavigate();

	const { mutate: startAttendance, data: startData } = useAttendanceStartMutation();
	const { mutate: endAttendance } = useAttendanceEndMutation();

	useEffect(() => {
		const localAttendance = loadAttendance();
		if (startData) {
			const { date, time } = convertToKST(startData.data.startAt);
			saveAttendance(date, time);
			setStartDateTime({ date, time });
		} else if (localAttendance) {
			setIsWorking(true);
			const { date, time } = localAttendance;
			setStartDateTime({ date, time });
		}
	}, [startData]);

	const attendanceTime = 'attendanceTime';

	//출근 시, localStorage에 출근시간 저장
	const saveAttendance = (date: string, time: string) => {
		localStorage.setItem(attendanceTime, JSON.stringify({ date, time }));
		setIsWorking(true);
	};

	//퇴근 시, localStorage에 출근시간 삭제
	const clearAttendance = () => {
		localStorage.removeItem(attendanceTime);
		setIsWorking(false);
	};

	//localStorage에서 출근상태 불러오기(새로고침 or 페이지 이동 시)
	const loadAttendance = () => {
		const attendanceTimes = localStorage.getItem(attendanceTime);
		if (attendanceTimes) {
			const { date, time } = JSON.parse(attendanceTimes);
			return { date, time };
		}
		return null;
	};

	//출근 버튼 클릭 시
	const handleStartAttendance = async () => {
		await startAttendance({ projectId, memberId });
		setIsWorking(true);
	};

	//퇴근 버튼 클릭 시
	const handleEndAttendance = async () => {
		await endAttendance({ projectId, memberId });
		clearAttendance();
		setIsWorking(false);
		setShowModal(true);
	};

	const closeModal = () => setShowModal(false);

	const confirmWork = async () => {
		// await
		closeModal();
		navigate(`/project/${projectId}/commute`);
	};

	//서버에서 받은 시간 포맷팅
	const convertToKST = (serverDateTime: string) => {
		console.log('server : ', serverDateTime);
		const [datePart, timePart] = serverDateTime.split('T');
		const date = datePart.split('-').slice(1).join('.'); // '02.14' 형식으로 변환
		const time = timePart.slice(0, 5); // '06:29' 형식으로 변환
		console.log(date, time);

		return { date, time };
	};

	console.log(isWorking);

	return (
		<div className='flex flex-col justify-center space-y-2'>
			<div className='flex flex-col items-center justify-center'>
				<div className={`text-lg font-bold ${!isWorking ? 'invisible' : ''}`}>{startDateTime?.date}</div>
				<div className={`text-2xl font-bold ${!isWorking ? 'invisible' : ''}`}>{startDateTime?.time}</div>
			</div>
			<div className='flex space-x-2'>
				<button
					disabled={isWorking}
					className={`flex w-20 h-20 rounded-2xl items-center justify-center  ${
						isWorking ? 'bg-gray-300' : 'commute-btn'
					}`}
					onClick={handleStartAttendance}
				>
					출근
				</button>
				<button
					disabled={!isWorking}
					className={`flex w-20 h-20 rounded-2xl items-center justify-center ${
						!isWorking ? 'bg-gray-300' : 'commute-btn'
					}`}
					onClick={handleEndAttendance}
				>
					퇴근
				</button>
			</div>
			{/* 여기부터 모달---------------------- */}
			{showModal && (
				<div
					className='fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full flex items-center justify-center z-[9999]'
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
