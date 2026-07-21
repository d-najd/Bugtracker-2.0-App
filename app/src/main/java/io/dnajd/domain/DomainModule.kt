package io.dnajd.domain

import com.google.gson.GsonBuilder
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import io.dnajd.data.google_auth.GoogleAuthApiServiceImpl
import io.dnajd.data.jwt_auth.JwtRefreshAuthApiServiceImpl
import io.dnajd.data.project.api.ProjectApiServiceImpl
import io.dnajd.data.project.api.ProjectApiServiceMock
import io.dnajd.data.project_icon.api.ProjectIconApiServiceImpl
import io.dnajd.data.project_table.api.ProjectTableApiServiceImpl
import io.dnajd.data.project_table.api.ProjectTableApiServiceMock
import io.dnajd.data.table_task.api.TableTaskApiServiceImpl
import io.dnajd.data.table_task.api.TableTaskApiServiceMock
import io.dnajd.data.task_comment.api.TaskCommentApiServiceImpl
import io.dnajd.data.user_authority.api.UserAuthorityApiServiceImpl
import io.dnajd.data.user_authority.api.UserAuthorityApiServiceMock
import io.dnajd.data.utils.JwtAuthenticator
import io.dnajd.domain.base.HostnameVerifierAlwaysTrue
import io.dnajd.domain.base.MutableListTypeAdapterFactory
import io.dnajd.domain.google_auth.service.GoogleAuthApiService
import io.dnajd.domain.jwt_auth.service.JwtRefreshAuthApiService
import io.dnajd.domain.project.service.ProjectApiService
import io.dnajd.domain.project_icon.service.ProjectIconApiService
import io.dnajd.domain.project_table.service.ProjectTableApiService
import io.dnajd.domain.table_task.service.TableTaskApiService
import io.dnajd.domain.task_comment.service.TaskCommentApiService
import io.dnajd.domain.user_authority.service.UserAuthorityApiService
import io.dnajd.util.BugtrackerDateFormat
import io.dnajd.util.GsonUTCDateAdapter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.InjektModule
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.addSingletonFactory
import uy.kohesive.injekt.api.get
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class DomainModule : InjektModule {
	companion object {
		private const val USE_MOCKS = false
	}

	override fun InjektRegistrar.registerInjectables() {
		val loggingInterceptor = HttpLoggingInterceptor()
		loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

		// TODO this disabled SSL certificates, since I am getting invalid certificate before an request is EVEN SENT!
		// This should be urgently fixed
		val trustAllCerts = arrayOf<TrustManager>(
			object : X509TrustManager {
				override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
				override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
				override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
			},
		)
		// Install the all-trusting trust manager
		val sslContext = SSLContext.getInstance("SSL")
			.apply {
				init(null, trustAllCerts, SecureRandom())
			}

		addSingletonFactory {
			OkHttpClient
				.Builder()
				.sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
				.hostnameVerifier { _, _ -> true } // Trust all hostnames/IPs
				.addInterceptor(loggingInterceptor)
				.addInterceptor(JwtAuthenticator)
				.hostnameVerifier(HostnameVerifierAlwaysTrue)
				.build()
		}

		addSingletonFactory {
			GsonBuilder()
				.setDateFormat(
					BugtrackerDateFormat
						.defaultRequestDateFormat()
						.toPattern(),
				)
				.registerTypeAdapter(
					Date::class.java,
					GsonUTCDateAdapter(BugtrackerDateFormat.defaultRequestDateFormat()),
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
				addSingletonFactory<GoogleAuthApiService> { GoogleAuthApiServiceImpl }
				addSingletonFactory<JwtRefreshAuthApiService> { JwtRefreshAuthApiServiceImpl }
				addSingletonFactory<ProjectApiService> { ProjectApiServiceMock }
				addSingletonFactory<ProjectTableApiService> { ProjectTableApiServiceMock }
				addSingletonFactory<TableTaskApiService> { TableTaskApiServiceMock }
				addSingletonFactory<TaskCommentApiService> { TaskCommentApiServiceImpl }
				addSingletonFactory<UserAuthorityApiService> { UserAuthorityApiServiceMock }
				addSingletonFactory<ProjectIconApiService> { ProjectIconApiServiceImpl }
			}

			false -> {
				addSingletonFactory<GoogleAuthApiService> { GoogleAuthApiServiceImpl }
				addSingletonFactory<JwtRefreshAuthApiService> { JwtRefreshAuthApiServiceImpl }
				addSingletonFactory<ProjectApiService> { ProjectApiServiceImpl }
				addSingletonFactory<ProjectTableApiService> { ProjectTableApiServiceImpl }
				addSingletonFactory<TableTaskApiService> { TableTaskApiServiceImpl }
				addSingletonFactory<TaskCommentApiService> { TaskCommentApiServiceImpl }
				addSingletonFactory<UserAuthorityApiService> { UserAuthorityApiServiceImpl }
				addSingletonFactory<ProjectIconApiService> { ProjectIconApiServiceImpl }
			}
		}
	}
}
