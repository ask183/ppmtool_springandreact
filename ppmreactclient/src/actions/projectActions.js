import axios from 'axios';
import { GET_ERRORS } from './types';

//history lets us to redirect when we submit on the form after we successfully create project and push it to dashboard
//async means the function always returns a promise, and you will use it with await
export const createProject = (project, history) => async (dispatch) => {
  try {
    const res = await axios.post('http://localhost:8080/api/project', project);
    history.push('/dashboard');
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data,
    });
  }
};
