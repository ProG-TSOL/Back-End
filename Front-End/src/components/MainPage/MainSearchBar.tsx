import React, { Component } from 'react';

const tags = ["무관", "JAVA", "REACT", "VITE", "VUE", "TypeScript", "HTML", "CSS", "SASS", "SCSS", "JavaScript"];
const status = ["무관", "모집중", "진행중", "완료"];

export default class MainSearchBar extends Component {
  render() {
    return (
      <div>


        <div className='flex justify-end'>
          <button className='bg-orange-300 p-3 mt-5 mr-5 font-bold'>직접 모집하기</button>
        </div>
        <div className='flex justify-between mx-5 my-3 bg-sub-color p-5 items-center'>
          <div>
            <label htmlFor="techStack">기술 스택</label>
            <select id="techStack" className="ml-2 p-2">
              {tags.map((tag, index) => (
                <option key={index} value={tag}>{tag}</option>
              ))}
            </select>
          </div>
          <div>
            <label htmlFor="recruitmentStatus">모집 상태</label>
            <select id="recruitmentStatus" className="ml-2 p-2">
              {status.map((stat, index) => (
                <option key={index} value={stat}>{stat}</option>
              ))}
            </select>
          </div>
          <div>
            <label htmlFor="search">검색창</label>
            <input id="search" className="ml-2 p-2" type="text" placeholder="검색어를 입력하세요" />
            <button className="ml-2 p-2 bg-main-color text-white">검색</button>
          </div>

          
        </div>


      </div>
    );
  }
}
