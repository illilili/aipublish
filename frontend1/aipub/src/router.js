import { createBrowserRouter } from 'react-router-dom';
import WriterRegister from './writers/WriterRegister';
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
        path: '/writer_register',
        element: <WriterRegister />,
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
    }

]);
export default router;