import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import ArticleForm from '../ArticleForm/ArticleForm';
import './Header.scss';

function Header() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [showForm, setShowForm] = useState(false);

  function handleLogout() {
    logout();
    navigate('/');
  }

  return (
    <>
      <header className="header">
        <Link to="/" className="header__logo">My Blog</Link>
        <nav className="header__nav">
          {user ? (
            <>
              <span className="header__greeting">Hello, {user.name}</span>
              <button className="btn btn-primary" onClick={() => setShowForm(true)}>
                New Article
              </button>
              <button className="btn btn-outline" onClick={handleLogout}>
                Logout
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="btn btn-outline">Login</Link>
              <Link to="/register" className="btn btn-primary">Register</Link>
            </>
          )}
        </nav>
      </header>
      {showForm && <ArticleForm onClose={() => setShowForm(false)} />}
    </>
  );
}

export default Header;
