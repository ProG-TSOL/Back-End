import { useEffect, useState } from 'react';
import KeepBoard from '../../../components/retrospectMemo/KeepBoard';
import ProblemBoard from '../../../components/retrospectMemo/ProblemBoard';
import TryBoard from '../../../components/retrospectMemo/TryBoard';
import KPTMemo from '../../../components/retrospectMemo/WriteKptMemo';
import { FaAnglesLeft, FaPlus } from 'react-icons/fa6';
import { useNavigate, useParams } from 'react-router-dom';
import { axiosInstance } from '../../../apis/lib/axios';
import { useRefreshAuth } from '../../../hooks/useRefreshAuth';

export const RetrospectPage = () => {
	const { isLoading, isAuthenticated } = useRefreshAuth();

	const [kptData, setKptData] = useState({ Keep: [], Problem: [], Try: [] });
	const [modalOpen, setModalOpen] = useState(false);
	const navigate = useNavigate();
	const params = useParams<{ projectId: string }>();

	const projectId = params.projectId;

	useEffect(() => {
		const getKPT = async () => {
			if (!isAuthenticated) return; // 인증이 완료되지 않았다면 함수를 종료한다.

			try {
				const response = await axiosInstance.get(`/retrospects/${projectId}`);
				setKptData(response.data.data);
			} catch (error) {
				console.error(error);
			}
		};

		if (!isLoading) {
			getKPT();
		}
	}, [isLoading, isAuthenticated, projectId]); // 의존성 배열에 isLoading과 isAuthenticated 추가

	if (isLoading) {
		return <div>Loading...</div>; // 로딩 중인 경우 로딩 인디케이터를 표시
	}

	return (
		<div>
			<p>현재 N주차</p>
			<div className='flex justify-center items-center'>
				<div className='mt-10'>
					<KeepBoard memos={kptData.Keep} />
					<ProblemBoard memos={kptData.Problem} />
					<TryBoard memos={kptData.Try} />
					<div className='flex justify-center my-4'>
						<FaAnglesLeft
							onClick={() => navigate(`/project/${projectId}/prevretrospect`)} // 후에 url project params 받아서 다시 수정
							className='cursor-pointer bg-sub-color border-black border-1 text-main-color font-bold rounded-full shadow-lg flex items-center justify-center w-12 h-12 m-2'
						/>
						<FaPlus
							onClick={() => setModalOpen(true)}
							className='cursor-pointer m-2 bg-main-color text-white font-bold rounded-full shadow-lg flex items-center justify-center w-12 h-12'
						/>
					</div>
					<KPTMemo modalOpen={modalOpen} setModalOpen={setModalOpen} />
				</div>
			</div>
		</div>
	);
};

export default RetrospectPage;
