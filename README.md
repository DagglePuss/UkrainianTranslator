# Ukrainian Translator

An Android app for translating between Ukrainian and 30+ languages, powered by the [LibreTranslate](https://libretranslate.com) API.

## Features

- **Bidirectional translation** — Translate to and from Ukrainian with support for 30+ languages
- **Auto-detect** — Automatically detect the source language
- **Translation history** — All translations saved locally with Room database
- **Swap languages** — One-tap source/target language swap
- **Copy to clipboard** — Quickly copy translated text
- **Ukrainian-first** — Ukrainian is always listed first in language selectors
- **Dark mode** — Full light and dark theme support
- **Dynamic color** — Material You dynamic theming on Android 12+

## Screenshots

*Coming soon*

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM (ViewModel + StateFlow) |
| Networking | Retrofit + OkHttp |
| Local Storage | Room |
| Dependency Injection | Hilt |
| Navigation | Navigation Compose |
| Async | Kotlin Coroutines + Flow |

## Theme

The app uses a Ukrainian-themed color palette:

- **Blue** `#0057B7` — Primary color
- **Yellow** `#FFD700` — Accent color

## Project Structure

```
app/src/main/java/com/ukrainiantranslator/
├── data/
│   ├── local/          # Room database, DAO, entities
│   ├── remote/         # Retrofit API interface, DTOs
│   └── repository/     # Repository implementation
├── domain/model/       # Domain models (Language, TranslationResult)
├── di/                 # Hilt dependency injection modules
├── ui/
│   ├── screens/
│   │   ├── translate/  # Translation screen + ViewModel
│   │   └── history/    # History screen + ViewModel
│   ├── navigation/     # Navigation graph + screen routes
│   └── theme/          # Material 3 theme (colors, typography)
└── util/               # NetworkResult, Constants, ClipboardHelper
```

## Building

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34

### Build

```bash
./gradlew assembleDebug
```

The APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

## API

This app uses the [LibreTranslate](https://libretranslate.com) open-source translation API. The base URL is configurable via `LIBRE_TRANSLATE_BASE_URL` in `app/build.gradle.kts` — you can point it at a self-hosted instance.

## License

MIT
