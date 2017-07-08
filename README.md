# Problem
The OnePlus 5 does not allow you to set the phone to vibrate by tapping the ringer icon or by setting the volume to 0.

# Solution
When the volume is set to 1, automatically set the ringer mode to vibrate.

# Issues
 * If the hardware slider is in the priority position, it is not possible to set the ringer mode to vibrate.
   The attempt to do so will be ignored.
 * If the interruption filter is set to priority mode (not using the hardware slider) then setting the ringer mode to vibrate will cancel the interruption filter.
   This app restores the priority mode after setting the ringer mode to vibrate.
