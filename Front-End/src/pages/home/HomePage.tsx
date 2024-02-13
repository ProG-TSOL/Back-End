import logo from '../../assets/logo.png';
import { useNavigate } from 'react-router-dom';

const HomePage = () => {
	const navigate = useNavigate();

	const moveRecruit = () => {
		navigate('../recruit/');
	};

	const moveLogin = () => {
		navigate('../login/');
	};

	return (
		<div>
			<div className='flex flex-col items-center justify-center'>
				<img src={logo} alt='React logo' className='w-4/12 h-auto' />
				<div className='font-bold text-4xl'>Programmer가 되고 싶은 ProG들</div>
				<div className='grid items-center justify-center text-center'>
					<span className='bg-main-color p-5 m-3 text-xl text-white cursor-pointer' onClick={moveRecruit}>
						프로젝트 찾아보기
					</span>
					<span className='bg-yellow-300 p-5 mx-3 text-xl cursor-pointer' onClick={moveLogin}>
						로그인으로 시작하기
					</span>
				</div>
			</div>
		</div>
	);
};

export default HomePage;

// import { Link, useNavigate } from 'react-router-dom';
// import '../../styles/home.scss';
// import { useEffect, useState } from 'react';
// import { IoIosSearch } from 'react-icons/io';
// import { axiosInstance } from '../../apis/lib/axios';
// import Slider from 'react-slick';
// import 'slick-carousel/slick/slick.css';
// import 'slick-carousel/slick/slick-theme.css';

// const HomePage = () => {
// 	const navigate = useNavigate();

// 	const formatDate = (dateString) => {
// 		const options = {
// 			year: 'numeric',
// 			month: '2-digit',
// 			day: '2-digit',
// 			hour: '2-digit',
// 			minute: '2-digit',
// 			hour12: false,
// 		};
// 		return new Intl.DateTimeFormat('ko-KR', options).format(new Date(dateString));
// 	};

// 	const [projectList, setProjectList] = useState<
// 		{
// 			id: number;
// 			statusCode: {
// 				id: number;
// 				detailName: string;
// 				detailDescription: string;
// 				imgUrl: string;
// 				isUse: boolean;
// 			};
// 			title: string;
// 			viewCnt: number;
// 			createdAt: string;
// 		}[]
// 	>([]); // 프로젝트 리스트

// 	// ■■■■■■■■■■■■ 프로젝트 주간 인기 모집글 조회 ■■■■■■■■■■■■
// 	const getProjectList = async () => {
// 		try {
// 			const response = await axiosInstance.get(`/projects/hotProject`, {});
// 			if (response.data.status === 'OK') {
// 				const data = response.data.data;

// 				const formattedData = data.map((item) => ({
// 					...item,
// 					createdAt: formatDate(item.createdAt),
// 				}));

// 				setProjectList(formattedData);
// 			}
// 			ProjectCarousel();
// 		} catch (error) {
// 			console.error('Loading failed:', error);
// 		}
// 	};

// 	// ■■■■■■■■■■■■■■■■■■■ 캐러셀 start ■■■■■■■■■■■■■■■■■■■

// 	// 캐러셀 버튼 (prev, next) custom
// 	const PrevArrow = (props) => {
// 		// carousel-left-btn
// 		// carousel-right-btn
// 		const { className, style, onClick } = props;
// 		return (
// 			<div className={'carousel-btn carousel-prev'} onClick={onClick}>
// 				<span>&#10094;</span>
// 			</div>
// 		);
// 	};

// 	const NextArrow = (props) => {
// 		const { className, style, onClick } = props;
// 		return (
// 			<div className={'carousel-btn carousel-next'} onClick={onClick}>
// 				<span>&#10095;</span>
// 			</div>
// 		);
// 	};

// 	// 캐러셀 라이브러리
// 	const Carousel = () => {
// 		// 캐러셀 설정 값
// 		const settings = {
// 			dots: true, // 캐러셀 개수 표시 점
// 			infinite: true, // 무한 캐러셀
// 			speed: 500, // 슬라이드 전환 속도
// 			slidesToShow: 4, // 한 번에 보여줄 슬라이드 개수
// 			slidesToScroll: 4, // 한 번에 넘길 슬라이드 개수
// 			nextArrow: <NextArrow />,
// 			prevArrow: <PrevArrow />,
// 		};

// 		return (
// 			<div className='slider-container'>
// 				<Slider {...settings}>
// 					{/*데이터 수 만큼 반복*/}
// 					{projectList.map((project, index) => (
// 						// <div key={index} className="project-card" onClick={moveRecruit}>
// 						<div key={index} className={'project-box'}>
// 							<div>
// 								<Link to={`recruit/project/${project.id}`} className={'project-card'}>
// 									<div className={'tag-box'}>
// 										<div>
// 											{/*태그 ex) 프로젝트*/}
// 											<p className={'project-tag'}>🗂 프로젝트</p>
// 										</div>
// 										<div
// 											className={
// 												project.statusCode.detailName === 'Proceeding' ? 'recruitment-completed' : 'recruiting'
// 											}
// 										>
// 											{project.statusCode.detailName === 'Proceeding' ? <p>✔ 모집완료</p> : <p>🚨 모집중</p>}
// 										</div>
// 									</div>
// 									<div className={'dead-line'}>
// 										<p>
// 											생성일 | <span>{project.createdAt}</span>
// 										</p>
// 									</div>
// 									<h1>{project.title}</h1>
// 									<div>
// 										<p> 👀 조회수 {project.viewCnt} 회 </p>
// 									</div>
// 								</Link>
// 							</div>
// 						</div>
// 					))}
// 				</Slider>
// 			</div>
// 		);
// 	};

// 	// 프로젝트 캐로셀 컴포넌트
// 	const ProjectCarousel = () => {
// 		return (
// 			<div className='project-container'>
// 				<div>
// 					<h3 className={'project-board-h3'}>🔥 이번 주 인기 모집글</h3>
// 					<Carousel />
// 				</div>
// 			</div>
// 		);
// 	};

// 	// ■■■■■■■■■■■■■■■■■■■ 캐러셀 end ■■■■■■■■■■■■■■■■■■■

// 	// 1. 검색바에 타이핑하면 검색 결과가 나오도록
// 	// 2. 인기 모집글 조회
// 	//   - 인기 모집글 필요 데이터
// 	const Search = () => {
// 		const [textIndex, setTextIndex] = useState(0);
// 		const [charIndex, setCharIndex] = useState(0);
// 		const [isDeleting, setIsDeleting] = useState(false);
// 		const [isInputActive, setIsInputActive] = useState(false);
// 		const [inputValue, setInputValue] = useState('');
// 		const texts = ['단기 프로젝트', 'Java', '빅데이터 분석'];

// 		// ■■■■■■■■■■■■ 이벤트 ■■■■■■■■■■■■
// 		const handleOnKeyPress = (e) => {
// 			// 프로젝트 모집 페이지 호출
// 			if (e.key == 'Enter') {
// 				const search = e.target.value;
// 				console.log(`보낸 값 : ${search}`);
// 				navigate('../recruit/', { keyword: search });
// 			}
// 		};
// 		const handleOnFocus = () => {
// 			setIsInputActive(true);
// 		};

// 		const handleChange = (e) => {
// 			setInputValue(e.target.value);
// 		};

// 		useEffect(() => {
// 			if (isInputActive) {
// 				return;
// 			}

// 			const interval = setInterval(() => {
// 				setCharIndex((oldCharIndex) => {
// 					if (!isDeleting && oldCharIndex < texts[textIndex].length - 1) {
// 						// 작성 모드이고 현재 텍스트의 모든 글자가 출력되지 않았다면, 글자 인덱스를 증가
// 						return oldCharIndex + 1;
// 					} else if (isDeleting && oldCharIndex > -1) {
// 						// 삭제 모드이고 현재 텍스트의 모든 글자가 삭제되지 않았다면, 글자 인덱스를 감소
// 						return oldCharIndex - 1;
// 					} else if (!isDeleting && oldCharIndex === texts[textIndex].length - 1) {
// 						// 현재 텍스트의 모든 글자가 출력되었다면, 작성/삭제 모드를 전환
// 						setTimeout(() => setIsDeleting(!isDeleting), 300); // 0.3초 지연 후 전환
// 						return oldCharIndex;
// 					} else {
// 						// 현재 텍스트의 모든 글자가 삭제되었다면, 작성/삭제 모드를 전환하고 텍스트 인덱스를 증가
// 						setIsDeleting(!isDeleting);
// 						setTextIndex((oldTextIndex) => (oldTextIndex + 1) % texts.length);
// 						return 0;
// 					}
// 				});

// 				setInputValue(texts[textIndex].substring(0, Math.max(0, charIndex + 1)));
// 			}, 100); // 0.5초 간격으로 글자 변경

// 			return () => {
// 				clearInterval(interval); // 컴포넌트가 언마운트되면 setInterval을 정리
// 			};
// 		}, [textIndex, charIndex, texts, isDeleting, isInputActive, inputValue]);

// 		const inputSearch = document.getElementById('search');

// 		//  ■■■■■ input 이벤트리스너 추가 ■■■■■
// 		inputSearch?.addEventListener('keypress', handleOnKeyPress);

// 		return (
// 			<input
// 				id={'search'}
// 				className='search-input'
// 				type='text'
// 				value={inputValue}
// 				onFocus={handleOnFocus}
// 				onChange={handleChange}
// 			/>
// 		);
// 	};

// 	//  onMount
// 	useEffect(() => {
// 		getProjectList();
// 	}, []);

// 	// ■■■■■■■■■■■■ UI ■■■■■■■■■■■■
// 	return (
// 		<div className='flex flex-col justify-center main-container'>
// 			<div className='flex flex-col items-center mt-16'>
// 				<h1 className={'main-h1'}>어떤 프로젝트를 찾으시나요?</h1>
// 				{/*검색바*/}
// 				<div className={'search-bar-box flex justify-center content-center'}>
// 					<div>
// 						<IoIosSearch />
// 						<Search />
// 					</div>
// 				</div>
// 			</div>
// 			<ProjectCarousel />
// 		</div>
// 	);
// };

// export default HomePage;
