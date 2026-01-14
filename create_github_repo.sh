#!/bin/bash

#########################################
# GitHub Repository Creation Script
# Creates a repository with configurable
# name and Personal Access Token
#########################################

set -e

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_info() {
    echo -e "${BLUE}ℹ${NC} $1"
}

print_success() {
    echo -e "${GREEN}✓${NC} $1"
}

print_error() {
    echo -e "${RED}✗${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}⚠${NC} $1"
}

# Check if GitHub CLI is installed
if ! command -v gh &> /dev/null; then
    print_error "GitHub CLI is not installed."
    echo "Please install it from: https://cli.github.com/"
    exit 1
fi

# Get repository name from parameter or prompt user
if [ -z "$1" ]; then
    echo ""
    read -p "Enter repository name: " REPO_NAME
else
    REPO_NAME=$1
fi

# Validate repository name
if [ -z "$REPO_NAME" ]; then
    print_error "Repository name cannot be empty."
    exit 1
fi

# Get Personal Access Token from parameter or prompt user
if [ -z "$2" ]; then
    echo ""
    read -sp "Enter GitHub Personal Access Token: " PAT
    echo ""
else
    PAT=$2
fi

# Validate PAT
if [ -z "$PAT" ]; then
    print_error "Personal Access Token cannot be empty."
    exit 1
fi

echo ""
print_info "Starting GitHub repository creation process..."
echo ""

# Authenticate with GitHub using PAT
print_info "Authenticating with GitHub CLI..."
echo "$PAT" | gh auth login --with-token

if [ $? -eq 0 ]; then
    print_success "Authentication successful"
else
    print_error "Authentication failed. Please check your Personal Access Token."
    exit 1
fi

echo ""

# Create local directory
if [ -d "$REPO_NAME" ]; then
    print_warning "Directory '$REPO_NAME' already exists. Skipping directory creation."
else
    print_info "Creating local directory..."
    mkdir -p "$REPO_NAME"
    print_success "Directory created: $REPO_NAME"
fi

cd "$REPO_NAME"

echo ""

# Initialize git repository if not already initialized
if [ -d ".git" ]; then
    print_warning "Git repository already initialized. Skipping git init."
else
    print_info "Initializing local git repository..."
    git init
    print_success "Git repository initialized"
fi

echo ""

# Check if remote already exists
if git remote get-url origin &> /dev/null; then
    print_warning "Remote 'origin' already exists. Skipping remote creation."
else
    print_info "Creating GitHub repository..."

    # Create repository on GitHub with SSH protocol
    gh repo create "$REPO_NAME" --source=. --remote=origin --push 2>/dev/null || {
        print_error "Failed to create repository on GitHub."
        exit 1
    }

    print_success "GitHub repository created"
fi

echo ""

# Configure git to use SSH
print_info "Configuring git to use SSH..."
REMOTE_URL=$(git remote get-url origin)

# If remote is using HTTPS, convert to SSH
if [[ $REMOTE_URL == https://github.com* ]]; then
    # Extract username and repo from HTTPS URL
    USERNAME=$(echo "$REMOTE_URL" | sed 's|https://github.com/\([^/]*\)/.*|\1|')
    REPO=$(echo "$REMOTE_URL" | sed 's|.*/\([^/]*\)\.git|\1|')

    # Set to SSH URL
    SSH_URL="git@github.com:$USERNAME/$REPO.git"
    git remote set-url origin "$SSH_URL"
    print_success "Remote URL changed to SSH: $SSH_URL"
else
    print_success "Remote already using SSH: $REMOTE_URL"
fi

echo ""

# Create initial README if it doesn't exist
if [ ! -f "README.md" ]; then
    print_info "Creating initial README.md..."
    cat > README.md << EOF
# $REPO_NAME

## Description
Add your project description here.

## Getting Started
Add setup instructions here.

## License
MIT
EOF
    print_success "README.md created"
else
    print_warning "README.md already exists. Skipping."
fi

echo ""

# Stage and commit if there are changes
if [ -n "$(git status --porcelain)" ]; then
    print_info "Staging and committing initial files..."
    git add .
    git commit -m "Initial commit"
    print_success "Files committed"

    echo ""

    # Push to remote
    print_info "Pushing to GitHub..."
    git push -u origin main 2>/dev/null || git push -u origin master 2>/dev/null || {
        print_error "Failed to push to GitHub."
        exit 1
    }
    print_success "Pushed to GitHub"
else
    print_warning "No changes to commit."
fi

echo ""
echo -e "${GREEN}================================${NC}"
print_success "Repository creation complete!"
echo -e "${GREEN}================================${NC}"
echo ""

# Display repository information
print_info "Repository Details:"
echo "  Name: $REPO_NAME"
echo "  Location: $(pwd)"
echo "  Remote: $(git remote get-url origin)"
echo ""

# Display verification commands
print_info "Verification Commands:"
echo "  git remote -v"
echo "  git log --oneline"
echo ""

print_success "You can now start working on your repository!"
