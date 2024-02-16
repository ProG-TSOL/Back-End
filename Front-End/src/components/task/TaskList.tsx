import { useEffect, useState } from "react";
import TaskIndex from "./TaskIndex";
import { axiosInstance } from "../../apis/lib/axios";
import { useParams } from "react-router-dom";

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

const TaskList = () => {
  const { projectId } = useParams();
  const [tasks, setTasks] = useState<Task[]>([]);

  useEffect(() => {
    const fetchTasks = async () => {
      try {
        const response = await axiosInstance.get(`/works/${projectId}`);
        const responseData = response.data.data ? response.data.data : [];
        setTasks(responseData);
        console.log(response.data);
      } catch (error) {
        console.error(error);
      }
    };

    fetchTasks();
  }, [projectId]);

  const filteredTasks = (typeId: number) => {
    // tasks가 배열인지 확인하고, 맞다면 필터링을 진행
    return Array.isArray(tasks) ? tasks.filter(task => task.typeCode.id === typeId) : [];
  };

  return (
    <div className="flex justify-center mt-5">
      <div className="w-3/4 bg-white shadow-lg rounded-lg overflow-hidden">
        <div className="flex">
          <div className="border-main-color border-2 w-1/2 flex-grow p-4">
            <span className="font-bold text-xl">업무명</span>
          </div>
          <div className="border-main-color border-2 w-1/6 p-4 text-center">
            <span className="font-bold text-xl">상태</span>
          </div>
          <div className="border-main-color border-2 w-1/6 p-4 text-center">
            <span className="font-bold text-xl">담당자</span>
          </div>
          <div className="border-main-color border-2 w-1/12 p-4 text-center">
            <span className="font-bold text-xl">시작</span>
          </div>
          <div className="border-main-color border-2 w-1/12 p-4 text-center">
            <span className="font-bold text-xl">마감</span>
          </div>
        </div>
        <TaskIndex title="1. 분석 🔮" tasks={filteredTasks(4)} />
        <TaskIndex title="2. 설계 🎯" tasks={filteredTasks(5)} />
        <TaskIndex title="3. 개발 👩‍💻" tasks={filteredTasks(6)} />
        <TaskIndex title="4. 테스트 🕵️‍♀️" tasks={filteredTasks(7)} />
        <TaskIndex title="5. 기타 🎸" tasks={filteredTasks(8)} />
      </div>
    </div>
  );
};

export default TaskList;
