# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## 1.2.0 (v95) - 2026-08-08
### Added
- Added custom 5-minute initialization timeout for JDT Language Server (jdtls) startup.

### Changed
- Switched to `BuiltinFileType.JAVA` icon for server registration.
- Refactored LSP install script extraction to use temporary directory (`getTempDir()`).
- Updated to latest SDK API
- Changed minAppVersion to 95

### Fixed
- Prevented uninstallation script execution during extension removal if jdtls is not installed.

## 1.1.0 (v87) - 2026-06-24
### Added
- Auto-update support for Eclipse JDT Language Server (jdtls).

### Fixed
- Extension crash when the `onInstalled()` method is missing.

## 1.0.2 (v87) - 2026-06-03
### Fixed
- Java Language Server is now properly removed during extension uninstallation.

## 1.0.1 (v87) - 2026-06-02
### Added
- Implemented `onUpdated()` to ensure resources are cleaned up during extension updates.

### Changed
- Added the requirement for the minimum version.

## 1.0.0 (v87) - 2026-05-31
### Added
- Full Java Language Server Protocol (LSP) integration.
- Project-wide symbol indexing powered by Eclipse JDTLS.
