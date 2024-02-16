// RecruitCard.tsx
import { Link } from 'react-router-dom';
import useRecruitStore from '../../stores/useRecruitStore';
import logo from '../../assets/logo.png';
import ImageWithFallback from '../../utils/DefaultImgage.tsx';

import '../../styles/component/recruit-card.scss';

const RecruitCard = () => {
	const { searchResults } = useRecruitStore();

	return (
		<div className='recurit-card-box grid grid-cols-6'>
			{searchResults.map((result) => (
				<div
					key={result.id}
					className='relative shadow-xl w-64 grid place-items-center m-3 border-2 border-[var(--main-color30)]
                     rounded-xl overflow-hidden transition-all duration-300 ease-in-out hover:border-black hover:rounded-lg
                     hover:-translate-y-2 hover:shadow-[0_0.8rem_0_0_var(--gray100)]'
				>
					<Link to={`/recruit/project/${result.id}`} onClick={() => window.scrollTo({ top: 0 })}>
						<div className='flex justify-between w-auto mt-2'>
							<span
								className={` text-black font-bold px-2 py-1 rounded-lg ${
									result.statusCode.detailDescription === '모집중'
										? 'bg-yellow-200'
										: result.statusCode.detailDescription === '진행중'
										? 'bg-green-200'
										: result.statusCode.detailDescription === '완료'
										? 'bg-red-200'
										: ''
								}`}
							>
								{result.statusCode.detailDescription}
							</span>
							<div className={`flex font-bold py-1`}>
								<span>❤️{result.likeCnt}</span>
							</div>
						</div>
						<div className={'project-img-box'}>
							<ImageWithFallback
								src={result.projectImgUrl || logo}
								alt='{Project Thumbnail}'
								style={'mt-2'}
								type={'project'}
							/>
						</div>
						<div className='font-bold w-56 mt-2 truncate'>{result.title}</div>
						<div className='my-1 truncate w-56'>
							{result.techCodes.slice(0, 6).map((tag) => (
								<span key={tag.id} className='mr-2 p-1 bg-sub-color text-black rounded-lg mb-1 h-8'>
									{tag.detailName}
								</span>
							))}
						</div>
						<div className='text-gray-600 font-semibold m-2 text-center'>
							모집인원 {result.current}/{result.total}
						</div>
						<div className='flex font-bold justify-end'>
							<span>👀 조회수 {result.viewCnt}</span>
						</div>
					</Link>
				</div>
			))}
		</div>
	);
};

export default RecruitCard;

// const truncate = (str: string, n: number) => {
//     return str?.length > n ? str.substr(0, n - 1) + "..." : str;
// };
