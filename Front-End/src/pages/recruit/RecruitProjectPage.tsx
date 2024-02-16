/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import Comment from '../../components/comment/Comment';
import { axiosInstance } from '../../apis/lib/axios';
import { useRequireAuth } from '../../hooks/useRequireAuth';
import { useUserStore } from '../../stores/useUserStore';
import ImageWithFallback from "../../utils/DefaultImgage.tsx";

import '../../styles/page/project-main/project-detail.scss'


const RecruitProjectPage = () => {
  useRequireAuth();
  const { profile } = useUserStore();
  const memberId = profile?.id;
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [img, setImg] = useState('');
  const [mystack, setMyStack] = useState<string[]>([]);
  const [period, setPeriod] = useState<number>(0);
  const [number, setNumber] = useState<number>(0);
  const [view, setView] = useState('');
  const [like, setLike] = useState('');
  const [isLike, setIsLike] = useState<number>(0);
  const [positions, setPositions] = useState({
    posName: [],
    posCode: [],
    posNowNumber: [],
    posNumber: [],
  });
  const [MyProject, setMyProject] = useState(true);
  const [getApply, setGetApply] = useState('');
  const { projectId } = useParams();

  const apply = async (posName: never, posCode: never) => {
    console.log(`Applying for position: ${posName} with Code: ${posCode} in ${projectId} by ${memberId}`);
    try {
      const response = await axiosInstance.post(`/projects/${projectId}/application/${memberId}?jobCode=${posCode}`);
      console.log(response);
      getData();
    }
    catch (error) {
      console.error("Apply failed:", error);
    }
  }
  const cancelApply = async () => {
    try {
      const response = await axiosInstance.delete(`/projects/${projectId}/refuseMember/${memberId}?refuseMemberId=${memberId}`);
      console.log(response);
      getData();
    }
    catch (error) {
      console.error("Apply failed:", error);
    }
  }

  const addLike = async () => {
    try {
      const response = await axiosInstance.post(`/projects/${projectId}/like/${memberId}/add`);
      console.log(response);
      getData();
    }
    catch (error) {
      console.error("Apply failed:", error);
    }
  }
  const deleteLike = async () => {
    try {
      const response = await axiosInstance.delete(`/projects/${projectId}/like/${memberId}/delete`);
      console.log(response);
      getData();
    }
    catch (error) {
      console.error("Apply failed:", error);
    }
  }
  const toggleLike = async () => {
    try {
      if (isLike === 0) {
        await addLike();
      } else if (isLike === 1) {
        await deleteLike();
      }
    } catch (error) {
      console.error("Toggle like failed:", error);
    }
  };

  const getData = async () => {
    try {
      const response = await axiosInstance.get(`/projects/${projectId}/${memberId}`, {
      });
      const data = response.data.data;

      console.log(data)
      setTitle(data.title);
      setDescription(data.content);
      setImg(data.projectImgUrl);
      const mystack = data.techCodes.map((tech: { detailName: any; }) => tech.detailName);
      setMyStack(mystack);
      setPeriod(data.period);
      setLike(data.likeCnt);
      setView(data.viewCnt);
      setIsLike(data.isLike);
      let memberNum = 0; // memberNum을 먼저 초기화

      for (let i = 0; i < data.projectTotals.length; i++) {
        memberNum += data.projectTotals[i].total;
      }
      setNumber(memberNum);
      if (data.applicationStatus) {
        setGetApply(data.applicationStatus.id)
      }
      else {
        setGetApply('');
      }
      console.log(getApply)

      const currentArray = data.projectTotals.map((item: { current: any; }) => item.current);
      const totalArray = data.projectTotals.map((item: { total: any; }) => item.total);
      const jobIdArray = data.projectTotals.map((item: { jobCode: { id: any; }; }) => item.jobCode.id);
      const jobDescriptionArray = data.projectTotals.map((item: { jobCode: { detailDescription: any; }; }) => item.jobCode.detailDescription);
      setPositions({
        posName: jobDescriptionArray,
        posCode: jobIdArray,
        posNowNumber: currentArray,
        posNumber: totalArray,
      })


      setMyProject(data.isMember);

    } catch (error) {
      console.error("Loading failed:", error);
    }
  };
  useEffect(() => {
    getData();
    console.log(projectId)
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);


  return (
    <div>
      <div className='flex justify-center project-main'>
        <div className='w-10/12 h-full flex p-10 flex-col '>

          <div className='h-auto shadow-xl rounded-3xl p-5'>

            <div className='align-middle text-center text-4xl font-bold mt-5'> {title}</div>
            <hr className='m-3 border-2'/>
            <div className='flex justify-center m-5'>
              <div className='project-thumnail'>
                <ImageWithFallback src={img || ''} style={'w-full h-auto'} alt={''} type={'project'}/>
                {/*<img src={img|| logo} className='w-full h-auto' alt='Project' />*/}
              </div>
            </div>

            <div className='flex flex-col'>
              <div>
                <h1>프로젝트 정보</h1>
                <div className={'flex gap-x-4 mt-4'}>
                  <div className='project-detail-btn p-2 items-center gap-x-2'>
                    <p>예상기간</p>
                    <p className=''>
                      {period === 0 ? '미정' : `${period}주`}
                    </p>
                  </div>
                  <div className='project-detail-btn p-2 items-center gap-x-4'>
                    <p>총 인원</p>
                    <p className=''>{number}명</p>
                  </div>
                </div>
              </div>

              {/*구성인원*/}
              <div>
                <h1>모집분야</h1>
                <div className='flex flex-col'>
                  {positions.posName.map((name, index) => (
                      <div key={index} className='h-8 m-1'>
                        <div className={'tech-list'}>
                          <p>{name}</p>
                          <p>{positions.posNowNumber[index]}/{positions.posNumber[index]}</p>
                          {positions.posNowNumber[index] < positions.posNumber[index] ? (
                              <>
                                {getApply === '' ? (
                                    <button className='bg-main-color text-white p-1 rounded '
                                            onClick={() => apply(name, positions.posCode[index])}>
                                      지원하기
                                    </button>
                                ) : getApply === positions.posCode[index] ? (
                                    <button className='bg-red-300 text-white p-1 rounded'
                                            onClick={() => cancelApply()}>
                                      취소하기
                                    </button>
                                ) : (
                                    <button className='bg-white text-white p-1 rounded' disabled>
                                      공백
                                    </button>
                                )}
                              </>
                          ) : (
                              <button className='bg-orange-500 text-black p-1 rounded' disabled>
                                모집완료
                              </button>
                          )}
                        </div>
                      </div>
                  ))}
                </div>
              </div>
            </div>

            <div className=''>
              <h1>기술스택</h1>
              <div className='mt-4'>
                <div className='flex flex-wrap'>
                  {mystack.map((tech, index) => (
                      <div key={index} className='bg-sub-color p-1 m-1 rounded-lg'>
                        {tech}
                      </div>
                  ))}
                </div>
              </div>
            </div>

            <div className=''>
              <h1>본문</h1>
              <div className=' mt-4'>
                <div>
                  {description.split('\n').map((line, index) => (
                      <React.Fragment key={index}>
                        {line}
                        <br/>
                      </React.Fragment>
                  ))}
                </div>
              </div>
            </div>

            <div className='mt-4'>
              <div className=' h-auto flex-col '>
                {MyProject ? (
                    <div className='flex flex-col'>
                      <h1>프로젝트 상세</h1>
                      <div className='flex items-center justify-center mt-4'>
                        <Link
                            to={`/project/${projectId}`}
                            className=' project-detail-btn text-white text-center'>
                          프로젝트로 이동하기
                        </Link>
                      </div>
                    </div>
                ) : (
                    <>
                    </>
                )}
              </div>
            </div>

            {/*<hr className='m-3 border-main-color border-1'/>*/}
            <div className='content-foot'>
              <div className='project-detail-btn'>
                <Link to='../recruit' onClick={() => window.scrollTo({top: 0})}>
                  목록
                </Link>
              </div>
              <div className={'flex gap-x-6 px-5 items-center'}>
                <p>👀조회수{view}</p>
                <p className={`cursor-pointer ${isLike === 1 ? 'liked' : 'dint-liked'}`}
                   onClick={toggleLike}>
                  {/*❤️️{like}*/}
                  ️️{like}
                </p>
              </div>
            </div>

            <div className='m-5'>
              <Comment contentCode='프로젝트' contentId={projectId}/>
            </div>

          </div>

        </div>
      </div>
    </div>
  );
};

export default RecruitProjectPage;
