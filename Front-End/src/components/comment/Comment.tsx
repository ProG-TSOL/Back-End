import React, { useEffect, useState } from 'react';
import { useUserStore } from '../../stores/useUserStore';
import { axiosInstance } from '../../apis/lib/axios.ts';
import NestedComment from './NestedComment.tsx';

interface CommentProps {
	contentCode: string;
	contentId?: string;
}

const Comment: React.FC<CommentProps> = ({ contentCode, contentId }) => {
	const profile = useUserStore((state) => state.profile);
	const [comments, setComments] = useState([]);
	const [newComment, setNewComment] = useState('');
	const [editedComment, setEditedComment] = useState<string[]>([]);
	const [showReplies, setShowReplies] = useState<boolean[]>([]);
	const [showEditor, setShowEditor] = useState<boolean[]>([]);
	const [showModal, setShowModal] = useState(false);
	const [message, setMessage] = useState('');
	const memberId = profile?.id;

	useEffect(() => {
		fetchComments();
		console.log(profile?.id);
	}, []);

	//댓글 목록 불러오기
	const fetchComments = async () => {
		try {
			const response = await axiosInstance.get('/comments', {
				params: {
					memberId: memberId,
					contentCode: contentCode,
					contentId: contentId,
				},
			});

			console.log(response);

			setComments(response.data.data);
		} catch (error) {
			console.log(error);
		}
	};

	const addComment = async () => {
		try {
			await axiosInstance.post('/comments', {
				contentCode: contentCode,
				contentId: contentId,
				memberId: memberId,
				parentId: null,
				content: newComment,
			});

			fetchComments();
			setShowModal(true);
			setMessage('댓글이 등록되었습니다.');
			setNewComment('');
		} catch (error) {
			setShowModal(true);
			setMessage('댓글 등록에 실패했습니다.');
			console.log(error);
		}
	};

	const updateComment = async (commentId: number, index: number) => {
		try {
			await axiosInstance.patch(`/comments/${commentId}/${memberId}`, {
				content: editedComment[index],
			});

			setShowModal(true);
			setMessage('댓글이 수정되었습니다.');
			changeEditor(index, '');
			fetchComments();
		} catch (error) {
			setShowModal(true);
			setMessage('댓글 수정에 실패했습니다.');
			console.log(error);
		}
	};

	const deleteComment = async (commentId: number) => {
		try {
			await axiosInstance.delete(`/comments/${commentId}/${memberId}`);

			setShowModal(true);
			setMessage('댓글이 삭제되었습니다.');
			fetchComments();
		} catch (error) {
			setShowModal(true);
			setMessage('댓글 삭제에 실패했습니다.');
			console.log(error);
		}
	};

	const changeEditor = (index: number, content: string) => {
		setShowEditor((prev) => {
			const newState = [...prev];
			newState[index] = !newState[index];
			return newState;
		});
		setEditedComment((prev) => {
			const newEditedCommentContents = [...prev];
			newEditedCommentContents[index] = content;
			return newEditedCommentContents;
		});
	};

	const toggleReplies = (index: number) => {
		setShowReplies((prev) => {
			const newState = [...prev];
			newState[index] = !newState[index];
			return newState;
		});
	};

	const formatDate = (dateString: string): string => {
		const serverDateTime = new Date(dateString);

		const koreaDateTime = new Date(serverDateTime.getTime() + 9 * 60 * 60 * 1000);

		const formattedDateTime = `${koreaDateTime.getFullYear()}.${String(koreaDateTime.getMonth() + 1).padStart(
			2,
			'0',
		)}.${String(koreaDateTime.getDate()).padStart(2, '0')} ${String(koreaDateTime.getHours()).padStart(
			2,
			'0',
		)}:${String(koreaDateTime.getMinutes()).padStart(2, '0')}:${String(koreaDateTime.getSeconds()).padStart(2, '0')}`;

		return formattedDateTime;
	};

	return (
		<div>
			<div className='text-xl px-3 py-3'>댓글 {comments.length}개</div>
			<div className='py-3 px-3'>
				<div className='flex flex-col'>
					<div className='flex'>
						<img src={profile?.imgUrl} alt='Profile' className='flex-none w-12 h-12 rounded-full'></img>
						<div className='flex-initial w-full mx-3 my-3.5'>
							<p className='font-bold'>{profile?.nickname}</p>
						</div>
					</div>

					<textarea
						id='comment'
						name='comment'
						value={newComment}
						onChange={(e) => setNewComment(e.target.value)}
						className='flex-none h-20 w-full mr-2 p-2 my-2 resize-none rounded border border-gray-300'
						placeholder='댓글을 입력하세요'
					/>
					<button className='bg-main-color text-white p-2 w-20 rounded ml-auto' onClick={addComment}>
						작성
					</button>
				</div>
			</div>
			<div>
				{comments.map((comment, index) => (
					<div className='flex flex-col my-3' key={comment.id}>
						<div className='p-2'>
							<div className='flex items-center'>
								<img src={comment.member.imgUrl} alt='Profile' className='flex-none w-12 h-12 rounded-full' />
								<div className='flex-initial w-full mx-3'>
									<p className='font-bold'>{comment.member.nickname}</p>
									<p className='text-xs'>
										{comment.createdAt === comment.modifiedAt
											? formatDate(comment.createdAt)
											: formatDate(comment.modifiedAt) + ' (수정됨)'}
									</p>
								</div>
								{!comment.isDeleted && comment.member.id === memberId && (
									<div className='flex-initial w-32'>
										<button className='p-2 text-sm rounded' onClick={() => changeEditor(index, comment.content)}>
											수정
										</button>
										<button className='p-2 text-sm rounded' onClick={() => deleteComment(comment.id)}>
											삭제
										</button>
									</div>
								)}
							</div>
						</div>
						<div className='mx-3 my-3'>
							{comment.isDeleted ? (
								<p className='text-gray-500 italic'> ❗️삭제된 댓글입니다.</p>
							) : !showEditor[index] ? (
								<p>
									{' '}
									{comment.content.split('\n').map((line, index) => (
										<React.Fragment key={index}>
											{line}
											<br />
										</React.Fragment>
									))}{' '}
								</p>
							) : (
								<div className='my-3'>
									<div className='flex flex-col'>
										<textarea
											id='comment'
											name='comment'
											value={editedComment[index]}
											onChange={(e) =>
												setEditedComment((prev) => {
													const newEditedCommentContents = [...prev];
													newEditedCommentContents[index] = e.target.value;
													return newEditedCommentContents;
												})
											}
											className='flex-none h-20 w-full mr-2 p-2 my-2 resize-none rounded border border-gray-300'
											placeholder='댓글을 입력하세요'
										/>
										<div className='flex justify-end space-x-2'>
											<button
												className='bg-main-color text-white p-2 w-20 rounded'
												onClick={() => updateComment(comment.id, index)}
											>
												저장
											</button>
											<button
												className='bg-red-400 text-white p-2 w-20 rounded'
												onClick={() => changeEditor(index, '')}
											>
												취소
											</button>
										</div>
									</div>
								</div>
							)}
						</div>
						<div className=''>
							{(!comment.isDeleted || (comment.isDeleted && comment.children > 0)) && (
								<button className='text-sm mx-2 mb-2' onClick={() => toggleReplies(index)}>
									답글{comment.children > 0 ? ' ' + comment.children : ''}
								</button>
							)}
							{showReplies[index] && (
								<NestedComment contentCode={contentCode} contentId={contentId} parentId={comment.id} />
							)}
						</div>
						<hr />
					</div>
				))}
			</div>
			{showModal && (
				<div
					className='fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full flex items-center justify-center'
					id='my-modal'
				>
					<div className='relative mx-auto p-5 border w-96 shadow-lg rounded-md bg-white'>
						<div className='mt-3 text-center'>
							<h3 className='text-lg leading-6 font-medium text-gray-900'>{message}</h3>
							<div className='mt-2 px-7 py-3'></div>
							<div className='items-center px-4 py-3'>
								<button
									id='ok-btn'
									onClick={() => setShowModal(false)}
									className='px-4 py-2 bg-main-color text-white text-base font-medium rounded-md w-full shadow-sm hover:bg-blue-400 focus:outline-none focus:ring-2 focus:ring-green-300'
								>
									확인
								</button>
							</div>
						</div>
					</div>
				</div>
			)}
		</div>
	);
};

export default Comment;
