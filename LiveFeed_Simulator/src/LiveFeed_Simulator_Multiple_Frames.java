import java.io.File;
import java.io.FilenameFilter;
import ij.IJ;
import ij.ImagePlus;
import ij.Prefs;
import ij.gui.GenericDialog;
import ij.io.OpenDialog;
import ij.plugin.PlugIn;
import ij.process.LUT;

/**
*
*  LiveFeed_Simulator.java, 1 dec. 2017
   Fabrice P Cordelieres, fabrice.cordelieres at gmail.com

   Copyright (C) 2017 Fabrice P. Cordelieres

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
 * This plugin aims at simulating the behavior of the any software, pushing the image data stream towards ImageJ
 * @author fab
 *
 */
public class LiveFeed_Simulator_Multiple_Frames implements PlugIn{
	/** Path to the folder where the images to use for simulation are stored **/
	String imagePath=null;
	
	/** The images' names to use for simulation, as an array of String **/
	String[] imagesNames=null;
	
	/** Time interval in msec **/
	long timeInterval=(long) Prefs.get("LiveFeed_Simulator_Multiple_Frame_time.double", 2000);
	
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
					return name.toLowerCase().endsWith(".tif");
				}
			});
		}
		
		if(null!=imagePath && null!=imagesNames) {
			doContinue=true;
			GenericDialog gd=new GenericDialog("Live Feed Simulator, Multiple Frames: Parameters");
			gd.addNumericField("Time_interval_(sec)", timeInterval/1000, 3);
			gd.showDialog();
			
			if(gd.wasCanceled()) doContinue=false;
			
			timeInterval=(long) gd.getNextNumber()*1000;
			Prefs.set("LiveFeed_Simulator_Multiple_Frame_time.double", timeInterval);
		}
		return doContinue;
	}

	/**
	 * Loads the input images, one image at a time
	 */
	public void process() {
		if(null!=imagePath && null!=imagesNames) {
			LUT lut=getLUT();
			for(int i=0; i<imagesNames.length; i++) {
				ImagePlus ip=new ImagePlus(imagePath+imagesNames[i]);
				ip.setLut(lut);
				ip.show();
				try {
					Thread.sleep(timeInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(IJ.escapePressed()) break;
			}
		}
	}
	
	/**
	 * Generates the golden-like LUT
	 * @return the golden-like LUT
	 */
	public static LUT getLUT(){
		byte[] r=new byte[256];
		byte[] g=new byte[256];
		byte[] b=new byte[256];
		
		for(int i=0; i<256; i++) {
			r[i]=(byte) (255-i);
			g[i]=(byte) Math.max(0, Math.round(255-i*256/152));
			b[i]=(byte) Math.max(0, Math.round(255-i*256/114));
		}
		
		return new LUT(r, g, b);
	}
}
