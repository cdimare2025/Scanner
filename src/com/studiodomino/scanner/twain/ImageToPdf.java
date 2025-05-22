/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.studiodomino.scanner.twain;

/**
 *
 * @author Donato
 */
/**
 * ImageToPdf.java
 */


import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Image;
import java.io.OutputStream;

public class ImageToPdf 
{

    public static OutputStream ImmagineAPdf(Image[] im, OutputStream out) {
    try {
         Rectangle r = new Rectangle(PageSize.A4);
         r.setBackgroundColor(new Color(0xC0, 0xC0, 0xC0));
         Document document = new Document(r);
         PdfWriter.getInstance(document, out);
         document.open();
         for (int i=0;i<im.length;i++) {
             com.lowagie.text.Image img1 = com.lowagie.text.Image.getInstance(im[i], null);
            float leastPercent = 0F;
            float horizontalPercent = 0F;
            float imgWidth;
            float imgHeight;

 
            img1.scaleToFit(document.getPageSize().width(), document.getPageSize().height());
            img1.setAbsolutePosition(0, 0);
            document.add(img1);
             document.newPage();
         }
         document.close();        
        return out;
    } catch (Exception e) {
    }
    return null;
    
    }
/*
    private void scaleImageToFitPage(final Image img)
    {
        float leastPercent = 0F;
        float horizontalPercent = 0F;
        float imgWidth;
        float imgHeight;

        final float usableWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
        final float usableHeight = document.getPageSize().getHeight() - document.topMargin() - document.bottomMargin();

        if (isAutoImageRotate && (img.getWidth() > usableWidth) && (img.getWidth() > img.getHeight())
                && (document.getPageSize() == PageSize.LETTER))
        {
            img.setRotationDegrees(90);
            imgWidth = img.rotate().getWidth();
            imgHeight = img.rotate().getHeight();
        }
        else
        {
            imgWidth = img.getWidth();
            imgHeight = img.getHeight();
        }

        if (imgWidth > usableWidth)
        {
            horizontalPercent = usableWidth / imgWidth;
            leastPercent = horizontalPercent;
        }

        if (imgHeight > usableHeight)
        {
            leastPercent = usableHeight / imgHeight;
            if (horizontalPercent > 0F)
            {
                leastPercent = Math.min(horizontalPercent, leastPercent);
            }
        }

        if (leastPercent > 0F)
        {
            img.scalePercent(leastPercent * 100);
        }

    }
*/    
}