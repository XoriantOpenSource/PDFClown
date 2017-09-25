package org.pdfclown.samples.cli;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import javax.imageio.ImageIO;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;


/**
  This sample demonstrates <b>how to render a PDF page as a raster image</b>.
  <p>Note: rendering is currently in pre-alpha stage; therefore this sample is
  nothing but an initial stub (no assumption to work!).</p>

  @author Stefano Chizzolini (http://www.stefanochizzolini.it)
  @since 0.1.0
  @version 0.1.2, 09/24/12
*/
public class RenderingSample
  extends Sample
{
  @Override
  public void run(
    )
  {
    java.io.File file = null;
    try
    {
      // 1. Opening the PDF file...
      {
        String filePath = promptFileChoice("Please select a PDF file");
        try
        {
        file = new java.io.File(filePath);
        
        }
        catch(Exception e)
        {throw new RuntimeException(filePath + " file access error.",e);}
      }
      
      try
      {
      java.io.RandomAccessFile raf = new java.io.RandomAccessFile(file, "r");
  	FileChannel channel = raf.getChannel();
  	ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
  	PDFFile pdf = new PDFFile(buf);
      

      // 2. Page rasterization.
  	
      int pageIndex = promptPageChoice("Select the page to render", pdf.getNumPages());
      
      PDFPage myPage = pdf.getPage(pageIndex);
      Rectangle rect = new Rectangle(0, 0, (int) myPage.getBBox().getWidth(),
              (int) myPage.getBBox().getHeight());
BufferedImage bufferedImage = new BufferedImage(rect.width, rect.height,
               BufferedImage.TYPE_INT_RGB);

Image image = myPage.getImage(rect.width, rect.height,    // width & height
         rect,                       // clip rect
         null,                       // null for the ImageObserver
         true,                       // fill background with white
         true                        // block until drawing is done
);
Graphics2D bufImageGraphics = bufferedImage.createGraphics();
bufImageGraphics.drawImage(image, 0, 0, null);


raf.close();
           

      // 3. Save the page image!
      ImageIO.write(bufferedImage,"jpg",new java.io.File(getOutputPath("ContentRenderingSample"+pageIndex+".jpg")));}
      catch(FileNotFoundException f)
      {
    	  f.printStackTrace();
      }
      catch(IOException e)
      {e.printStackTrace();}
      
    }
   
  }
}
