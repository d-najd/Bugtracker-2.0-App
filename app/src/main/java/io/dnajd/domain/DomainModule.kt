package io.dnajd.domain

import com.google.gson.GsonBuilder
import io.dnajd.data.library.FakeLibraryRepository
import io.dnajd.data.library.ProjectRepositoryImpl
import io.dnajd.data.utils.Urls
import io.dnajd.domain.project.interactor.GetProjects
import io.dnajd.domain.project.service.ProjectRepository
import io.dnajd.util.BugtrackerDateFormat
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.*

class DomainModule : InjektModule {
    companion object {
        private const val USE_FAKES = true
    }
    
    override fun InjektRegistrar.registerInjectables() {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        addSingletonFactory {
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        }
        
        addSingletonFactory {
            GsonBuilder()
                .setDateFormat(BugtrackerDateFormat.defaultRequestDateFormat().toPattern())
                .create()
        }

        addSingletonFactory {
            Retrofit.Builder()
                .baseUrl(Urls.API)
                .addConverterFactory(GsonConverterFactory.create())
                .client(Injekt.get())
                .build()
        }
        
        when (USE_FAKES) {
            true -> {
                addSingletonFactory<ProjectRepository> { FakeLibraryRepository }
            }
            false -> {
                addSingletonFactory<ProjectRepository> { ProjectRepositoryImpl }
            }
        }

        addFactory { GetProjects(get()) }
    }
}
