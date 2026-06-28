import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;


public class ImageEditor {
	
	private int rows;
	private int cols;
	private Pixel[][] image;
//	private String command;
	private int blur_value;
	
	//CONSTRUCTORS--------------------------------------------------------------------------------
	public ImageEditor(Pixel[][] image, int width, int height, String command){
		this.image = image;
		rows = height;
		cols = width;
//		this.command = command;
		
		if (command.equals("invert"))
			invert();
		else if (command.equals("grayscale"))
			grayscale();
		else if (command.equals("emboss"))
			emboss();
	}
	
	public ImageEditor(Pixel[][] image, int width, int height, int blur_value){
		this.image = image;
		rows = height;
		cols = width;
//		this.command = "motionblur";
		this.blur_value = blur_value;
		
		motionBlur();
	}

	//IMAGE EDITING METHODS-----------------------------------------------------------------------
	public void invert(){
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				image[i][j].setRed(255 - image[i][j].getRed());
				image[i][j].setGreen(255 - image[i][j].getGreen());
				image[i][j].setBlue(255 - image[i][j].getBlue());
			}
		}
	}
	
	public void grayscale(){
		int gray_value = -1;
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				gray_value = (image[i][j].getRed() + image[i][j].getGreen() + image[i][j].getBlue())/3;
					
				//image[i][j] = new Pixel(gray_value, gray_value, gray_value);
				
				image[i][j].setRed(gray_value);
				image[i][j].setGreen(gray_value);
				image[i][j].setBlue(gray_value);
			}
		}
	}
	
	public void emboss(){
		int redDiff;
		int greenDiff;
		int blueDiff;
		int maxDiff;
		int v;
		Pixel[][] imagemod = new Pixel[rows][cols];
		
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				if (i == 0 || j == 0)
				{
					redDiff = greenDiff = blueDiff = 0;
				}
				else
				{
					redDiff = image[i][j].getRed() - image[i-1][j-1].getRed();
					greenDiff = image[i][j].getGreen() - image[i-1][j-1].getGreen();
					blueDiff = image[i][j].getBlue() - image[i-1][j-1].getBlue();
				}
				
				maxDiff = redDiff;
				if(Math.abs(greenDiff) > Math.abs(maxDiff))
					maxDiff = greenDiff;
				if(Math.abs(blueDiff) > Math.abs(maxDiff))
					maxDiff = blueDiff;
				
				v = 128 + maxDiff;
				if (v < 0)
					v = 0;
				else if (v > 255)
					v = 255;
				
				imagemod[i][j] = new Pixel(v,v,v);
			}
		}
		image = imagemod;
	}

	public void motionBlur(){
		
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				int totRed = 0;
				int totGreen = 0;
				int totBlue = 0;
				int bv = 0; //blur value to use for this pixel
				
				
				if (j+blur_value > cols)
					bv = cols - j;
				else
					bv = blur_value;
				
				for (int n = 0;n < bv; n++)//fix if out of bounds
				{
					totRed += image[i][j+n].getRed();
					totGreen += image[i][j+n].getGreen();
					totBlue += image[i][j+n].getBlue();
				}
				
				image[i][j].setRed(totRed/bv);
				image[i][j].setGreen(totGreen/bv);
				image[i][j].setBlue(totBlue/bv);
			}
		}
	}
	
	//PRINT TO FILE-------------------------------------------------------------------------------
	public void print(PrintWriter writer){
		writer.printf("P3 %d %d 255 ", cols, rows);
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				writer.printf("%d %d %d ", image[i][j].getRed(), image[i][j].getGreen(), image[i][j].getBlue());
			}
		}
		
	}
	
	//MAIN METHOD---------------------------------------------------------------------------------
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		if (args.length != 3 && args.length != 4)
		{
			error();
			return;
		}
		
		if (!args[2].equals("invert") && !args[2].equals("grayscale") && !args[2].equals("emboss") && !args[2].equals("motionblur"))
		{
			error();
			return;
		}
		
		if (args[2].equals("motionblur") && args.length != 4)
		{
			error();
			return;
		}
		
		File file = new File(args[0]);
		Scanner scnr = new Scanner(file);
		scnr.useDelimiter("(\\s+)(#[^\\n]*\\n)?(\\s*)|(#[^\\n]*\\n)(\\s*)");
		
		ppmReader reader = new ppmReader(scnr);
		
		
		ImageEditor img_edit;
		if (args[2].equals("motionblur"))
		{
			int blur_value = Integer.parseInt(args[3]);
			img_edit = new ImageEditor(reader.getImage(), reader.getWidth(), reader.getHeight(), blur_value);
		}
		else
		{
			img_edit = new ImageEditor(reader.getImage(), reader.getWidth(), reader.getHeight(), args[2]);
		}
		
		PrintWriter writer = new PrintWriter(new File(args[1]));
		img_edit.print(writer);
		
		scnr.close();
		writer.close();
	
	
	}
	
	public static void error(){
		System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
	}

}
