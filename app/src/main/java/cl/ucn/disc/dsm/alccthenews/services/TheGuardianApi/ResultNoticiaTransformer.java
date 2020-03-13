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
import cl.ucn.disc.dsm.alccthenews.services.NewsApi.NewsApiNoticiaService.NewsAPIException;
import cl.ucn.disc.dsm.alccthenews.services.Transform;
import net.openhft.hashing.LongHashFunction;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.ZonedDateTime;

public class ResultNoticiaTransformer {

  /**
   * The Logger
   */
  private static final Logger log = LoggerFactory.getLogger(ResultNoticiaTransformer.class);

  @RequiresApi(api = VERSION_CODES.O)
  public static Noticia transform(Result result) {

    //initial validations
    if (result == null) {
      throw new NewsAPIException("Result is NULL");
    } else if (result.webUrl == null) {
      throw new NewsAPIException("WebURL is NULL");
    }

    //minimium validation
    if (result.webTitle == null || result.webPublicationDate == null) {
      throw new NewsAPIException("Title or date NULL");
    }
    //try to format date
    ZonedDateTime publishedAt;
    try {
      publishedAt = Transform.parseZonedDateTime(result.webPublicationDate)
          .withZoneSameInstant(Noticia.ZONE_ID);
    } catch (Exception e) {
      log.error("No es posible tranformar el webPublicationDate con threetenabp.");
      throw new NewsAPIException("Error in format time");
    }

    String resumen = "";
    //validate fields and define default
    if (result.fields == null) {
      result.fields = new Field();
      result.fields.thumbnail = null;
      result.fields.standfirst = "Content not found";
      resumen = "Content not found";

    } else {
      //delete html code
      resumen = result.fields.standfirst;
      if (resumen.contains("<p>")) {
        resumen = StringUtils.substringBetween(resumen,
            "<p>", "</p>");

      } else if (resumen.contains("<li>")) {
        int pos1 = resumen.indexOf("<li>");
        int pos2 = resumen.indexOf("</li>");
        resumen = resumen.substring(pos1 + 4, pos2);
      }

      if (resumen.contains("<strong>")) {
        resumen = resumen.replace("<strong>", "");
        resumen = resumen.replace("</strong>", "");
      }

      if (resumen.contains("<br>")) {
        resumen = resumen.replace("<br>", "");
      }
      if (resumen.contains("<hr>")) {
        resumen = resumen.replace("<hr>", "");
      }
    }
    // The unique id (computed from hash)
    final Long theId = LongHashFunction.xx()
        .hashChars(result.webTitle + result.webPublicationDate);

    return new Noticia(
        theId,
        result.webTitle, "The Guardian",
        "The Guardian",
        result.webUrl,
        result.fields.thumbnail, resumen, result.fields.standfirst, publishedAt);
  }
}
