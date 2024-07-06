package com.free.tvtracker

import android.app.Application
import android.content.Context
import android.util.Log
import com.free.tvtracker.di.appModules
import com.free.tvtracker.ui.details.DetailsViewModel
import com.free.tvtracker.ui.discover.DiscoverViewModel
import com.free.tvtracker.ui.finished.FinishedShowsViewModel
import com.free.tvtracker.ui.person.PersonViewModel
import com.free.tvtracker.ui.search.AddTrackedViewModel
import com.free.tvtracker.ui.settings.SettingsViewModel
import com.free.tvtracker.ui.settings.login.LoginViewModel
import com.free.tvtracker.ui.settings.signup.SignupViewModel
import com.free.tvtracker.ui.splash.SplashViewModel
import com.free.tvtracker.ui.watching.WatchingViewModel
import com.free.tvtracker.ui.watchlist.WatchlistedShowsViewModel
import com.free.tvtracker.ui.welcome.WelcomeViewModel
import com.posthog.android.PostHogAndroid
import com.posthog.android.PostHogAndroidConfig
import io.sentry.android.core.SentryAndroid
import io.sentry.android.core.SentryAndroidOptions
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module


class AndroidApplication : Application() {
    companion object {
        lateinit var context: Context
    }

    @Suppress("KotlinConstantConditions")
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        if (BuildConfig.ANDROID_KEY_POSTHOG == "null") {
            Log.i(
                "SETUP",
                "Posthog key is missing. Use `export ANDROID_KEY_POSTHOG=key` to set it, then make sure AS/IDEA has" +
                    " access to the environment variables, one way is to run it from the same terminal session as " +
                    "you set the env var, ie `nohup idea &`"
            )
        } else {
            val config = PostHogAndroidConfig(
                apiKey = BuildConfig.ANDROID_KEY_POSTHOG,
                host = "https://eu.i.posthog.com",
                captureScreenViews = true,
                captureApplicationLifecycleEvents = true,
            ).apply {
                sessionReplay = false
                debug = BuildConfig.DEBUG
            }
            PostHogAndroid.setup(this, config)
        }
        if (BuildConfig.KEY_DSN_SENTRY == "null") {
            Log.i(
                "SETUP",
                "Sentry key is missing. Use `export KEY_DSN_SENTRY=key` to set it, then make sure AS/IDEA has" +
                    " access to the environment variables, one way is to run it from the same terminal session as " +
                    "you set the env var, ie `nohup idea &`"
            )
        } else {
            SentryAndroid.init(this) { options: SentryAndroidOptions ->
                options.dsn = BuildConfig.KEY_DSN_SENTRY
            }
        }
        startKoin {
            modules(appModules())
            modules(
                module {
                    viewModel { SplashViewModel(get(), get()) }
                    viewModel { WelcomeViewModel(get(), get()) }
                    viewModel { AddTrackedViewModel(get(), get(), get(), get(), get()) }
                    viewModel { WatchingViewModel(get(), get(), get(), get()) }
                    viewModel { FinishedShowsViewModel(get(), get(), get(), get()) }
                    viewModel { WatchlistedShowsViewModel(get(), get(), get(), get()) }
                    viewModel { DetailsViewModel(get(), get(), get(), get(), get()) }
                    viewModel { PersonViewModel(get(), get()) }
                    viewModel { LoginViewModel(get(), get()) }
                    viewModel { SignupViewModel(get()) }
                    single {
                        // shared on TvTrackerTheme for all activities
                        SettingsViewModel(get(), get(), get())
                    }
                    single {
                        // why `single` and not `viewmodel`? to share it
                        // between the discover and recommendations activities
                        DiscoverViewModel(get(), get(), get(), get(), get())
                    }
                }
            )
        }
    }
}
