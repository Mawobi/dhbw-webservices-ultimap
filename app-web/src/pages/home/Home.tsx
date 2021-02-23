import React from 'react';
import './home.scss';
import Map from '../../components/Map/Map';

export default function Home() {
  return (
    <div>
      <Map />
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
