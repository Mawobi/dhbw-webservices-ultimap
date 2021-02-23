import React from 'react';
import './Navbar.scss';
import Tab from './Tab';

export default function NavBar() {
  return (
    <nav className={'nav'}>
      <ul className={'nav__tabs'}>
        <li>
          <Tab />
        </li>
        <li>
          <Tab name="About" href="/about" />
        </li>
        <li>
          <Tab />
        </li>
      </ul>
    </nav>
  );
}
