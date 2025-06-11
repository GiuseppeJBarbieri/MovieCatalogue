# Movie Catalogue App

A modern Android application built with Jetpack Compose that allows users to browse trending movies, view their details, and manage a list of their favorite films.

---

## ✨ Features

* **Trending Movies:** Discover the latest popular movies.
* **Movie Details:** Tap on any movie to view comprehensive information including overview, release date, and poster.
* **Favorite Movies:** Easily add or remove movies from your favorites list.
* **Favorites View:** A dedicated screen to browse all your favorited movies.
* **Reactive UI:** Utilizes Kotlin **Flows** to provide a smooth, real-time user experience as data changes (e.g., favoriting a movie instantly updates its status across the app).

---

## 🛠️ Technologies Used

* **Kotlin:** The primary language for Android development.
* **Jetpack Compose:** Android's modern toolkit for building native UI.
* **Android Architecture Components:**
    * **ViewModel:** Manages UI-related data in a lifecycle-conscious way.
    * **Flow:** Kotlin Coroutines' asynchronous stream for emitting multiple values.
* **Hilt:** A dependency injection library for Android that provides a standard way to incorporate Dagger into an Android app.
* **Kotlin Coroutines:** For asynchronous programming and background operations.
* **Retrofit:** A type-safe HTTP client for Android and Java, used for API communication (e.g., fetching movie data).
* **Coil:** An image loading library for Android, powered by Kotlin Coroutines.
* **Jetpack Navigation Compose:** For managing in-app navigation.

---

## 🏗️ Architecture

The app follows an **MVVM (Model-View-ViewModel)** architectural pattern, separating concerns into distinct layers:

* **View (Composables):** The UI layer, responsible for observing `ViewModel` states and rendering the user interface. It sends user events back to the `ViewModel`.
* **ViewModel:** Exposes **`StateFlow`s** for UI observation, handles UI logic, and interacts with the `Repository` to fetch or persist data.
* **Repository (`MovieRepository`):** An abstraction layer that handles data operations, providing a clean API for the `ViewModel` regardless of whether data comes from a remote API, local database, or preferences. It typically orchestrates data sources.
* **Data Layer (Models, APIs, Local Storage):** Contains data models, network service interfaces (Retrofit), and local data sources (e.g., for favorites).

---

## 🚀 Getting Started

To get a local copy up and running, follow these simple steps.

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/your-username/movie-catalogue-app.git](https://github.com/your-username/movie-catalogue-app.git)
    ```
    (Replace `your-username/movie-catalogue-app.git` with your actual repository URL)
2.  **Open in Android Studio:**
    Open the cloned project in Android Studio (Jellyfish | 2023.3.1 or newer recommended).
3.  **Obtain an API Key:**
    This application requires an API key from The Movie Database (TMDB).
    * Go to [The Movie Database (TMDB) API](https://www.themoviedb.org/documentation/api) and sign up for an account.
    * Generate a new API key.
    * Create a `local.properties` file in the root of your project (same level as `build.gradle.kts` files).
    * Add your API key to `local.properties` like this:
        ```properties
        TMDB_API_KEY="YOUR_API_KEY_HERE"
        ```
        (Replace `YOUR_API_KEY_HERE` with your actual TMDB API key)
4.  **Build and Run:**
    Sync the project with Gradle files and run the app on an Android emulator or a physical device.

---

## 📸 Screenshots (Optional)
![Home](https://github.com/user-attachments/assets/8c51e221-8746-42c0-94cb-dcd059f83e2c)
![Favorites](https://github.com/user-attachments/assets/f0916cbc-8528-466a-b39f-82d38de6fb23)
![Details](https://github.com/user-attachments/assets/46cfc208-34d2-4f0c-8587-c44c86f1485e)

---

## 🛣️ Future Enhancements

* **Search Functionality:** Implement movie search.
* **User Authentication:** Allow users to log in.
* **More Movie Categories:** Explore different movie lists (e.g., popular, top-rated).
* **Testing:** Add unit and UI tests.
* **Favorites:** Show correct favorite icon if movie has already beed favorited on the home view
* **Error Handling:** Ran out of time for some of the extra UI for Error handling on the favorites screen.
* **ROOM IMPL:** Implement ROOM for local database saves instead of using shared preferences.

---

## 🙏 Acknowledgments

* This product uses the TMDB API but is not endorsed or certified by TMDB.
