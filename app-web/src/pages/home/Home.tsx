import React from 'react';
import './home.scss';

export default function Home() {
  return (
    <div>
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
