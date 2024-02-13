import React, { useState, ChangeEvent, useEffect } from "react";
import { axiosInstance } from "../../apis/lib/axios";
import { useRequireAuth } from '../../hooks/useRequireAuth';
import {useUserStore} from '../../stores/useUserStore';
import { useParams } from 'react-router-dom';

interface PositionState {
  positionId: number[]; // Changed from positionName to positionId
  positionDetailDescription: string[]; // New addition to handle display names
  positionNumber: number[];
  positionCurrent: number[];
}

interface PositionItem {
  jobCode: number; // Using id to track the selected position
  total: number;
  current: number;
}

interface PositionList{
  id : number;
  name: string;
  total: number;
  current:number;
}

export const position = {
  totalList: [] as PositionItem[],
};

interface PositionProps {
  initialTags?: PositionList[];
}

const PositionChange: React.FC<PositionProps> = ({ initialTags = [] }: PositionProps) => {
  useRequireAuth();
  const { profile } = useUserStore();
  const memberId = profile?.id;
  const { projectId } = useParams();
  const [positionList, setPositionList] = useState<{ id: number; detailDescription: string }[]>([]);
  useEffect(() => {
    const getPositionList = async () => {
      try {
        const response = await axiosInstance.get('/codes/details/Job');
        if (response.data.status === 'OK') {
          setPositionList(response.data.data.map(({ id, detailDescription }: { id: number; detailDescription: string }) => ({ id, detailDescription })));
        }
      } catch (error) {
        console.error("Failed to fetch positions:", error);
      }
    };
  
    getPositionList();
  
    // initialTags를 기반으로 position.totalList와 state를 업데이트
    position.totalList = initialTags.map(tag => ({
      jobCode: tag.id,
      total: tag.total,
      current: tag.current,
    }));
  
    setState({
      positionId: initialTags.map(tag => tag.id),
      positionDetailDescription: initialTags.map(tag => tag.name),
      positionNumber: initialTags.map(tag => tag.total),
      positionCurrent: initialTags.map(tag => tag.current),
    });
  
    // 추가된 로그로 확인 (실제 코드에서는 제거 가능)
    console.log(position.totalList);
    console.log(state);
  
  }, [initialTags]); // initialTags가 변경될 때마다 useEffect 실행
  
  
  const [state, setState] = useState<PositionState>({
    positionId: [0], // Initialized with 0 indicating no selection
    positionDetailDescription: [""], // New state for display descriptions
    positionNumber: [1],
    positionCurrent: [0],
  },);

  const handlePositionChange = (index: number, id: number) => {
    const selectedOption = positionList.find(option => option.id === id);
    if (!selectedOption) return; // Early exit if no matching option found
  
    setState(prevState => {
      const updatedPositions = prevState.positionId.map((pid, idx) => idx === index ? id : pid);
      const updatedDescriptions = prevState.positionDetailDescription.map((desc, idx) => idx === index ? selectedOption.detailDescription : desc);
  
      // Update position.totalList here
      position.totalList[index].jobCode = id;
  
      return {
        ...prevState,
        positionId: updatedPositions,
        positionDetailDescription: updatedDescriptions,
      };
    });
  };
  
  


  const handleAddPosition = () => {
    setState(prevState => ({
      positionId: [...prevState.positionId, 0], // 새 포지션 기본값 추가
      positionDetailDescription: [...prevState.positionDetailDescription, ""], // 새 설명 기본값 추가
      positionNumber: [...prevState.positionNumber, 1], // 새 수량 기본값 추가
      positionCurrent: [...prevState.positionCurrent, 0], // 새 현재값 기본값 추가
    }));
  
    // 불변성을 유지하며 position.totalList 업데이트
    position.totalList = [...position.totalList, { jobCode: 0, total: 1, current: 0 }];
    console.log(position.totalList);
    console.log(state);
  };
  
  const handleRemovePosition = async (index: number) => {
    try {
      // 서버에서 포지션 제거 요청
      await axiosInstance.delete(`/projects/${projectId}/${memberId}/projectTotal/${position.totalList[index].jobCode}`);
      setState(prevState => ({
        positionId: prevState.positionId.filter((_, i) => i !== index),
        positionDetailDescription: prevState.positionDetailDescription.filter((_, i) => i !== index),
        positionNumber: prevState.positionNumber.filter((_, i) => i !== index),
        positionCurrent: prevState.positionCurrent.filter((_, i) => i !== index),
      }));
  
      // 불변성을 유지하며 position.totalList 업데이트
      position.totalList = position.totalList.filter((_, i) => i !== index);
    } catch (error) {
      console.error("Failed to remove position:", error);
    }
  };
  
  const handlePositionNumberChange = (index: number, value: number) => {
    setState(prevState => ({
      ...prevState,
      positionNumber: prevState.positionNumber.map((num, i) => i === index ? Math.max(1, value) : num),
    }));
  
    // 불변성을 유지하며 position.totalList 업데이트
    position.totalList = position.totalList.map((item, i) => i === index ? { ...item, total: Math.max(1, value) } : item);
  };
  

  const handleIncrementPositionNumber = (index: number) => {
    handlePositionNumberChange(index, state.positionNumber[index] + 1);
  };

  const handleDecrementPositionNumber = (index: number) => {
    handlePositionNumberChange(index, state.positionNumber[index] - 1);
  };

  return (
    <div>
      <div className="my-3">
        <label htmlFor="PositionName" className="font-bold text-lg my-3">
          포지션
        </label>
        <div>
          {state.positionDetailDescription.map((description, index) => (
            <div key={index} className="flex items-center mb-2">
              <button
                onClick={() => handleRemovePosition(index)}
                className="mr-2 p-2 pr-3 bg-red-500 text-white"
              >
                -
              </button>
              <select
                id={`PositionName-${index}`}
                name={`PositionName-${index}`}
                className="w-1/3 h-10"
                value={state.positionId[index]}
                onChange={(e: ChangeEvent<HTMLSelectElement>) => {
                  const id = parseInt(e.target.value, 10); // Convert string to number
                  if (!isNaN(id)) {
                    handlePositionChange(index, id);
                  }
                }}
              >
                <option value={0}>포지션 선택</option>
                {positionList.map((option) => (
                  <option key={option.id} value={option.id}>
                    {option.detailDescription}
                  </option>
                ))}
              </select>
              <div className="flex items-center">
                <button
                  onClick={() => handleDecrementPositionNumber(index)}
                  className={`p-2 bg-blue-500 text-white ${state.positionNumber[index] <= 1
                      ? "bg-gray-300 cursor-not-allowed"
                      : ""
                    }`}
                  disabled={state.positionNumber[index] <= 1}
                >
                  -
                </button>
                <input
                  type="text"
                  id={`PositionNumber-${index}`}
                  name={`PositionNumber-${index}`}
                  className="w-1/3 h-10"
                  placeholder="인원수"
                  value={state.positionNumber[index] || 1}
                  onChange={(e: ChangeEvent<HTMLInputElement>) =>
                    handlePositionNumberChange(index, +e.target.value)
                  }
                />
                <button
                  onClick={() => handleIncrementPositionNumber(index)}
                  className={`p-2 bg-blue-500 text-white ${state.positionNumber[index] >= 10
                      ? "bg-gray-300 cursor-not-allowed"
                      : ""
                    }`}
                  disabled={state.positionNumber[index] >= 10}
                >
                  +
                </button>
              </div>
            </div>
          ))}
          <button
            onClick={handleAddPosition}
            className="p-2 bg-green-500 text-white"
          >
            +
          </button>
        </div>
      </div>
    </div>
  );
};

export default PositionChange;
