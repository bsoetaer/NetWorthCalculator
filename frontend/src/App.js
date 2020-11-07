import logo from './logo.svg';
import './App.css';
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
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <p>
            Hello World! Current time: {this.state.currentTime}
          </p>
          <a
            className="App-link"
            href="https://reactjs.org"
            target="_blank"
            rel="noopener noreferrer"
          >
            Learn React
          </a>
        </header>
      </div>
    );
  }
}

export default App;
