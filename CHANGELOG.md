# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
