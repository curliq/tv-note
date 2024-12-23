A simple TV show tracking mobile app using Compose Multiplatform. It uses TMDB for all the media.

You can download for $2.99 or host your own. If you're a user then PRs are welcome.

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

# Tech

The main goal of this project is to experiment with a compose multiplatform app while keeping the experience as native as possible.

### Android

The Android codebase is fairly straightfoward, there are 2 difference from an android-only codebase 
1. having expect/actual interfaces instead of just `interface`.
2. Prefering multiplatform libraries over the jvm/android industry standard libraries:
    - Ktor instead of Retrofit.
    - Landscapist (image loader) instead of coil/glide/etc.
    - Sqldelight instead of room.
    - Kotlinx serialization instead of Gson.
    - Certain compose multiplatform APIs are different than android compose APIs, eg Res.drawable codegen instead of R.drawable cml vectors.
    - Sentry was particularly confusing to implement into the 3 builds

### iOS

The iOS codebase uses SwiftUI for the navigation components because this was the only way to have the native animation when opening a screen.
Although I wasn't able to achieve the topbar and navbar native interactions with scrollable content (ie the blur blackground transition), I'm not super happy with this on iOS but it's a [compose issue](https://youtrack.jetbrains.com/issue/CMP-4944).

I've found other quirks with compose on iOS, such as keyboard responsiveness and rendering delays, but compose-ios is indeed in alpha.

Overall compose multiplatform was definitely time efficient, but the user exprience compromises are significant. If I was to build a production app I would still use Kotlin multiplatform, but consume the view state in swift and build all the iOS UI in SwiftUI to ensure a native UX while still not duplicating most of the codebase.

### Spring boot (web api)

The only impact to the web project was pulling the API models to a different gradle module that I can share with the compose project, which is really nice, I would've copy pasted these models otherwise.
