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
		<div className='flex justify-center'>
			<button type='button' onClick={() => onNavigate('PREV')} className='flex text-2xl'>
				👈
			</button>
			<div className='flex text-2xl'>{label}</div>
			<button type='button' onClick={() => onNavigate('NEXT')} className='flex text-2xl'>
				👉
			</button>
			<button type='button' onClick={goToToday} className='flex text-2xl '>
				오늘
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

	const fetchAttendanceLogs = useCallback(
		async (year: number, month: number) => {
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
		const year = currentDate.getFullYear();
		const month = currentDate.getMonth() + 1; // JavaScript의 getMonth()는 0부터 시작하므로 1을 더해줍니다.
		fetchAttendanceLogs(year, month);
	}, [currentDate, fetchAttendanceLogs]);

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
