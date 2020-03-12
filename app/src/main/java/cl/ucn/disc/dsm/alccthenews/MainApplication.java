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

package cl.ucn.disc.dsm.alccthenews;

import android.app.Application;
import android.os.Build.VERSION_CODES;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.VmPolicy;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jakewharton.threetenabp.AndroidThreeTen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alvaro Lucas Castillo Calabacero
 */
public class MainApplication extends Application {

  /**
   * The Logger.
   */
  private static final Logger log = LoggerFactory.getLogger(MainApplication.class);

  /**
   * Called when the application is starting, before any activity, service, or receiver objects (excluding content
   * providers) have been created.
   */
  @RequiresApi(api = VERSION_CODES.M)
  @Override
  public void onCreate() {
    super.onCreate();

    // AndroidThreeTen initialization
    AndroidThreeTen.init(this);

    // Day and Night support
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

    // Facebook fresco
    Fresco.initialize(this);

    // Enforce strict mode in debug mode
    if (BuildConfig.DEBUG) {

      StrictMode.setThreadPolicy(new ThreadPolicy.Builder()
          .detectDiskReads()
          .detectDiskWrites()
          .detectNetwork()
          .penaltyLog() //.penaltyFlashScreen()
          .build());

      StrictMode.setVmPolicy(new VmPolicy.Builder()
          .detectLeakedSqlLiteObjects()
          .detectLeakedClosableObjects()
          .detectLeakedRegistrationObjects()
          .detectActivityLeaks()
          .detectCleartextNetwork()
          .penaltyLog()
          .build());
    }
    log.debug("Initializing: Done.");
  }
}
