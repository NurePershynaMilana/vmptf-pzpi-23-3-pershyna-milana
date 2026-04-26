import { useState } from 'react';
import api from '../../api/axios';
import './CommentForm.scss';

function CommentForm({ articleId, onCommentAdded }) {
  const [text, setText] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    if (!text.trim()) { setError('Comment cannot be empty'); return; }
    setLoading(true);
    try {
      const res = await api.post(`/api/comments/${articleId}`, { text });
      onCommentAdded(res.data);
      setText('');
      setError('');
    } catch (err) {
      setError(err.response?.data?.error || 'Failed to post comment');
    } finally {
      setLoading(false);
    }
  }

  return (
    <form className="comment-form" onSubmit={handleSubmit}>
      <textarea
        className="comment-form__input"
        rows={7}
        placeholder="Write a comment..."
        value={text}
        onChange={e => setText(e.target.value)}
      />
      {error && <span className="field-error">{error}</span>}
      <button type="submit" className="btn btn-primary" disabled={loading}>
        {loading ? 'Posting...' : 'Post Comment'}
      </button>
    </form>
  );
}

export default CommentForm;
