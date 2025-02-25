package io.dnajd.domain

import com.google.gson.GsonBuilder
import io.dnajd.data.project.MockProjectRepository
import io.dnajd.data.project.RemoteProjectRepository
import io.dnajd.data.project_table.MockProjectTableRepository
import io.dnajd.data.project_table.RemoteProjectTableRepository
import io.dnajd.data.table_task.MockTableTaskRepository
import io.dnajd.data.table_task.RemoteTableTaskRepository
import io.dnajd.data.user_authority.MockUserAuthorityRepository
import io.dnajd.data.user_authority.RemoteUserAuthorityRepository
import io.dnajd.data.utils.Urls
import io.dnajd.domain.project.interactor.CreateProject
import io.dnajd.domain.project.interactor.DeleteProject
import io.dnajd.domain.project.interactor.GetProject
import io.dnajd.domain.project.interactor.RenameProject
import io.dnajd.domain.project.service.ProjectRepository
import io.dnajd.domain.project_table.interactor.CreateProjectTable
import io.dnajd.domain.project_table.interactor.DeleteProjectTable
import io.dnajd.domain.project_table.interactor.GetProjectTable
import io.dnajd.domain.project_table.interactor.RenameProjectTable
import io.dnajd.domain.project_table.interactor.SwapProjectTables
import io.dnajd.domain.project_table.service.ProjectTableRepository
import io.dnajd.domain.table_task.interactor.CreateTableTask
import io.dnajd.domain.table_task.interactor.GetTableTask
import io.dnajd.domain.table_task.interactor.MoveTableTask
import io.dnajd.domain.table_task.interactor.SwapTableTaskTable
import io.dnajd.domain.table_task.interactor.SwapTableTasks
import io.dnajd.domain.table_task.interactor.UpdateTableTaskDescription
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
import uy.kohesive.injekt.api.InjektModule
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.addFactory
import uy.kohesive.injekt.api.addSingletonFactory
import uy.kohesive.injekt.api.get

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
				addSingletonFactory<ProjectRepository> { MockProjectRepository }
				addSingletonFactory<ProjectTableRepository> { MockProjectTableRepository }
				addSingletonFactory<TableTaskRepository> { MockTableTaskRepository }
				addSingletonFactory<UserAuthorityRepository> { MockUserAuthorityRepository }
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
