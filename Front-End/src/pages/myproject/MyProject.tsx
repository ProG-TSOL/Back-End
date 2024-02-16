import { useEffect, useState } from 'react';
import ParticipationProjects from '../../components/mypage/ParticipationProjects.tsx';
import '../../styles/my-project-page.scss';
import { codeDetailItem, getCodeDetailList } from '../../utils/getCodeDetailList.ts';

export interface MenuProps {
	statusCode: number;
	description: string;
	total: number;
	isActive?: boolean;
	api?: string;
	index: number;
}

const useCodeDetailList = () => {
	// const[codeDetails, setCodeDetails] = useState<codeDetailItem[]>([]);
	const [codeDetails, setCodeDetails] = useState<codeDetailItem[]>([]);
	useEffect(() => {
		const fetchCodeDetails = async () => {
			const result = await getCodeDetailList('ProjectStatus');
			setCodeDetails(result);
		};

		fetchCodeDetails();
	}, []);

	// useEffect(() => {
	// },[codeDetails]);

	return codeDetails;
};

const MyProject = () => {
	const [activeItem, setActiveItem] = useState<MenuProps[]>([]);
	const [selectItem, setSelectItem] = useState<MenuProps>();
	const [memberId, setMemberId] = useState<number>(0);
	const codeDetails = useCodeDetailList();
	const Menu = () => {
		const handleClick = (clickedItem: MenuProps) => {
			setActiveItem(
				activeItem.map((item) =>
					item.statusCode === clickedItem.statusCode ? { ...item, isActive: true } : { ...item, isActive: false },
				),
			);
			setSelectItem(clickedItem);
		};

		return (
			<ul id={'condition-menu'}>
				{activeItem.map((item, index) => (
					<li key={index} onClick={() => handleClick(item)} className={item.isActive ? 'active' : ''}>
						<button>
							<span>{item.description}</span>
							{/*시간되면 카운트 api 만들어서 넣기*/}
							{/*<span>({item.total})</span>*/}
						</button>
					</li>
				))}
			</ul>
		);
	};

	// let memberId = 0;

	// 로컬 스토리지에서 userProfile을 가져옴

	const userProfileKey = 'userProfile';
	const userProfileString = localStorage.getItem(userProfileKey);
	const getMemberId = () => {
		if (userProfileString) {
			console.log(`여기 타나 타나탄타나`);
			const userProfile = JSON.parse(userProfileString);
			// memberId = userProfile.id;
			setMemberId(userProfile.id);
		}
	};
	console.log(`회원 IO : ${memberId}`);

	const makeCode = () => {
		const menuPropsItems: MenuProps[] = codeDetails.map((item) => {
			let makeDescription;
			let makeIndex;
			switch (item.detailName) {
				case 'Recruiting':
					makeDescription = '모집중';
					makeIndex = 1;
					break;
				case 'Proceeding':
					makeDescription = '진행중';
					makeIndex = 2;
					break;
				case 'Complete':
					makeDescription = '완료';
					makeIndex = 3;
					break;
				default:
					makeDescription = '';
					makeIndex = 99;
					break;
			}

			return {
				statusCode: item.id,
				// description: item.detailName,
				description: makeDescription,
				total: 0, // TODO : 시간되면 카운트 api 만들어서 넣기
				isActive: false,
				api: `/projects/myproject/${memberId}/${item.id}`,
				index: makeIndex,
			};
		});

		const additionalItem: MenuProps[] = [
			{
				statusCode: 0, // 전체
				description: '전체',
				total: 0,
				isActive: true,
				api: `/projects/myproject/${memberId}`,
				index: 0,
			},
			{
				statusCode: -1, // 신청중
				description: '신청중',
				total: 0,
				isActive: false,
				api: `/projects/myproject-signed/${memberId}`,
				index: 4,
			},
		];
		additionalItem.forEach((item) => {
			menuPropsItems.push(item);
		});

		setActiveItem([...menuPropsItems].sort((a, b) => a.index - b.index));
		console.log(`additionalItem[0] : ${JSON.stringify(additionalItem[0])}`);
		setSelectItem(additionalItem[0]);
	};

	useEffect(() => {
		getMemberId();
		makeCode();
	}, [codeDetails, userProfileString]);

	return (
		<div className={'my-project-container'}>
			<header className='grid text-center'>
				<Menu />
			</header>
			<div>
				{/*<main >*/}
				<ParticipationProjects selectItem={selectItem} />
			</div>
		</div>
	);
};

export default MyProject;
