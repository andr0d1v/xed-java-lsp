set -e

source "$LOCAL/bin/utils"

info 'Preparing...'
apt update && apt upgrade -y
apt install -y curl ca-certificates tar

ACTION="install"
JDTLS_VERSION=""

for arg in "$@"; do
  case "$arg" in
    --uninstall) ACTION="uninstall" ;;
    --update)    ACTION="update" ;;
    *)           JDTLS_VERSION="$arg" ;;
  esac
done

JDTLS_URL="https://download.eclipse.org/jdtls/snapshots/$JDTLS_VERSION"
INSTALL_DIR="$HOME/.lsp/java"

execute_installation() {
  if ! command -v java >/dev/null 2>&1; then
    info 'Installing OpenJDK...'
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

  echo "$JDTLS_VERSION" > version.txt
}

install() {
  info 'Installing Eclipse JDT Language Server (jdtls)...'
  execute_installation
  info 'Eclipse JDT Language Server (jdtls) installed successfully.'
  exit 0
}

update() {
  info 'Updating Eclipse JDT Language Server (jdtls)...'
  execute_installation
  info 'Eclipse JDT Language Server (jdtls) updated successfully.'
  exit 0
}

uninstall() {
  info 'Uninstalling Eclipse JDT Language Server (jdtls)...'
  rm -rf "$INSTALL_DIR"
  info 'Eclipse JDT Language Server (jdtls) uninstalled successfully.'
  exit 0
}

case "$ACTION" in
  uninstall) uninstall;;
  update)    update;;
  *)         install;;
esac
