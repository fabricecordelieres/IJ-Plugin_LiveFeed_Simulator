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
public class LiveFeed_Simulator_Single_Frame implements PlugIn{
	/** Full path to the image to use for simulation **/
	String imagePath=null;
	
	/** The image's name to use for simulation **/
	String imageName=null;
	
	/** Time interval in msec **/
	long timeInterval=(long) Prefs.get("LiveFeed_Simulator_Single_Frame_time.double", 2000);
	
	/** Tag used to only show the ImagePlus once, otherwise to close the plugin **/
	boolean neverShown=true;
	
	/** The ImagePlus to be updated to simulate the live feed **/
	ImagePlus timelapse=null;
	
		@Override
	public void run(String arg) {
		if(GUI()) process();
	}
	
		
	/**
	 * Displays the graphical user interface
	 */
	public boolean GUI() {
		boolean doContinue=false;
		
		OpenDialog od=new OpenDialog("Point at the stack file to use for simulation");
		imagePath=od.getPath();
		imageName=od.getFileName();
		
		if(null!=imagePath && null!=imageName) {
			doContinue=true;
			
			GenericDialog gd=new GenericDialog("Live Feed Simulator, Single Frame: Parameters");
			gd.addNumericField("Time_interval_(sec)", timeInterval/1000, 3);
			gd.showDialog();
			
			if(gd.wasCanceled()) doContinue=false;
			
			timeInterval=(long) gd.getNextNumber()*1000;
			Prefs.set("LiveFeed_Simulator_Single_Frame_time.double", timeInterval);
		}
		
		return doContinue;
	}

	/**
	 * Displays the input stack, one image at a time
	 */
	public void process() {
		if(null!=imagePath && null!=imageName) {
			ImagePlus ip = new ImagePlus(imagePath);
	
			/** The output image, where the timelapse is displayed **/
			timelapse = new ImagePlus("LiveFeed_Simulator_Single_Frame-"+imageName, ip.getProcessor());
			timelapse.setLut(getLUT());
			timelapse.show();
	
			while (0 != 1) {
				for (int i = 1; i <= ip.getNSlices(); i++) {
					ip.setSlice(i);
					timelapse.setProcessor(ip.getProcessor());
					timelapse.updateImage();
					timelapse.changes=false;
						
					try {
						Thread.sleep(timeInterval);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					if(IJ.escapePressed()) break;
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
