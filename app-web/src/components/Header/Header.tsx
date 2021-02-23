import React from 'react';
import './Header.scss';
import { Link } from 'react-router-dom';

export default function Header() {
  return (
    <header className={'um-header'}>
      Ich bin ein Header
      <nav>
        <ul>
          <li>
            <Link to="/">Home</Link>
          </li>
          <li>
            <Link to="/about">About</Link>
          </li>
          <li>
            <Link to="/contact">Contact</Link>
          </li>
        </ul>
      </nav>
    </header>
  );
}
