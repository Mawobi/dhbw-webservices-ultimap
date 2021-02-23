import React, { useEffect } from 'react';
import './styles/App.scss';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import Home from './pages/home/Home';
import NavBar from './components/navbar/Navbar';

export default function App() {
  // disable preload
  useEffect(() => {
    document.body.classList.remove('preload');
  });

  return (
    <Router>
      <div>
        <Switch>
          <Route exact path="/" component={Home} />
          <Route render={() => <h1>404: page not found</h1>} />
        </Switch>
      </div>

      <NavBar />
    </Router>
  );
}
