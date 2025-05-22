/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.studiodomino.scanner.twain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * @author Donato
 */
public class JarLib
{
  public static boolean load(Class paramClass, String paramString)
  {
    try
    {
      loadX(paramClass, paramString);
      return true;
    } catch (Exception localException1) {
      System.out.println("JarLib.load\n\tException = " + localException1.getMessage());
      System.err.println("JarLib.load\n\tException = " + localException1.getMessage());
      localException1.printStackTrace();
    } catch (Error localError1) {
      System.out.println("JarLib.load\n\tError = " + localError1);
      System.err.println("JarLib.load\n\tError = " + localError1);
      localError1.printStackTrace();
    }
    try {
      System.loadLibrary(paramString);
      System.out.println("JarLib.load: Successfully loaded library [" + paramString + "] from some default system folder");
      return true;
    } catch (Exception localException2) {
      System.out.println("JarLib.load\n\tException = " + localException2.getMessage());
      System.err.println("JarLib.load\n\tException = " + localException2.getMessage());
    } catch (Error localError2) {
      System.out.println("JarLib.load\n\tError = " + localError2);
      System.err.println("JarLib.load\n\tError = " + localError2);
      localError2.printStackTrace();
    }
    return false;
  }

  private static void loadX(Class paramClass, String paramString) throws IOException, UnsatisfiedLinkError {
    String str1 = System.mapLibraryName(paramString);
    URL localURL = paramClass.getResource(getOsSubDir() + "/" + str1);
    if (localURL == null)
      throw new UnsatisfiedLinkError(JarLib.class.getName() + ".loadX: Could not find library [" + str1 + "]");
    try
    {
      URI localURI = new URI(localURL.toString());
      String str2 = localURI.getScheme();
      if (str2.equals("file")) {
        System.load(new File(localURI).getAbsolutePath());
        System.out.println("JarLib.load: Successfully loaded library [" + localURL + "] from mmsc standard file location");
      } else if ((str2.equals("jar")) || (str2.equals("nbjcl")))
      {
        File localFile1 = new File(System.getProperty("java.io.tmpdir"), "mmsc"); localFile1.mkdirs();

        File[] arrayOfFile = localFile1.listFiles();
        for (int i = 0; i < arrayOfFile.length; i++) {
          if (arrayOfFile[i].getName().endsWith(str1)) {
            arrayOfFile[i].delete();
          }
        }

        File localFile2 = File.createTempFile("mmsc", str1, localFile1);
        extract(localFile2, localURL);
        System.load(localFile2.getAbsolutePath());

        System.out.println("JarLib.load: Successfully loaded library [" + localURL + "] from jar file location");
      } else {
        throw new UnsatisfiedLinkError(JarLib.class.getName() + ".loadX:\n\tUnknown URI-Scheme [+scheme+]; Could not load library [" + localURI + "]");
      }
    } catch (URISyntaxException localURISyntaxException) {
      throw new UnsatisfiedLinkError(JarLib.class.getName() + ".loadX:\n\tURI-Syntax Exception; Could not load library [" + localURL + "]");
    }
  }

  private static void extract(File paramFile, URL paramURL) throws IOException {
    InputStream localInputStream = paramURL.openStream();
    FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
    byte[] arrayOfByte = new byte[4096];
    int i = 0;
    while ((i = localInputStream.read(arrayOfByte)) > 0) {
      localFileOutputStream.write(arrayOfByte, 0, i);
    }
    localFileOutputStream.close();
    localInputStream.close();
  }

  private static String getOsSubDir()
  {
    String str1 = System.getProperty("os.name");
    String str2;
    if (str1.startsWith("Linux")) {
      str2 = System.getProperty("os.arch");
      if (str2.endsWith("64")) {
        return "lin64";
      }
      return "lin32";
    }

    if (str1.startsWith("Windows")) {
      str2 = System.getProperty("os.arch");
      if (str2.endsWith("64")) {
        return "win64";
      }
      return "win32";
    }

    if (str1.startsWith("Mac")) return "mac";
    return "";
     
  }
}