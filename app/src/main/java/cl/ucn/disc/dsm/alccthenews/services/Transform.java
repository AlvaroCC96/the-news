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

package cl.ucn.disc.dsm.alccthenews.services;

import android.os.Build.VERSION_CODES;
import androidx.annotation.RequiresApi;
import cl.ucn.disc.dsm.alccthenews.model.Noticia;
import cl.ucn.disc.dsm.alccthenews.services.GNews.Article2;
import cl.ucn.disc.dsm.alccthenews.services.NewsApi.Article;
import cl.ucn.disc.dsm.alccthenews.services.NewsApi.NewsApiNoticiaService;
import cl.ucn.disc.dsm.alccthenews.services.NewsApi.Source;
import java.net.URI;
import java.net.URISyntaxException;
import javax.xml.transform.Transformer;
import net.openhft.hashing.LongHashFunction;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeParseException;

/**
 * @author Alvaro Lucas Castillo Calabacero
 */
public class Transform {
  /**
   * The logger.
   */
  private static final Logger log = LoggerFactory.getLogger(Transformer.class);

  /**
   * Article to Noticia.
   *
   * @param article2 to transform
   * @return the Noticia.
   */
  @RequiresApi(api = VERSION_CODES.O)
  public static Noticia transform2(final Article2 article2) {

    // Nullity
    if (article2 == null) {
      throw new NewsApiNoticiaService.NewsAPIException("Article was null");
    }

    // The host
    final String host = getHost(article2.url);

    // Si el articulo es null ..
    if (article2.title == null) {

      log.warn("Article without title: {}", toString(article2));

      // .. y el contenido es null, lanzar exception!
      if (article2.description == null) {
        throw new NewsApiNoticiaService.NewsAPIException("Article without title and description");
      }

      article2.title = "No Title*";
    }

    // FIXED: En caso de no haber una fuente.
    if (article2.source == null) {
      article2.source = new cl.ucn.disc.dsm.alccthenews.services.GNews.Source();

      if (host != null) {
        article2.source.name = host;
      } else {
        article2.source.name = "No Source*";
        log.warn("Article without source: {}", toString(article2));
      }
    }

    // The date.
    final ZonedDateTime publishedAt = ZonedDateTime
        .parse(article2.publishedAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));

    // The unique id (computed from hash)
    final Long theId = LongHashFunction.xx()
        .hashChars(article2.title + article2.source.name);

    // The Noticia.
    return new Noticia(
        theId,
        article2.title,
        article2.source.name,
        "GNews",
        article2.url,
        article2.image,
        article2.description,
        article2.source.url,
        publishedAt
    );

  }

  @RequiresApi(api = VERSION_CODES.O)
  public static Noticia transform(final Article article) {

    // Nullity
    if (article == null) {
      throw new NewsApiNoticiaService.NewsAPIException("Article was null");
    }

    // The host
    final String host = getHost(article.url);

    // Si el articulo es null ..
    if (article.title == null) {

      log.warn("Article without title: {}", toString(article));

      // .. y el contenido es null, lanzar exception!
      if (article.description == null) {
        throw new NewsApiNoticiaService.NewsAPIException("Article without title and description");
      }


      article.title = "No Title*";
    }

    // FIXED: En caso de no haber una fuente.
    if (article.source == null) {
      article.source = new Source();

      if (host != null) {
        article.source.name = host;
      } else {
        article.source.name = "No Source*";
        log.warn("Article without source: {}", toString(article));
      }
    }

    // FIXED: Si el articulo no tiene author
    if (article.author == null) {

      if (host != null) {
        article.author = host;
      } else {
        article.author = "No Author*";
        log.warn("Article without author: {}", toString(article));
      }
    }

    // The date.
    final ZonedDateTime publishedAt = parseZonedDateTime(article.publishedAt)
        .withZoneSameInstant(Noticia.ZONE_ID);

    // The unique id (computed from hash)
    final Long theId = LongHashFunction.xx()
        .hashChars(article.title + article.source.name);

    // The Noticia.
    return new Noticia(
        theId,
        article.title,
        article.source.name,
        article.author,
        article.url,
        article.urlToImage,
        article.description,
        article.content,
        publishedAt
    );

  }


  /**
   * Get the host part of one url.
   *
   * @param url to use.
   * @return the host part (without the www)
   */
  private static String getHost(final String url) {
    try {

      final URI uri = new URI(url);
      final String hostname = uri.getHost();

      // to provide faultproof result, check if not null then return only hostname, without www.
      if (hostname != null) {
        return hostname.startsWith("www.") ? hostname.substring(4) : hostname;
      }

      return null;

    } catch (final URISyntaxException | NullPointerException ex) {
      return null;
    }
  }

  /**
   * Convierte una fecha de {@link String} a una {@link ZonedDateTime}.
   *
   * @param fecha to parse.
   * @return the fecha.
   * @throws cl.ucn.disc.dsm.alccthenews.services.NewsApi.NewsApiNoticiaService .NewsAPIException en
   *                                                                            caso de no lograr
   *                                                                            convertir la fecha.
   */
  public static ZonedDateTime parseZonedDateTime(final String fecha) {

    // Na' que hacer si la fecha no existe
    if (fecha == null) {
      throw new NewsApiNoticiaService.NewsAPIException("Can't parse null fecha");
    }

    try {
      // Tratar de convertir la fecha ..
      return ZonedDateTime.parse(fecha);
    } catch (DateTimeParseException ex) {

      // Mensaje de debug
      log.error("Can't parse date: ->{}<-. Error: ", fecha, ex);

      // Anido la DateTimeParseException en una NoticiaTransformerException.
      throw new NewsApiNoticiaService.NewsAPIException("Can't parse date: " + fecha, ex);
    }
  }

  /**
   *
   * @param target , object to be transformed into string
   * @param <T> String returned
   * @return string model of target
   */
  public static <T> String toString(final T target) {
    return ReflectionToStringBuilder.toString(target
        , ToStringStyle.MULTI_LINE_STYLE);
  }
}
