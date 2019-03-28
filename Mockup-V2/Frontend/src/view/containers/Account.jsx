import React, { Component } from 'react';
import { connect } from 'react-redux';
import { updateUserInfo } from '../../actions/AuthActions';
import { _translator } from '../components/Translator';
import { ExLang } from '../../assets/lib/Languages';
import { validDate, validEmail, validSelect } from '../../assets/lib/Validator';

class Account extends Component {
  render() {
    const { userdata } = this.props;
    const { username, firstName, lastName, language, dateOfBirth } = userdata;
    return (
      <div className="app-main__inner full-height-mobile">
        <div className="row justify-content-center">
          <div className="col-md-11">
            <div className="card">
              <h5 className="card-header">{_translator('account_yourData')}</h5>
              <div className="card-body">
                <form onSubmit={this.handleSubmit}>
                  <label htmlFor="firstName">
                    {_translator('gen_firstName')}
                  </label>
                  <div className="input-group mb-3">
                    <input
                      type="text"
                      className="form-control"
                      id="firstName"
                      defaultValue={firstName}
                    />
                  </div>
                  <label htmlFor="lastName">
                    {_translator('gen_lastName')}
                  </label>
                  <div className="input-group mb-3">
                    <input
                      type="text"
                      className="form-control"
                      id="lastName"
                      defaultValue={lastName}
                    />
                  </div>
                  <label htmlFor="username">{_translator('gen_email')}</label>
                  <div className="input-group mb-3">
                    <input
                      type="email"
                      className="form-control"
                      id="username"
                      defaultValue={username}
                    />
                  </div>
                  <div className="position-relative form-group">
                    <label htmlFor="dateOfBirth">
                      {_translator('gen_birthDate')}
                    </label>
                    <input
                      name="dateOfBirth"
                      id="dateOfBirth"
                      placeholder={_translator('gen_birthDate')}
                      type="date"
                      className="form-control"
                      onChange={this.handleChange}
                      defaultValue={new Date(dateOfBirth)
                        .toISOString()
                        .substr(0, 10)}
                    />
                  </div>
                  <div className="position-relative form-group">
                    <label htmlFor="language">
                      {_translator('gen_language')}
                    </label>
                    <select
                      defaultValue={language}
                      className="form-control"
                      name="language"
                      id="language"
                      onChange={this.handleChange}
                    >
                      {ExLang.map(lang => (
                        <option value={lang} key={'ALang_' + lang}>
                          {_translator('gen_' + lang)}
                        </option>
                      ))}
                    </select>
                  </div>
                  <button type="submit" className="mt-2 btn btn-primary">
                    {_translator('gen_modify')}
                  </button>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }

  handleSubmit = e => {
    e.preventDefault();
    const formData = {
      firstName: e.target.firstName.value,
      lastName: e.target.lastName.value,
      username: e.target.username.value,
      dateOfBirth: e.target.dateOfBirth.value,
      language: e.target.language.value
    };
    if (this.isFormValid(formData)) {
      console.log('Ok, dati validi > ', formData);
      updateUserInfo();
    }
  };

  isFormValid = formData => {
    const { firstName, lastName, dateOfBirth, username, language } = formData;
    return (
      firstName &&
      firstName !== '' &&
      lastName &&
      lastName !== '' &&
      validDate(dateOfBirth) &&
      validEmail(username) &&
      validSelect(language, ExLang)
    );
  };
}

const mapDispatchToProps = dispatch => {
  return {
    updateUserInfo: data => dispatch(updateUserInfo(data))
  };
};

const mapStateToProps = state => {
  return {
    userdata: state.auth.user
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Account);