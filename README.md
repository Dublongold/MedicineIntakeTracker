# Medicine Intake Tracker

A modern Android application for tracking medicine intake, built with **Jetpack Compose** following **Clean Architecture** principles.

## Features

### Medicine Management
- Add, edit, and delete medicines with custom details
- Choose from 5 visual icons (Tablet, Bandage, Syringe, Blisters, Bottle)
- Set prescribed daily intake limits per medicine

### Intake Tracking
- Log medicine intakes with automatic date/time recording
- Smart validation: enforces minimum 2-hour intervals between intakes
- Respects prescribed daily limits with configurable grace periods
- Quick-add for regular medicines on the home screen

### History & Statistics
- View complete intake history organized by date
- **General Statistics**: Total intakes, best streak medicine, top adherence rates
- **Per-Medicine Analytics**:
  - Adherence percentage
  - Best consecutive streak
  - Average intakes per day
  - Interval duration analysis

### Customization
- **Themes**: System, Light, or Dark mode
- **Languages**: English, Ukrainian, Russian (with system default)
- **Yesterday Hours**: Configure when "yesterday" ends (for late-night intakes)
- **Grace Period**: Customizable window for intake validation

## Tech Stack

| Layer | Technology |
|-------|------------|
| **UI** | Jetpack Compose, Material3, Adaptive Navigation Suite |
| **Architecture** | Clean Architecture (Domain / Data / UI modules) |
| **DI** | Koin |
| **Navigation** | Navigation3 (Kotlin Serialization) |
| **Build** | Kotlin DSL, Gradle Version Catalogs |
| **Min SDK** | 26 (Android 8.0) |

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│                         UI Layer                        │
│  (Jetpack Compose + ViewModels + Navigation3)            │
├─────────────────────────────────────────────────────────┤
│                       Domain Layer                      │
│  (Entities, Use Cases, Repository Interfaces)            │
├─────────────────────────────────────────────────────────┤
│                        Data Layer                       │
│  (Repository Implementations, DataSources, Mappers)     │
└─────────────────────────────────────────────────────────┘
```

## Screenshots

> _Screenshots to be added_

## Getting Started

### Prerequisites
- Android Studio Ladybug or newer
- JDK 17+

### Build & Run
```bash
./gradlew :app:assembleDebug
```

Or open in Android Studio and click **Run**.

## Project Structure

```
MedicineIntakeTracker/
├── app/                    # UI layer (Compose screens, ViewModels)
├── domain/                 # Business logic (entities, use cases)
├── data/                   # Data layer (repositories, persistence)
└── gradle/
    └── libs.versions.toml  # Version catalog
```

## License

```
Copyright (c) 2026

Licensed under the Apache License, Version 2.0
```

---

Built with ❤️ using Jetpack Compose and Clean Architecture.
