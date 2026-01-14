# GitHub Repository Creation Script - Usage Guide

## Overview
`create_github_repo.sh` is an automated Bash script that creates a GitHub repository directly from your terminal. It handles authentication, local setup, remote configuration, and initial commits with minimal user input.

---

## Prerequisites

Before using this script, ensure you have:

### 1. GitHub CLI Installed
Check if GitHub CLI is installed:
```bash
gh --version
```

If not installed, download from: https://cli.github.com/

**Installation Examples:**

**macOS (Homebrew):**
```bash
brew install gh
```

**Ubuntu/Debian:**
```bash
sudo apt-get install gh
```

**Windows (Chocolatey):**
```bash
choco install gh
```

### 2. Git Installed
```bash
git --version
```

### 3. GitHub Personal Access Token
You'll need a Personal Access Token (PAT) for authentication. See [Creating a Personal Access Token](#creating-a-personal-access-token) below.

---

## Creating a Personal Access Token

### Step-by-Step Guide:

1. Go to: https://github.com/settings/tokens
2. Click **"Generate new token"** → **"Generate new token (classic)"**
3. Fill in the details:
   - **Note:** Give it a descriptive name (e.g., "CLI Script Token")
   - **Expiration:** Choose an expiration period
   - **Scopes:** Select the following:
     - ✅ `repo` (Full control of private repositories)
     - ✅ `workflow` (Update GitHub Action workflows)
     - ✅ `write:packages` (Upload packages to Package Registry)
     - ✅ `admin:public_key` (Manage SSH keys)

4. Click **"Generate token"**
5. **Copy the token immediately** (you won't be able to see it again)
6. Store it securely (consider using a password manager)

---

## Usage Methods

### Method 1: Interactive Mode (Most Secure)
Run the script without arguments and enter information when prompted:
```bash
./create_github_repo.sh
```

**What happens:**
1. You'll be prompted for repository name
2. You'll be prompted for Personal Access Token (input is hidden)
3. Rest of the process is automated

### Method 2: Command-Line Arguments
Pass both repository name and PAT as arguments:
```bash
./create_github_repo.sh "my-awesome-project" "ghp_xxxxxxxxxxxxxxxxxxxx"
```

**Note:** Your PAT will be visible in shell history. Use interactive mode for sensitive environments.

### Method 3: Repository Name Only
Pass only the repository name, get prompted for PAT:
```bash
./create_github_repo.sh "my-awesome-project"
```

---

## Installation

### Step 1: Download the Script
The script should be in your project directory. If not, copy it:
```bash
cp create_github_repo.sh ~/path/to/your/projects/
```

### Step 2: Make it Executable
```bash
chmod +x create_github_repo.sh
```

### Step 3: Run the Script
```bash
./create_github_repo.sh
```

---

## Practical Examples

### Example 1: Create a Personal Portfolio Repository
```bash
./create_github_repo.sh "personal-portfolio"
```
Then enter your Personal Access Token when prompted.

### Example 2: Create a Backend API Project
```bash
./create_github_repo.sh "my-api-backend" "ghp_1a2b3c4d5e6f7g8h9i0j1k2l3m4n5o6p"
```

### Example 3: Create a React App
```bash
./create_github_repo.sh
# Enter: my-react-app
# Enter: your_personal_access_token
```

---

## What the Script Does

### Automated Steps:

1. **Validates Prerequisites**
   - Checks if GitHub CLI is installed
   - Validates repository name and PAT

2. **Authenticates**
   - Uses provided PAT to login to GitHub CLI

3. **Creates Local Directory**
   - Makes a new folder with repository name
   - Skips if directory already exists

4. **Initializes Git**
   - Initializes local git repository
   - Skips if already initialized

5. **Creates GitHub Repository**
   - Creates remote repository on GitHub
   - Automatically uses SSH configuration

6. **Configures SSH**
   - Converts HTTPS URLs to SSH (if needed)
   - Ensures secure key-based authentication

7. **Initial Setup**
   - Creates README.md with template
   - Makes first commit
   - Pushes to GitHub

---

## Output and Verification

### Expected Output:
```
ℹ Starting GitHub repository creation process...

ℹ Authenticating with GitHub CLI...
✓ Authentication successful

ℹ Creating local directory...
✓ Directory created: my-repo

ℹ Initializing local git repository...
✓ Git repository initialized

ℹ Creating GitHub repository...
✓ GitHub repository created

ℹ Configuring git to use SSH...
✓ Remote URL changed to SSH: git@github.com:username/my-repo.git

ℹ Creating initial README.md...
✓ README.md created

ℹ Staging and committing initial files...
✓ Files committed

ℹ Pushing to GitHub...
✓ Pushed to GitHub

================================
✓ Repository creation complete!
================================

ℹ Repository Details:
  Name: my-repo
  Location: /Users/username/my-repo
  Remote: git@github.com:username/my-repo.git

ℹ Verification Commands:
  git remote -v
  git log --oneline

✓ You can now start working on your repository!
```

### Verify Repository Creation:
```bash
# Check remote URL
git remote -v

# View commit history
git log --oneline

# View repository status
git status
```

---

## Troubleshooting

### Issue 1: "GitHub CLI is not installed"
**Solution:** Install GitHub CLI
```bash
# macOS
brew install gh

# Ubuntu/Debian
sudo apt-get install gh

# For other systems, visit: https://cli.github.com/
```

### Issue 2: "Authentication failed. Please check your Personal Access Token"
**Solutions:**
- Verify the PAT is correct and not expired
- Generate a new PAT at: https://github.com/settings/tokens
- Ensure the PAT has required scopes: `repo`, `workflow`

### Issue 3: "Failed to create repository on GitHub"
**Possible causes:**
- Repository with that name already exists on your GitHub account
- PAT doesn't have `repo` scope permissions
- Network connectivity issues

**Solution:** Try with a different repository name or check PAT permissions

### Issue 4: SSH Key Not Found
**Error:** `Permission denied (publickey)`

**Solution:** Set up SSH keys
```bash
# Generate SSH key
ssh-keygen -t ed25519 -C "your_email@example.com"

# Add SSH key to GitHub: https://github.com/settings/keys
```

### Issue 5: Script Permission Denied
**Error:** `permission denied: ./create_github_repo.sh`

**Solution:** Make script executable
```bash
chmod +x create_github_repo.sh
```

### Issue 6: Repository Already Exists Locally
**Solution:** The script will skip existing directories. To start fresh:
```bash
rm -rf my-repo
./create_github_repo.sh "my-repo"
```

---

## Advanced Usage

### Using with Environment Variables
Store your PAT in an environment variable for security:
```bash
export GITHUB_PAT="ghp_xxxxxxxxxxxxxxxxxxxx"
./create_github_repo.sh "my-repo" "$GITHUB_PAT"
```

### Batch Create Multiple Repositories
Create a list of repositories:
```bash
#!/bin/bash
repos=("project-1" "project-2" "project-3")
for repo in "${repos[@]}"; do
    ./create_github_repo.sh "$repo" "$GITHUB_PAT"
done
```

### Integrate with Project Templates
After script completes, add your project structure:
```bash
./create_github_repo.sh "my-project"
cd my-project

# Add your template structure
mkdir src tests docs
touch src/main.py tests/test_main.py

git add .
git commit -m "Add project structure"
git push
```

---

## Security Best Practices

### ✅ Do's:
- ✅ Use interactive mode (`./create_github_repo.sh`) for sensitive environments
- ✅ Store PAT in environment variables or password managers
- ✅ Rotate your PAT regularly
- ✅ Use PATs with minimal required scopes
- ✅ Set expiration dates on PATs

### ❌ Don'ts:
- ❌ Don't share your PAT in chat, email, or version control
- ❌ Don't commit PAT to git repositories
- ❌ Don't use the same PAT across multiple machines
- ❌ Don't pass PAT as command-line argument in production scripts (it appears in shell history)

---

## Features Summary

| Feature | Details |
|---------|---------|
| **SSH Configuration** | Automatically uses SSH for secure authentication |
| **Configurable Name** | Specify repository name via argument or prompt |
| **Configurable Token** | Provide PAT via argument or secure prompt |
| **Auto-initialization** | Sets up git, creates README, makes first commit |
| **Error Handling** | Validates prerequisites and handles edge cases |
| **Color Output** | Clear visual feedback with colored status messages |
| **Idempotent** | Can run multiple times on same directory safely |

---

## Useful Commands After Creation

```bash
# Navigate to repository
cd my-repo

# View repository information
git remote -v
git log --oneline
git status

# Add more files
echo "# My Project" > CONTRIBUTING.md
git add CONTRIBUTING.md
git commit -m "Add contributing guidelines"
git push

# Create a new branch
git checkout -b feature/new-feature
# Make changes...
git push -u origin feature/new-feature

# View on GitHub
gh repo view --web
```

---

## Getting Help

### Script Help
For GitHub CLI help:
```bash
gh help repo create
gh auth --help
```

### Report Issues
If you encounter issues:
1. Check the [Troubleshooting](#troubleshooting) section
2. Verify all prerequisites are installed
3. Check GitHub CLI authentication: `gh auth status`

### Useful Links
- [GitHub CLI Documentation](https://cli.github.com/manual)
- [Personal Access Token Creation](https://github.com/settings/tokens)
- [SSH Key Setup](https://docs.github.com/en/authentication/connecting-to-github-with-ssh)
- [Git Documentation](https://git-scm.com/doc)

---

## License
This script is provided as-is for automation purposes.
