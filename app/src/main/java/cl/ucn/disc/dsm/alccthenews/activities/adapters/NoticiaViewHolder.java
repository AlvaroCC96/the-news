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

package cl.ucn.disc.dsm.alccthenews.activities.adapters;

import android.os.Build.VERSION_CODES;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import cl.ucn.disc.dsm.alccthenews.databinding.RowNoticiaBinding;
import cl.ucn.disc.dsm.alccthenews.model.Noticia;
import java.util.Date;
import org.ocpsoft.prettytime.PrettyTime;
import org.threeten.bp.DateTimeUtils;

/**
 * @author Alvaro Lucas Castillo Calabacero
 */
public class NoticiaViewHolder extends RecyclerView.ViewHolder {

  /**
   * The Date formatter.
   */
  private static final PrettyTime PRETTY_TIME = new PrettyTime();

   /**
   * The Bindings
   */
  private final RowNoticiaBinding binding;

  /**
   * The Constructor.
   *
   * @param rowNoticiaBinding to use.
   */
  public NoticiaViewHolder(RowNoticiaBinding rowNoticiaBinding) {
    super(rowNoticiaBinding.getRoot());
    this.binding = rowNoticiaBinding;
  }

  /**
   * Bind the Noticia to the ViewHolder.
   *
   * @param noticia to bind.
   */
  @RequiresApi(api = VERSION_CODES.O)
  public void bind(final Noticia noticia) {

    this.binding.tvTitulo.setText(noticia.getTitulo());
    this.binding.tvResumen.setText(noticia.getResumen());
    this.binding.tvAutor.setText(noticia.getAutor());
    this.binding.tvFuente.setText(noticia.getFuente());

    // FIXED: The format of the date whit PRETTY TIME.
    final Date date = DateTimeUtils.toDate(noticia.getFecha().toInstant());
    this.binding.tvFecha.setText(PRETTY_TIME.format(date));



  }
}
