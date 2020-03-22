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

import cl.ucn.disc.dsm.alccthenews.model.Noticia;
import cl.ucn.disc.dsm.alccthenews.services.GNews.GNewsApiNoticiaService;
import cl.ucn.disc.dsm.alccthenews.services.NewsApi.NewsApiNoticiaService;
import cl.ucn.disc.dsm.alccthenews.services.mockup.MockupNoticiaService;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alvaro Lucas Castillo Calabacero
 */
public class NoticiaServiceTest {
  /**
   * The Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(NoticiaServiceTest.class);

  /**
   * Test {@link NoticiaService#getNoticias(int)}
   */
  @Test
  public void testGetNoticiasMockup() {

    log.debug("Testing the NoticiaService ..");

    // The noticia service
    final NoticiaService noticiaService = new MockupNoticiaService();

    // The List of Noticia.
    final List<Noticia> noticias = noticiaService.getNoticias(2);

    Assertions.assertNotNull(noticias);
    Assertions.assertEquals(noticias.size(), 2, "Error de tamanio");

    for (final Noticia noticia : noticias) {
      log.debug("Noticia: {}.", noticia);
    }

    log.debug("Done.");
  }
  /**
   * Test {@link NoticiaService#getNoticias(int)} with NewsAPI.org
   */
  @Test
  public void testGetNoticiasNewsApi() {

    final int size = 20;

    log.debug("Testing the NewsApiNoticiaService, requesting {} News.", size);

    // The noticia service
    final NoticiaService noticiaService = new NewsApiNoticiaService();

    // The List of Noticia.
    final List<Noticia> noticias = noticiaService.getNoticias(size);

    Assertions.assertNotNull(noticias);
    Assertions.assertEquals(noticias.size(), size, "Error de tamanio");

    for (final Noticia noticia : noticias) {
      log.debug("Noticia: {}.", noticia);
    }

    log.debug("Done.");

  }

  /**
   * Test {@link NoticiaService#getNoticias(int)} with https://gnews.io/
   */
  @Test
  public void testGetNoticiasGNewsApi() {

    final int size = 10;

    log.debug("Testing the GNewsApiNoticiaService, requesting {} News.", size);

    // The noticia service
    final NoticiaService noticiaService = new GNewsApiNoticiaService();

    // The List of Noticia.
    final List<Noticia> noticias = noticiaService.getNoticias(size);

    Assertions.assertNotNull(noticias);
    Assertions.assertEquals(noticias.size(), size, "Error de tamanio");

    for (final Noticia noticia : noticias) {
      log.debug("Noticia: {}.", noticia);
    }

    log.debug("Done.");

  }
}


