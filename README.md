# LiveFeed Simulator
This ImageJ plugin aims at simulating software the would push images at regular time interval into ImageJ. It comes in two flavours, simulating two possible behaviours.

# One plugin, two functionalities:

## Single frame
Individual images are pushed towards one single image container. The image is refreshed as a new image is becoming available. 

## Multiple frames
Individual images are pushed towards individual image container. The new image is loaded onto its own window as it becomes available.

# How to use it ?

For both functionalities, the plugins expects a folder containing individual images to be used for simulating the images's stream.
When launched, both plugins will ask the user to point at this folder.
A second dialog box should then pop-up: it allows setting the time delay to wait for before loading next image.

**NB:** To stop running the plugin before last image has been reached, press the ```Esc``` key


