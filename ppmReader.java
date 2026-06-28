import java.util.*;

public class ppmReader {

	private int width;
	private int height;
	private Pixel[][] image;
	
	public ppmReader(Scanner scnr)
	{
		read(scnr);
	}
	
	public void read(Scanner scnr)
	{
		scnr.next();//skips "P3"
		width = scnr.nextInt();
		height = scnr.nextInt();
		scnr.next();//skips max height value
		readPixels(scnr);
	}
	
	public void readPixels(Scanner scnr)
	{
		image = new Pixel[height][width];
		for (int i = 0; i < height; i++){
			for (int j = 0; j < width; j++){
				image[i][j] = new Pixel(scnr.nextInt(), scnr.nextInt(), scnr.nextInt());
				/*
				image[i][j].setRed(scnr.nextInt());
				image[i][j].setGreen(scnr.nextInt());
				image[i][j].setBlue(scnr.nextInt());
				*/
			}
		}
	
	}
	
	public Pixel[][] getImage()
	{
		return image;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
}
