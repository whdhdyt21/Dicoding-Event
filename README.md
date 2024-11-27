# Dicoding Event

Dicoding Event is an Android application that helps users discover, save, and participate in various exciting events available on the Dicoding platform. The app is equipped with features to provide an optimal user experience.

## Key Features
- **Upcoming Events**: Display a list of upcoming events.
- **Finished Events**: Display a list of completed events.
- **Favorite**: Save your favorite events using Room Database for offline access.
- **Event Details**: Show detailed information about an event, including:
  - Event location
  - Event schedule
  - Event description
  - **UI Features:**
    - **ImageView** for displaying event cover.
    - **TextView** for event name, organizer name, time, quota, and description.
    - **FloatingActionButton** for adding the event to favorites.
    - **Button** for event registration.
    - **Error Page** with retry functionality.
    - **ProgressBar** for loading indicator.
- **Settings**:
  - Dark Mode for a more comfortable interface at night.
  - Daily Reminder to get notified about new events daily.
- **Search**: Quickly search for events by keyword.

## Prerequisites
Before starting this project, ensure you have installed the following tools:
- Android Studio (at least version Arctic Fox or newer)
- Latest Gradle version
- Java Development Kit (JDK) version 11 or newer

## Initial Setup
1. Clone this repository to your computer:
   ```bash
   git clone https://github.com/whdhdyt21/Dicoding-Event.git
2. Open the project in Android Studio.
3. Add the following property to the `local.properties` file located in the root directory:
   ```makefile
   BASE_URL = "https://event-api.dicoding.dev/"
   ```
   The `local.properties` file is used to store sensitive local configurations and will not be committed to Git.

## Technologies Used
- **Kotlin**: Main programming language for app development.
- **MVVM Architecture**: To separate business logic from the user interface.
- **Retrofit**: For communication with the REST API.
- **Room Database**: For local data storage.
- **LiveData and ViewModel**: For efficient data management.
- **WorkManager**: For daily reminder notifications.
- **Material Design**: To create a modern and responsive user interface.

## How to Run the Project
1. Ensure all prerequisites are installed, and the `local.properties` file is configured.
2. Build and run the project from Android Studio.
3. The application will connect to the **Dicoding Event API** available at `https://event-api.dicoding.dev/`.

## Contribution
Contributions are welcome! If you have ideas or improvements, follow these steps:
1. Fork this repository.
2. Create a new branch for your feature or fix:
   ```bash
   git checkout -b new-feature
   ```
3. Commit your changes:
   ```bash
   git commit -m "Add new feature"
   ```
4. Push to your branch:
   ```bash
   git push origin new-feature
   ```
5. Open a pull request on GitHub.

## License
This project is licensed under the [MIT License](LICENSE).
