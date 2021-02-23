import { IconContext } from 'react-icons';
import { Link } from 'react-router-dom';
import React from 'react';
import './Tab.scss';
import { IoHome } from 'react-icons/io5';

export default function Tab(props: any) {
  return (
    <Link className={'nav__tab'} to={props.href ?? '/'}>
      <IconContext.Provider value={{ className: 'nav__icon' }}>
        <div>{props.icon ?? <IoHome />}</div>
      </IconContext.Provider>

      {props.name ?? 'Home'}
    </Link>
  );
}
