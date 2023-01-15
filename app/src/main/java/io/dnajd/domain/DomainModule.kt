package io.dnajd.domain

import com.google.gson.GsonBuilder
import io.dnajd.data.project.FakeProjectRepository
import io.dnajd.data.project.RemoteProjectRepository
import io.dnajd.data.project_table.FakeProjectTableRepository
import io.dnajd.data.project_table.RemoteProjectTableRepository
import io.dnajd.data.project_table_task.FakeProjectTableTaskRepository
import io.dnajd.data.project_table_task.RemoteProjectTableTaskRepository
import io.dnajd.data.utils.Urls
import io.dnajd.domain.project.interactor.GetProjects
import io.dnajd.domain.project.service.ProjectRepository
import io.dnajd.domain.project_table.interactor.GetProjectTables
import io.dnajd.domain.project_table.interactor.RenameProjectTable
import io.dnajd.domain.project_table.interactor.SwapProjectTablePositions
import io.dnajd.domain.project_table.service.ProjectTableRepository
import io.dnajd.domain.project_table_task.interactor.MoveProjectTableTaskPositions
import io.dnajd.domain.project_table_task.service.ProjectTableTaskRepository
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
                addSingletonFactory<ProjectRepository> { FakeProjectRepository }
                addSingletonFactory<ProjectTableRepository> { FakeProjectTableRepository }
                addSingletonFactory<ProjectTableTaskRepository> { FakeProjectTableTaskRepository }
            }
            false -> {
                addSingletonFactory<ProjectRepository> { RemoteProjectRepository }
                addSingletonFactory<ProjectTableRepository> { RemoteProjectTableRepository }
                addSingletonFactory<ProjectTableTaskRepository> { RemoteProjectTableTaskRepository }
            }
        }

        addFactory { GetProjects(get()) }
        addFactory { GetProjectTables(get()) }
        addFactory { RenameProjectTable(get()) }
        addFactory { SwapProjectTablePositions(get()) }
        addFactory { MoveProjectTableTaskPositions(get()) }
    }
}
