import { useParams } from 'react-router-dom';
import { useActionQuery } from '../../apis/useActionQuery';

const ActionBoard = () => {
	const { projectId } = useParams();
	const { data: action, isLoading, error } = useActionQuery(Number(projectId));

	if (isLoading) return <div>Loading...</div>;
	if (error) return <div>Error{error.message}</div>;

	console.log(action);
	// console.log(action);
	//상위 3개의 action만 추출
	const displayActions = action?.data?.slice(0, 3) || [];

	return (
		<div className='flex flex-col rounded-xl bg-sub-color h-36 w-[85%] px-4 py-2'>
			<div className='flex text-main-color font-semibold text-2xl'>Action</div>
			<div className='flex flex-col'>
				{displayActions.map((act, index) => (
					<div key={index} className='flex flex-col my-1 font-semibold text-lg justify-center'>
						{`${index + 1}. ${act.content}`}
					</div>
				))}
			</div>
		</div>
	);
};

export default ActionBoard;
