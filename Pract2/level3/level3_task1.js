// Рівень 3
const usersJson = JSON.stringify([
  { id: 1, name: 'Олена Шевченко',    email: 'o.shevchenko@mail.ua',  role: 'Маркетолог',          group: 'ЦА',               joined: '01.03.2024' },
  { id: 2, name: 'Андрій Бондаренко', email: 'a.bondarenko@biz.ua',   role: 'Підприємець',          group: 'Постійні клієнти', joined: '15.11.2023' },
  { id: 3, name: 'Катерина Іваненко', email: 'k.ivanenko@corp.ua',    role: 'HR-менеджер',          group: 'ЦА',               joined: '22.07.2024' },
  { id: 4, name: 'Максим Гриценко',   email: 'm.hrytsenko@dev.ua',    role: 'Розробник',            group: 'Інші',             joined: '05.01.2024' },
  { id: 5, name: 'Тетяна Лисенко',    email: 't.lysenko@shop.ua',     role: 'Власниця магазину',    group: 'Постійні клієнти', joined: '10.09.2023' },
  { id: 6, name: 'Іван Коваленко',    email: 'i.kovalenko@start.ua',  role: 'CEO Startup',          group: 'ЦА',               joined: '28.02.2024' }
]);

let users = JSON.parse(usersJson);
let nextId = Math.max(...users.map(u => u.id)) + 1;
let activeFilter = 'all';
let editingId = null;

const grid    = document.getElementById('users-grid');
const search  = document.getElementById('search');
const overlay = document.getElementById('modal-overlay');

document.getElementById('btn-add').addEventListener('click', () => openModal());
document.getElementById('btn-cancel').addEventListener('click', closeModal);
document.getElementById('btn-save').addEventListener('click', saveUser);
overlay.addEventListener('click', e => { if (e.target === overlay) closeModal(); });
search.addEventListener('input', render);

document.querySelectorAll('.filter-btn').forEach(btn => {
  btn.addEventListener('click', () => {
    document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    activeFilter = btn.dataset.filter;
    render();
  });
});

function getInitials(name) {
  return name.split(' ').slice(0, 2).map(w => w[0]).join('').toUpperCase();
}

function groupClass(group) {
  if (group === 'ЦА')               return 'group-ca';
  if (group === 'Постійні клієнти') return 'group-reg';
  return 'group-other';
}

function avatarColor(name) {
  const colors = ['#1a1714', '#c05621', '#276749', '#6b46c1', '#2b6cb0', '#9b2335'];
  let h = 0;
  for (const c of name) h = (h * 31 + c.charCodeAt(0)) % colors.length;
  return colors[h];
}

function render() {
  const q = search.value.toLowerCase();

  const filtered = users.filter(u => {
    const matchFilter = activeFilter === 'all' || u.group === activeFilter;
    const matchSearch = u.name.toLowerCase().includes(q) || u.email.toLowerCase().includes(q);
    return matchFilter && matchSearch;
  });

  document.getElementById('stat-total').textContent = users.length;
  document.getElementById('stat-ca').textContent    = users.filter(u => u.group === 'ЦА').length;
  document.getElementById('stat-reg').textContent   = users.filter(u => u.group === 'Постійні клієнти').length;
  document.getElementById('stat-other').textContent = users.filter(u => u.group === 'Інші').length;

  if (!filtered.length) {
    grid.innerHTML = '<div class="empty-state"><div>Нічого не знайдено</div></div>';
    return;
  }

  grid.innerHTML = '';

  filtered.forEach(u => {
    const card = document.createElement('div');
    card.className = 'user-card';
    card.innerHTML = `
      <div class="card-top">
        <div class="avatar" style="background:${avatarColor(u.name)}">${getInitials(u.name)}</div>
        <div class="card-info">
          <div class="user-name">${u.name}</div>
          <div class="user-email">${u.email}</div>
        </div>
        <span class="group-tag ${groupClass(u.group)}">${u.group}</span>
      </div>
      <div class="card-divider"></div>
      <div class="card-meta">
        <div class="meta-item"><div class="meta-label">Посада</div>${u.role}</div>
        <div class="meta-item"><div class="meta-label">Приєднався</div>${u.joined}</div>
      </div>
      <div class="card-actions">
        <button class="action-btn" data-edit="${u.id}">Редагувати</button>
        <button class="action-btn danger" data-delete="${u.id}">Видалити</button>
      </div>
    `;
    grid.appendChild(card);
  });

  grid.querySelectorAll('[data-edit]').forEach(btn => {
    btn.addEventListener('click', () => editUser(Number(btn.dataset.edit)));
  });

  grid.querySelectorAll('[data-delete]').forEach(btn => {
    btn.addEventListener('click', () => deleteUser(Number(btn.dataset.delete)));
  });
}

function deleteUser(id) {
  users = users.filter(u => u.id !== id);
  render();
}

function editUser(id) {
  const u = users.find(u => u.id === id);
  if (!u) return;
  editingId = id;
  document.getElementById('modal-title').textContent = 'Редагувати користувача';
  document.getElementById('f-name').value  = u.name;
  document.getElementById('f-email').value = u.email;
  document.getElementById('f-role').value  = u.role;
  document.getElementById('f-group').value = u.group;
  overlay.classList.add('open');
}

function openModal() {
  editingId = null;
  document.getElementById('modal-title').textContent = 'Новий користувач';
  document.getElementById('f-name').value  = '';
  document.getElementById('f-email').value = '';
  document.getElementById('f-role').value  = '';
  document.getElementById('f-group').value = 'ЦА';
  overlay.classList.add('open');
}

function closeModal() {
  overlay.classList.remove('open');
  editingId = null;
}

function saveUser() {
  const name  = document.getElementById('f-name').value.trim();
  const email = document.getElementById('f-email').value.trim();
  const role  = document.getElementById('f-role').value.trim();
  const group = document.getElementById('f-group').value;

  if (!name || !email) {
    alert('Заповніть імʼя та email');
    return;
  }

  if (editingId !== null) {
    const idx = users.findIndex(u => u.id === editingId);
    users[idx] = { ...users[idx], name, email, role, group };
  } else {
    const today  = new Date();
    const joined = [
      String(today.getDate()).padStart(2, '0'),
      String(today.getMonth() + 1).padStart(2, '0'),
      today.getFullYear()
    ].join('.');
    users.push({ id: nextId++, name, email, role, group, joined });
  }

  closeModal();
  render();
}

render();