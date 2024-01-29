import React, { useState, ChangeEvent } from 'react';

interface PositionState {
  positionName: string[];
  positionNumber: number[];
}

export const position = {
  posName: [] as string[],
  posNumber: [1],
};

const Position: React.FC = () => {
  const [state, setState] = useState<PositionState>({
    positionName: [''],
    positionNumber: [1],
  });

  const handlePositionNameChange = (index: number, value: string) => {
    const updatedPositionName = [...state.positionName];
    updatedPositionName[index] = value;
    setState((prev) => ({
      ...prev,
      positionName: updatedPositionName,
    }));
    position.posName = updatedPositionName;
  };

  const handleAddPositionName = () => {
    setState((prev) => {
      const newPositionName = [...prev.positionName, ''];
      const newPositionNumber = [...prev.positionNumber, 1];

      position.posName = newPositionName;
      position.posNumber = newPositionNumber;

      return {
        positionName: newPositionName,
        positionNumber: newPositionNumber,
      };
    });
  };

  const handleRemovePositionName = (index: number) => {
    const updatedPositionName = [...state.positionName];
    const updatedPositionNumber = [...state.positionNumber];
    updatedPositionName.splice(index, 1);
    updatedPositionNumber.splice(index, 1);
    setState({
      positionName: updatedPositionName,
      positionNumber: updatedPositionNumber,
    });
    position.posName = updatedPositionName;
    position.posNumber = updatedPositionNumber;
  };

  const handlePositionNumberChange = (index: number, value: number) => {
    const updatedPositionNumber = [...state.positionNumber];
    updatedPositionNumber[index] = Math.max(1, value);
    setState((prev) => ({
      ...prev,
      positionNumber: updatedPositionNumber,
    }));
    position.posNumber = updatedPositionNumber;
  };

  const handleIncrementPositionNumber = (index: number) => {
    const updatedPositionNumber = [...state.positionNumber];
    updatedPositionNumber[index] += 1;
    setState((prev) => ({
      ...prev,
      positionNumber: updatedPositionNumber,
    }));
    position.posNumber[index] = updatedPositionNumber[index];
  };

  const handleDecrementPositionNumber = (index: number) => {
    const updatedPositionNumber = [...state.positionNumber];
    updatedPositionNumber[index] = Math.max(1, updatedPositionNumber[index] - 1);
    setState((prev) => ({
      ...prev,
      positionNumber: updatedPositionNumber,
    }));
    position.posNumber[index] = updatedPositionNumber[index];
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
              <input
                type='text'
                id={`PositionName-${index}`}
                name={`PositionName-${index}`}
                className='w-1/3 h-10'
                placeholder='포지션명'
                value={positionName}
                onChange={(e: ChangeEvent<HTMLInputElement>) =>
                  handlePositionNameChange(index, e.target.value)
                }
              />
              <div className='flex items-center'>
                <button
                  onClick={() => handleDecrementPositionNumber(index)}
                  className={`p-2 bg-blue-500 text-white ${
                    state.positionNumber[index] <= 1 ? 'bg-gray-300 cursor-not-allowed' : ''
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
                  className={`p-2 bg-blue-500 text-white ${
                    state.positionNumber[index] >= 10 ? 'bg-gray-300 cursor-not-allowed' : ''
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
