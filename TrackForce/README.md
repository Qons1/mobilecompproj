# TrackForce

TrackForce is an Android application for employee tracking and task management, built with modern Android development practices.

## Features

- Employee attendance tracking with location
- Task management with geolocation support
- Real-time data synchronization
- Offline support
- Battery and network status monitoring

## Tech Stack

- Kotlin
- MVVM Architecture
- Firebase (Auth, Firestore)
- Hilt for dependency injection
- WorkManager for background tasks
- Coroutines & Flow for async operations
- Location Services

## Setup

1. Clone the repository
2. Open the project in Android Studio
3. Create a Firebase project and add `google-services.json` to the app directory
4. Build and run the project

## Requirements

- Android Studio Arctic Fox or newer
- JDK 17
- Android SDK 34
- Google Play Services
- Firebase project

## Building

```bash
# Clean and build the project
./gradlew clean build

# Run tests
./gradlew test

# Build debug APK
./gradlew assembleDebug
```

## Project Structure

- `app/src/main/java/com/example/trackforce/`
  - `activities/` - UI Activities
  - `data/models/` - Data models
  - `repository/` - Data repositories
  - `viewmodels/` - ViewModels
  - `workers/` - Background workers
  - `utils/` - Utility classes

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details 