import React, { useEffect } from 'react';
import './styles/App.scss';
import Header from './components/Header/Header';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import Home from './pages/home/Home';

export default function App() {
  // disable preload
  useEffect(() => {
    document.body.classList.remove('preload');
  });

  return (
    <Router>
      <div>
        <Header />

        <Switch>
          <Route exact path="/" component={Home} />
        </Switch>
      </div>
    </Router>
  );
}
