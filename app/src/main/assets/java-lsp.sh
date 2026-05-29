set -e

source "$LOCAL/bin/utils"

info 'Preparing...'
apt update && apt upgrade -y
apt install -y curl ca-certificates tar

JDTLS_URL="https://download.eclipse.org/jdtls/snapshots/jdt-language-server-latest.tar.gz"
INSTALL_DIR="$HOME/.lsp/java"

install() {
  info 'Installing Eclipse JDT Language Server (jdtls)...'

  if ! command -v java >/dev/null 2>&1; then
    info 'Installing java...'
    apt install -y default-jdk
  fi

  rm -rf "$INSTALL_DIR"
  mkdir -p "$INSTALL_DIR"
  cd "$INSTALL_DIR"

  info "Downloading jdtls..."
  curl -L -o jdtls.tar.gz "$JDTLS_URL"

  info "Extracting..."
  tar -xzf jdtls.tar.gz
  rm jdtls.tar.gz

  if [ -d "./bin" ]; then
    chmod +x bin/jdtls
  fi

  date +%Y-%m-%d > version.txt

  info 'Java LSP (jdtls) installed successfully.'
  exit 0
}

uninstall() {
  info 'Uninstalling Java LSP (jdtls)...'

  rm -rf "$INSTALL_DIR"

  info 'jdtls uninstalled successfully.'
  exit 0
}

update() {
  info 'Updating Java LSP (jdtls)...'
  install
}

case "$1" in
  --uninstall) uninstall;;
  --update) update;;
  *) install;;
esac