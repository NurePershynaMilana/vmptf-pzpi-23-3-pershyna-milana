// Рівень 2
const EXAMPLE = {
  users: [
    { id: 1, name: 'Аліса Коваль',    age: 28, active: true,  role: 'admin',  tags: ['js', 'react'] },
    { id: 2, name: 'Богдан Мороз',    age: 34, active: false, role: 'user',   tags: ['python'] },
    { id: 3, name: 'Христина Лис',    age: 22, active: true,  role: 'editor', tags: ['design', 'ux'] }
  ],
  meta: { total: 3, page: 1, version: '2.0', created: null }
};

let currentTab = 'tree';
let parsedData = null;

document.getElementById('btn-visualize').addEventListener('click', visualize);
document.getElementById('btn-example').addEventListener('click', loadExample);
document.getElementById('btn-clear').addEventListener('click', clearAll);

document.querySelectorAll('.view-tab').forEach(btn => {
  btn.addEventListener('click', () => switchTab(btn.dataset.tab));
});

function loadExample() {
  document.getElementById('json-input').value = JSON.stringify(EXAMPLE, null, 2);
}

function clearAll() {
  document.getElementById('json-input').value = '';
  document.getElementById('output-area').innerHTML =
    '<div class="placeholder"><div>Введіть JSON та натисніть «Візуалізувати»</div></div>';
  document.getElementById('stats').style.display = 'none';
  parsedData = null;
}

function visualize() {
  const raw = document.getElementById('json-input').value.trim();
  if (!raw) return;

  try {
    parsedData = JSON.parse(raw);
  } catch (e) {
    document.getElementById('output-area').innerHTML =
      `<div class="error-box">❌ Недійсний JSON: ${e.message}</div>`;
    document.getElementById('stats').style.display = 'none';
    parsedData = null;
    return;
  }

  renderTab(currentTab);
  updateStats(parsedData, raw);
}

function switchTab(tab) {
  currentTab = tab;
  document.querySelectorAll('.view-tab').forEach(t => t.classList.remove('active'));
  document.getElementById('tab-' + tab).classList.add('active');
  if (parsedData !== null) renderTab(tab);
}

function renderTab(tab) {
  const area = document.getElementById('output-area');

  if (tab === 'tree') {
    area.innerHTML = '';
    const div = document.createElement('div');
    div.className = 'tree-root';
    renderTree(parsedData, div, null);
    area.appendChild(div);
  } else if (tab === 'table') {
    renderTable(parsedData, area);
  } else {
    area.innerHTML =
      `<pre style="color:var(--text);font-family:var(--mono);font-size:0.82rem;line-height:1.65;padding:0;white-space:pre-wrap;">${syntaxHighlight(JSON.stringify(parsedData, null, 2))}</pre>`;
  }
}

function syntaxHighlight(json) {
  return json.replace(
    /("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(?:\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g,
    match => {
      if (/^"/.test(match)) {
        return /:$/.test(match)
          ? `<span class="key-name">${match}</span>`
          : `<span class="val-string">${match}</span>`;
      }
      if (/true|false/.test(match)) return `<span class="val-bool">${match}</span>`;
      if (/null/.test(match))       return `<span class="val-null">${match}</span>`;
      return `<span class="val-number">${match}</span>`;
    }
  );
}

function renderTree(data, container, key) {
  const row = document.createElement('div');
  row.style.lineHeight = '1.8';

  const keySpan = key !== null
    ? `<span class="key-name">"${key}"</span><span class="val-bracket">: </span>`
    : '';

  if (data === null) {
    row.innerHTML = `${keySpan}<span class="val-null">null</span>`;
    container.appendChild(row);
  } else if (typeof data === 'string') {
    row.innerHTML = `${keySpan}<span class="val-string">"${escapeHtml(data)}"</span>`;
    container.appendChild(row);
  } else if (typeof data === 'number') {
    row.innerHTML = `${keySpan}<span class="val-number">${data}</span>`;
    container.appendChild(row);
  } else if (typeof data === 'boolean') {
    row.innerHTML = `${keySpan}<span class="val-bool">${data}</span>`;
    container.appendChild(row);
  } else if (Array.isArray(data)) {
    const id = 'n' + Math.random().toString(36).slice(2);
    row.innerHTML = `<button class="toggle-btn" data-target="${id}">▾</button>${keySpan}<span class="val-bracket">[</span><span class="type-pill type-array">${data.length} items</span>`;
    container.appendChild(row);
    const children = document.createElement('div');
    children.className = 'tree-node collapsible';
    children.id = id;
    data.forEach((item, i) => renderTree(item, children, i));
    const closing = document.createElement('div');
    closing.innerHTML = '<span class="val-bracket">]</span>';
    container.appendChild(children);
    container.appendChild(closing);
  } else if (typeof data === 'object') {
    const id = 'n' + Math.random().toString(36).slice(2);
    const keys = Object.keys(data);
    row.innerHTML = `<button class="toggle-btn" data-target="${id}">▾</button>${keySpan}<span class="val-bracket">{</span><span class="type-pill type-object">${keys.length} keys</span>`;
    container.appendChild(row);
    const children = document.createElement('div');
    children.className = 'tree-node collapsible';
    children.id = id;
    keys.forEach(k => renderTree(data[k], children, k));
    const closing = document.createElement('div');
    closing.innerHTML = '<span class="val-bracket">}</span>';
    container.appendChild(children);
    container.appendChild(closing);
  }
}

document.getElementById('output-area').addEventListener('click', e => {
  const btn = e.target.closest('.toggle-btn');
  if (!btn) return;
  const el = document.getElementById(btn.dataset.target);
  if (!el) return;
  el.classList.toggle('collapsed');
  btn.textContent = el.classList.contains('collapsed') ? '▸' : '▾';
});

function renderTable(data, area) {
  const arr = Array.isArray(data)
    ? data
    : (typeof data === 'object' && data !== null ? [data] : null);

  if (!arr || !arr.length || typeof arr[0] !== 'object') {
    area.innerHTML =
      '<div class="placeholder"><div>Таблиця доступна лише для масивів об\'єктів.</div></div>';
    return;
  }

  const keys = [...new Set(arr.flatMap(row => Object.keys(row || {})))];
  let html = '<div class="table-wrap"><table><thead><tr>';
  keys.forEach(k => { html += `<th>${escapeHtml(k)}</th>`; });
  html += '</tr></thead><tbody>';

  arr.forEach(row => {
    html += '<tr>';
    keys.forEach(k => {
      const val = row[k];
      const display =
        val === null          ? '<span class="val-null">null</span>'
        : typeof val === 'boolean' ? `<span class="val-bool">${val}</span>`
        : typeof val === 'object'  ? `<span class="val-bracket">${JSON.stringify(val)}</span>`
        : escapeHtml(String(val));
      html += `<td>${display}</td>`;
    });
    html += '</tr>';
  });

  html += '</tbody></table></div>';
  area.innerHTML = html;
}

function updateStats(data, raw) {
  let keys = 0, arrays = 0, depth = 0;

  function walk(obj, d) {
    if (d > depth) depth = d;
    if (Array.isArray(obj)) {
      arrays++;
      obj.forEach(v => walk(v, d + 1));
    } else if (obj && typeof obj === 'object') {
      const k = Object.keys(obj);
      keys += k.length;
      k.forEach(key => walk(obj[key], d + 1));
    }
  }

  walk(data, 0);
  document.getElementById('stat-keys').textContent   = keys;
  document.getElementById('stat-arrays').textContent = arrays;
  document.getElementById('stat-depth').textContent  = depth;
  document.getElementById('stat-size').textContent   = new Blob([raw]).size + ' bytes';
  document.getElementById('stats').style.display     = 'flex';
}

function escapeHtml(str) {
  return String(str)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;');
}