import React, { useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { deleteKPT, modifyKPT } from '../../hooks/useModifyDeleteKPT';

interface Memo {
	retrospectId: number;
	content: string;
}

interface KeepDetailProps {
	isOpen: boolean;
	onClose: () => void;
	memos: Memo[];
	onKPTUpdate: () => void;
}

const ProblemDetail: React.FC<KeepDetailProps> = ({ isOpen, onClose, memos, onKPTUpdate }) => {
	const [editingId, setEditingId] = useState<number | null>(null);
	const [editContent, setEditContent] = useState<string>('');
	const queryClient = useQueryClient();

	const modifyMutation = useMutation({
		mutationFn: (data: { retrospectId: number; text: string }) => modifyKPT(data),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ['memos'] }).then(() => {
				onKPTUpdate();
			});
			setEditingId(null); // 수정이 성공적으로 완료된 후 편집 모드 종료
		},
	});

	const deleteMutation = useMutation({
		mutationFn: (retrospectId: number) => deleteKPT(retrospectId),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ['memos'] }).then(() => {
				onKPTUpdate();
			});
		},
	});

	const handleEditChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		const newValue = e.target.value.slice(0, 50); // 최대 50글자까지 입력 가능
		setEditContent(newValue);
	};

	if (!isOpen) return null;

	return (
		<div className='fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50'>
			<div className='bg-white p-5 rounded-lg shadow-lg w-11/12 max-w-3xl'>
				<h2 className='text-lg font-bold mb-4'>전체 Problem</h2>
				<div className='overflow-y-auto max-h-96'>
					{memos.map((memo) => (
						<div key={memo.retrospectId} className='group bg-red-300 p-4 rounded-md mb-4 flex relative'>
							{editingId === memo.retrospectId ? (
								<>
									<input
										type='text'
										value={editContent}
										onChange={handleEditChange} // 글자 수 제한 적용
										className='p-2 w-full'
									/>
									<button
										onClick={() =>
											modifyMutation.mutate({
												retrospectId: memo.retrospectId,
												text: editContent,
											})
										}
										className='absolute right-0 top-3 mr-6 mt-2 bg-green-500 text-white px-2 py-1 rounded hover:bg-green-700'
									>
										등록
									</button>
								</>
							) : (
								<>
									<p>{memo.content}</p>
									<button
										onClick={() => {
											setEditingId(memo.retrospectId);
											setEditContent(memo.content);
										}}
										className='absolute right-0 top-0 mr-16 mt-2 bg-blue-500 text-white px-2 py-1 rounded hover:bg-blue-700 opacity-0 group-hover:opacity-100 transition-opacity duration-300 ease-in-out'
									>
										수정
									</button>
									<button
										onClick={() => deleteMutation.mutate(memo.retrospectId)}
										className='absolute right-0 top-0 mr-2 mt-2 bg-red-500 text-white px-2 py-1 rounded hover:bg-red-700 opacity-0 group-hover:opacity-100 transition-opacity duration-300 ease-in-out'
									>
										삭제
									</button>
								</>
							)}
						</div>
					))}
				</div>
				<div className='flex justify-end mt-4'>
					<button
						onClick={onClose}
						className='px-4 py-2 bg-red-500 text-white rounded hover:bg-red-700 transition-colors'
					>
						닫기
					</button>
				</div>
			</div>
		</div>
	);
};

export default ProblemDetail;
