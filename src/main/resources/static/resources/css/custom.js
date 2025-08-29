// Dark-mode toggle with localStorage persistence (no backend changes)
(function () {
  const ROOT = document.documentElement;         // <html>
  const KEY = 'pc-theme';                        // storage key

  // On first load, apply saved theme if any
  try {
    if (localStorage.getItem(KEY) === 'dark') {
      ROOT.classList.add('dark');
    }
  } catch (_) { /* ignore storage errors */ }

  // Delegate click: works even if button is rendered later
  document.addEventListener('click', (e) => {
    const t = e.target;
    if (!t || t.id !== 'themeToggle') return;
    ROOT.classList.toggle('dark');
    try {
      localStorage.setItem(KEY, ROOT.classList.contains('dark') ? 'dark' : 'light');
    } catch (_) { /* ignore */ }
  });
})();
