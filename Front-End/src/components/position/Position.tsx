import React, { useState, ChangeEvent } from 'react';

interface PositionState {
  positionName: string[];
  positionNumber: number[];
  positionCurrent: number[];
}

interface PositionItem {
  jobCode: string;
  total: number;
  current: number;
}

const positionList = ["포지션을 선택해 주세요", "프론트엔드", "백엔드", "기획", "디자인", "UI/UX"];

export const position = {
  totalList: [{ jobCode: '', total: 1, current: 0 }] as PositionItem[],
};


const Position: React.FC = () => {
  const [state, setState] = useState<PositionState>({
    positionName: [''],
    positionNumber: [1],
    positionCurrent: [0],
  });

  const handlePositionNameChange = (index: number, value: string) => {
    const updatedPositionName = [...state.positionName];
    updatedPositionName[index] = value;

    setState((prev) => ({
      ...prev,
      positionName: updatedPositionName,
    }));

    if (position.totalList[index]) {
      position.totalList[index].jobCode = value;
    }
  };


  const handleAddPositionName = () => {
    setState((prev) => {
      const newPositionName = [...prev.positionName, ''];
      const newPositionNumber = [...prev.positionNumber, 1];
      const newPositionCurrent = [...prev.positionCurrent, 0];
  
      return {
        positionName: newPositionName,
        positionNumber: newPositionNumber,
        positionCurrent: newPositionCurrent,
      };
    });
    position.totalList.push({ jobCode: '', total: 1, current: 0 });
  };
  
  
  

  const handleRemovePositionName = (index: number) => {
    const updatedPositionName = [...state.positionName];
    const updatedPositionNumber = [...state.positionNumber];
    const updatedPositionCurrent = [...state.positionCurrent];
    updatedPositionName.splice(index, 1);
    updatedPositionNumber.splice(index, 1);
    updatedPositionCurrent.splice(index, 1);
    setState({
      positionName: updatedPositionName,
      positionNumber: updatedPositionNumber,
      positionCurrent: updatedPositionCurrent,
    });
    position.totalList.splice(index, 1);
  };

  const handlePositionNumberChange = (index: number, value: number) => {
    // Ensure the index is valid
    if (index < 0 || index >= state.positionNumber.length) {
      return;
    }

    // Update the state with the new position number
    setState((prev) => {
      const updatedPositionNumber = [...prev.positionNumber];
      updatedPositionNumber[index] = Math.max(1, value);

      // Update the totalList array
      const updatedTotalList = [...position.totalList];
      updatedTotalList[index] = {
        ...updatedTotalList[index],
        total: updatedPositionNumber[index],
      };

      return {
        ...prev,
        positionNumber: updatedPositionNumber,
      };
    });
  };


  const handleIncrementPositionNumber = (index: number) => {
    const updatedPositionNumber = [...state.positionNumber];
    updatedPositionNumber[index] += 1;
    setState((prev) => ({
      ...prev,
      positionNumber: updatedPositionNumber,
    }));
    position.totalList[index].total = updatedPositionNumber[index];
  };

  const handleDecrementPositionNumber = (index: number) => {
    const updatedPositionNumber = [...state.positionNumber];
    updatedPositionNumber[index] = Math.max(1, updatedPositionNumber[index] - 1);
    setState((prev) => ({
      ...prev,
      positionNumber: updatedPositionNumber,
    }));
    position.totalList[index].total = updatedPositionNumber[index];
  };

  return (
    <div>
      <div className='my-3'>
        <label htmlFor='PositionName' className='font-bold text-lg my-3'>
          포지션
        </label>
        <div>
          {state.positionName.map((positionName, index) => (
            <div key={index} className='flex items-center mb-2'>
              <button
                onClick={() => handleRemovePositionName(index)}
                className='mr-2 p-2 bg-red-500 text-white'
              >
                -
              </button>
              <select
                id={`PositionName-${index}`}
                name={`PositionName-${index}`}
                className='w-1/3 h-10'
                value={positionName}
                onChange={(e: ChangeEvent<HTMLSelectElement>) => {
                  handlePositionNameChange(index, e.target.value);
                  console.log(`Selected position at index ${index}: ${e.target.value}`);
                }}
              >
                {positionList.map((option, optionIndex) => (
                  <option key={optionIndex} value={option}>
                    {option}
                  </option>
                ))}
              </select>

              <div className='flex items-center'>
                <button
                  onClick={() => handleDecrementPositionNumber(index)}
                  className={`p-2 bg-blue-500 text-white ${state.positionNumber[index] <= 1 ? 'bg-gray-300 cursor-not-allowed' : ''
                    }`}
                  disabled={state.positionNumber[index] <= 1}
                >
                  -
                </button>
                <input
                  type='text'
                  id={`PositionNumber-${index}`}
                  name={`PositionNumber-${index}`}
                  className='w-1/3 h-10'
                  placeholder='인원수'
                  value={state.positionNumber[index] || 1}
                  onChange={(e: ChangeEvent<HTMLInputElement>) =>
                    handlePositionNumberChange(index, +e.target.value)
                  }
                />
                <button
                  onClick={() => handleIncrementPositionNumber(index)}
                  className={`p-2 bg-blue-500 text-white ${state.positionNumber[index] >= 10 ? 'bg-gray-300 cursor-not-allowed' : ''
                    }`}
                  disabled={state.positionNumber[index] >= 10}
                >
                  +
                </button>
              </div>
            </div>
          ))}
          <button onClick={handleAddPositionName} className='p-2 bg-green-500 text-white'>
            +
          </button>
        </div>
      </div>
    </div>
  );
};

export default Position;