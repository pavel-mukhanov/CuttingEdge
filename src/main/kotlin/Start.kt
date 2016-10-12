import com.github.salomonbrys.kotson.jsonObject
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable
import java.util.*

/**
 * Created by pavelmukhanov on 27/08/16.
 */

interface GitHubService {
    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String): Observable<List<Repo>>
}

data class Repo(val name: String)

fun main(args: Array<String>) {
    Observable
            .fromCallable { 1 }
            .subscribe { println(it) }

    val obj: JsonObject = jsonObject(
            "name" to "kotson",
            "creation" to Date().time,
            "files" to 4)

    println("my very first kotson object: " + obj)

    val retrofit = Retrofit
            .Builder()
            .baseUrl("https://api.github.com/")
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val service = retrofit.create(GitHubService::class.java)

    service
            .listRepos("octocat")
            .flatMap { Observable.from(it) }
            .subscribe {
                println(it)
            }
}

