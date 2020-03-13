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

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author Alvaro Lucas Castillo Calabcero
 */
public interface TheGuardianApi {

  /**
   * The URL
   */
  String BASE_URL = "http://content.guardianapis.com/";

  /**
   * The API Key
   */
  String API_KEY = "a26c1c35-6caa-4230-9e99-7994c39c0cdc";

  /**
   * http://content.guardianapis.com/
   */
  @GET("search")
  Call<Response> getContent(
      @Query("api-key") String API_KEY,
      @Query("page-size") int page_size,
      @Query("show-fields") String fields);
}