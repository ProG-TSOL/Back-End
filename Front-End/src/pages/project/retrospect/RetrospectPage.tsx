import { useState } from 'react';
import KeepBoard from '../../../components/retrospectMemo/KeepBoard';
import ProblemBoard from '../../../components/retrospectMemo/ProblemBoard';
import TryBoard from '../../../components/retrospectMemo/TryBoard';
import KPTMemo from '../../../components/retrospectMemo/WriteKptMemo';

export const RetrospectPage = () => {
	const [modalOpen, setModalOpen] = useState(false);

	return (
		<div className='flex justify-between'>
			<div className='mt-10'>
				<KeepBoard />
				<ProblemBoard />
				<TryBoard />
				<div className='flex justify-center my-4'>
					<button
						className='bg-blue-500 hover:bg-blue-700 text-white font-bold rounded-full shadow-lg flex items-center justify-center w-12 h-12'
						onClick={() => setModalOpen(true)}
					>
						<span className='text-2xl'>+</span>
					</button>
				</div>
				<KPTMemo modalOpen={modalOpen} setModalOpen={setModalOpen} />
			</div>
		</div>
	);
};

export default RetrospectPage;
