import { createBrowserRouter } from 'react-router-dom';
import AuthorApplicationPage from './writers/AuthorApplicationPage';
import WriterManagementPage from './writers/WriterManagementPage';
import AdminSignupPage from './writers/AdminSignupPage';
import Home from './mainPage/Home';
import Login from './users/Login';
import Signup from './users/Signup';
import MyPage from './users/MyPage';



const router = createBrowserRouter([
    {
        id: 0,
        path: '/',
        element: <Home />,
    },
    {
        id: 1,
        path: '/author/application',
        element: <AuthorApplicationPage />,
    },
    {
        id: 2,
        path: '/login',
        element: <Login/>,
    },
    {
        id: 3,
        path: '/signup',
        element: <Signup/>,
    },
    {
        id: 4,
        path: '/mypage',
        element: <MyPage/>,
    },
    {
        id: 5,
        path: '/author/mangement',
        element: <WriterManagementPage/>,
    },
    {
        id: 6,
        path: '/admin/signup',
        element: <AdminSignupPage/>,
    }



]);
export default router;