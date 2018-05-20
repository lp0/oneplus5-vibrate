/*
	oneplus5-vibrate - Android OnePlus 5 Vibrate Service

	Copyright 2018  Simon Arlott

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
package uk.me.sa.android.oneplus5.vibrate.ui;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import uk.me.sa.android.oneplus5.vibrate.R;

@EBean
public class VibrateOnlyNotification {
	private static final Logger log = LoggerFactory.getLogger(VibrateOnlyNotification.class);

	@RootContext
	Context context;

	@SystemService
	NotificationManager notificationManager;

	boolean vibrateOnly;

	@AfterInject
	public void onStart() {
		log.debug("Clearing all notifications");
		vibrateOnly = false;
		notificationManager.cancelAll();
	}

	public synchronized void setState(boolean vibrateOnly) {
		if (vibrateOnly == this.vibrateOnly) {
			return;
		}

		if (vibrateOnly) {
			log.debug("Adding notification");
			Notification notification = new NotificationCompat.Builder(context).setContentTitle(context.getResources().getString(R.string.vibrate_only))
					.setOngoing(true).setWhen(0).setSmallIcon(R.drawable.ic_vibration).build();
			notification.visibility = Notification.VISIBILITY_PUBLIC;
			notificationManager.notify(1, notification);
		} else {
			log.debug("Removing notification");
			notificationManager.cancelAll();
		}

		this.vibrateOnly = vibrateOnly;
	}
}
