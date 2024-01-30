import React, { useState, ChangeEvent } from 'react';
import TechStack, { techStack } from '../../components/techstack/TechStack';
import Position, { position } from '../../components/position/Position';

interface State {
  projectTitle: string;
  projectContent: string;
  projectImage: string | null;
  projectPeriodNum: number;
  projectPeriodUnit: string;
}

const RecruitWritePage: React.FC = () => {
  const [state, setState] = useState<State>({
    projectTitle: '',
    projectContent: '',
    projectImage: null,
    projectPeriodNum: 0,
    projectPeriodUnit: '',
  });

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
      console.log("포지션 토탈리스트");
      console.log(position.totalList[0]);
      alert('한 개 이상의 포지션을 입력해주세요.');
      return;
    } else {
      console.log("포지션 토탈리스트");
      console.log(position.totalList);

      let periodCal;
      if(state.projectPeriodUnit === '달'){
        periodCal=state.projectPeriodNum*4;
      }
      else{
        periodCal=state.projectPeriodNum;
      }

      const projectData = {
        title: projectTitle,
        content: projectContent,
        totechList: [...techStack.mystack], // 객체 복사
        period: periodCal,
        totalList: position.totalList
      };
      console.log(projectData.totalList.length);

      const projectDataString = JSON.stringify(projectData);
      const form = new FormData();
      form.set('post', projectDataString);
      if (state.projectImage !== null) {
        form.set('image', state.projectImage);
      }

      console.log(projectDataString);
      console.log("FormData entries:");
      for (const pair of form.entries()) {
        console.log(pair[0] + ': ' + pair[1]);
      }
    }
  };

  return (
    <div className='bg-gray-100 w-screen h-max grid place-items-center'>
      <div className='bg-sub-color w-screen h-20 justify-center flex items-center font-bold text-4xl'>
        새 프로젝트 생성
      </div>
      <div className='bg-gray-300 w-9/12 h-auto p-16 m-5 border-black border-2 '>
        <div>
          <label htmlFor="projectTitle" className='font-bold text-lg my-3'>
            프로젝트 제목
          </label>
          <div>
            <input
              type="text"
              id="projectTitle"
              name="projectTitle"
              className='w-5/6 h-10'
              placeholder='제목을 입력해 주세요'
              onChange={handleInputChange}
            />
          </div>
        </div>
        <div className='my-3'>
          <label htmlFor="projectContent" className='font-bold text-lg '>
            프로젝트 내용
          </label>
          <div>
            <textarea
              id="projectContent"
              name="projectContent"
              className='w-5/6 h-40'
              placeholder='내용을 입력해 주세요'
              onChange={handleInputChange}
            />
          </div>

          <TechStack />

          <div className='my-3'>
            <label htmlFor='projectImage' className='font-bold text-lg'>
              프로젝트 이미지 업로드
            </label>
            <input
              type='file'
              id='projectImage'
              name='projectImage'
              accept='image/*'
              onChange={handleImageChange}
              className='w-5/6 mt-2'
            />
            {state.projectImage && <img src={state.projectImage} alt='Uploaded' className='mt-2 max-h-40' />}
          </div>

          <div className='my-3'>
            <label htmlFor="projectPeriod" className='font-bold text-lg my-3'>
              프로젝트 기간
            </label>
            <div>
              <span className='p-2'>약</span>
              <input
                type="text"
                id="projectPeriodNum"
                name="projectPeriodNum"
                className='w-20 h-10 p-1 mr-2'
                onChange={handleInputChange}
              />
              <button
                className={`p-2 ${state.projectPeriodUnit === '주' ? 'bg-main-color text-white' : ''}`}
                onClick={() => handleUnitClick('주')}
              >
                주
              </button>
              <button
                className={`p-2 ${state.projectPeriodUnit === '달' ? 'bg-main-color text-white' : ''}`}
                onClick={() => handleUnitClick('달')}
              >
                달
              </button>
              <span className='ml-3'>
                달을 선택하시면 자동으로 주 단위로 계산됩니다.
              </span>
            </div>
          </div>

          <Position />
        </div>
        <button
          onClick={handleSave}
          className="mt-5 bg-main-color text-white p-3"
        >
          저장
        </button>
      </div>
    </div>
  );
};

export default RecruitWritePage;

