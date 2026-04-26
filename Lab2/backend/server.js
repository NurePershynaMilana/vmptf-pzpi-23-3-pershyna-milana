import express from 'express';
import cors from 'cors';
import { readFileSync, writeFileSync } from 'fs';
import { fileURLToPath } from 'url';
import { dirname, join } from 'path';
import authRoutes from './routes/auth.js';
import articleRoutes from './routes/articles.js';
import commentRoutes from './routes/comments.js';
import categoryRoutes from './routes/categories.js';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

export const DB_PATH = join(__dirname, 'db.json');

export function readDB() {
  return JSON.parse(readFileSync(DB_PATH, 'utf-8'));
}

export function writeDB(data) {
  writeFileSync(DB_PATH, JSON.stringify(data, null, 2));
}

const app = express();

app.use(cors({ origin: 'http://localhost:8080' }));
app.use(express.json());

app.use('/api/auth', authRoutes);
app.use('/api/articles', articleRoutes);
app.use('/api/comments', commentRoutes);
app.use('/api/categories', categoryRoutes);

app.listen(3001, () => {
  console.log('Server running on http://localhost:3001');
});
