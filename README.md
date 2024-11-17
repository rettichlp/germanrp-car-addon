# germanrp-car-addon

Assistance systems and other nice-to-have features for the cars on GermanRP

<!-- TOC -->
* [germanrp-car-addon](#germanrp-car-addon)
  * [Assistance systems](#assistance-systems)
    * [Lane assist](#lane-assist)
    * [Collision Avoidance Assist: Adaptive Cruise Control](#collision-avoidance-assist-adaptive-cruise-control)
    * [Collision Avoidance Assist: Automatic Emergency Braking (AEB)](#collision-avoidance-assist-automatic-emergency-braking-aeb)
    * [Collision Avoidance Alert: Blind-spot warning](#collision-avoidance-alert-blind-spot-warning)
    * [Traffic sign recognition](#traffic-sign-recognition)
    * [Rear cross traffic alert](#rear-cross-traffic-alert)
    * [Car Location Assist](#car-location-assist)
<!-- TOC -->

## Assistance systems

Roads on GermanRP are usually 3 blocks wide with an additional marker block on each side. Vehicles consist of a main-armorstand and,
depending on the number of seats, several sub-armorstands.

### Lane assist

The assistant is activated as soon as the cruise control (GermanRP function) is set to a three-digit speed and the steering has not
been adjusted for more than 3 seconds.
As soon as the assistant is active, the viewing direction of the main-armorstand is read, and it is calculated whether the vehicle is
moving parallel to the x or z axis. If the vehicle is not exactly aligned with an axis, the vehicle is rotated automatically for small
deviations and the assistant is deactivated for larger deviations (> 30°).

### Collision Avoidance Assist: Adaptive Cruise Control

The assistant is activated as soon as the cruise control (GermanRP function) is set to a three-digit speed. If a main-armorstand of
another vehicle is detected during driving, a number of checks are carried out which are only carried out in a hierarchy if the higher
check was answered with yes:

1. is the vehicle on the movement vector of your own vehicle
2. is the detected vehicle moving or parking (the latter triggers the AEB)
3. is the detected vehicle moving away from your own vehicle or is it getting closer (the latter triggers the AEB and the acoustic
   warning signals)

After checking the individual steps, the cruise control is adjusted to the speed of the detected vehicle. After 5 seconds, the check
is carried out again to increase the speed again if necessary. If the speed has not changed excessively, no further check is carried
out until the driver manually intervenes in the control and, for example, accelerates himself.

### Collision Avoidance Assist: Automatic Emergency Braking (AEB)

### Collision Avoidance Alert: Blind-spot warning

### Traffic sign recognition

### Rear cross traffic alert

### Car Location Assist
