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

package cl.ucn.disc.dsm.alccthenews.services.TheGuardianApi;

import android.os.Build.VERSION_CODES;
import androidx.annotation.RequiresApi;
import cl.ucn.disc.dsm.alccthenews.model.Noticia;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TheGuardianNoticiaService {

  private final TheGuardianApi theGuardianApi;

  public TheGuardianNoticiaService() {
    // https://futurestud.io/tutorials/retrofit-getting-started-and-android-client
    this.theGuardianApi = new Retrofit.Builder()
        // The main URL
        .baseUrl(TheGuardianApi.BASE_URL)
        // JSON to POJO
        .addConverterFactory(GsonConverterFactory.create())
        // Validate the interface
        .validateEagerly(true)
        // Build the Retrofit ..
        .build()
        // .. get the Content.
        .create(TheGuardianApi.class);

  }

  @RequiresApi(api = VERSION_CODES.N)
  public List<Noticia> getNoticiasFromCall(Call<TheGuardianResult> theCall) {

    try {
      // Get the result from the call
      final Response<TheGuardianResult> response = theCall.execute();

      // UnSuccessful !
      if (!response.isSuccessful()) {

        // Error!
        throw new TheGuardianAPIException(
            "Can't get the NewsResult, code: " + response.code(),
            new HttpException(response)
        );

      }

      final TheGuardianResult theResult = response.body();

      // No body
      if (theResult == null) {
        throw new TheGuardianAPIException("ContentResult was null");
      }

      // No articles
      if (theResult.response == null) {
        throw new TheGuardianAPIException("Content in NewsResult was null");
      }

      // Article to Noticia (transformer)
      return theResult.response.results.stream().map(ResultNoticiaTransformer::transform)
          .collect(Collectors.toList());

    } catch (final IOException ex) {
      throw new TheGuardianAPIException("Can't get the NewsResult", ex);
    }

  }

  public TheGuardianApi getTheGuardianApi() {
    return this.theGuardianApi;
  }

  public static final class TheGuardianAPIException extends RuntimeException {

    public TheGuardianAPIException(final String message) {
      super(message);
    }

    public TheGuardianAPIException(final String message, final Throwable cause) {
      super(message, cause);
    }
  }

}
