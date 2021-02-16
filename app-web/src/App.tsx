import React, { useEffect } from 'react';
import './styles/App.scss';
import Header from './components/Header/Header';

export default function App() {
  // disable preload
  useEffect(() => {
    document.body.classList.remove('preload');
  });

  return (
    <div>
      <Header />
      <p> Hallo Welt!</p>
      <button className={'btn'} onClick={toggleDarkMode}>
        Theme ändern
      </button>
    </div>
  );
}

function toggleDarkMode() {
  const html = document.documentElement;
  html.setAttribute('theme', html.getAttribute('theme') === 'dark' ? 'light' : 'dark');
}
