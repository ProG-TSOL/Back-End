// import axios from 'axios';

// const Login = async () => {
//   try {
//     const login = await axios.post('http://i10a210.p.ssafy.io:8080/members/login', {
//       email: 'aaaa@naver.com',
//       password: '1234',
//     }).then(response => {
//       console.log(response);
//     });
//     // Handle the response as needed
//     console.log('Login successful:',login);
//   } catch (error) {
//     console.error('Error fetching:');
//   }
// };

// const Post = async () => {
//   try {
//     const myHeaders = {
//       Authorization: 'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiZXhwIjoxNzA2NzcxMDg1LCJpYXQiOjE3MDY3NjkyODUsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdfQ.78mo5e5rp6_kHkDI0NQJu2R9H9bKclBRfenCCMp9g8w',
//     };
//     const data1 = {
//       title: '제목입니다',
//       content: '내용이예요',
//       period: 10,
//       totechList: [{ techCode: 11 }, { techCode: 12 }],
//       totalList: [
//         { jobCode: 14, total: 2, current: 1 },
//         { jobCode: 15, total: 3, current: 0 },
//       ],
//     };
//     const formData = new FormData();
//     formData.append('memberId', '1');
//     formData.append('post', new Blob([JSON.stringify(data1)], { type: 'application/json' }));
//     console.log(formData);

//     const response = await axios.post('http://i10a210.p.ssafy.io:8080/projects/1', formData, {
//       headers: {
//         ...myHeaders,
//         'Content-Type': 'multipart/form-data',
//       },
//     });

//     // Handle the response as needed
//     console.log('Post successful:', response.data);
//   } catch (error) {
//     console.error('Error fetching:', error.message);
//     if (error.response) {
//       console.error('Response data:', error.response.data);
//       console.error('Response status:', error.response.status);
//       console.error('Response headers:', error.response.headers);
//     }
//   }
// };

// const Get = async () => {
//   try {
//     const myHeaders = {
//       Authorization: 'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiZXhwIjoxNzA2NzczMTQ2LCJpYXQiOjE3MDY3NzEzNDYsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdfQ.L8YW6eBOB9z83i7BFaonvI4vp0HBYs9O3vSGM8qe7II',
//     };
//     const response = await axios.get('http://i10a210.p.ssafy.io:8080/projects', {
//       headers: myHeaders,
//       params: {
//         keyword: '',
//         techCodes: '13',
//         statusCode: '',
//         page: '1',
//         size: '10',
//       },
//     });

//     // Handle the response as needed
//     console.log('Get successful:', response.data);
//   } catch (error) {
//     console.error('Error fetching:', error.message);
//     if (error.response) {
//       console.error('Response data:', error.response.data);
//       console.error('Response status:', error.response.status);
//       console.error('Response headers:', error.response.headers);
//     }
//   }
// };

// const HomePage = () => {
//   return (
//     <div>
//       <div className='flex flex-col items-center justify-center'>
//         <div className='font-bold text-4xl'>Programmer가 되고 싶은 ProG들</div>
//         <div className='grid items-center justify-center text-center'>
//           <span className='bg-main-color p-5 m-3 text-xl text-white cursor-pointer' onClick={Login}>
//             로그인
//           </span>
//           <span className='bg-yellow-300 p-5 m-3 text-xl cursor-pointer' onClick={Post}>
//             post 테스트
//           </span>
//           <span className='bg-yellow-300 p-5 m-3 text-xl cursor-pointer' onClick={Get}>
//             Get 테스트
//           </span>
//         </div>
//       </div>
//     </div>
//   );
// };

// export default HomePage;
