/* eslint-disable react-refresh/only-export-components */
import { useState, ChangeEvent, useEffect } from 'react';
import { axiosInstance } from '../../apis/lib/axios';
import { useRequireAuth } from '../../hooks/useRequireAuth';

import '../../styles/component/project/position.scss';

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

export const position = {
	totalList: [{ jobCode: 0, total: 1, current: 0 }] as PositionItem[],
};

const Position: React.FC = () => {
	useRequireAuth();
	const [positionList, setPositionList] = useState<{ id: number; detailDescription: string }[]>([]);
	const [isSelected, setIsSelected] = useState(false); // isSelected를 상태로 관리

	useEffect(() => {
		const getPositionList = async () => {
			try {
				const response = await axiosInstance.get('/codes/details/Job');
				if (response.data.status === 'OK') {
					setPositionList(
						response.data.data.map(({ id, detailDescription }: { id: number; detailDescription: string }) => ({
							id,
							detailDescription,
						})),
					);
				}
			} catch (error) {
				console.error('Failed to fetch positions:', error);
			}
		};
		getPositionList();
		position.totalList.push({ jobCode: 0, total: 1, current: 0 });
	}, []);

	const [state, setState] = useState<PositionState>({
		positionId: [0], // Initialized with 0 indicating no selection
		positionDetailDescription: [''], // New state for display descriptions
		positionNumber: [1],
		positionCurrent: [0],
	});
	const [selectedPositionIds, setSelectedPositionIds] = useState<number[]>([]);

	const handlePositionChange = (index: number, id: number) => {
		const selectedOption = positionList.find((option) => option.id === id);
		const newSelectedIds = [...selectedPositionIds];
		newSelectedIds[index] = id; // 현재 선택된 항목으로 ID 업데이트
		setSelectedPositionIds(newSelectedIds.filter((id) => id !== 0)); // 0이 아닌 ID만 저장

		if (!selectedOption) return; // Early exit if no matching option found

		setState((prevState) => {
			const updatedPositions = prevState.positionId.map((pid, idx) => (idx === index ? id : pid));
			const updatedDescriptions = prevState.positionDetailDescription.map((desc, idx) =>
				idx === index ? selectedOption.detailDescription : desc,
			);

			// Update position.totalList here
			position.totalList[index].jobCode = id;

			return {
				...prevState,
				positionId: updatedPositions,
				positionDetailDescription: updatedDescriptions,
			};
		});
	};

	const handleParticipateButtonClick = (index: number) => {
		const updatedPositionCurrent = Array(state.positionCurrent.length).fill(0);
		updatedPositionCurrent[index] = 1;
		setIsSelected(true);
		console.log(isSelected);
		setState((prev) => ({
			...prev,
			positionCurrent: updatedPositionCurrent,
		}));
		position.totalList.forEach((item, i) => (item.current = i === index ? 1 : 0));
	};

	const handleAddPosition = () => {
		setState((prev) => {
			return {
				positionId: [...prev.positionId, 0], // Adding default value for new position
				positionDetailDescription: [...prev.positionDetailDescription, ''], // Adding empty string for new description
				positionNumber: [...prev.positionNumber, 1],
				positionCurrent: [...prev.positionCurrent, 0],
			};
		});
		position.totalList.push({ jobCode: 0, total: 1, current: 0 });
	};

	const handleRemovePosition = (index: number) => {
		setState((prev) => ({
			positionId: prev.positionId.filter((_, i) => i !== index),
			positionDetailDescription: prev.positionDetailDescription.filter((_, i) => i !== index),
			positionNumber: prev.positionNumber.filter((_, i) => i !== index),
			positionCurrent: prev.positionCurrent.filter((_, i) => i !== index),
		}));

		position.totalList.splice(index, 1);
		setSelectedPositionIds((prevIds) => {
			const removedId = state.positionId[index];
			return prevIds.filter((id) => id !== removedId);
		});
	};

	const handlePositionNumberChange = (index: number, value: number) => {
		const updatedPositionNumber = [...state.positionNumber];
		updatedPositionNumber[index] = Math.max(1, value);
		setState((prev) => ({
			...prev,
			positionNumber: updatedPositionNumber,
		}));
		if (position.totalList[index]) {
			position.totalList[index].total = value;
		}
	};

	const handleIncrementPositionNumber = (index: number) => {
		handlePositionNumberChange(index, state.positionNumber[index] + 1);
	};

	const handleDecrementPositionNumber = (index: number) => {
		handlePositionNumberChange(index, state.positionNumber[index] - 1);
	};

	return (
		<div>
			<div className='my-3'>
				<label htmlFor='PositionName' className='font-bold text-3xl my-3'>
					포지션
				</label>
				<hr className='my-3 border-1' />
				{!isSelected && <div className='flex justify-start mb-3 font-bold'>내가 참여할 포지션을 선택해 주세요</div>}
				<div>
					{state.positionDetailDescription.map((_description, index) => (
						<div key={index} className='flex items-center mb-2'>
							<button onClick={() => handleRemovePosition(index)} className='mr-2 p-3 pr-3 text-white rounded-lg'>
								➖
							</button>
							<select
								id={`PositionName-${index}`}
								name={`PositionName-${index}`}
								className='w-1/3 h-10 position-select'
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
									<option key={option.id} value={option.id} disabled={selectedPositionIds.includes(option.id)}>
										{option.detailDescription}
									</option>
								))}
							</select>
							<div className='flex items-center mx-7'>
								<div className={'grid grid-cols-3'}>
									<button
										onClick={() => handleDecrementPositionNumber(index)}
										className={`text-2xl position-btn text-white ${
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
										className='w-10 h-10 text-center'
										placeholder='인원수'
										value={state.positionNumber[index] || 1}
										onChange={(e: ChangeEvent<HTMLInputElement>) => handlePositionNumberChange(index, +e.target.value)}
									/>
									<button
										onClick={() => handleIncrementPositionNumber(index)}
										className={`text-2xl position-btn text-white ${
											state.positionNumber[index] >= 10 ? 'bg-gray-300 cursor-not-allowed' : ''
										}`}
										disabled={state.positionNumber[index] >= 10}
									>
										+
									</button>
								</div>
								<button
									className={`p-2 select-btn-border font-bold ml-6 px-7 ${
										state.positionCurrent[index] === 1 ? 'bg-main-color text-white' : ''
									}`}
									onClick={() => handleParticipateButtonClick(index)}
								>
									선택
								</button>
							</div>
						</div>
					))}
					<button onClick={handleAddPosition} className='p-2 text-white rounded-lg position-add-btn'>
						포지션 추가
					</button>
				</div>
			</div>
		</div>
	);
};

export default Position;
