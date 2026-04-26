import { Router } from 'express';
import { readDB, writeDB } from '../server.js';
import authMiddleware from '../middleware/authMiddleware.js';

const router = Router();

router.get('/', (req, res) => {
  const db = readDB();
  res.json(db.categories);
});

router.post('/', authMiddleware, (req, res) => {
  const { name } = req.body;
  if (!name || !name.trim()) {
    return res.status(400).json({ error: 'Category name is required' });
  }
  const db = readDB();
  const trimmed = name.trim();
  if (db.categories.find(c => c.name.toLowerCase() === trimmed.toLowerCase())) {
    return res.status(400).json({ error: 'Category already exists' });
  }
  const newCategory = {
    id: db.categories.length ? Math.max(...db.categories.map(c => c.id)) + 1 : 1,
    name: trimmed,
  };
  db.categories.push(newCategory);
  writeDB(db);
  res.status(201).json(newCategory);
});

export default router;
