import React, { ReactElement, useRef, useState } from 'react';
import { axiosInstance } from '../../apis/lib/axios';
import { useUserStore } from '../../stores/useUserStore';
import { useParams } from 'react-router-dom';
import { useDetailCodes } from '../../hooks/useDetailCodes';

interface KPTMemoProps {
	modalOpen: boolean;
	setModalOpen: (value: boolean) => void;
	onKPTUpdate: () => void;
}

interface DetailCodeData {
	id: number;
	detailName: string;
}

const WriteKPTMemo: React.FC<KPTMemoProps> = ({ modalOpen, setModalOpen, onKPTUpdate }): ReactElement => {
	const modalBackground = useRef<HTMLDivElement>(null);
	const [selectedSection, setSelectedSection] = useState<string | null>('Keep');
	const [textareaValue, setTextareaValue] = useState<string>('');
	const [showConfirmationModal, setShowConfirmationModal] = useState<boolean>(false);
	const [nextSection, setNextSection] = useState<string | null>(null);
	const { profile } = useUserStore();
	const { projectId } = useParams();
	const { data: detailCodesData } = useDetailCodes('KPT');

	const closeModalAndClearText = () => {
		setModalOpen(false); // 모달 닫기
		setTextareaValue(''); // 텍스트 영역 초기화
	};

	const writeKPT = async () => {
		if (profile && projectId && detailCodesData && detailCodesData.length > 0) {
			// 수정: 상태 검사를 배열 길이로 변경
			const selectedDetailCode = detailCodesData.find(
				(d: DetailCodeData) => d.detailName === selectedSection,
			) as DetailCodeData;
			if (!selectedDetailCode) {
				console.error('선택된 섹션에 대한 상세 코드 정보가 없습니다.');
				return;
			}

			const numPId = projectId;

			const KPTdata = {
				projectId: numPId,
				memberId: profile.id,
				kptCode: selectedDetailCode.id,
				week: 1,
				content: textareaValue,
			};

			console.log(KPTdata);

			try {
				await axiosInstance.post('/retrospects', KPTdata);
				closeModalAndClearText();
				onKPTUpdate();
			} catch (error) {
				console.log('비상');
			}
		}
	};

	const handleSectionClick = (section: string) => {
		if (textareaValue) {
			setShowConfirmationModal(true);
			setNextSection(section);
		} else {
			setSelectedSection(section);
		}
	};

	const confirmSectionChange = () => {
		setSelectedSection(nextSection);
		setTextareaValue('');
		setShowConfirmationModal(false);
	};

	const getBorderColorClass = () => {
		switch (selectedSection) {
			case 'Keep':
				return 'border-green-500'; // 초록색 테두리
			case 'Problem':
				return 'border-red-500'; // 빨간색 테두리
			case 'Try':
				return 'border-main-color'; // 메인 색상 테두리
			default:
				return 'border-main-color'; // 기본 테두리 색상
		}
	};

	const handleTextareaChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
		const inputValue = e.target.value;
		if (inputValue.length > 50) {
			setTextareaValue(inputValue.slice(0, 50));
		} else {
			setTextareaValue(inputValue);
		}
	};

	return (
		<div>
			{modalOpen && (
				<div
					className='fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-10'
					ref={modalBackground}
					onClick={(e) => {
						if (e.target === modalBackground.current) {
							closeModalAndClearText(); // 모달 닫고 텍스트 초기화
						}
					}}
				>
					<div className='bg-white p-5 rounded-lg shadow-lg w-11/12 max-w-2xl'>
						<div className='flex'>
							<div
								className={`flex-1 p-4 cursor-pointer ${selectedSection === 'Keep' ? 'bg-sub-color' : ''}`}
								onClick={() => handleSectionClick('Keep')}
							>
								<h2 className='text-center'>Keep</h2>
							</div>
							<div className='border-main-color border-r'></div>
							<div
								className={`flex-1 p-4 cursor-pointer ${selectedSection === 'Problem' ? 'bg-sub-color' : ''}`}
								onClick={() => handleSectionClick('Problem')}
							>
								<h2 className='text-center'>Problem</h2>
							</div>
							<div className='border-main-color border-r'></div>
							<div
								className={`flex-1 p-4 cursor-pointer ${selectedSection === 'Try' ? 'bg-sub-color' : ''}`}
								onClick={() => handleSectionClick('Try')}
							>
								<h2 className='text-center'>Try</h2>
							</div>
						</div>
						{selectedSection && (
							<div className='mt-4'>
								<textarea
									className={`w-full h-32 p-2 rounded border-2 ${getBorderColorClass()}`}
									value={textareaValue}
									placeholder='50자 이내로 작성해주세요'
									onChange={handleTextareaChange}
								></textarea>
							</div>
						)}
						<div className='flex justify-center mt-4'>
							<button className='modal-close-btn border-2 mr-2 border-main-color' onClick={writeKPT}>
								회고 등록
							</button>
							<button className='modal-close-btn border-2 border-main-color' onClick={() => closeModalAndClearText()}>
								닫기
							</button>
						</div>
					</div>
				</div>
			)}
			{showConfirmationModal && (
				<div className='fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-20'>
					<div className='bg-white p-5 rounded-lg shadow-lg max-w-sm mx-auto'>
						<h3 className='text-lg leading-6 font-medium text-gray-900 mb-4'>변경 확인</h3>
						<p className='text-sm text-gray-500'>
							선택한 섹션으로 변경하면 현재 작성 중인 내용이 초기화됩니다. 계속하시겠습니까?
						</p>
						<div className='flex justify-end mt-4'>
							<button
								className='inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 mr-3'
								onClick={() => setShowConfirmationModal(false)}
							>
								취소
							</button>
							<button
								className='inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500'
								onClick={confirmSectionChange}
							>
								확인
							</button>
						</div>
					</div>
				</div>
			)}
		</div>
	);
};

export default WriteKPTMemo;
