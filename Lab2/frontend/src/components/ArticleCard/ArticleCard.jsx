import { useNavigate } from 'react-router-dom';
import './ArticleCard.scss';

function ArticleCard({ article }) {
  const navigate = useNavigate();
  const preview = article.content.length > 150
    ? article.content.slice(0, 150) + '...'
    : article.content;

  const date = new Date(article.createdAt).toLocaleDateString('en-US', {
    year: 'numeric', month: 'short', day: 'numeric',
  });

  return (
    <div className="card" onClick={() => navigate(`/articles/${article.id}`)}
      role="button" tabIndex={0}
      onKeyDown={e => e.key === 'Enter' && navigate(`/articles/${article.id}`)}>
      <div className="card__top">
        <span className="card__badge">{article.category}</span>
        <h3 className="card__title">{article.title}</h3>
      </div>
      <div className="card__body">
        <p className="card__preview">{preview}</p>
        <div className="card__footer">
          <span>{article.authorName}</span>
          <span>{date}</span>
        </div>
      </div>
    </div>
  );
}

export default ArticleCard;
