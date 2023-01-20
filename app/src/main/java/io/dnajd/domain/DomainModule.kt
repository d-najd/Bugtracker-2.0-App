package io.dnajd.domain

import com.google.gson.GsonBuilder
import io.dnajd.data.project.FakeProjectRepository
import io.dnajd.data.project.RemoteProjectRepository
import io.dnajd.data.project_table.FakeProjectTableRepository
import io.dnajd.data.project_table.RemoteProjectTableRepository
import io.dnajd.data.project_table_task.FakeProjectTableTaskRepository
import io.dnajd.data.project_table_task.RemoteProjectTableTaskRepository
import io.dnajd.data.utils.Urls
import io.dnajd.domain.project.interactor.CreateProject
import io.dnajd.domain.project.interactor.GetProjects
import io.dnajd.domain.project.service.ProjectRepository
import io.dnajd.domain.project_table.interactor.*
import io.dnajd.domain.project_table.service.ProjectTableRepository
import io.dnajd.domain.project_table_task.interactor.CreateProjectTableTask
import io.dnajd.domain.project_table_task.interactor.GetProjectTableTasks
import io.dnajd.domain.project_table_task.interactor.MoveProjectTableTask
import io.dnajd.domain.project_table_task.interactor.SwapProjectTableTasks
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
        private const val USE_FAKES = false
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
                .addConverterFactory(GsonConverterFactory.create(Injekt.get()))
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
        addFactory { CreateProject(get()) }

        addFactory { GetProjectTables(get()) }
        addFactory { CreateProjectTable(get()) }
        addFactory { RenameProjectTable(get()) }
        addFactory { SwapProjectTables(get()) }
        addFactory { DeleteProjectTable(get()) }

        addFactory { GetProjectTableTasks(get()) }
        addFactory { CreateProjectTableTask(get()) }
        addFactory { SwapProjectTableTasks(get()) }
        addFactory { MoveProjectTableTask(get()) }
    }
}
