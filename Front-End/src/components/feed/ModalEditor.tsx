// ModalEditor.jsx
import React, { FC, useRef, useState } from 'react';
import ReactQuill from 'react-quill';
import { FaAnglesRight } from 'react-icons/fa6';

type ModalEditorProps = {
	isOpen?: boolean; // ?를 붙이면, 선택 가능한 Type이 됨.
	onClose?: () => void;
	value: string;
	onChange: (content: string) => void;
	onSubmit: (title: string, content: string) => void;
};

const ModalEditor: FC<ModalEditorProps> = ({
	isOpen = true,
	onClose = () => {}, //기본값을 빈 함수로 설정
	value,
	onChange,
	onSubmit,
}) => {
	const quillRef = useRef<ReactQuill>(null);
	const [title, setTitle] = useState('');
	if (!isOpen) return null;

	const handleTitleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		setTitle(e.target.value);
	};

	const handleSubmit = () => {
		onSubmit(title, value); // 제목과 내용을 백엔드로 보냄
		setTitle('');
		onClose(); // 모달 닫기
	};

	const modules = {
		toolbar: [
			[{ header: [1, 2, false] }],
			['bold', 'italic', 'underline', 'strike', 'blockquote'],
			[{ list: 'ordered' }, { list: 'bullet' }, { indent: '-1' }, { indent: '+1' }],
			['link'],
			[{ align: [] }, { color: [] }, { background: [] }],
			['clean'],
		],
	};

	return (
		<>
			<div className={`fixed inset-0 bg-black bg-opacity-50 z-30 ${!isOpen && 'hidden'}`} onClick={onClose}></div>
			<div
				className={`fixed right-0 mt-16 top-0 h-full overflow-y-auto bg-slate-50 p-8 rounded-md shadow-lg z-40 w-full max-w-2xl transition-transform transform ${
					isOpen ? 'translate-x-0' : 'translate-x-full'
				} ease-in-out duration-300`}
			>
				<div className='flex items-center mb-4'>
					<FaAnglesRight onClick={onClose} className='text-lg text-gray-600 mr-2 cursor-pointer' />
					<h1 className='text-xl font-bold'>자유 피드 등록</h1>
				</div>
				<div className='mb-4'>
					<p className='text-lg font-bold mb-2'>제목</p>
					<input
						className='w-full p-2 border border-gray-300 rounded'
						placeholder='제목을 입력하세요'
						value={title}
						onChange={handleTitleChange}
					/>
				</div>
				<p className='mb-2'>내용</p>
				<div className='mb-20'>
					<ReactQuill ref={quillRef} className='h-80' modules={modules} value={value} onChange={onChange} />
				</div>
				<div className='flex justify-end space-x-4 mt-4'>
					<button
						className='bg-main-color text-white p-2 rounded hover:bg-blue-700 transition duration-300'
						onClick={handleSubmit}
					>
						저장
					</button>
					<button
						className='bg-red-500 text-white p-2 rounded hover:bg-red-700 transition duration-300'
						onClick={onClose}
					>
						닫기
					</button>
				</div>
			</div>
		</>
	);
};

export default ModalEditor;
