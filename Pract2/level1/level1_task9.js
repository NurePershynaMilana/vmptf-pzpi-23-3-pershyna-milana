// Рівень 1
const quotesJson = JSON.stringify({
  categories: ['Мотивація', 'Мудрість', 'Кохання', 'Успіх', 'Природа'],
  quotes: [
    { category: 'Мотивація', text: 'Єдиний спосіб зробити велику роботу — любити те, що ти робиш.', author: 'Стів Джобс' },
    { category: 'Мотивація', text: 'Не чекай. Ніколи не буде «правильного» часу.', author: 'Наполеон Гілл' },
    { category: 'Мотивація', text: 'Вставай. Ти маєш більше можливостей, ніж думаєш.', author: 'Невідомий' },
    { category: 'Мудрість',  text: 'Пізнай самого себе.', author: 'Сократ' },
    { category: 'Мудрість',  text: 'Мудра людина говорить, бо їй є що сказати; дурень — бо треба щось сказати.', author: 'Платон' },
    { category: 'Мудрість',  text: 'Чим більше я знаю, тим більше я знаю, що нічого не знаю.', author: 'Сократ' },
    { category: 'Кохання',   text: 'Кохання — це не дивитися одне на одного, а дивитися разом в одному напрямку.', author: 'Антуан де Сент-Екзюпері' },
    { category: 'Кохання',   text: 'Серце кохає тих, хто робить його щасливим.', author: 'Паскаль' },
    { category: 'Успіх',     text: 'Успіх — це йти від невдачі до невдачі, не втрачаючи ентузіазму.', author: 'Вінстон Черчілль' },
    { category: 'Успіх',     text: 'Ваш час обмежений, тому не витрачайте його, живучи чужим життям.', author: 'Стів Джобс' },
    { category: 'Природа',   text: 'У кожній прогулянці з природою людина отримує набагато більше, ніж шукає.', author: 'Джон Мюір' },
    { category: 'Природа',   text: 'Земля не успадкована від предків — вона позичена у дітей.', author: 'Антуан де Сент-Екзюпері' }
  ]
});

const data = JSON.parse(quotesJson);

const catSelect   = document.getElementById('cat-select');
const quoteText   = document.getElementById('quote-text');
const quoteAuthor = document.getElementById('quote-author');
const quoteTag    = document.getElementById('quote-tag');
const btnNext     = document.getElementById('btn-next');

let currentFiltered = [];
let currentIndex = 0;

data.categories.forEach(cat => {
  const opt = document.createElement('option');
  opt.value = cat;
  opt.textContent = cat;
  catSelect.appendChild(opt);
});

function showQuote() {
  if (!currentFiltered.length) {
    quoteText.textContent = 'Немає цитат у цій категорії.';
    quoteAuthor.textContent = '';
    quoteTag.textContent = '';
    btnNext.style.display = 'none';
    return;
  }
  const q = currentFiltered[currentIndex];
  quoteText.textContent   = `«${q.text}»`;
  quoteAuthor.textContent = `— ${q.author}`;
  quoteTag.textContent    = q.category;
  btnNext.style.display   = currentFiltered.length > 1 ? 'block' : 'none';
}

function loadCategory(cat) {
  currentFiltered = data.quotes.filter(q => q.category === cat);
  currentFiltered.sort(() => Math.random() - 0.5);
  currentIndex = 0;
  showQuote();
}

catSelect.addEventListener('change', () => loadCategory(catSelect.value));

btnNext.addEventListener('click', () => {
  currentIndex = (currentIndex + 1) % currentFiltered.length;
  showQuote();
});

loadCategory(data.categories[0]);