import { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import api from '../../api/axios';
import ArticleCard from '../../components/ArticleCard/ArticleCard';
import SearchFilter from '../../components/SearchFilter/SearchFilter';
import './HomePage.scss';

function HomePage() {
  const location = useLocation();
  const [articles, setArticles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filters, setFilters] = useState({ search: '', category: '' });

  useEffect(() => {
    setLoading(true);
    const params = {};
    if (filters.search) params.search = filters.search;
    if (filters.category) params.category = filters.category;
    api.get('/api/articles', { params })
      .then(res => setArticles(res.data))
      .catch(() => setArticles([]))
      .finally(() => setLoading(false));
  }, [filters, location.key]);

  return (
    <main className="home">
      <div className="home__hero">
        <h1>Welcome to My Blog</h1>
        <p>Discover articles about technology, science and lifestyle</p>
      </div>
      <div className="home__content">
        <SearchFilter onFilter={setFilters} />
        {loading ? (
          <p className="home__loading">Loading...</p>
        ) : articles.length === 0 ? (
          <p className="home__empty">No articles found.</p>
        ) : (
          <div className="home__grid">
            {articles.map(article => (
              <ArticleCard key={article.id} article={article} />
            ))}
          </div>
        )}
      </div>
    </main>
  );
}

export default HomePage;
