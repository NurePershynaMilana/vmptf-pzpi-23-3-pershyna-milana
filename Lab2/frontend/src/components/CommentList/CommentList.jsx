import './CommentList.scss';

function CommentList({ comments }) {
  if (!comments.length) {
    return <p className="no-comments">No comments yet. Be the first!</p>;
  }
  return (
    <ul className="comment-list">
      {comments.map(c => (
        <li key={c.id} className="comment">
          <div className="comment__header">
            <strong>{c.authorName}</strong>
            <span>{new Date(c.createdAt).toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' })}</span>
          </div>
          <p className="comment__text">{c.text}</p>
        </li>
      ))}
    </ul>
  );
}

export default CommentList;
