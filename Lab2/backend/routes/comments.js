import { Router } from 'express';
import { readDB, writeDB } from '../server.js';
import authMiddleware from '../middleware/authMiddleware.js';

const router = Router();

router.post('/:articleId', authMiddleware, (req, res) => {
  const { text } = req.body;
  if (!text) {
    return res.status(400).json({ error: 'Comment text is required' });
  }
  const db = readDB();
  const articleId = parseInt(req.params.articleId);
  if (!db.articles.find(a => a.id === articleId)) {
    return res.status(404).json({ error: 'Article not found' });
  }
  const newComment = {
    id: db.comments.length ? Math.max(...db.comments.map(c => c.id)) + 1 : 1,
    articleId,
    text,
    authorName: req.user.name,
    createdAt: new Date().toISOString(),
  };
  db.comments.push(newComment);
  writeDB(db);
  res.status(201).json(newComment);
});

export default router;
