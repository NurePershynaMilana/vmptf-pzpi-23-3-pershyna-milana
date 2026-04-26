import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../api/axios';
import './ArticleForm.scss';

function ArticleForm({ onClose }) {
  const navigate = useNavigate();
  const [form, setForm] = useState({ title: '', content: '', category: '' });
  const [categories, setCategories] = useState([]);
  const [newCategory, setNewCategory] = useState('');
  const [showNewCategory, setShowNewCategory] = useState(false);
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    api.get('/api/categories').then(res => {
      setCategories(res.data);
      if (res.data.length) setForm(f => ({ ...f, category: res.data[0].name }));
    });
  }, []);

  function validate() {
    const errs = {};
    if (!form.title.trim()) errs.title = 'Title is required';
    if (!form.content.trim()) errs.content = 'Content is required';
    if (!form.category) errs.category = 'Category is required';
    return errs;
  }

  function handleAddCategory() {
    const trimmed = newCategory.trim();
    if (!trimmed) return;
    if (categories.some(c => c.name.toLowerCase() === trimmed.toLowerCase())) {
      setErrors(prev => ({ ...prev, newCategory: 'Category already exists' }));
      return;
    }
    setCategories(prev => [...prev, { id: `_new_${Date.now()}`, name: trimmed }]);
    setForm(f => ({ ...f, category: trimmed }));
    setNewCategory('');
    setShowNewCategory(false);
    setErrors(prev => { const e = { ...prev }; delete e.newCategory; return e; });
  }

  async function handleSubmit(e) {
    e.preventDefault();
    const errs = validate();
    if (Object.keys(errs).length) { setErrors(errs); return; }
    setLoading(true);
    try {
      await api.post('/api/articles', form);
      onClose();
      navigate('/');
    } catch (err) {
      setErrors({ general: err.response?.data?.error || 'Something went wrong' });
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <div className="modal__header">
          <h2>New Article</h2>
          <button className="modal__close" onClick={onClose}>✕</button>
        </div>
        <form onSubmit={handleSubmit} className="article-form">
          {errors.general && <p className="form-error">{errors.general}</p>}
          <div className="form-group">
            <label>Title</label>
            <input
              type="text"
              value={form.title}
              onChange={e => setForm({ ...form, title: e.target.value })}
            />
            {errors.title && <span className="field-error">{errors.title}</span>}
          </div>
          <div className="form-group">
            <label>Category</label>
            <div className="form-group__row">
              <select
                value={form.category}
                onChange={e => setForm({ ...form, category: e.target.value })}
              >
                {categories.map(c => <option key={c.id} value={c.name}>{c.name}</option>)}
              </select>
              <button
                type="button"
                className="btn-add-category"
                onClick={() => setShowNewCategory(v => !v)}
              >
                {showNewCategory ? '✕' : '+ New'}
              </button>
            </div>
            {errors.category && <span className="field-error">{errors.category}</span>}
            {showNewCategory && (
              <div className="new-category">
                <input
                  type="text"
                  placeholder="Category name..."
                  value={newCategory}
                  onChange={e => setNewCategory(e.target.value)}
                  onKeyDown={e => e.key === 'Enter' && (e.preventDefault(), handleAddCategory())}
                />
                <button type="button" className="btn-add-category btn-add-category--confirm" onClick={handleAddCategory}>
                  Add
                </button>
                {errors.newCategory && <span className="field-error">{errors.newCategory}</span>}
              </div>
            )}
          </div>
          <div className="form-group">
            <label>Content</label>
            <textarea
              rows={6}
              value={form.content}
              onChange={e => setForm({ ...form, content: e.target.value })}
            />
            {errors.content && <span className="field-error">{errors.content}</span>}
          </div>
          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? 'Publishing...' : 'Publish Article'}
          </button>
        </form>
      </div>
    </div>
  );
}

export default ArticleForm;
