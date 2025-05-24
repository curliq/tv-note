# About

A simple TV show tracking mobile app. It uses TMDB for all the media.

You can download for $2.99 or host your own. There are currently over 100 people using the app, and I'm planning to
maintain the server forever. There is also an export button in the settings screen.

PRs are welcome.

<p>
  <a href="https://apps.apple.com/gb/app/tv-note/id6504262750" target="_blank">
    <img src="https://github.com/user-attachments/assets/d361b7fe-9b7e-42c4-978b-31b3544bf2ed" alt="Image 1" height="80">
  </a>
  <a href="https://play.google.com/store/apps/details?id=com.free.tvtracker" target="_blank">
    <img src="https://github.com/user-attachments/assets/05be4ffe-7695-4367-96a6-ab99a2802141" alt="Image 2" height="80">
  </a>
</p>

#### App Store Screenshots

<p>
    <img src="https://github.com/user-attachments/assets/a855533b-697e-474d-9b2b-6b6929ea73a9" height="250"/>
    <img src="https://github.com/user-attachments/assets/c1460031-39bd-4db2-ba4e-c4f89abf75df" height="250"/>
    <img src="https://github.com/user-attachments/assets/0ff66655-7516-4013-90da-09ae2eef6306" height="250"/>
    <img src="https://github.com/user-attachments/assets/c094eb48-9384-49ab-9adb-ff2a3e648194" height="250"/>
    <img src="https://github.com/user-attachments/assets/13a44c9d-639b-4e78-baa7-44ed3090948b" height="250"/>
    <img src="https://github.com/user-attachments/assets/991c56b4-ac00-4a04-8447-e11919959f12" height="250"/>
    <img src="https://github.com/user-attachments/assets/65a5fa7b-22bb-4729-be86-9b7d70e71448" height="250"/>
    <img src="https://github.com/user-attachments/assets/f5f37289-77c9-4c96-97e8-a055e95d3c56" height="250"/>
</p>

# Roadmap

- [ ] Add custom lists
- [ ] Sync with Trakt.tv
- [ ] Sharing lists (design tbd)
- [ ] App-wide setting to show only TV shows or only Movies
- [ ] Region picker override setting
- [ ] Filters in discover screen, eg. by genre

# Running locally

### Web app

1. Install java if you haven't already
2. Set environment variables:
   ```
   export BOOT_JWT_PRIVATE_KEY="your_random_key"
   export BOOT_TMDB_TOKEN="your_tmdb_token"
   export GOOGLE_APPLICATION_CREDENTIALS="firebase_json_file_for_push_notifications"
   export BOOT_KEY_DSN_SENTRY=sentry_key
   export BOOT_DATABASE_USERNAME=your_db_username
   export BOOT_DATABASE_PASSWORD=your_db_pw
   export BOOT_DATABASE_URL=jdbc:postgresql://your_db_address 
   export PORT=8080
   ```
3. Run `./gradlew bootRun`
4. Open http://localhost:8080/ and check if running (should show 403 page)
5. Install postgres on your system
6. Create a database and run the migrations (./gradlew flywayMigrate)
7. Update environment variables to your database credentials
8. Run the app again

### Android app

1. Download Android Studio (or the android sdk executable)
2. Set the private keys as environment variables:
   ```
      export ANDROID_KEY_DSN_SENTRY=optional_sentry_key
      export ANDROID_KEY_POSTHOG=optional_posthog_key
      export ANDROID_SERVER_URL=ip_of_your_tvnote_api
      export ANDROID_SERVER_PORT=8080
      export ANDROID_OMDB_TOKEN=optional_omdb_token
      export ANDROID_RAPID_API_IMDB_TOKEN=optional_tapid_api_token
   ```
3. Run `./gradlew assembleDebug`
4. Install the apk on your android device

### iOS app

1. Download XCode
2. Open the `./opsApp` folder in XCode
3. Copy Secrets.plist.example into Secrets.plist and fill your keys.
4. Run the app on your ios device

You need to have a TMDB API key, you can get one [here](https://www.themoviedb.org/settings/api).

# Tech

I started this project with two goals:

- Experiment with a Compose Multiplatform app while keeping the experience as native as possible.
- Track the TV shows I watch.

### Android

The Android codebase is fairly straightforward, there are two differences from an android-only codebase

1. having expect/actual interfaces instead of just `interface`.
2. Using multiplatform libraries over the jvm/android industry standard libraries:
    - Ktor instead of Retrofit.
    - Landscapist (image loader) instead of coil/glide/etc.
    - SQLdelight instead of room.
    - Kotlinx serialization instead of Gson.
    - Certain compose multiplatform APIs are different than android compose APIs, eg Res.drawable codegen instead of
      R.drawable cml vectors.
    - Sentry was particularly confusing to implement into the 3 builds

### iOS

The iOS codebase uses SwiftUI for the navigation components because this was the only way to have the native animation
when opening a screen.
Although I wasn't able to achieve the topbar and navbar native interactions with scrollable content (ie the blur
background transition), I'm not super happy with this on iOS, but it's
a [compose issue](https://youtrack.jetbrains.com/issue/CMP-4944).

I've found other quirks with Compose on iOS, such as keyboard responsiveness and rendering delays, but compose-ios is
indeed in alpha.

Overall compose multiplatform was definitely time-efficient, but the user experience compromises are significant. If I
was to build a production app I would still use Kotlin multiplatform, but consume the view state in swift and build all
the iOS UI in SwiftUI to ensure a native UX while still not duplicating most of the codebase.

### Spring boot (web api)

The only impact to the web project was extracting the API types to a different Gradle module that I can share with the
Compose project, which is really nice. With multiple projects I would have had to copy paste these models.
