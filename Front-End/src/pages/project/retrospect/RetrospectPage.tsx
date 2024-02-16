import { useEffect, useState } from 'react';
import KeepBoard from '../../../components/retrospectMemo/KeepBoard';
import ProblemBoard from '../../../components/retrospectMemo/ProblemBoard';
import TryBoard from '../../../components/retrospectMemo/TryBoard';
import KPTMemo from '../../../components/retrospectMemo/WriteKptMemo';
import { useNavigate, useParams } from 'react-router-dom';
import { axiosInstance } from '../../../apis/lib/axios';
import { useRefreshAuth } from '../../../hooks/useRefreshAuth';
import '../../../styles/page/retrospect-page.scss';
import { useRequireAuth } from '../../../hooks/useRequireAuth';

export const RetrospectPage = () => {
	const { isLoading, isAuthenticated } = useRefreshAuth();
	const [kptData, setKptData] = useState({ Keep: [], Problem: [], Try: [] });
	const [modalOpen, setModalOpen] = useState(false);
	const navigate = useNavigate();
	const params = useParams<{ projectId: string }>();
	const projectId = params.projectId;

	useRequireAuth();

	const onHandleClick = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>, type: string) => {
		e.preventDefault();

		if (type === 'list') {
			navigate(`/project/${projectId}/prevretrospect`);
		} else if (type === 'add') {
			setModalOpen(true);
		}
	};

	const getKPT = async () => {
		if (!isAuthenticated) return; // 인증이 완료되지 않았다면 함수를 종료한다.

		try {
			const response = await axiosInstance.get(`/retrospects/${projectId}`);
			setKptData(response.data.data);
		} catch (error) {
			console.error(error);
		}
	};
	useEffect(() => {
		if (!isLoading) {
			getKPT();
		}
	}, [isLoading, isAuthenticated, projectId]); // 의존성 배열에 isLoading과 isAuthenticated 추가

	if (isLoading) {
		return <div>Loading...</div>; // 로딩 중인 경우 로딩 인디케이터를 표시
	}

	return (
		<div className={'retrospect-container'}>
			{/*header*/}
			<header className='flex justify-center justify-between px-7 items-center'>
				<div className={'flex justify-center items-center oblique-line'}>
					<p className={'text-4xl font-bold '}>🚀 이주의 회고</p>
				</div>
				<div className={'flex justify-center btn-box'}>
					<button onClick={(e) => onHandleClick(e, 'list')}>목록</button>
					<button onClick={(e) => onHandleClick(e, 'add')}>추가</button>
					{/*<FaAnglesLeft*/}
					{/*    onClick={() => navigate(`/project/${projectId}/prevretrospect`)} // 후에 url project params 받아서 다시 수정*/}
					{/*    className='cursor-pointer bg-sub-color border-black border-1 text-white font-bold rounded-full shadow-lg flex items-center justify-center w-12 h-12 m-2'*/}
					{/*/>*/}
					{/*<FaPlus*/}
					{/*    onClick={() => setModalOpen(true)}*/}
					{/*    className='cursor-pointer m-2 bg-sub-color text-white font-bold rounded-full shadow-lg flex items-center justify-center w-12 h-12'*/}
					{/*/>*/}
				</div>
			</header>
			{/*content*/}
			<section className='px-7'>
				<div className='flex grow kp-box'>
					<KeepBoard memos={kptData.Keep} onKPTUpdate={getKPT} />
					<ProblemBoard memos={kptData.Problem} onKPTUpdate={getKPT} />
				</div>
				<div className={'mt-8 t-box'}>
					<TryBoard memos={kptData.Try} onKPTUpdate={getKPT} />
					<KPTMemo modalOpen={modalOpen} setModalOpen={setModalOpen} onKPTUpdate={getKPT} />
				</div>
			</section>
		</div>
	);
};

export default RetrospectPage;
