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


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface GNewsApi {

  final String BASE_URL = "https://gnews.io/api/v3/";
  final String API_KEY = "0d194e505de173cb271edc62b375c329";

  @Headers({"X-Api-Key: " + API_KEY})
  @GET("top-news?token=" + API_KEY)
  Call<GNewsApiResult> getTopHeadlines(
      @Query("lang") final String lang, @Query("max") final int max);

  /**
   * @return the call of {@link GNewsApiResult}.
   */
  @Headers({"X-Api-Key: " + API_KEY})
  @GET("everything?sources=ars-technica,wired,hacker-news,recode")
  Call<GNewsApiResult> getEverything(@Query("pageSize") final int pageSize);

}

