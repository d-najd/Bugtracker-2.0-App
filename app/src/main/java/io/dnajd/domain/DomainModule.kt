package io.dnajd.domain

import com.google.gson.GsonBuilder
import io.dnajd.data.project.FakeProjectRepository
import io.dnajd.data.project.RemoteProjectRepository
import io.dnajd.data.project_table.FakeProjectTableRepository
import io.dnajd.data.project_table.RemoteProjectTableRepository
import io.dnajd.data.table_task.FakeTableTaskRepository
import io.dnajd.data.table_task.RemoteTableTaskRepository
import io.dnajd.data.user_authority.FakeUserAuthorityRepository
import io.dnajd.data.user_authority.RemoteUserAuthorityRepository
import io.dnajd.data.utils.Urls
import io.dnajd.domain.project.interactor.CreateProject
import io.dnajd.domain.project.interactor.DeleteProject
import io.dnajd.domain.project.interactor.GetProject
import io.dnajd.domain.project.interactor.RenameProject
import io.dnajd.domain.project.service.ProjectRepository
import io.dnajd.domain.project_table.interactor.*
import io.dnajd.domain.project_table.service.ProjectTableRepository
import io.dnajd.domain.table_task.interactor.*
import io.dnajd.domain.table_task.service.TableTaskRepository
import io.dnajd.domain.user_authority.interactor.CreateUserAuthority
import io.dnajd.domain.user_authority.interactor.DeleteUserAuthority
import io.dnajd.domain.user_authority.interactor.GetUserAuthorities
import io.dnajd.domain.user_authority.service.UserAuthorityRepository
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
                .baseUrl(Urls.API.baseUrlLocal)
                .addConverterFactory(GsonConverterFactory.create(Injekt.get()))
                .client(Injekt.get())
        }

        addSingletonFactory {
            Injekt.get<Retrofit.Builder>()
                .build()
        }

        when (USE_FAKES) {
            true -> {
                addSingletonFactory<ProjectRepository> { FakeProjectRepository }
                addSingletonFactory<ProjectTableRepository> { FakeProjectTableRepository }
                addSingletonFactory<TableTaskRepository> { FakeTableTaskRepository }
                addSingletonFactory<UserAuthorityRepository> { FakeUserAuthorityRepository }
            }
            false -> {
                addSingletonFactory<ProjectRepository> { RemoteProjectRepository }
                addSingletonFactory<ProjectTableRepository> { RemoteProjectTableRepository }
                addSingletonFactory<TableTaskRepository> { RemoteTableTaskRepository }
                addSingletonFactory<UserAuthorityRepository> { RemoteUserAuthorityRepository }
            }
        }

        addFactory { GetProject(get()) }
        addFactory { CreateProject(get()) }
        addFactory { RenameProject(get()) }
        addFactory { DeleteProject(get()) }

        addFactory { GetProjectTable(get()) }
        addFactory { CreateProjectTable(get()) }
        addFactory { RenameProjectTable(get()) }
        addFactory { SwapProjectTables(get()) }
        addFactory { DeleteProjectTable(get()) }

        addFactory { GetTableTask(get()) }
        addFactory { CreateTableTask(get()) }
        addFactory { SwapTableTasks(get()) }
        addFactory { MoveTableTask(get()) }
        addFactory { SwapTableTaskTable(get()) }
        addFactory { UpdateTableTaskDescription(get()) }

        addFactory { GetUserAuthorities(get()) }
        addFactory { CreateUserAuthority(get()) }
        addFactory { DeleteUserAuthority(get()) }
    }
}
