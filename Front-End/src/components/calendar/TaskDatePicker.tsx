import React from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { FaCalendarCheck } from 'react-icons/fa'; // 'fa6'은 잘못된 표기, 'fa'가 맞습니다.

interface TaskDatePickerProps {
	selectedDate: Date;
	setSelectedDate: (date: Date) => void;
}

const TaskDatePicker: React.FC<TaskDatePickerProps> = ({ selectedDate, setSelectedDate }) => {
	// 오늘 날짜에서 하루를 더한 날짜를 계산합니다.
	const tomorrow = new Date();
	tomorrow.setDate(tomorrow.getDate() + 1);

	// DatePicker 변경 핸들러
	const handleChange = (date: Date | null) => {
		// date가 null이 아닐 때만 상태를 업데이트합니다.
		if (date) {
			setSelectedDate(date);
		}
	};

	return (
		<div className='flex z-50 relative'>
			<FaCalendarCheck className='mt-2 mr-2' />
			<DatePicker
				id='datepicker'
				selected={selectedDate}
				onChange={handleChange}
				minDate={tomorrow} // 오늘 날짜의 다음날을 최소 날짜로 설정
				dateFormat='yyyy-MM-dd'
				className='mb-2 block w-36 pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md'
			/>
		</div>
	);
};

export default TaskDatePicker;
