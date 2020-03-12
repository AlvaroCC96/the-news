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

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION_CODES;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import cl.ucn.disc.dsm.alccthenews.R;
import cl.ucn.disc.dsm.alccthenews.databinding.PopupImageBinding;
import cl.ucn.disc.dsm.alccthenews.databinding.RowNoticiaBinding;
import cl.ucn.disc.dsm.alccthenews.model.Noticia;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * @author Alvaro Lucas Castillo Calabacero
 */
public class NoticiaAdapter extends RecyclerView.Adapter<NoticiaViewHolder> {

  /**
   * The List of Noticias.
   */
  private List<Noticia> theNoticias;

  /**
   * The Constructor.
   */
  public NoticiaAdapter() {

    // Empty list
    this.theNoticias = new ArrayList<>();

    // Each Noticia has unique id
    this.setHasStableIds(true);

  }

  /**
   * Change the current List of Noticias.
   *
   * @param noticias to use.
   */
  public void setNoticias(final List<Noticia> noticias) {

    // Update the noticias
    this.theNoticias = noticias;

    // Notify to re-layout
    this.notifyDataSetChanged();
  }
  /**
   * Called by RecyclerView to display the data at the specified position. This method should update the contents of the
   * ViewHolder to reflect the item at the given position.
   */
  @RequiresApi(api = VERSION_CODES.O)
  @Override
  public void onBindViewHolder(@NonNull NoticiaViewHolder holder, int position) {
    holder.bind(this.theNoticias.get(position));
  }

  /**
   * Returns the total number of items in the data set held by the adapter.
   */
  @Override
  public int getItemCount() {
    return this.theNoticias.size();
  }

  /**
   * Return the stable ID for the item at position.
   */
  @RequiresApi(api = VERSION_CODES.O)
  @Override
  public long getItemId(int position) {
    return this.theNoticias.get(position).id;
  }

  /**
   * Show a image popup with the url.
   *
   * @param noticia  to show.
   * @param inflater used to inflate the popup.
   * @param context  used to build the dialog.
   */
  @RequiresApi(api = VERSION_CODES.O)
  private void showImagePopup(final Noticia noticia, final LayoutInflater inflater,
      final Context context) {

    // The popupimage
    final PopupImageBinding popupImageBinding = PopupImageBinding.inflate(inflater);

    // The URL of the photo
    popupImageBinding.pdvFoto.setPhotoUri(Uri.parse(noticia.getUrlFoto()));

    // The Dialog
    final Dialog dialog = new Dialog(context, R.style.PopupDialog);
    dialog.setContentView(popupImageBinding.getRoot());
    dialog.show();

  }

  /**
   * Called when RecyclerView needs a newViewHolder of the given type to represent an item.
   */
  @RequiresApi(api = VERSION_CODES.O)
  @NotNull
  @Override
  public NoticiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    // The inflater
    final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

    // The row of noticia
    final RowNoticiaBinding rowNoticiaBinding = RowNoticiaBinding.inflate(
        layoutInflater,
        parent,
        false
    );

    // The NoticiaViewHolder
    final NoticiaViewHolder noticiaViewHolder = new NoticiaViewHolder(rowNoticiaBinding);

    // Click over the image
    rowNoticiaBinding.sdvFoto.setOnClickListener(view -> {

      // The position
      final int position = noticiaViewHolder.getAdapterPosition();

      // The id
      final long id = noticiaViewHolder.getItemId();
      //log.debug("Click! position: {}, id: {}.", position, Long.toHexString(id));

      // Noticia to show
      final Noticia noticia = this.theNoticias.get(position);

      // Nothing to do
      if (noticia.getUrlFoto() == null) {
        return;
      }

      // Popup the image
      this.showImagePopup(noticia, layoutInflater, parent.getContext());
    });

    // Click in the row
    rowNoticiaBinding.getRoot().setOnClickListener(view -> {

      // The position
      final int position = noticiaViewHolder.getAdapterPosition();

      // The id
      final long id = noticiaViewHolder.getItemId();
      //log.debug("Click! position: {}, id: {}.", position, Long.toHexString(id));

      // Noticia to show
      final Noticia noticia = this.theNoticias.get(position);

      //log.debug("Link: {}.", noticia.getUrl());
      if (noticia.getUrl() != null) {

        // Open the browser
        parent.getContext().startActivity(
            new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(noticia.getUrl())
            )
        );
      }

    });

    return noticiaViewHolder;

  }

}
