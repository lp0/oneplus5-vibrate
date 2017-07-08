/*
	oneplus5-vibrate - Android OnePlus 5 Vibrate Service

	Copyright 2017  Simon Arlott

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.me.sa.android.oneplus5.vibrate;

import org.androidannotations.annotations.EService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;

@EService
public class NotificationListener extends NotificationListenerService {
	private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);

	private AudioManager audioManager;
	private ContentObserver contentObserver = new ContentObserver(new Handler()) {

		@Override
		public boolean deliverSelfNotifications() {
			return false;
		}

		@Override
		public void onChange(boolean selfChange) {
			int volume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
			int ringerMode = audioManager.getRingerMode();

			// Silent: Volume 0 and mode 0 (silent)
			// Priority: Volume 1 and mode 0 (silent)
			// Normal: Volume 1 and mode 2 (ring)

			log.debug("Volume is {}; Ringer mode is {}", volume, ringerMode);
			if (volume == 1 && ringerMode != AudioManager.RINGER_MODE_VIBRATE) {
				setVibrateMode();
			}
		}

	};

	@Override
	public void onCreate() {
		audioManager = (AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
		getApplicationContext().getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, contentObserver);
	}

	@Override
	public void onListenerConnected() {
		log.info("Notification listener connected");
	}

	public void setVibrateMode() {
		int interruptionFilter = getCurrentInterruptionFilter();
		if (interruptionFilter == INTERRUPTION_FILTER_NONE || interruptionFilter == INTERRUPTION_FILTER_ALARMS)
			return;
		log.info("Setting ringer mode to vibrate");
		audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		if (interruptionFilter == INTERRUPTION_FILTER_PRIORITY) {
			log.info("Restoring interruption filter to priority");
			requestInterruptionFilter(interruptionFilter);
		}
	}

	@Override
	public void onDestroy() {
		getApplicationContext().getContentResolver().unregisterContentObserver(contentObserver);
	}
}
