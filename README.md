# LiveFeed Simulator
This ImageJ plugin aims at simulating software that would push images at regular time interval into ImageJ. It comes in three flavours, simulating three possible behaviours.

# One plugin, three functionalities:

## Single frame
Individual images are pushed towards one single image container. The image is refreshed as a new image is becoming available. 

## Multiple frames
Individual images are pushed towards individual image container. The new image is loaded onto its own window as it becomes available.

## Save
Individual images are pushed towards a designated output folder. The "new" files are copied to the output folder as they become available.

# How to install it ?
1. Download the the [lastest release here](https://github.com/fabricecordelieres/IJ_LiveFeed_Simulator/releases/download/v1.0.0/LiveFeed_Simulator.jar).
2. Either drag-and-drop the jar file to the ImageJ's toolbar or save it to ImageJ's plugins folder (within ImageJ's installation folder).

# How to use it ?

For both functionalities, the plugins expects a folder containing individual images to be used for simulating the images's stream.
When launched, both plugins will ask the user to point at this folder.
A second dialog box should then pop-up: it allows setting the time delay to wait for before loading next image.

**NB:** To stop running the plugin before last image has been reached, press the ```Esc``` key


