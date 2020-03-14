/*
 * Copyright [2020] [Alvaro Lucas Castillo Calabacero]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cl.ucn.disc.dsm.alccthenews.services.GNews;

import android.os.Build.VERSION_CODES;
import androidx.annotation.RequiresApi;
import cl.ucn.disc.dsm.alccthenews.model.Noticia;
import cl.ucn.disc.dsm.alccthenews.services.NewsApi.NewsApiNoticiaService;
import cl.ucn.disc.dsm.alccthenews.services.NoticiaService;
import cl.ucn.disc.dsm.alccthenews.services.Transform;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GNewsApiNoticiaService implements NoticiaService {

  /**
   * The logger.
   */
  private static final Logger log = LoggerFactory.getLogger(NewsApiNoticiaService.class);

  /**
   * The GNewsAPI.
   */
  private final GNewsApi gnewsApi;

  /**
   * The Constructor.
   */
  public GNewsApiNoticiaService() {

    // Logging with slf4j
    final HttpLoggingInterceptor loggingInterceptor =
        new HttpLoggingInterceptor(log::debug).setLevel(Level.BODY);

    // Web Client
    final OkHttpClient httpClient = new Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .callTimeout(10, TimeUnit.SECONDS)
        .addNetworkInterceptor(loggingInterceptor)
        .build();

    // https://futurestud.io/tutorials/retrofit-getting-started-and-android-client
    this.gnewsApi = new Retrofit.Builder()
        // The main URL
        .baseUrl(GNewsApi.BASE_URL)
        // JSON to POJO
        .addConverterFactory(GsonConverterFactory.create())
        // Validate the interface
        .validateEagerly(true)
        // The client
        .client(httpClient)
        // Build the Retrofit ..
        .build()
        // .. get the NewsApi.
        .create(GNewsApi.class);
  }

  /**
   * Get the Noticias from the Call.
   *
   * @param theCall to use.
   * @return the {@link List} of {@link Noticia}.
   */
  @RequiresApi(api = VERSION_CODES.N)
  private static List<Noticia> getNoticiasFromCall(final Call<GNewsApiResult> theCall) {

    try {

      // Get the result from the call
      final Response<GNewsApiResult> response = theCall.execute();

      // UnSuccessful !
      if (!response.isSuccessful()) {

        // Error!
        throw new GNewsAPIException(
            "Can't get the GNewsResult, code: " + response.code(),
            new HttpException(response)
        );

      }

      final GNewsApiResult theResult = response.body();

      // No body
      if (theResult == null) {
        throw new GNewsAPIException("GNewsResult was null");
      }

      // No articles
      if (theResult.articles == null) {
        throw new GNewsAPIException("Articles in GNewsResult was null");
      }

      // Article to Noticia (transformer)
      return theResult.articles.stream()
          .map(Transform::transform2)
          .collect(Collectors.toList());

    } catch (final IOException ex) {
      throw new GNewsAPIException("Can't get the GNewsResult", ex);
    }

  }

  @RequiresApi(api = VERSION_CODES.N)
  @Override
  public List<Noticia> getNoticias(int pageSize) {
    // the Call
    final Call<GNewsApiResult> theCall = this.gnewsApi.getEverything(pageSize);

    // Process the Call.
    return getNoticiasFromCall(theCall);
  }

  /**
   * The Exception.
   */
  public static final class GNewsAPIException extends RuntimeException {

    public GNewsAPIException(final String message) {
      super(message);
    }

    public GNewsAPIException(final String message, final Throwable cause) {
      super(message, cause);
    }

  }
}
