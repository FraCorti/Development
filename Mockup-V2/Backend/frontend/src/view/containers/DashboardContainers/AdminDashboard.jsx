import React, { Component } from 'react';
import { connect } from 'react-redux';
import _translator from '../../../helpers/Translator';
import {
  fetchUsersList,
  fetchDeveloperList
} from '../../../actions/AdminActions';

class AdminDashboard extends Component {
  constructor(props) {
    super(props);
    const { admin, fetchUsersListDispatch, fetchDeveloperListDispatch } = props;
    const { devList, usersList } = admin;
    if (usersList.length < 1) fetchUsersListDispatch();
    if (devList.length < 1) fetchDeveloperListDispatch();
  }

  render() {
    const { user, admin } = this.props;
    const { firstName, language } = user;
    const { devList, usersList } = admin;
    const numOfStudents = usersList.filter(
      _user => _user.role === 'ROLE_STUDENT'
    ).length;
    const numOfDevs = usersList.filter(_user => _user.role === 'ROLE_DEVELOPER')
      .length;
    const numOfTeachers = usersList.filter(
      _user => _user.role === 'ROLE_TEACHER'
    ).length;
    const devToAccept = devList.length;

    const students = (
      <div className="col-md-6 ">
        <div className="card mb-3 widget-content bg-midnight-bloom">
          <div className="widget-content-wrapper text-white">
            <div className="widget-content-left">
              <div className="widget-heading">
                {_translator('adminDashboard_students', language)}
              </div>
              <div className="widget-subheading">
                {_translator('adminDashboard_numStudents', language)}
              </div>
            </div>
            <div className="widget-content-right">
              <div className="widget-numbers text-white">
                <span>{numOfStudents}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    );

    const teachers = (
      <div className="col-md-6">
        <div className="card mb-3 widget-content bg-arielle-smile">
          <div className="widget-content-wrapper text-white">
            <div className="widget-content-left">
              <div className="widget-heading">
                {_translator('adminDashboard_teachers', language)}
              </div>
              <div className="widget-subheading">
                {_translator('adminDashboard_numTeachers', language)}
              </div>
            </div>
            <div className="widget-content-right">
              <div className="widget-numbers text-white">
                <span>{numOfTeachers}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    );

    const devs = (
      <div className="col-md-6 ">
        <div className="card mb-3 widget-content bg-happy-green">
          <div className="widget-content-wrapper text-white">
            <div className="widget-content-left">
              <div className="widget-heading">
                {_translator('adminDashboard_devs', language)}
              </div>
              <div className="widget-subheading">
                {_translator('adminDashboard_numDevs', language)}
              </div>
            </div>
            <div className="widget-content-right">
              <div className="widget-numbers text-white">
                <span>{numOfDevs}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    );

    const devsAccept = (
      <div className="col-md-6 ">
        <div className="card mb-3 widget-content bg-premium-dark">
          <div className="widget-content-wrapper text-white">
            <div className="widget-content-left">
              <div className="widget-heading">
                {_translator('adminDashboard_devWait', language)}
              </div>
              <div className="widget-subheading">
                {_translator('adminDashboard_numDevWait', language)}
              </div>
            </div>
            <div className="widget-content-right">
              <div className="widget-numbers text-white">
                <span>{devToAccept}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    );

    return (
      <React.Fragment>
        <div className="row justify-content-center">
          <div className="py-5 text-center">
            <h2>{_translator('gen_userDashboard', language)}</h2>
            <p className="lead">
              {_translator('dashboard_hiUser', language)}
              {firstName}
            </p>
          </div>
        </div>
        <div className="row justify-content-center">
          {students}
          {teachers}
          {devs}
          {devsAccept}
        </div>
      </React.Fragment>
    );
  }
}

const mapStateToProps = store => {
  return {
    user: store.auth.user,
    admin: store.admin
  };
};

const mapDispatchToProps = dispatch => {
  return {
    fetchDeveloperListDispatch: () => dispatch(fetchDeveloperList()),
    fetchUsersListDispatch: () => dispatch(fetchUsersList())
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AdminDashboard);
