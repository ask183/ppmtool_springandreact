import axios from 'axios';
import { DELETE_PROJECT, GET_ERRORS, GET_PROJECT, GET_PROJECTS } from './types';

//history lets us to redirect when we submit on the form after we successfully create project and push it to dashboard
//async means the function always returns a promise, and you will use it with await
export const createProject = (project, history) => async (dispatch) => {
  try {
    await axios.post('/api/project', project);
    history.push('/dashboard');

    dispatch({
      type: GET_ERRORS,
      payload: {},
    });
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data,
    });
  }
};

//this is what we're going to be passing onto the projectReducer
export const getProjects = () => async (dispatch) => {
  const res = await axios.get('/api/project/all');
  dispatch({
    type: GET_PROJECTS,
    payload: res.data, //data we get from the database
  });
};

export const getProject = (id, history) => async (dispatch) => {
  //takes parameters id & history because once we're done we want to reload dashboard
  try {
    const res = await axios.get(`/api/project/${id}`);
    dispatch({
      type: GET_PROJECT,
      payload: res.data,
    });
  } catch (error) {
    history.push('/dashboard');
  }
};

export const deleteProject = (id) => async (dispatch) => {
  if (
    window.confirm(
      'Are you sure? This will delete the project and all the data related to it'
    )
  ) {
    await axios.delete(`/api/project/${id}`);
    dispatch({
      type: DELETE_PROJECT,
      payload: id,
    });
  }
};
