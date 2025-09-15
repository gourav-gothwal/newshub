# 📰 NewsHub

A modern **News Application** built with **Kotlin (XML-based UI)** that delivers the latest and trending news articles from around the world.  
NewsHub integrates **Trivia News API** for fetching real-time content, with support for **offline storage**, **bookmarks**, and **article sharing**.  

This project is developed collaboratively by contributors as a portfolio-ready Android app.

---

## 🚀 Features

- 🌍 **Fetch Latest News** – Powered by [Trivia News API](https://rapidapi.com/trivia/api/trivia-news/).
- 🔐 **Secure Login** – Integrated **Firebase Authentication** (Email/Password, Google Sign-In).
- 💾 **Offline Support** – Local persistence with **Room Database**.
- ☁️ **Cloud Storage** – Sync and backup using **Firestore**.
- 📌 **Bookmark Articles** – Save favorite articles for later.
- 📤 **Share News** – Share interesting articles with friends.
- 🔎 **Search & Filter** – Quickly find the news you care about.
- 📱 **Clean UI** – Built with XML layouts for simplicity and performance.
- 📰 **Categorized News sections** - Divisions based on categories like Sports, Tech, Politics, etc.

---

## 🛠️ Tech Stack

- **Language**: Kotlin  
- **UI**: XML Layouts  
- **Database**: Room Database  
- **Backend Services**: Firebase Authentication, Firestore  
- **API**: Trivia News API  
- **Architecture**: MVVM (Model-View-ViewModel)  
- **Other**: Retrofit, Glide/Picasso (for images), Coroutines, LiveData  

---

## 📂 Project Structure

```
NewsHub/
 ├── app/
 │   ├── data/          # Repositories, Room Entities, DAO
 │   ├── network/       # Retrofit API services
 │   ├── ui/            # Activities, Fragments, Adapters, ViewModels
 │   ├── utils/         # Helper classes, extensions
 │   └── ...
 └── build.gradle
```

---

## 🔑 Getting Started

### 1. Clone Repository
```bash
git clone https://github.com/gourav-gothwal/newshub.git
cd newshub
```

### 2. Add API Key
- Sign up for **Trivia News API** on RapidAPI.
- Add your API key in `local.properties`:
  ```properties
  NEWS_API_KEY="your_api_key_here"
  ```

### 3. Firebase Setup
- Add your app to Firebase console.
- Download `google-services.json` and place it in `/app` folder.

### 4. Build & Run
- Open project in **Android Studio**.
- Sync Gradle and run on emulator/device.

---

## 📸 Screenshots

Coming Soon

---

## 👥 Contributors

- Gourav Gothwal (https://github.com/gourav-gothwal)  
- Goutam Singh (https://github.com/Goutam-0810)  

---

## 📜 License

This project is licensed under the **MIT License** – see the [LICENSE](LICENSE) file for details.

---

## 💡 Future Enhancements

- 🔔 Push Notifications for breaking news.  
- 🎨 Dark Mode support.  
- 🌐 Multi-language support.  

---
