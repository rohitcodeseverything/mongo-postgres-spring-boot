# Create a GitHub Repository from Terminal

## Prerequisites
- Git installed on your system
- GitHub account
- GitHub CLI (optional, but recommended) - [Install here](https://cli.github.com/)

---

## Method 1: Using GitHub CLI (Recommended)

### Step 1: Authenticate with GitHub
```bash
gh auth login
```
Follow the prompts to authenticate. Choose:
- GitHub.com
- HTTPS (or SSH if you prefer)
- Authenticate with a web browser

### Step 2: Create Repository Locally
```bash
mkdir my-repository
cd my-repository
git init
```

### Step 3: Create Remote Repository on GitHub
```bash
gh repo create my-repository --source=. --remote=origin --push
```

Replace `my-repository` with your desired repository name.

### Step 4: Add Your First File and Push
```bash
echo "# My Repository" > README.md
git add README.md
git commit -m "Initial commit"
git push origin main
```

---

## Method 2: Manual Setup (Web UI + Terminal)

### Step 1: Create Local Repository
```bash
mkdir my-repository
cd my-repository
git init
```

### Step 2: Create Repository on GitHub Web UI
1. Go to https://github.com/new
2. Enter repository name: `my-repository`
3. Choose visibility (Public or Private)
4. Click "Create repository"

### Step 3: Connect Local Repository to GitHub
Copy the commands from GitHub (they'll look like this):
```bash
git remote add origin https://github.com/YOUR_USERNAME/my-repository.git
git branch -M main
git push -u origin main
```

Replace `YOUR_USERNAME` with your GitHub username.

### Step 4: Add Initial Content
```bash
echo "# My Repository" > README.md
git add README.md
git commit -m "Initial commit"
git push -u origin main
```

---

## Verify Repository Creation
```bash
git remote -v
```

You should see output like:
```
origin  https://github.com/YOUR_USERNAME/my-repository.git (fetch)
origin  https://github.com/YOUR_USERNAME/my-repository.git (push)
```

---

## Useful GitHub CLI Commands
```bash
# List your repositories
gh repo list

# View repository details
gh repo view

# Open repository in browser
gh repo view --web
```

---

## Notes
- Replace `my-repository` with your actual repository name
- Replace `YOUR_USERNAME` with your GitHub username
- Use HTTPS for easier setup, or SSH if you have SSH keys configured
- The `main` branch is the default branch (previously `master`)
