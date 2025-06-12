# AnuKit

> A small functional-style Java utility library for safer and expressive programming.

AnuKit provides a minimal set of functional tools and abstractions inspired by Rust's `Result`, functional chains, and safe exception handling.

## Features

- ✅ `Result<T, E>` for expressive success/error states
- 🔁 `Pipe<T>` for fluent, exception-safe transformations
- ⚙️ `AnuKit` utilities for:
    - Safe exception wrapping (`tryWrap`, `safeMap`)
    - Asynchronous execution (`tryWrapAsync`, `runAsync`)
    - Stream transformation with error-safe mapping

## Getting Started

### Installation (via JitPack)

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
  <groupId>com.github.anugrahatworkE</groupId>
  <artifactId>AnuKit</artifactId>
  <version>v0.0.2</version>
</dependency>
```

### Example

```java
Pipe.of("anu")
    .then(String::toUpperCase)
    .then(s -> s + "-KIT")
    .getResult()
    .unwrapOrThrow(); // → "ANU-KIT"
```

## Modules

### `Result<T, E>`

Handles success or error values explicitly.

```java
Result<String, Exception> result = AnuKit.tryWrap(() -> "ok!");
```

### `Pipe<T>`

Safe transformation pipeline with graceful error propagation.

### `AnuKit`

Functional helpers and async-safe execution flows.

## License

MIT © 2025 Anugrah Atwork
