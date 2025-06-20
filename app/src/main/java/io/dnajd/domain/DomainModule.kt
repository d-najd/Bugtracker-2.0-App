package io.dnajd.domain

import com.google.gson.GsonBuilder
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import io.dnajd.data.google_auth.RemoteGoogleAuthRepository
import io.dnajd.data.project.MockProjectRepository
import io.dnajd.data.project.RemoteProjectRepository
import io.dnajd.data.project_table.MockProjectTableRepository
import io.dnajd.data.project_table.RemoteProjectTableRepository
import io.dnajd.data.table_task.MockTableTaskRepository
import io.dnajd.data.table_task.RemoteTableTaskRepository
import io.dnajd.data.user_authority.MockUserAuthorityRepository
import io.dnajd.data.user_authority.RemoteUserAuthorityRepository
import io.dnajd.data.utils.JwtAuthenticator
import io.dnajd.domain.google_auth.service.GoogleAuthRepository
import io.dnajd.domain.project.service.ProjectRepository
import io.dnajd.domain.project_table.service.ProjectTableRepository
import io.dnajd.domain.table_task.service.TableTaskRepository
import io.dnajd.domain.user_authority.service.UserAuthorityRepository
import io.dnajd.domain.utils.MutableListTypeAdapterFactory
import io.dnajd.util.BugtrackerDateFormat
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.InjektModule
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.addSingletonFactory
import uy.kohesive.injekt.api.get

class DomainModule : InjektModule {
	companion object {
		private const val USE_MOCKS = false
	}

	override fun InjektRegistrar.registerInjectables() {
		val loggingInterceptor = HttpLoggingInterceptor()
		loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
		addSingletonFactory {
			OkHttpClient
				.Builder()
				.addInterceptor(loggingInterceptor)
				.addInterceptor(JwtAuthenticator)
				.build()
		}

		addSingletonFactory {
			GsonBuilder()
				.setDateFormat(
					BugtrackerDateFormat
						.defaultRequestDateFormat()
						.toPattern()
				)
				.registerTypeAdapterFactory(MutableListTypeAdapterFactory())
				.create()
		}

		addSingletonFactory {
			Retrofit
				.Builder()
				.addConverterFactory(GsonConverterFactory.create(Injekt.get()))
				.addCallAdapterFactory(ResultCallAdapterFactory.create())
				.client(Injekt.get<OkHttpClient>())
		}

		addSingletonFactory {
			Injekt
				.get<Retrofit.Builder>()
				.build()
		}

		when (USE_MOCKS) {
			true -> {
				addSingletonFactory<GoogleAuthRepository> { RemoteGoogleAuthRepository }
				addSingletonFactory<ProjectRepository> { MockProjectRepository }
				addSingletonFactory<ProjectTableRepository> { MockProjectTableRepository }
				addSingletonFactory<TableTaskRepository> { MockTableTaskRepository }
				addSingletonFactory<UserAuthorityRepository> { MockUserAuthorityRepository }
			}

			false -> {
				addSingletonFactory<GoogleAuthRepository> { RemoteGoogleAuthRepository }
				addSingletonFactory<ProjectRepository> { RemoteProjectRepository }
				addSingletonFactory<ProjectTableRepository> { RemoteProjectTableRepository }
				addSingletonFactory<TableTaskRepository> { RemoteTableTaskRepository }
				addSingletonFactory<UserAuthorityRepository> { RemoteUserAuthorityRepository }
			}
		}
	}
}
