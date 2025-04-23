<p align="center">
  <img src="https://github.com/user-attachments/assets/f44d5f11-d178-40bc-849e-94a2c465798f"
</p>

<h1 align="center">🌍 WanderMate - Your Smart Travel Companion</h1>

<p align="center">
 WanderMate is a <strong>Java-based Android travel app</strong> powered by <strong>Firebase</strong>, integrating <strong>Google Maps API</strong> and <strong>OpenWeather API</strong> to help users explore destinations, check weather, and navigate effortlessly.
</p>

---

## 📱 Features

- 🔐 **Authentication**
  - User registration and login using Firebase Authentication.
  - Secure login/logout functionality.

- 🏠 **Home Page**
  - Displays user’s **current location** on top.
  - Showcases **popular destinations** as interactive cards.
  - Clicking a destination opens a **detailed map view** with info.

- 🔎 **Location Search Page**
  - Search any destination by city/place name.
  - Displays:
    - 🗺️ Table of **nearby places**
    - 📍 Table of **popular destinations**
  - Selecting a location fetches and shows it on Google Maps.

- 🌤️ **Weather Page**
  - Integrated with **OpenWeather API**.
  - After searching a city, view:
    - Temperature
    - Humidity
    - Wind speed
    - Weather description

- 🙋‍♀️ **Profile Page**
  - Edit profile (name & email).
  - Switch between **light** and **dark** modes.
  - Change password.
  - Logout functionality.

---

## 🛠️ Built With

- **Language**: Java
- **Framework**: Android SDK (Namespace: `com.example.wandermate`)
- **Database & Auth**: Firebase Realtime Database, Firebase Auth
- **APIs**:
  - Google Maps API
  - Google Places API
  - OpenWeather API
- **Libraries**:
  - UI: Material Components, ConstraintLayout, CardView, ViewPager2, CircleImageView
  - Networking: Retrofit2, OkHttp, GSON
  - Image Loading: Glide

---

## 🧱 Project Setup

### 📦 Clone the Repository

```bash
git clone https://github.com/your-username/wandermate.git
cd wandermate

```
🧰 Build with Gradle
Run
```bash
./gradlew build
```
Or open the project in Android Studio, let Gradle sync automatically, and then run it on an emulator or physical device.

🔑 API Keys Setup
1. Get Your API Keys
Google Maps Platform

OpenWeatherMap

2. Add Keys in local.properties
# local.properties
```bash
GOOGLE_MAPS_API_KEY=your_google_maps_key_here
OPEN_WEATHER_API_KEY=your_openweather_key_here
```

## 📸 Screenshots
<details>
<summary> Click to view app screenshots</summary>

- **Resgister Page**

 ![Register ](https://github.com/user-attachments/assets/9a9137b1-7d6b-48f6-923f-344c41b1d897)

- **Login Page**

 ![Login](https://github.com/user-attachments/assets/d35cdf7c-a85c-4b3c-a096-e3e1eca16928)

- **Home Page** 
  
 ![Home](https://github.com/user-attachments/assets/15323918-5ada-41cf-8c46-a31aec864bfa)

- **Location Search Page**
  
 ![Search](https://github.com/user-attachments/assets/ef8dec37-305f-441b-ab7d-c277d22c1648)


- **Weather Page**
  
 ![weather](https://github.com/user-attachments/assets/40f23acd-da4d-45f4-a7ae-5b97d1ce8d62)


- **Profile Page**

 ![profile](https://github.com/user-attachments/assets/ba32806b-64dc-4bc7-b27e-facad3cd0e65)

</details>
---


## 📃 License
This project is licensed under the MIT License.
See the LICENSE file for details.

## ⭐ Support
If you found this project helpful, don't forget to give it a ⭐ on GitHub.
It helps others discover and use the project too!

## 📬 Contact
If you have any questions, feedback, or just want to say hi — feel free to reach out!

- Developer: @ridmii
- 📧 Email: heyridmi@gmail.com

