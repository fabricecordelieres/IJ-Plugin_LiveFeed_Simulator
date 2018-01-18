import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.FileChannel;

import ij.IJ;
import ij.Prefs;
import ij.gui.GenericDialog;
import ij.io.OpenDialog;
import ij.io.SaveDialog;
import ij.plugin.PlugIn;

/**
*
*  LiveFeed_Simulator.java, 17 jan. 2018
   Fabrice P Cordelieres, fabrice.cordelieres at gmail.com

   Copyright (C) 2018 Fabrice P. Cordelieres

   License:
   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
*/

/**
 * This plugin aims at simulating the behavior of the any software, saving images to an output folder
 * @author fab
 *
 */
public class LiveFeed_Simulator_Save implements PlugIn{
	/** Path to the folder where the images to use for simulation are stored **/
	String imagePath=null;
	
	/** The images' names to use for simulation, as an array of String **/
	String[] imagesNames=null;
	
	/** Path to the output folder where the images should be saved **/
	String savePath=null;
	
	/** Time interval in msec **/
	long timeInterval=(long) Prefs.get("LiveFeed_Simulator_Save_time.double", 2000);
	
	boolean escPressed=false;
	
	@Override
	public void run(String arg) {
		if(GUI()) process();
	}
	
		
	/**
	 * Displays the graphical user interface
	 */
	public boolean GUI() {
		boolean doContinue=false;
		
		OpenDialog od=new OpenDialog("Point one file, in the folder where files to be used for simulation are stored");
		imagePath=od.getDirectory();
		
		if(null!=imagePath) {
			imagesNames=new File(imagePath).list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return true; //name.toLowerCase().endsWith(".tif");
				}
			});
		}
		
		SaveDialog sd=new SaveDialog("Point at the folder where images should be saved", null, null);
		savePath=sd.getDirectory();
		
		if(null!=imagePath && null!=savePath && null!=imagesNames) {
			doContinue=true;
			GenericDialog gd=new GenericDialog("Live Feed Simulator, Save: Parameters");
			gd.addNumericField("Time_interval_(sec)", timeInterval/1000, 3);
			gd.showDialog();
			
			if(gd.wasCanceled()) doContinue=false;
			
			timeInterval=(long) gd.getNextNumber()*1000;
			Prefs.set("LiveFeed_Simulator_Save_time.double", timeInterval);
		}
		return doContinue;
	}

	/**
	 * Loads the input images, one image at a time
	 */
	public void process() {
		if(null!=imagePath && null!=savePath && null!=imagesNames) {
			for(int i=0; i<imagesNames.length; i++) {
				try {
					copy(imagePath+imagesNames[i], savePath+imagesNames[i]);
					Thread.sleep(timeInterval);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(IJ.escapePressed()) break;
			}
		}
	}
	
	/**
	 * Copies a source file to destination
	 * @param sourcePath source file path
	 * @param destinationPath destination file path
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static void copy(String sourcePath, String destinationPath) throws IOException {
	    File sourceFile=new File(sourcePath);
	    File destFile=new File(destinationPath);
		
		if(!destFile.exists()) {
	        destFile.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;

	    try {
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();
	        destination.transferFrom(source, 0, source.size());
	    }
	    finally {
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	    }
	}
}
