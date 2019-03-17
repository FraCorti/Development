import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';

class SignedOutLinks extends Component {
  state = {};

  render() {
    return (
      <React.Fragment>
        <div className="app-header-right">
          <div className="header-btn-lg pr-0">
            <div className="widget-content p-0">
              <div className="widget-content-wrapper">
              
                <div className="widget-content-left">
                  <ul className="header-menu nav">
                    <li className="nav-item">
                      <NavLink to="/signup" className="nav-link">
                        <i className="material-icons text-info">person_add</i>
                        Registrati
                      </NavLink>
                    </li>
                    <li className="btn-group nav-item">
                      <NavLink to="/signin" className="nav-link">
                        <i className="material-icons text-primary">person</i>
                        Accedi
                      </NavLink>
                    </li>
                  </ul>
                </div>

                <div className="widget-content-right header-user-info ml-3" />
              </div>
            </div>
          </div>
        </div>
      </React.Fragment>
    );
  }
}

export default SignedOutLinks;