import logo from './logo.svg';
import './App.css';
import Calculator from './Calculator';
import { Component } from 'react';

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      currentTime: null
    }
  }

  render() {
    return (
      <div className="App">
        <div className="Calculator">
          <Calculator/>
        </div>
      </div>
    );
  }
}

export default App;
