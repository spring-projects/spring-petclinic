# 🧠 PR Review Memory System

Automatyczny system uczenia się z historii review'ów PR w GitHub, który wykorzystuje embeddingi Claude i FAISS do dostarczania kontekstowych sugestii review'ów.

## 🎯 Jak to działa

1. **Zbieranie wiedzy**: Każdy review PR → embeddingi → przechowywane w gałęzi `memory`
2. **Automatyczne review**: Nowy PR → wyszukiwanie podobnych przypadków → generowanie sugestii przez Claude

## 🚀 Setup

### 1. GitHub Secrets

Dodaj w Settings → Secrets and variables → Actions:

```bash
ANTHROPIC_API_KEY=your_claude_api_key_here
```

### 2. Utworzenie gałęzi memory

```bash
# Automatycznie tworzona przez pierwszego workflow, ale można stworzyć ręcznie:
git checkout --orphan memory
git rm -rf .
mkdir memory_data
echo '{"embeddings": [], "metadata": {"version": "1.0"}}' > memory_data/metadata.json
git add memory_data/metadata.json
git commit -m "Initialize memory branch"
git push origin memory
```

### 3. Permissions

W Settings → Actions → General → Workflow permissions:
- ✅ **Read and write permissions**
- ✅ **Allow GitHub Actions to create and approve pull requests**

## 📋 Workflow'y

### `collect-reviews.yml`
**Trigger**: `pull_request_review` + `pull_request_review_comment`
- Ekstraktuje komentarze z review'ów
- Tworzy embeddingi przez Claude API
- Zapisuje w FAISS index na gałęzi `memory`

### `auto-review.yml`  
**Trigger**: `pull_request` (opened/synchronize)
- Analizuje zmiany w PR
- Wyszukuje podobne przypadki z memory
- Generuje kontekstowe sugestie przez Claude
- Postuje komentarze na PR

## 🛠 Komponenty

### Core Files
- `scripts/claude_embeddings.py` - Claude API integration + embedding management
- `scripts/faiss_memory_manager.py` - FAISS vector storage + similarity search
- `scripts/extract_review.py` - Ekstrakcja review'ów z GitHub events
- `scripts/generate_review.py` - Generowanie automatycznych review'ów
- `scripts/post_review.py` - Postowanie komentarzy na GitHub

### Memory Structure
```
memory/ branch:
├── memory_data/
│   ├── faiss.index          # FAISS vector index
│   └── metadata.json        # Review metadata + mappings
```

## 🔧 Konfiguracja

### Environment Variables
```bash
GITHUB_TOKEN=automatic        # Provided by GitHub Actions
ANTHROPIC_API_KEY=required    # Your Claude API key
PR_NUMBER=automatic          # From GitHub event
REPO_NAME=automatic          # From GitHub context
```

### Customization

W `claude_embeddings.py`:
- **Similarity threshold**: `min_similarity=0.3` (30%)
- **Embedding dimension**: `dimension=768`
- **Top results**: `top_k=5`

W `generate_review.py`:
- **Chunk size**: `max_lines=20`
- **Claude model**: `claude-3-sonnet-20240229`
- **Max tokens**: `300`

## 📊 Przykład działania

### 1. Review submission
```
PR #123: "Fix null pointer exception"
Review: "Should validate input parameters before processing"
→ Embedding created → Stored in memory
```

### 2. New PR analysis  
```
PR #456: Similar null handling code
→ Finds similar pattern (85% similarity)
→ Suggests: "Consider validating input parameters (based on past review by @senior-dev)"
```

### 3. Comment format
```markdown
This method should validate the payment object for null values before processing

---
🤖 This suggestion is based on 2 similar past review(s) by @senior-dev (similarity: 85%)
📋 Related areas: security, validation
```

## 🎯 Features

- **Smart chunking**: Dzieli duże diff'y na smaller reviewable chunks
- **Context awareness**: Uwzględnia filename, PR title, kod context
- **Tag extraction**: Automatyczne tagowanie (security, performance, style, etc.)
- **Similarity filtering**: Tylko relevantne sugestie (>30% similarity)
- **Fallback handling**: General comments jeśli inline fails
- **Batch processing**: Efficient embedding storage
- **Memory persistence**: Incrementally updated knowledge base

## 🚨 Troubleshooting

### Workflow nie działa?
1. Sprawdź czy `ANTHROPIC_API_KEY` jest ustawiony
2. Verify repo permissions (write access)
3. Check czy memory branch exists

### Brak sugestii?
1. Memory może być pusta (first PRs)
2. Low similarity threshold
3. Check Claude API quota/limits

### Error w komentarzach?
1. PR może być za duży (rate limiting)
2. File paths changed between review i post
3. GitHub API rate limits

## 📈 Monitoring

### Memory stats
```bash
git checkout memory
python -c "
from scripts.faiss_memory_manager import FAISSMemoryManager
m = FAISSMemoryManager()
m.load_from_files('memory_data/faiss.index', 'memory_data/metadata.json')
print(m.get_stats())
"
```

### Workflow logs
- GitHub Actions → Repository → Actions tab
- Check individual workflow runs for detailed logs

## 🔒 Security Notes

- Claude API key stored as GitHub Secret
- Memory branch contains only embeddings, nie raw code
- No sensitive data in embeddings (hashed IDs)
- GitHub token auto-scoped to repository

## 🚀 Rozszerzenia

### Możliwe ulepszenia:
1. **Custom tagging** - Team-specific categories
2. **Reviewer weighting** - Trust scores based on experience  
3. **File type awareness** - Different models per language
4. **Integration with IDE** - VSCode extension
5. **Analytics dashboard** - Review patterns visualization
6. **A/B testing** - Compare human vs AI review effectiveness