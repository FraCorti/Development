import React, { Component } from "react";
import NewExsercise from "./exercise/NewExercise";
class Body extends Component {
  state = {};
  render() {
    return (
      <div className="app-main__inner">
        <NewExsercise />
      </div>
    );
  }
}

export default Body;
