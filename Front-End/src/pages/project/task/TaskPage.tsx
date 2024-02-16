import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { axiosInstance } from '../../../apis/lib/axios';
import TaskIndex from '../../../components/task/TaskIndex';
import { useRequireAuth } from '../../../hooks/useRequireAuth';

import '../../../styles/page/project-home/project-task.scss';

interface Task {
	id: number;
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
const TaskPage = () => {
	useRequireAuth();

	const { projectId } = useParams();
	const [tasks, setTasks] = useState<Task[]>([]);

	// 태스크 데이터를 불러오는 함수
	const fetchTasks = async () => {
		try {
			const response = await axiosInstance.get(`/works/${projectId}`);
			const responseData = response.data.data ? response.data.data : [];
			setTasks(responseData);
		} catch (error) {
			console.error(error);
		}
	};

	// 컴포넌트 마운트 시 태스크 데이터 불러오기
	useEffect(() => {
		fetchTasks();
	}, [projectId]);

	// 태스크 유형별로 필터링하는 함수
	const filteredTasks = (typeId: number) => {
		return tasks.filter((task) => task.typeCode.id === typeId);
	};

	return (
		<div className='flex justify-center mt-5 overflow-y-auto task-main'>
			<div className='w-3/4 bg-white shadow-lg'>
				<div className='flex'>
					<div className='table-border-color border-y-2 w-32 text-center flex-grow p-4'>
						<span className='font-bold text-xl'>업무명</span>
					</div>
					<div className='table-border-color border-2 w-32 p-4 text-center'>
						<span className='font-bold text-xl'>상태</span>
					</div>
					<div className='table-border-color border-y-2 w-32 p-4 text-center'>
						<span className='font-bold text-xl'>우선순위</span>
					</div>
					<div className='table-border-color border-2 w-1/6 p-4 text-center'>
						<span className='font-bold text-xl'>담당자</span>
					</div>
					<div className='table-border-color border-y-2 w-1/12 p-4 text-center'>
						<span className='font-bold text-xl'>시작</span>
					</div>
					<div className='table-border-color border-y-2 w-1/12 p-4 text-center'>
						<span className='font-bold text-xl'>마감</span>
					</div>
				</div>
				<TaskIndex title='1. 분석 🔮' tasks={filteredTasks(4)} onTaskUpdate={fetchTasks} />
				<TaskIndex title='2. 설계 🎯' tasks={filteredTasks(5)} onTaskUpdate={fetchTasks} />
				<TaskIndex title='3. 개발 👩‍💻' tasks={filteredTasks(6)} onTaskUpdate={fetchTasks} />
				<TaskIndex title='4. 테스트 🕵️‍♀️' tasks={filteredTasks(7)} onTaskUpdate={fetchTasks} />
				<TaskIndex title='5. 기타 🎸' tasks={filteredTasks(8)} onTaskUpdate={fetchTasks} />
			</div>
		</div>
	);
};

export default TaskPage;
