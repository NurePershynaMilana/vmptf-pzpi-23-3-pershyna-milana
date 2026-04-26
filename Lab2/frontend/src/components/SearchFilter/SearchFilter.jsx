import { useState, useEffect } from 'react';
import api from '../../api/axios';
import './SearchFilter.scss';

function SearchFilter({ onFilter }) {
  const [search, setSearch] = useState('');
  const [category, setCategory] = useState('');
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    api.get('/api/categories').then(res => setCategories(res.data));
  }, []);

  useEffect(() => {
    const timer = setTimeout(() => {
      onFilter({ search, category });
    }, 300);
    return () => clearTimeout(timer);
  }, [search, category]);

  return (
    <div className="search-filter">
      <div className="search-filter__input-wrap">
        <svg className="search-filter__icon" viewBox="0 0 20 20" fill="none">
          <circle cx="9" cy="9" r="6" stroke="#c4b5fd" strokeWidth="1.8"/>
          <path d="M13.5 13.5L17 17" stroke="#c4b5fd" strokeWidth="1.8" strokeLinecap="round"/>
        </svg>
        <input
          className="search-filter__input"
          type="text"
          placeholder="Search articles..."
          value={search}
          onChange={e => setSearch(e.target.value)}
        />
        {search && (
          <button className="search-filter__clear" onClick={() => setSearch('')}>✕</button>
        )}
      </div>
      <select
        className="search-filter__select"
        value={category}
        onChange={e => setCategory(e.target.value)}
      >
        <option value="">All categories</option>
        {categories.map(c => (
          <option key={c.id} value={c.name}>{c.name}</option>
        ))}
      </select>
    </div>
  );
}

export default SearchFilter;
