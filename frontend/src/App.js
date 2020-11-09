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
  
  componentDidMount()
  {
    fetch('http://localhost:8080/time')
    .then(response => response.text())
    .then((data) => this.setState({currentTime: data}))
    .catch(err => { console.log(err); });
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
