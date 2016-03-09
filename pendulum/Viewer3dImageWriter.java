package pendulum;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Raster;
import javax.vecmath.Point3f;

public class Viewer3dImageWriter {
	private Viewer3d viewer;
	
	private BufferedImage bufferedImage;
	private ImageComponent2D imageComponent;
	private Raster raster;
	private File dir;
	
	private int count = 0;
	private boolean canWrite = true;
	public Viewer3dImageWriter(File dir, Viewer3d viewer, int width, int height) {
		if (!dir.isDirectory()) {
			canWrite = false;
			return;
		}
		
		try {
			dir.createNewFile();
			Runtime.getRuntime().exec("rm " + dir.getAbsolutePath() + "*.png");
		} catch (IOException e) {
			canWrite = false;
		}
		
		
		
		if (!canWrite)
			return;
		
		this.dir = dir;
		this.viewer = viewer;
		
		bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		imageComponent =
			new ImageComponent2D(ImageComponent.FORMAT_RGB, bufferedImage);
		raster= new Raster(new Point3f(-1.0f, -1.0f, -1.0f),
					Raster.RASTER_COLOR, 0, 0, bufferedImage.getWidth(),
					bufferedImage.getHeight(), imageComponent, null);
	}
	
	public void write() {
		if (!canWrite)
			return;
		
		viewer.getComponent().getGraphicsContext3D().readRaster(raster);
		bufferedImage = raster.getImage().getImage();
		String imgFilename = String.format("%05d.png", count);
		File file = new File(dir, imgFilename);
		try {
			ImageIO.write(bufferedImage, "png", file);
		} catch (IOException e) {
			canWrite = false;
		}
		
		count++;
	}
	
	public int getCount() {
		return count;
	}
	
	public boolean canWrite() {
		return canWrite;
	}
}
