import { GET_ERRORS } from '../actions/types'; //first thing in reducer is import the action type you want to use

const initialState = {};

export default function (state = initialState, action) {
  switch (action.type) {
    case GET_ERRORS:  //if we have errors from the server this is going to return the errors and this is going to be dispatched to the store, in order to hook it to the store we have to bring it to the root reducer "index.js"
      return action.payload; 

    default:
      return state;
  }
}
