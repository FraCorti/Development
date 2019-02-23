import React, { Component } from "react";
import Word from "./Word";
class EsecutionExercise extends Component {
  state = {
    it: {
      adjective: {
        text: "adjective",
        data: {
          type: [["ordinal", "O"], ["qualificative", "Q"], ["possessive", "P"]],
          degree: [["superlative", "S"]],
          gen: [["feminile", "F"], ["masculine", "M"], ["common", "C"], ["neuter", "N"]],
          num: [["singular", "S"], ["plural", "P"], ["invariable", "N"]],
          possessorpers: [["1", "1"], ["2", "2"], ["3", "3"]],
          possessornum: [["singular", "S"], ["plural", "P"], ["invariable", "N"]]
        }
      },
      conjunction: {
        text: "conjunction",
        data: {
          type: [["coordinating", "C"], ["subordinating", "S"]]
        }
      },
      determiner: {
        text: "determiner",
        data: {
          type: [
            ["article", "A"],
            ["demonstrative", "D"],
            ["exclamative", "E"],
            ["indefinite", "I"],
            ["interrogative", "T"],
            ["numeral", "N"],
            ["possessive", "P"]
          ]
        }
      }
    }
  };

  render() {
    let out = null;
    if (this.props.phrase && this.props.phrase.length) {
      out = (
        <div className="row">
          <div className="col-sm-12 col-md-6">
            <div className="main-card mb-3 card">
              <div className="card-body">
                <h5 className="card-title">Esegui l'esercizio</h5>
                <ul className="list-group">
                  {this.props.phrase &&
                    this.props.phrase.map((item, index) => {
                      return <Word key={"s" + item} parola={item} ger={this.state.it} />;
                    })}
                </ul>

                <button className="btn btn-success">Completa</button>
              </div>
            </div>
          </div>
        </div>
      );
    }

    return <React.Fragment>{out}</React.Fragment>;
  }
}

export default EsecutionExercise;
