import "./index.css";
import { createBrowserRouter, RouterProvider } from "react-router-dom";

//페이지 import
import RecruitPage from "./pages/recruit/RecruitPage";
import RecruitWritePage from "./pages/recruit/RecruitWritePage";
import MyProjectPage from "./pages/myproject/MyProject"
import SignUpPage from "./pages/admin/SignUpPage";
import LoginPage from "./pages/admin/LoginPage";
import CommutePage from "./pages/project/commute/CommutePage";
import TaskPage from "./pages/project/task/TaskPage";
import FeedPage from "./pages/project/feed/FeedPage";
import RetrospectPage from "./pages/project/retrospect/RetrospectPage";
import MyPage from "./pages/mypage/MyPage";
import HomePage from "./pages/home/HomePage";
import ProjectPage from "./pages/project/ProjectPage";
import Layout from "./layout/Layout";

import RecruitProjectPage from "./pages/recruit/RecruitProjectPage";

function App() {
  //라우터 설정
  const router = createBrowserRouter([
    {
      path: "/",
      element: <Layout />, // Layout 컴포넌트를 사용
      children: [
        // 자식 라우트를 설정
        {
          index: true, // '/' 경로에 대응
          element: <HomePage />,
        },
        {
          path: "login",
          element: <LoginPage />,
        },
        {
          path: "signup",
          element: <SignUpPage />,
        },
        {
          path: "mypage",
          element: <MyPage />,
        },
        {
          path: "recruit",
          element: <RecruitPage />,
        },
        {
          path: "recruit/write",
          element: <RecruitWritePage />,
        },
        {
          path: "myproject",
          element: <MyProjectPage />,
        },
        {
          path: "recruit/project",
          element: <RecruitProjectPage />,
        },
        {
          path: "project",
          element: <ProjectPage />,
          children: [
            {
              path: "commute",
              element: <CommutePage />,
            },
            {
              path: "task",
              element: <TaskPage />,
            },
            {
              path: "feed",
              element: <FeedPage />,
            },
            {
              path: "retrospect",
              element: <RetrospectPage />,
            },
          ],
        },
      ],
    },
  ]);

  return (
    <div>
      <RouterProvider router={router} />
    </div>
  );
}

export default App;
