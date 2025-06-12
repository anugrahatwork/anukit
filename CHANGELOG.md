# Changelog

## [v0.0.2] - 2025-06-12
### Added
- `Pipe<T>`: Fluent chaining wrapper with result-safe transformation and error handling.
- `Result<T, E>`: Rust-style result type supporting `ok`, `err`, and `none` states.
- `AnuKit`: Core utility class including:
    - Functional interfaces (`CheckedSupplier`, `SafeTransformer`, `Modifier`, etc.)
    - Safe execution methods: `tryWrap`, `safeMap`, and their async counterparts
    - `ResultStreamHolder`: Stream-safe wrapper for functional list transformation

### Changed
- Improved naming conventions to follow functional and Rust-inspired style.
- Renamed `getOk()` and `getErr()` to `Ok()` and `Err()` respectively for clarity.

### Tests
- Added comprehensive unit tests for:
    - `Pipe`
    - `Result`
    - `AnuKit` and its interfaces

---