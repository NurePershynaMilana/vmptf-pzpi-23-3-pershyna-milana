import { Router } from 'express';
import { readDB, writeDB } from '../server.js';
import authMiddleware from '../middleware/authMiddleware.js';

const router = Router();

router.get('/', (req, res) => {
  const { search, category } = req.query;
  const db = readDB();
  let articles = [...db.articles].sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
  if (search) {
    articles = articles.filter(a => a.title.toLowerCase().includes(search.toLowerCase()));
  }
  if (category) {
    articles = articles.filter(a => a.category === category);
  }
  res.json(articles);
});

router.get('/:id', (req, res) => {
  const db = readDB();
  const article = db.articles.find(a => a.id === parseInt(req.params.id));
  if (!article) {
    return res.status(404).json({ error: 'Article not found' });
  }
  const comments = db.comments.filter(c => c.articleId === article.id);
  res.json({ ...article, comments });
});

router.post('/', authMiddleware, (req, res) => {
  const { title, content, category } = req.body;
  if (!title || !content || !category) {
    return res.status(400).json({ error: 'All fields are required' });
  }
  const db = readDB();
  if (!db.categories.find(c => c.name.toLowerCase() === category.toLowerCase())) {
    db.categories.push({
      id: db.categories.length ? Math.max(...db.categories.map(c => c.id)) + 1 : 1,
      name: category,
    });
  }
  const newArticle = {
    id: db.articles.length ? Math.max(...db.articles.map(a => a.id)) + 1 : 1,
    title,
    content,
    category,
    authorId: req.user.id,
    authorName: req.user.name,
    createdAt: new Date().toISOString(),
  };
  db.articles.push(newArticle);
  writeDB(db);
  res.status(201).json(newArticle);
});

router.delete('/:id', authMiddleware, (req, res) => {
  const db = readDB();
  const index = db.articles.findIndex(a => a.id === parseInt(req.params.id));
  if (index === -1) {
    return res.status(404).json({ error: 'Article not found' });
  }
  if (db.articles[index].authorId !== req.user.id) {
    return res.status(403).json({ error: 'Forbidden' });
  }
  db.articles.splice(index, 1);
  db.comments = db.comments.filter(c => c.articleId !== parseInt(req.params.id));
  writeDB(db);
  res.json({ success: true });
});

export default router;
