/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { useState, ChangeEvent, useEffect } from 'react';
import TechStackChange, { techStack } from '../../../components/techstack/TechStackChange';
import Position, { position } from '../../../components/position/PositionChange';
import { useParams } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import { axiosInstance } from "../../../apis/lib/axios";
import { useRequireAuth } from '../../../hooks/useRequireAuth';
import {useUserStore} from '../../../stores/useUserStore';


interface State {
  projectTitle: string;
  projectContent: string;
  projectImage: string | null;
  projectPeriodNum: number;
  projectPeriodUnit: string;
}

interface TechList{
  id : number;
  name: string;
}
interface PositionList{
  id : number;
  name: string;
  total: number;
  current:number;
}

const ProjectSettingPage: React.FC = () => {
  useRequireAuth();
  const [techLists, setTechLists] = useState<TechList[]>([]); // 상태 추가
  const [positionLists, setPositionLists] = useState<PositionList[]>([]); // 상태 추가
  const { projectId } = useParams<{ projectId: string }>();
  const { profile } = useUserStore();
  const memberId=profile?.id;
  const navigate = useNavigate();
  const [state, setState] = useState<State>({
    projectTitle: '',
    projectContent: '',
    projectImage: null,
    projectPeriodNum: 0,
    projectPeriodUnit: '주',
  });
  const [isModalVisible, setModalVisible] = useState(false);
  useEffect(() => {
    getData();
    return () => {
    };
  }, []);

  const getData = async () => {
    try {
      const response = await axiosInstance.get(`/projects/${projectId}/${memberId}`);
      const data = response.data.data;
      const techLists: TechList[] = data.techCodes.map((techCode: { id: number; detailName: string }) => ({
        id: techCode.id,
        name: techCode.detailName,
      }));
      setTechLists(techLists);
      const positionLists: PositionList[] = data.projectTotals.map((item: { jobCode: { id: any; detailDescription: any; }; total: any; current: any; }) => ({
        id: item.jobCode.id, // jobCode 객체 내의 id 속성
        name: item.jobCode.detailDescription, // jobCode 객체 내의 detailDescription 속성
        total: item.total, // 각 요소의 total 속성
        current: item.current // 각 요소의 current 속성
      }));
      setPositionLists(positionLists);
      console.log(positionLists)
      console.log(data);
      console.log(data.projectTotals)
      const projectPeriodNum = data.period;
      const projectPeriodUnit = '주'; // Default to weeks, adjust as needed
      
      // Update the form state with fetched data
      setState({
        projectTitle: data.title || '',
        projectContent: data.content || '',
        projectImage: data.image || null, // Adjust the key based on actual response
        projectPeriodNum: projectPeriodNum,
        projectPeriodUnit: projectPeriodUnit,
        // Include additional fields as necessary
      });
  
  
    } catch (error) {
      console.error("Failed to fetch project data:", error);
    }
  };

  const handleInputChange = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    if (name === 'projectPeriodNum') {
      setState((prev) => ({
        ...prev,
        projectPeriodNum: parseInt(value, 10) || 0,
      }));
    } else {
      setState((prev) => ({
        ...prev,
        [name]: value,
      }));
    }
  };

  const handleImageChange = (e: ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];

    if (file) {
      const imageUrl = URL.createObjectURL(file);
      setState((prev) => ({
        ...prev,
        projectImage: imageUrl,
      }));
    }
  };

  const handleUnitClick = (unit: string) => {
    setState((prev) => ({
      ...prev,
      projectPeriodUnit: unit,
    }));
  };

  const handleSave = async () => {
    const { projectTitle, projectContent } = state;

    if (projectTitle === '') {
      alert('제목을 입력해주세요.');
      return;
    } else if (projectContent === '') {
      alert('본문을 입력해주세요.');
      return;
    } else if (position.totalList.length === 0) {
      alert('한 개 이상의 포지션을 입력해주세요.');
      return;
    } else {
      let periodCal;
      if (state.projectPeriodUnit === '달') {
        periodCal = state.projectPeriodNum * 4;
      } else {
        periodCal = state.projectPeriodNum;
      }

      const projectData = {
        title: projectTitle,
        content: projectContent,
        period: periodCal,
        totechList: [...techStack.mystack], // 객체 복사
        totalList: position.totalList,
      };
      console.log(projectData);

      const projectDataString = JSON.stringify(projectData);
      const form = new FormData();
      form.set('patch', new Blob([projectDataString],{type: 'application/json'}));
      if (state.projectImage !== null) {
        form.set('file', state.projectImage);
      }
      console.log(projectDataString);
      console.log('FormData entries:');
      for (const pair of form.entries()) {
        console.log(pair[0] + ': ' + pair[1]);
      }
      console.log(memberId);
      try {
        const response = await axiosInstance.patch(`/projects/${projectId}/${memberId}`, form, {
          headers: {
            'Content-Type': undefined, 
          },
        });
        console.log("Response:", response);
        setModalVisible(true);
      } catch (error) {
        console.error("Post failed:", error);
      }
    }
  };
  const closeModal = () => {
    // Close the modal
    setModalVisible(false);
    navigate('../');
    window.scrollTo({ top: 0 });
  };

  return (
    <div className='bg-gray-100 w-11/12 h-max grid place-items-center'>
      <div className='bg-sub-color w-11/12 h-20 justify-center flex items-center font-bold text-4xl'>
        프로젝트 수정
      </div>
      <div className='bg-gray-300 w-9/12 h-auto p-16 m-5 border-black border-2 '>
        <div>
          <label htmlFor='projectTitle' className='font-bold text-lg my-3'>
            프로젝트 제목
          </label>
          <div>
            <input
              type='text'
              id='projectTitle'
              name='projectTitle'
              className='w-full h-10'
              value={state.projectTitle}
              onChange={handleInputChange}
            />
          </div>
        </div>
        <div className='my-3'>
          <label htmlFor='projectContent' className='font-bold text-lg '>
            프로젝트 내용
          </label>
          <div>
            <textarea
              id='projectContent'
              name='projectContent'
              className='w-full h-40'
              value={state.projectContent}
              onChange={handleInputChange}
            />
          </div>

          <TechStackChange initialTags={techLists} />

          <div className='my-3'>
            <label htmlFor='projectImage' className='font-bold text-lg'>
              프로젝트 이미지 업로드
            </label>
            <div>
              <input
                type='file'
                id='projectImage'
                name='projectImage'
                accept='image/*'
                onChange={handleImageChange}
                className='w-max mt-2'
              />
              {state.projectImage && <img src={state.projectImage} alt='Uploaded' className='mt-2 max-h-40' />}
            </div>
          </div>

          <div className='my-3'>
            <label htmlFor='projectPeriod' className='font-bold text-lg my-3'>
              프로젝트 기간
            </label>
            <div>
              <span className='p-2'>약</span>
              <input
                type='text'
                id='projectPeriodNum'
                name='projectPeriodNum'
                className='w-20 h-10 p-1 mr-2'
                value={state.projectPeriodNum}
                onChange={handleInputChange}
              />
              <button
                className={`p-2 ${state.projectPeriodUnit === '주' ? 'bg-main-color text-white' : ''
                  } border-main-color border-2`}
                onClick={() => handleUnitClick('주')}
              >
                주
              </button>
              <button
                className={`p-2 ${state.projectPeriodUnit === '달' ? 'bg-main-color text-white' : ''
                  } border-main-color border-2`}
                onClick={() => handleUnitClick('달')}
              >
                달
              </button>
              <div className='ml-8 bg-white p-1 w-max mt-3'>달을 선택하시면 자동으로 주 단위로 계산됩니다.</div>
            </div>
          </div>

          <Position initialTags={positionLists}/>
        </div>
        <button onClick={handleSave} className='mt-5 bg-main-color text-white p-3'>
          저장
        </button>

        {isModalVisible && (
          <div className='fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center'>
            <div className='bg-white p-8'>
              <p>저장되었습니다!</p>
              <button onClick={closeModal}>닫기</button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default ProjectSettingPage;

