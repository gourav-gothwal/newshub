# 📰 NewsHub

A modern, feature-rich **News Application** built with **Kotlin** and **XML-based UI**. NewsHub delivers the latest and trending news articles from around the world using the **NewsData.io API**, featuring a stunning UI with smooth animations and robust offline support.

This project demonstrates modern Android development practices including MVVM architecture, Coroutines, Room Database, and Firebase Authentication.

---

## 🚀 Key Features

### 📱 User Interface & Experience
- **Breaking News Carousel**: A visually engaging carousel with smooth scale-and-fade animations for top headlines.
- **Categorized News**: Easy navigation through categories like Technology, Sports, Business, Health, and Entertainment.
- **Modern Dashboard**: A clean, card-based layout with a polished search bar and "Recent News" section.
- **Fluid Animations**: Custom `PageTransformer` for carousel and optimized RecyclerViews.

### 🔐 Authentication & Profile
- **Secure Login**: Integrated **Firebase Authentication** supporting both Email/Password and **Google Sign-In**.
- **Splash Screen**: Smart redirection based on user login state.
- **Profile Management**: View and edit profile details, change password, and secure logout.

### 💾 Data & Offline Support
- **Local Bookmarks**: Save articles for later reading using **Room Database**.
- **Efficient Caching**: Optimized network calls and image loading with **Glide**.
- **Search Functionality**: Real-time search to find specific news topics instantly.

---

## 🛠️ Tech Stack

- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **UI Components**: XML Layouts, ConstraintLayout, ViewPager2, Material Design Components
- **Networking**: Retrofit2 + Gson
- **Image Loading**: Glide
- **Local Database**: Room Database (with KSP)
- **Authentication**: Firebase Auth (Google Sign-In)
- **Concurrency**: Kotlin Coroutines & Flow
- **API Provider**: [NewsData.io](https://newsdata.io/)

---

## 📂 Project Structure

```
NewsHub/
 ├── app/
 │   ├── src/main/java/com/example/testapp/
 │   │   ├── adapters/      # RecyclerView & ViewPager Adapters
 │   │   ├── api/           # Retrofit Service & Client
 │   │   ├── data/          # Room Database, DAOs, Entities, Repositories
 │   │   ├── fragments/     # UI Fragments (Home, Profile, Search)
 │   │   ├── models/        # Data Classes
 │   │   ├── viewmodel/     # ViewModels
 │   │   └── ...            # Activities & Utils
 │   └── res/               # Layouts, Drawables, Values
 └── build.gradle.kts       # Dependencies & Build Config
```

---

## 🔑 Getting Started

### 1. Clone Repository
```bash
git clone https://github.com/gourav-gothwal/newshub.git
cd newshub
```

### 2. Configure API Key
- Sign up for a free API key at [NewsData.io](https://newsdata.io/).
- Add your API key to `local.properties` or `strings.xml` (as `api_key`).

### 3. Firebase Setup
- Create a project in the [Firebase Console](https://console.firebase.google.com/).
- Enable **Authentication** (Email/Password and Google providers).
- Add your Android app (`com.example.testapp`).
- **CRITICAL**: Add your machine's **SHA-1 Fingerprint** to the Firebase Console project settings for Google Sign-In to work.
  - Run `gradlew signingReport` to find your SHA-1.
- Download `google-services.json` and place it in the `app/` directory.

### 4. Build & Run
- Open the project in **Android Studio**.
- Sync Gradle to download dependencies.
- Build and run on an Emulator or Physical Device.

---
## 👥 Contributors

- **Gourav Gothwal** ([@gourav-gothwal](https://github.com/gourav-gothwal))
- **Goutam Singh** ([@Goutam-0810](https://github.com/Goutam-0810))

---

## 📜 License

This project is licensed under the **MIT License**.
