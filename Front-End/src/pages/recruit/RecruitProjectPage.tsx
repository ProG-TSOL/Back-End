import React from 'react'
const title = "프로젝트 Prog "
const description = "개굴개굴 개구리 노래를한다  \n 개굴개굴 \n 객개굴 객개굴 깨굴깨굴 \n 꺠꾸리"
// const img = "http://localhost:5173/3a17d8ae-3701-4d91-99f1-e4f3187aec78"
const img2 = "/src/assets/GitHubIcon.png"
const mystack = ["JAVA", "C", "C++", "C#", "Python"]
const period = "3주"
const positions = {
    posName: ['프론트엔드', '백엔드', '기획', '기획'],
    posNowNumber: [2, 2, 0],
    posNumber: [3, 2, 1]
};

const RecruitProjectPage = () => {
    return (
        <div>
            <div className='flex justify-between'>
                <div className=' w-screen h-screen flex p-10 flex-col'>
                    <div>

                        <div className='flex h-100 bg-gray-100'>
                            <div className='w-7/12'>

                                <div className='border-black border-2 h-20 p-2'>
                                    <div className='text-2xl'>
                                        제목
                                    </div>
                                    <div className='text-3xl font-bold'>
                                        {title}
                                    </div>
                                </div>
                                <div className='border-black border-2 h-52 p-2'>
                                    <div className='text-2xl'>
                                        설명
                                    </div>
                                    <div className='font-bold'>
                                        {description.split('\n').map((line, index) => (
                                            <React.Fragment key={index}>
                                                {line}
                                                <br />
                                            </React.Fragment>
                                        ))}
                                    </div>
                                </div>
                                <div className='border-black border-2 h-40 p-2'>
                                    <div className='text-2xl'>
                                        기술스택
                                    </div>
                                    <div className="flex flex-wrap">
                                        {mystack.map((tech, index) => (
                                            <div key={index} className="bg-gray-200 p-1 m-1 rounded-full">
                                                {tech}
                                            </div>
                                        ))}
                                    </div>
                                </div>

                            </div>
                            <div className='w-5/12'>

                                <div className='border-black border-2 h-48 p-2'>
                                    <div className='text-2xl'>
                                        사진
                                    </div>
                                    <img src={img2} className='w-full h-36' />
                                </div>
                                <div className='border-black border-2 h-24 p-2'>
                                    <div className='text-2xl'>
                                        예상 진행기간
                                    </div>
                                    <div className='text-center text-3xl'>
                                        {period}
                                    </div>
                                </div>
                                <div className='border-black border-2 h-40 bg-gray-100 p-2 flex-col'>
                                    <div className='text-2xl h-8'>
                                        구성인원
                                    </div>
                                    <div className='overflow-y-auto h-28 pt-20 flex flex-col items-center justify-center'>
                                        {positions.posName.map((name, index) => (
                                            <div key={index} className='h-8 m-1'>
                                                {`${name}: ${positions.posNowNumber[index]} / ${positions.posNumber[index]}명`}
                                                {positions.posNowNumber[index] < positions.posNumber[index] ? (
                                                    <button className='ml-3 bg-main-color text-white p-1 rounded'>
                                                        지원하기
                                                    </button>
                                                ) : (
                                                    <button className='ml-3 bg-orange-500 text-black p-1 rounded' disabled>
                                                        모집완료
                                                    </button>
                                                )}
                                            </div>
                                        ))}
                                    </div>



                                </div>
                            </div>
                        </div>
                    </div>
                    <div className='border-black border-2 h-auto bg-gray-100 p-2'>
                        <div className='text-2xl'>
                            댓글
                        </div>
                    </div>


                </div>
            </div>
        </div>
    );
}

export default RecruitProjectPage