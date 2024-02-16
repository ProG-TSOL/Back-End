/* eslint-disable @typescript-eslint/no-unused-vars */
import {
	Calendar,
	// CalendarProps,
	momentLocalizer,
	ToolbarProps,
} from 'react-big-calendar';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import moment from 'moment';
import 'moment/locale/ko'; //한국어 locale 설정
import { axiosInstance } from '../../apis/lib/axios';
import { useState, useEffect, useCallback, SetStateAction } from 'react';
import { FaCircleChevronRight, FaCircleChevronLeft } from 'react-icons/fa6';
import { useLocation } from 'react-router-dom';
interface CommuteCheckBtnProps {
	projectId: number;
	memberId: number;
}

const localizer = momentLocalizer(moment);
moment.locale('ko'); //locale 한국어로 설정

//툴바 커스텀
const CustomToolbar: React.FC<ToolbarProps> = ({ onNavigate, label, onView }) => {
	const goToToday = () => {
		const now = new Date();
		onNavigate('TODAY', now);
		onView('month');
	};

	return (
		<div className='flex justify-between items-center mb-3'>
			<div className='flex justify-center items-center'>
				<FaCircleChevronLeft
					type='button'
					onClick={() => onNavigate('PREV')}
					className='flex mr-3 text-2xl cursor-pointer bg-white text-main-color'
				/>

				<div className='flex text-2xl'>{label}</div>

				<FaCircleChevronRight
					type='button'
					onClick={() => onNavigate('NEXT')}
					className='flex ml-3 text-2xl cursor-pointer bg-white text-main-color'
				/>
			</div>

			<button
				type='button'
				onClick={goToToday}
				className='flex text-medium w-14 h-10 rounded-2xl bg-main-color text-white justify-center items-center'
			>
				Today
			</button>
		</div>
	);
};

//날짜 칸 커스텀
const dayPropGetter = (date: Date) => {
	const today = new Date(); //오늘 날짜

	const style = {
		backgroundColor:
			date.getDate() === today.getDate() &&
			date.getMonth() === today.getMonth() &&
			date.getFullYear() === today.getFullYear()
				? '#EBE9FC'
				: 'white',
		borderRadius: '20px',
	};
	return {
		style,
	};
};

interface CommuteWorkData {
	id: number;
	workingDay: string;
	workingTime: string;
}

interface CommuteWork {
	responseTime: string;
	status: string;
	cnt: number;
	data: CommuteWorkData[];
}

const CommuteCalendar = ({ projectId, memberId }: CommuteCheckBtnProps) => {
	const [events, setEvents] = useState<{ title: string; start: Date; end: Date }[]>([]);
	const [currentDate, setCurrentDate] = useState(new Date());
	const location = useLocation();

	const fetchAttendanceLogs = useCallback(
		async (_year: number, _month: number) => {
			try {
				// const { data } = await axiosInstance.get(`/attendances/${projectId}/${memberId}?month=${month}`);
				const { data } = await axiosInstance.get<CommuteWork>(`/attendances/${projectId}/${memberId}?month=2`);
				const datas = data.data;

				//workingTime 변환하는 로직
				const formattedEvents = datas.map((log) => {
					const [hours, minutes] = log.workingTime.split(':');
					const formattedTime = `${parseInt(hours, 10)}h ${parseInt(minutes, 10)}m`;
					return {
						title: formattedTime,
						start: new Date(log.workingDay),
						end: new Date(log.workingDay),
					};
				});

				console.log(datas);
				setEvents(formattedEvents);
			} catch (error) {
				console.error('근무 기록 가져오기 실패', error);
			}
		},
		[projectId, memberId],
	);

	useEffect(() => {
		const month = currentDate.getMonth() + 1; // JavaScript의 getMonth()는 0부터 시작하므로 1을 더해줍니다.
		fetchAttendanceLogs(month);
	}, [currentDate, fetchAttendanceLogs, location]);

	const handleNavigate = (newDate: SetStateAction<Date>) => {
		setCurrentDate(newDate);
	};

	return (
		<Calendar
			localizer={localizer}
			events={events}
			views={['month']}
			components={{
				toolbar: CustomToolbar,
			}}
			dayPropGetter={dayPropGetter}
			onNavigate={handleNavigate}
			style={{ height: 530, width: '85%' }}
		/>
	);
};

export default CommuteCalendar;
