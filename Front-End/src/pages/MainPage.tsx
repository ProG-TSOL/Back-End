import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import MainCard from '../components/MainPage/MainCard.tsx';
import MainSearchBar from '../components/MainPage/MainSearchBar.tsx';
import Pagination from '@mui/material/Pagination';

const App: React.FC = () => {
  return (
    <React.StrictMode>
      <div className='grid text-center'>
        <div className='font-bold text-6xl'>프로젝트 찾기</div>
        <MainSearchBar />
      </div>
      <div className="grid grid-cols-4 gap-4 p-10 m-10">
        <MainCard />
        <MainCard />
        <MainCard />
        <MainCard />
        <MainCard />
        <MainCard />
        <MainCard />
        <MainCard />
        <MainCard />
        <MainCard />
        <MainCard />
        <MainCard />
        <MainCard />
        <MainCard />
      </div>
      <div className='flex justify-center mb-10'>
      <Pagination count={30} variant="outlined" shape="rounded"/>
      </div>
    </React.StrictMode>
  );
};

ReactDOM.createRoot(document.getElementById('root')!).render(<App />);
