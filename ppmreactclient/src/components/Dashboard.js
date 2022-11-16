import React, { Component } from 'react';
import CreateProjectButton from './Project/CreateProjectButton';
import ProjectItem from './Project/ProjectItem';
import { connect } from 'react-redux';
import { getProjects } from '../actions/projectActions';
import PropTypes from 'prop-types';

class Dashboard extends Component {
  //lifecycle hook dictates what happens when we mount the component (how the component should load & needs to call the action we created getProjects())
  componentDidMount() {
    this.props.getProjects();
  }

  render() {
    const { projects } = this.props.project

    return (
      <div className="projects">
        <div className="container">
          <div className="row">
            <div className="col-md-12">
              <h1 className="display-4 text-center">Projects</h1>
              <br />
              <CreateProjectButton />
              <br />
              <hr />
              {projects.map((project) => (
                <ProjectItem key={project.id} project={project} />
              ))}
            </div>
          </div>
        </div>
      </div>
    );
  }
}

//set up prop types
Dashboard.propTypes = {
  project: PropTypes.object.isRequired,
  getProjects: PropTypes.func.isRequired,
};

//map our state to props
const mapStateToProps = (state) => ({
  project: state.project,
});

export default connect(mapStateToProps, { getProjects })(Dashboard); //connect to redux store
