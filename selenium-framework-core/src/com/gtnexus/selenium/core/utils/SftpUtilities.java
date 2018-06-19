package com.gtnexus.selenium.core.utils;

import java.util.Properties;
import java.io.File;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

public class SftpUtilities {
	
	static Properties props;
	
	
 /**
  * @author Satish
  * This method is responsible to download the file from sftp server.	
  * @author ckumar2/Satish.D
  * @param propertiesFilename
  * @param fileToFTP
  * @param client
  * @return
  */
	
	
	 public static boolean sFTPDownload(String propertiesFilename, String fileToFTP,String client){

		  props = new Properties();
		  StandardFileSystemManager manager = new StandardFileSystemManager();

		  try {
		  	   
		   String serverAddress = "10.237.210.33";
		   String userId = "qa\\srvqaftpdroprw";
		   String password = "D#rt3$zt3rw";
		   String sourceremoteDirectory ="transfer\\out\\freight_contracting\\xml\\";
		   String DestinationDirectory = "C:\\AUTOMATION_FRAMEWORK\\DATA\\XML_Files\\ASN\\";
		  
		   //Initializes the file manager
		   manager.init();
		   		   
		   //Setup our SFTP configuration
		   FileSystemOptions opts = new FileSystemOptions();
		   SftpFileSystemConfigBuilder.getInstance().setPreferredAuthentications(opts,"publickey,keyboard-interactive,password");
		   SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
		   SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
		   SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

		   //Create the SFTP URI using the host name, userid, password,  remote path and file name
		   String sftpUri = "sftp://" + userId + ":" + password +  "@" + serverAddress + "/" + sourceremoteDirectory + fileToFTP;
		   System.out.println("file-->"+sftpUri);

		   // Create local file object
		   String filepath = DestinationDirectory +  fileToFTP;
		   File file = new File(filepath);
		   FileObject localFile = manager.resolveFile(file.getAbsolutePath());
		   System.out.println("File download ......"+localFile);
		   Framework_Utils.gtnInfoLog("File download ......"+localFile);
		   // Create remote file object
		  	       
		   FileObject remoteFile = manager.resolveFile(sftpUri, opts);			     
		   System.out.println("File download ......"+remoteFile);
		   // Copy local file to sftp server
		   localFile.copyFrom(remoteFile, Selectors.SELECT_SELF);
		   System.out.println("File Download is successfull.");
		   Framework_Utils.gtnInfoLog("File Download is successfull.");

		  }
		  catch (Exception ex) {
		   ex.printStackTrace();
		   return false;
		  }
		  finally {
		   manager.close();
		  }

		  return true;
		 }

 /**
  *  This method is responsible to Upload the file to sftp server.
  * @author ckumar2/Satish.D
  * @param propertiesFilename
  * @param fileToFTP
  * @param client
  * @return
  */
	 
	 
	 public static boolean sFTPUpload(String propertiesFilename, String fileToFTP,String client){

		  props = new Properties();
		  StandardFileSystemManager manager = new StandardFileSystemManager();

		  try {
		   	   
		   String serverAddress = "10.237.210.33";
		   String userId = "qa\\srvqaftpdroprw";
		   String password = "D#rt3$zt3rw";
		   String sourceremoteDirectory ="mssgserver/in/purchase_order/xml/";
		   String DestinationDirectory = System.getProperty("user.dir")+"\\resources\\datafiles\\uploads\\";

		   //check if the file exists
		   String filepath = DestinationDirectory +  fileToFTP;
		   File file = new File(filepath);
		   if (!file.exists())
		    throw new RuntimeException("Error. Local file not found");

		   //Initializes the file manager
		   manager.init();
		   
		   //Setup our SFTP configuration
		   FileSystemOptions opts = new FileSystemOptions();
		   SftpFileSystemConfigBuilder.getInstance().setPreferredAuthentications(opts,"publickey,keyboard-interactive,password");
		   SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
		   SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, true);
		   SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);

		   //Create the SFTP URI using the host name, userid, password,  remote path and file name
		   String sftpUri = "sftp://" + userId + ":" + password +  "@" + serverAddress + "/" + sourceremoteDirectory + fileToFTP;
		   System.out.println("file-->"+sftpUri);

		   // Create local file object
		   FileObject localFile = manager.resolveFile(file.getAbsolutePath());
		   System.out.println("File uploading ......"+localFile);
		   Framework_Utils.gtnInfoLog("File uploading ......"+localFile);
		   // Create remote file object
		  	      	       
		   FileObject remoteFile = manager.resolveFile(sftpUri, opts);			        
		   System.out.println("File uploading ......"+remoteFile);
		   Framework_Utils.gtnInfoLog("File uploading ......"+remoteFile);

		   // Copy local file to sftp server
		   remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);
		   System.out.println("File upload successful");
		   Framework_Utils.gtnInfoLog("File upload successfully.");

		  }
		  catch (Exception ex) {
		   ex.printStackTrace();
		   return false;
		  }
		  finally {
		   manager.close();
		  }

		  return true;
	}
	 
		

	public static String msgServerPath(String filetype){
		
		String msgSrvrPath = null;
		
		switch(filetype){
		case "po":				//for purchase order
		msgSrvrPath="";
		break;
		
		case "inboundso":      //for inbound purchase order
		msgSrvrPath="";
		break;
		
		case "inboundsp":		//for inbound shipment plan
		msgSrvrPath="";
		break;
		
		case "inboundasn":		//for inbound ASN
		msgSrvrPath="";
		break;
		
		case "outboundsp":		//for Shipment plan outbound
		msgSrvrPath="";
		break;
		
		default:
		Framework_Utils.gtnInfoLog("Invalid FileType");	
		}
		
		return msgSrvrPath;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}// class ends here.

