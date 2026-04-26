import { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import api from '../../api/axios';
import { useAuth } from '../../context/AuthContext';
import CommentList from '../../components/CommentList/CommentList';
import CommentForm from '../../components/CommentForm/CommentForm';
import './ArticlePage.scss';

function ArticlePage() {
  const { id } = useParams();
  const { user } = useAuth();
  const navigate = useNavigate();
  const [article, setArticle] = useState(null);
  const [comments, setComments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [deleting, setDeleting] = useState(false);

  useEffect(() => {
    api.get(`/api/articles/${id}`)
      .then(res => {
        const { comments: c, ...rest } = res.data;
        setArticle(rest);
        setComments(c);
      })
      .catch(() => setArticle(null))
      .finally(() => setLoading(false));
  }, [id]);

  async function handleDelete() {
    if (!confirm('Delete this article?')) return;
    setDeleting(true);
    try {
      await api.delete(`/api/articles/${id}`);
      navigate('/');
    } finally {
      setDeleting(false);
    }
  }

  if (loading) return <p className="article-page__loading">Loading...</p>;
  if (!article) return <p className="article-page__loading">Article not found.</p>;

  const date = new Date(article.createdAt).toLocaleDateString('en-US', {
    year: 'numeric', month: 'long', day: 'numeric',
  });

  const isAuthor = user && user.id === article.authorId;

  return (
    <main className="article-page">
      <div className="article-page__hero">
        <div className="article-page__hero-inner">
          <div className="article-page__hero-top">
            <Link to="/" className="article-page__back">← Back to articles</Link>
            {isAuthor && (
              <button className="article-page__delete" onClick={handleDelete} disabled={deleting}>
                {deleting ? 'Deleting...' : 'Delete article'}
              </button>
            )}
          </div>
          <span className="article-page__badge">{article.category}</span>
          <h1 className="article-page__title">{article.title}</h1>
          <div className="article-page__meta">
            <span>By {article.authorName}</span>
            <span>{date}</span>
          </div>
        </div>
      </div>

      <div className="article-page__container">
        <article className="article-page__content">
          <p className="article-page__body">{article.content}</p>
        </article>

        <section className="article-page__comments">
          <h2>Comments ({comments.length})</h2>
          <CommentList comments={comments} />
          {user ? (
            <div className="article-page__comment-form">
              <h3>Leave a comment</h3>
              <CommentForm
                articleId={article.id}
                onCommentAdded={newComment => setComments(prev => [...prev, newComment])}
              />
            </div>
          ) : (
            <p className="article-page__login-hint">
              <Link to="/login">Login</Link> to leave a comment.
            </p>
          )}
        </section>
      </div>
    </main>
  );
}

export default ArticlePage;
