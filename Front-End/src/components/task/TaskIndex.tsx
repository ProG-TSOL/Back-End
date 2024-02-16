import React, { useState } from 'react';
import { FaCaretDown, FaCaretRight } from 'react-icons/fa6';
import DetailTask from './DetailTask';
import TaskOneDetail from './TaskOneDetail';
import moment from 'moment';
import Chip from './TaskChip';

type TodoListProps = {
	title: string;
	tasks: Task[];
	onTaskUpdate: () => void;
};

interface Task {
	id: number;
	workId?: number;
	title: string;
	statusCode: {
		id: number;
		detailName: string;
		detailDescription: string;
		imgUrl: string | null;
	};
	typeCode: {
		id: number;
		detailName: string;
		detailDescription: string;
		imgUrl: string | null;
	};
	priorityCode: {
		id: number;
		detailName: string;
		detailDescription: string;
		imgUrl: string | null;
	};
	producerMemberName: string;
	startDay: string;
	endDay: string;
}

const TaskIndex: React.FC<TodoListProps> = ({ title, tasks, onTaskUpdate }) => {
	const [showButton, setShowButton] = useState<boolean>(false);
	const [indexListOn, setIndexListOn] = useState<boolean>(false);
	const [showDetailTaskModal, setShowDetailTaskModal] = useState<boolean>(false);
	const [selectedTaskDetail, setSelectedTaskDetail] = useState<Task | null>(null);

	const handleIndexListOn = () => {
		setIndexListOn(!indexListOn);
	};

	const handleShowDetailTaskModal = () => {
		setShowDetailTaskModal((prev) => !prev);
	};

	const handleTaskClick = (taskDetail: Task) => {
		setSelectedTaskDetail(taskDetail);
	};

	return (
		<div>
			<div
				className='flex border bg-gray-100 p-2 items-center' // 여기
				onMouseEnter={() => setShowButton(true)}
				onMouseLeave={() => setShowButton(false)}
			>
				<div onClick={handleIndexListOn}>
					{indexListOn ? <FaCaretRight className='text-xl' /> : <FaCaretDown className='text-xl' />}
				</div>
				<span className='text-xl font-bold'>
					{title} ({tasks.length})
				</span>
				<button
					className={`ml-2 border-2 px-2 ${showButton ? 'block' : 'hidden'} transition-all duration-300`}
					onClick={handleShowDetailTaskModal}
				>
					업무 추가
				</button>
			</div>
			{!indexListOn && (
				<ul>
					{tasks.map((item) => (
						<li
							key={item.id}
							className='flex border border-gray-200 cursor-pointer hover:bg-sub-color'
							onClick={() => handleTaskClick(item)}
						>
							<div
								className={`flex-grow w-32 p-4 ${
									item.statusCode.detailDescription === '완료' ? 'line-through text-gray-400' : ''
								}`}
							>
								{item.title}
							</div>
							<div className='w-32 p-4 border-gray border-x-2 text-center'>
								<Chip
									label={item.statusCode.detailDescription}
									color={
										item.statusCode.detailDescription === '시작 전'
											? 'bg-yellow-500'
											: item.statusCode.detailDescription === '진행 중'
											? 'bg-blue-500'
											: 'bg-green-500'
									}
								/>
							</div>
							<div className='w-32 p-4 border-gray border-x-2 text-center'>
								<Chip
									label={item.priorityCode.detailDescription}
									color={
										item.priorityCode.detailDescription === '상'
											? 'bg-red-500'
											: item.priorityCode.detailDescription === '중'
											? 'bg-orange-500'
											: 'bg-gray-500'
									}
								/>
							</div>
							<div className='w-1/6 p-4 text-center'>{item.producerMemberName}</div>
							<div className='w-1/12 p-4 border-gray border-x-2 text-center'>
								{moment(item.startDay).format('MM-DD')}
							</div>
							<div className='w-1/12 p-4 text-center'>{moment(item.endDay).format('MM-DD')}</div>
						</li>
					))}
				</ul>
			)}
			{selectedTaskDetail && (
				<TaskOneDetail
					taskDetail={selectedTaskDetail}
					onClose={() => setSelectedTaskDetail(null)}
					onTaskUpdate={onTaskUpdate}
				/>
			)}
			{showDetailTaskModal && (
				<DetailTask onClose={() => setShowDetailTaskModal((prev) => !prev)} onTaskUpdate={onTaskUpdate} />
			)}
		</div>
	);
};

export default TaskIndex;
