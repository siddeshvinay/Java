/**
 * @author chethan kumar
 * This class is used for framework utilities like creating log files, log file content, create folder, etc 
 * to be used during the script generation and execution 
 */

package com.gtnexus.selenium.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.testng.Reporter;

import com.gtnexus.selenium.core.model.PropertiesHolder;

public class Framework_Utils {
		private static final String TASKLIST = "tasklist";
		private static final String KILL = "taskkill /IM ";
				
		
		/**
		 * @author Chethan Kumar
		 * @return
		 * @throws Throwable
		 */
		public static String gtnGetUniqueId() throws Exception
		{
			String ID = null;
			DateFormat dateFormat = new SimpleDateFormat("ddMMYY_hhmm");
			Date date = new Date();
			ID = System.getProperty("user.name") + dateFormat.format(date);
			System.out.println("Unique ID is generated  :  "+  "\"" +ID + "\"");
			return ID;
		}
		
		
		/**
		 * @author Chethan Kumar
		 * @param s
		 * @return
		 */
		public static boolean isInteger(String s) {
		    try { 
		        Integer.parseInt(s); 
		    } catch(NumberFormatException e) { 
		        return false; 
		    }
		    // only got here if we didn't return false
		    return true;
		}
				
		/**
		 * @author Chethan Kumar
		 * @return
		 * @throws Throwable
		 */
		public static void gtnCreateLogHeader(){
			try
			{
			DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_a");
			Date date = new Date();
			//System.out.println(dateFormat.format(date));
				File logfile = new File (System.getProperty("user.dir")+ GlobalVariables.LOGS + "GTNexus_Selenium_Log_"+dateFormat.format(date)+".txt");
				GlobalVariables.LogFileName = "GTNexus_Selenium_Log_"+dateFormat.format(date)+".txt";
				//System.setProperty("LogFilePath","Log_"+ dateFormat.format(date)+".txt");
				
				String original = "*";
				original = original + StringUtils.repeat("*", 100);
			
			    FileWriter fw = new FileWriter(logfile,true); //the true will append the new data
			    fw.write(original + System.getProperty("line.separator"));//appends the string to the file
			    fw.write("\t\t\t\t\t\t\t\tStart of log file " + System.getProperty("line.separator"));//appends the string to the file
			    fw.write(original+ System.getProperty("line.separator"));//appends the string to the file
			    fw.write("Environment : " + GlobalVariables.environment + System.getProperty("line.separator"));
			    fw.write("Release : " + GlobalVariables.release + System.getProperty("line.separator"));
			   // fw.write("Build Number : "+ GlobalVariables.buildNumber+ System.getProperty("line.separator"));
			    fw.write("User : " + System.getProperty("user.name") + System.getProperty("line.separator"));
			    fw.write("Browser: " + GlobalVariables.browserName + System.getProperty("line.separator"));
			    fw.write("BrowserVersion : " + GlobalVariables.browserVersion + System.getProperty("line.separator"));
			    fw.write(original + System.getProperty("line.separator"));//appends the string to the file
			    fw.write("\t-------------\t\t-----------\t\t------------"+System.getProperty("line.separator"));
			    fw.write("\t\t TIME \t\t\t RESULT \t\t DESCRIPTION "+System.getProperty("line.separator"));
			    fw.write("\t-------------\t\t-----------\t\t------------"+System.getProperty("line.separator"));
			    fw.close();
			}
			catch(IOException ioe)
			{
			    System.err.println("IOException: " + ioe.getMessage());
			}		
		}
		
		/**
		 * @author Chethan Kumar
		 * @return
		 * @throws Throwable
		 */
		public static void gtnEndLog(){
			try{
				 					 
				 String filepath = System.getProperty("user.dir")+GlobalVariables.LOGS +GlobalVariables.LogFileName;
				 System.out.println("filepath-->"+filepath);
				 Framework_Utils.gtnInfoLog("filepath-->"+filepath);
				 FileWriter fw1 = new FileWriter(filepath,true);
				 fw1.write(System.getProperty("line.separator") + System.getProperty("line.separator") + "Test Results are copied to :" + filepath +System.getProperty("line.separator"));
				 fw1.write("*******************************************************************************" + System.getProperty("line.separator"));
				 fw1.write("                   End of log file             " + System.getProperty("line.separator"));
				 fw1.write("*******************************************************************************"+ System.getProperty("line.separator"));
				 fw1.close();
							 				 
				}catch(IOException ioe){
					System.err.println("IOException: " + ioe.getMessage());
				}
		}
		
	
		
		
		
		
		/**
		 * @author Chethan Kumar
		 * @return
		 * @throws Throwable
		 */	
		public static void gtnWriteLog(String Status ,String msg){
		try{
			String filepath = System.getProperty("user.dir")+GlobalVariables.LOGS + GlobalVariables.LogFileName;
			 FileWriter fw1 = new FileWriter(filepath,true); //the true will append the new data
			 switch(Status){
			 case "PASS":
				 fw1.write(Framework_Utils.gtnGetTimeStamp() + " --- PASS --- " + msg+System.getProperty("line.separator"));//appends the string to the filec
				 break;
			 case "FAIL":
				 fw1.write(Framework_Utils.gtnGetTimeStamp() + " --- FAIL --- " + msg+System.getProperty("line.separator"));//appends the string to the filec
				 break;
			 case "INFO":
				 fw1.write(Framework_Utils.gtnGetTimeStamp() + " --- INFO --- " + msg+System.getProperty("line.separator"));//appends the string to the filec
				 break;
			  default:
				  fw1.write(msg+ System.getProperty("line.separator"));//appends the string to the filec
			 }		   
			    fw1.close();
			}catch(IOException ioe){
				System.err.println("IOException: " + ioe.getMessage());
			}
			
		}
		
		
		/**
		 * @author Chethan Kumar
		 * @return
		 * @throws Throwable
		 */	
		public static String gtnGetSimpleUniqueId() throws Exception
		{
			String ID = null;
			DateFormat dateFormat = new SimpleDateFormat("DDhhmmss");
			Date date = new Date();
			ID = dateFormat.format(date);
//			gtn_Logs.info("Simple unique ID is generated  :  "+  "\"" +ID + "\"");
			return ID;
		}
		
		public static void gtnPassLog (String str){
			Reporter.log("<font color = GREEN><b>" + Framework_Utils.gtnGetTimeStamp() + " :: PASS :: " + str + "</b></font>");
			Framework_Utils.gtnWriteLog("PASS", str);
		}
		
		public static void gtnFailLog (String str){
			Reporter.log("<font color = RED><i>" + Framework_Utils.gtnGetTimeStamp() + " :: FAIL :: " + str + "</i></font>");
			Framework_Utils.gtnWriteLog("FAIL", str);
		}
		
		public static void gtnInfoLog (String str){
			Reporter.log("<font color = BLUE><i>" + Framework_Utils.gtnGetTimeStamp() + " :: INFO :: " + str + "</i></font>");
			Framework_Utils.gtnWriteLog("INFO", str);
		}
		
		/**@author vananthaswamy
		 * @param str
		 */
		public static void gtnUpdateLog(String str){
			try{
				String filepath = System.getProperty("user.dir")+GlobalVariables.LOGS + GlobalVariables.LogFileName;
				 FileWriter fw1 = new FileWriter(filepath,true);
				 fw1.write(str + System.getProperty("line.separator"));
				 
				 fw1.close();
				}catch(IOException ioe){
					System.err.println("IOException: " + ioe.getMessage());
				}
		}
			
		public static float gtnTrimAmount(String strAMTValue, String strCurrancy){
			String str=((strAMTValue.replace(strCurrancy, "").replace(",", ""))).trim();
			Float f=new Float(str);
			return (f);	

		}
		
		public static float gtnStr2Float(String str){
			if (str.isEmpty()){
				return 0;
			}
			else{
				Float f=new Float((str.replace(",", "")).trim());
				return (f);	
			}
		}
		
		public static int gtnStr2Int(String str){
			if (str.isEmpty()){
				return 0;
			}
			else{
				Integer i=new Integer((str.replace(",", "")).trim());
				return (i);	
			}
		}

		public static float gtnRoundOff(float val) {
			  float p = (float)Math.pow(10,2);
			  val = val * p;
			  float tmp = Math.round(val);
			  return (float)tmp/p;
		}
		
		public static String gtnGetCurrentDateTime(String RequiredFormat){
			// yyyy/MM/dd HH:mm:ss
			DateFormat dateFormat = new SimpleDateFormat(RequiredFormat);
			Date date = new Date();
			return dateFormat.format(date);
		}
		
		public static String gtnConvertDate(String inputDate) throws ParseException{
			// yyyy/MM/dd HH:mm:ss
			
			DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date parsedDate = dateFormat1.parse(inputDate);
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			String returnDate = dateFormat.format(parsedDate);
			return returnDate;
		}
		
		public static String gtnGetFullformOfStatus(String code){
			
			String statusCode=code;
		
			if (statusCode==null){
				return "Payment Transmitted";
			}
			switch (statusCode){
			case "PTRG" : return "Payment Transmitted";
			case "PDNG" : return "Pending";
			case "ACTC" : return "Accepted: Technical Validation";
			case "ACCP" : return "Accepted: Service Validation";
			case "ACWC" : return "Accepted: Accepted with change";
			case "ACSP" : return "Accepted: Settlement In Process";
			case "ACSC" : return "Accepted: Settlement Complete";
			case "RJCT" : return "Rejected";
			default : Reporter.log("No matching status found for " + statusCode);
				return null;
			}
		}
		

		public static String gtnGetCurrencyCode(String inputCurrency){
			String currencyCode[]=inputCurrency.split(" ");
			return currencyCode[0];
		}
		
		/**
		 * @author Chethan Kumar
		 * @throws java.io.IOException
		 */
		public static String gtnConvertToAmountfield(String valueFromDB){
				double money=Double.parseDouble(valueFromDB);
				NumberFormat formatter = NumberFormat.getCurrencyInstance();
				String money1=formatter.format(money);
				String newMoney = money1.substring(1, money1.length());
				return newMoney;
			}

		/**
		 * @author Chethan Kumar
		 * @param sourceLocation
		 * @throws java.io.IOException
		 */
		public static void gtnCopyFolder(File sourceLocation , File targetLocation)   throws IOException {

			if (sourceLocation.isDirectory()) {
				if (!targetLocation.exists()) {
					targetLocation.mkdir();
				}

				String[] children = sourceLocation.list();
				for (int i=0; i<children.length; i++) {
					gtnCopyFolder(new File(sourceLocation, children[i]),
							new File(targetLocation, children[i]));
				}
			} else {
				InputStream in = new FileInputStream(sourceLocation);
				OutputStream out = new FileOutputStream(targetLocation);
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			}
		}

		/**
		 * @author Chethan Kumar
		 * @throws java.io.IOException
		 */
		public static void gtnCopyFile(File sourceFile, File destFile) throws IOException {
		    if(!destFile.exists()) {
		        destFile.createNewFile();
		    }
		    FileChannel source = null;
		    FileChannel destination = null;
		    try {
		        source = new FileInputStream(sourceFile).getChannel();
		        destination = new FileOutputStream(destFile).getChannel();

		        // previous code: destination.transferFrom(source, 0, source.size());
		        // to avoid infinite loops, should be:
		        long count = 0;
		        long size = source.size();
		        while((count += destination.transferFrom(source, count, size-count))<size);
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

		/**
		 * @author Chethan Kumar
		 * @param sourceLocation
		 * @throws java.io.IOException
		 */
		public static void gtnCreateFolder(File sourceLocation)throws IOException{
				if (!sourceLocation.exists()) {
					sourceLocation.mkdir();
				}
		}

		/**
		 * @author Chethan Kumar
		 * @throws java.io.IOException
		 */
		public static void gtnDeleteFolder(String location){

			File directory = new File(location);
			try{
				if(!directory.exists()){
					System.out.println(location + " Directory does not exist.");
				}
				else{
					if(directory.isDirectory()){
						directory.delete();
						System.out.println("Directory is deleted : " + directory.getAbsolutePath());
					}
				}
			}
			catch(Throwable t){
				Reporter.log(t.getMessage());
			}
		}

		/**
		 * @author Chethan Kumar
		 * @throws java.io.IOException
		 */
		public static String gtnCalDateTimeDiff(Date st, Date et){
	            // Custom date format
	        @SuppressWarnings("unused")
			SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
	        float diff = et.getTime() - st.getTime();
	        /*long diffSeconds = diff / 1000 % 60;
	        long diffMinutes = diff / (60 * 1000) % 60;
	        long diffHours = diff / (60 * 60 * 1000); */
	        //String totalDiff= Long.toString(diffHours) +":"+Long.toString(diffMinutes) +":"+Long.toString(diffSeconds);
	        float totalDif = diff/1000;
	        String totalDiff = new String().valueOf(totalDif);
	        return totalDiff;

		}

		/**
		 * @author Chethan Kumar
		 * @throws java.io.IOException
		 */
		public static String gtnGetTimeStamp(){
			  SimpleDateFormat df = new SimpleDateFormat( "dd/MM/yyyy hh:mm:ss a" ) ;
			     df.setTimeZone( TimeZone.getTimeZone( "IST" )  ) ;
				return(df.format(new Date()));
		}

		/**
		 * @author Chethan Kumar
		 * @throws java.io.IOException
		 */
		public static boolean gtnIsProcessRuning(String serviceName) throws Exception {
		 Process p = Runtime.getRuntime().exec(TASKLIST);
		 BufferedReader reader = new BufferedReader(new InputStreamReader(
		   p.getInputStream()));
		 String line;
		 while ((line = reader.readLine()) != null) {
		  //System.out.println(line);
		  if (line.contains(serviceName)) {
		   return true;
		  }
		 }

		 return false;

		}

		/**
		 * @author Chethan Kumar
		 * @throws java.io.IOException
		 */
		public static void gtnkillProcess(String serviceName){
			String ProcesstoKill = null;
			if(serviceName.equalsIgnoreCase("*iexplore")){
				ProcesstoKill = PropertiesHolder.properties.get("config").getProperty("IE_DRIVER");
			}else if(serviceName.equalsIgnoreCase("*chrome")){
				ProcesstoKill = PropertiesHolder.properties.get("config").getProperty("CHROME_DRIVER");
			}
		try{	
		  if (gtnIsProcessRuning(ProcesstoKill)) {
			  Runtime.getRuntime().exec(KILL + ProcesstoKill);
			 }
			}catch(Exception tmp){
				//tmp.printStackTrace();
				Framework_Utils.gtnInfoLog(tmp.getMessage());
				
				
			}
		}
		

	/**
	 * @exception
	 * @author Chethan Kumar
	 * @throws java.io.IOException
	 * 
	 * */
		public static void gtnSendEmail(String sourceFile){
			// Common variables
				String host = "smtp-relay.infor.com";
			//String host = "gthqexch1.gtnexus.local";
			//	String host = "qarelay1.gtnexus.local";
									
				String from = PropertiesHolder.properties.get("config").getProperty("EMAIL_FROM");
				String to = PropertiesHolder.properties.get("config").getProperty("EMAIL_TO");
				String cc = PropertiesHolder.properties.get("config").getProperty("EMAIL_CC");
				String[] toaddress = to.split(";");
				String[] ccaddress = cc.split(";");
				// Set properties
				Properties props = new Properties();
				props.put("mail.smtp.host", host);
				props.put("mail.debug", "true");								
		
				// Get session
				Session session = Session.getInstance(props);				
			     
				
				try {
				    // Instantiate a message
				    Message msg = new MimeMessage(session);
				    // Set the FROM message
				    msg.setFrom(new InternetAddress(from));
				    // The recipients can be more than one so we use an array but you can
				    // use 'new InternetAddress(to)' for only one address.
				    //InternetAddress[] address = {new InternetAddress(to)};
				    InternetAddress[] address = new InternetAddress[toaddress.length];
				    for(int i =0; i< toaddress.length; i++)
				    {
				        address[i] = new InternetAddress(toaddress[i]);
				    }
				    msg.setRecipients(Message.RecipientType.TO, address);
				    //cc list
				    InternetAddress[] address1 = new InternetAddress[ccaddress.length];
				    for(int j =0; j< ccaddress.length; j++)
				    {
				        address1[j] = new InternetAddress(ccaddress[j]);
				    }
				    msg.setRecipients(Message.RecipientType.CC, address1);
				    	    
				    
				    // Set the message subject and date we sent it.
				    //msg.setSubject(PropertiesHolder.properties.get("config").getProperty("EMAIL_SUBJECT")+" "+GlobalVariables.GTNID);
				    msg.setSubject(PropertiesHolder.properties.get("config").getProperty("EMAIL_SUBJECT")+" "+GlobalVariables.GTNID+"- "+GlobalVariables.TCTYPE_Val);
				    msg.setSentDate(new Date());
				    //fill message
				    // create the message part
				      String msgbody = PropertiesHolder.properties.get("config").getProperty("EMAIL_MSG");
				      MimeBodyPart body = new MimeBodyPart();
				      String resultspath = GlobalVariables.RESULTS_PATH.concat(GlobalVariables.PRODUCT+"/"+GlobalVariables.environment+"/"+GlobalVariables.release+"/" +GlobalVariables.exeMachineName+"/" +System.getProperty("user.name"));
				    //  body.setText("Hi All, " + "\n\n" + msgbody + "\n" + "For more details please refer Log file and Results file at shared location." +"\n\n" + resultspath +"\n\n\n" + "Regards, " + "\n" +"GTNexus Test Automation Team");
				      
				      String filepath = System.getProperty("user.dir")+GlobalVariables.LOGS +GlobalVariables.LogFileName;
				      System.out.println("Log File Path -->"+filepath);				    
				      body.setText("Hi All, " + "\n\n" + msgbody + "\n" + "For more details please refer Log file and Results file at shared location." +"\n\n" + resultspath +"\n\n\n" + "Regards, " + "\n" +"GTNexus Test Automation Team"+"\n\n"+"Log File Location: "+filepath +"\n\n\n\n" + "NOTE: This is an automatically generated email, Kindly do not reply.");	
				      
				      MimeBodyPart attach = new MimeBodyPart();
		            // Part two is attachment
		              DataSource source = new FileDataSource(sourceFile);
		              System.out.println(source.getName());
		              attach.setDataHandler(new DataHandler(source));
		              //attach.setFileName(GlobalVariables.LogFilePath);		             
		              System.out.println(GlobalVariables.LogFileName);
		              attach.setFileName(GlobalVariables.LogFileName);
		              attach.setDisposition(MimeBodyPart.ATTACHMENT);
		              Multipart multipart = new MimeMultipart();
		              multipart.addBodyPart(body);
		              multipart.addBodyPart(attach);
				    // Set message content
				      //msg.setContent(multipart, "text/html");
				      msg.setContent(multipart);
				    // Send the message
				    Transport.send(msg);
				}
				catch (MessagingException mex) {
				    mex.printStackTrace();
				}
		}

			/**
			 * @author Chethan Kumar
			 * Method to update Test case execution status into Automation DB
			 * @param
			 * @throws Exception
			 */		
			public static void gtnUpdateTCExeStatusToDB(String tcid, String tdisc, String st, String et, String ExeTime, XSSFCell testType, String Status, XSSFCell GTNid,String browserType) throws Exception {
				Connection con = null;
			    try {
			          
			    	DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_a");
					Date date = new Date();	
			    	
			    	  Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			           if (con==null){
			        	   //con=java.sql.DriverManager.getConnection("jdbc:sqlserver://QAAUTODB1B;databaseName=QA_AutomationResults;integratedSecurity=true");
			                 con =java.sql.DriverManager.getConnection(PropertiesHolder.properties.get("config").getProperty("QaAutoDB")); 	
			           }
			           StringBuffer Query= new StringBuffer();
			           				            
			        //   String[] release = GlobalVariables.release.split("Q");
			        //   String rel=release[0].trim().replaceAll(" ", "_");
			           			           
			           String rel=GlobalVariables.release;			           
			           			        			           
			           String[] build=new String[2];
			     			            
			           if (GlobalVariables.buildNumber!=null) {
			        	   build=GlobalVariables.buildNumber.split("\\("); 			        	   
					   }else{						   
						   build[0]="Could not capture the build number";
					   }
			           				          			           
			           Query.append("Insert into tt_automation_results_summary ").append("(gtn_product_name,environment,release,date_of_execution,exec_machine_name,exec_user_name,tc_id,tc_summary,status,exec_start_time,exec_end_time,time_taken,build_number,copied_results_path,comments,tc_type, testlink_id, browser_type)");
			           Query.append(" values ('").append(GlobalVariables.PRODUCT).append("', '").append(GlobalVariables.environment).append("', '").append(rel).append("', '" ).append(st).append("' , '").append(GlobalVariables.exeMachineName.toUpperCase()).append("' , '").append(System.getProperty("user.name")).append("' , '").append(tcid).append("' , '").append(tdisc).append("' , '").append(Status).append("' , '").append(st).append("' , '").append(et).append("' , '").append(ExeTime).append("' , '").append(build[0]).append("' , '").append(GlobalVariables.RESULTS_PATH+GlobalVariables.PRODUCT + "\\" +GlobalVariables.environment + "\\" +GlobalVariables.release+"\\" +GlobalVariables.exeMachineName+"\\" +System.getProperty("user.name") + "\\" + dateFormat.format(date)).append(" ','Selenium test execution', '").append(testType).append("' ,'").append(GTNid).append("' ,'").append(browserType).append("')");			           
			           Statement statement = null;
			           Thread.sleep(1000);
			           System.out.println("Query"+Query);

			           if (con != null) {
			                 statement = con.createStatement();
			                 boolean updated=statement.execute(Query.toString());
			                 if (!updated){
			                	 Framework_Utils.gtnUpdateLog("----------------------------------------------------------------------------------------------------------------");
			                	 Framework_Utils.gtnUpdateLog("TC - "+ tcid +" execution status is updated to Automation DB ");
			                 }else{
			                	 Framework_Utils.gtnUpdateLog("----------------------------------------------------------------------------------------------------------------");
			                	 Framework_Utils.gtnFailLog("TC - "+ tcid +" execution status is NOT updated to Automation DB ");
			                 }	 
			           }
			           statement.close();
			    } catch (Exception e) {
			    	Framework_Utils.gtnUpdateLog("----------------------------------------------------------------------------------------------------------------");
			    	Framework_Utils.gtnFailLog("Could't establish DB connection for the envt Auto DB ");
			    		e.printStackTrace();
			    }
			    finally {
			           con.commit();
			           con.close();
			    }

					
			}
		
		/**
		 * @author Chethan Kumar
		 * @throws IOException
		 */
			public static void gtnCopyResultsToCommonFolder() throws IOException{
				DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss_a");
				Date date = new Date();		
				File SourceFile= new File (System.getProperty("user.dir")+GlobalVariables.TESTNG_RESULT_PATH + GlobalVariables.TESTNG_RESULT);
				Framework_Utils.gtnCreateFolder(new File(GlobalVariables.RESULTS_PATH + "/" + GlobalVariables.PRODUCT+"/"));
				Framework_Utils.gtnCreateFolder(new File(GlobalVariables.RESULTS_PATH + "/" + GlobalVariables.PRODUCT+"/" + GlobalVariables.environment+"/"));
				Framework_Utils.gtnCreateFolder(new File(GlobalVariables.RESULTS_PATH + "/" + GlobalVariables.PRODUCT+"/" +GlobalVariables.environment + "/" +GlobalVariables.release+"/"));
				Framework_Utils.gtnCreateFolder(new File(GlobalVariables.RESULTS_PATH + "/" + GlobalVariables.PRODUCT+"/" +GlobalVariables.environment + "/" +GlobalVariables.release+"/" +GlobalVariables.exeMachineName+"/"));
				Framework_Utils.gtnCreateFolder(new File(GlobalVariables.RESULTS_PATH + "/" + GlobalVariables.PRODUCT+"/" +GlobalVariables.environment + "/" +GlobalVariables.release+"/" +GlobalVariables.exeMachineName+"/" +System.getProperty("user.name")+"/"));
				Framework_Utils.gtnCreateFolder(new File(GlobalVariables.RESULTS_PATH + "/" + GlobalVariables.PRODUCT+"/" +GlobalVariables.environment + "/" +GlobalVariables.release+"/" +GlobalVariables.exeMachineName+"/" +System.getProperty("user.name")+"/"+ dateFormat.format(date)));	

				// html report.
				File DF= new File (GlobalVariables.RESULTS_PATH +"/" + GlobalVariables.PRODUCT+ "/" +GlobalVariables.environment + "/" +GlobalVariables.release+"/" +GlobalVariables.exeMachineName+"/" +System.getProperty("user.name")+ "/" +dateFormat.format(date)+"/"+ GlobalVariables.TESTNG_RESULT);
				Framework_Utils.gtnCopyFile(SourceFile,DF);			
				Framework_Utils.gtnInfoLog("Executed TestLog Report are copied to :" + DF.getPath());
				GlobalVariables.RESULTS_File_PATH=DF.getPath();
												
				// Log file
				File LogFile= new File (System.getProperty("user.dir")+GlobalVariables.LOGS + GlobalVariables.LogFileName);
				File DesFile = new File (GlobalVariables.RESULTS_PATH +"/" + GlobalVariables.PRODUCT+ "/" +GlobalVariables.environment + "/" +GlobalVariables.release+"/" +GlobalVariables.exeMachineName+"/" +System.getProperty("user.name")+ "/" +dateFormat.format(date) + "/" + GlobalVariables.LogFileName);
				Framework_Utils.gtnCopyFile(LogFile,DesFile );
				Framework_Utils.gtnInfoLog("Executed TestLog Results are copied to :" + DesFile.getPath());
				
				// Data sheet.				
				
				File DatsheetFile=new File (GlobalVariables.KEYWORKFILEPATH);
				String[] DatasheetName=GlobalVariables.KEYWORKFILEPATH.split("\\\\");
				String destfileName=DatasheetName[DatasheetName.length-1];
				System.out.println("destfileName-->"+destfileName);	
																		
				File DestFile = new File (GlobalVariables.RESULTS_PATH +"/" + GlobalVariables.PRODUCT+ "/" +GlobalVariables.environment + "/" +GlobalVariables.release+"/" +GlobalVariables.exeMachineName+"/" +System.getProperty("user.name")+ "/" +dateFormat.format(date) +"/" + destfileName);
				Framework_Utils.gtnCopyFile(DatsheetFile,DestFile );
				Framework_Utils.gtnInfoLog("Executed Data Dheet File are copied to :" + DestFile.getPath());	
				
				// Send E-mail
				//if(PropertiesHolder.properties.get("config").getProperty("SEND_EMAIL").equalsIgnoreCase("true")){
						
				System.out.println("SendEmail-->"+GlobalVariables.SendEmail);				
				if(GlobalVariables.SendEmail){				
					Framework_Utils.gtnInfoLog("Sending Email to users");
					Framework_Utils.gtnSendEmail(System.getProperty("user.dir")+GlobalVariables.LOGS + GlobalVariables.LogFileName);
				}else{					
					Framework_Utils.gtnWriteLog("INFO", "Sending Email to users set as false.");
				}
				//End the log file
				Framework_Utils.gtnEndLog();
			}
			
			
		
 /**
 * @author Chethan Kumar
 * @param srcFolder
 * @return
 * @throws Exception 
  */
     
     public static String gtnGetFileFromFolder(String srcFolder) throws Exception{
            String flag = null;
            File folder = new File(srcFolder);
            int filecnt = 0;
            int counter = 0;
            do{
                  File[] listOfFiles = folder.listFiles();
                  filecnt = listOfFiles.length;
                  Thread.sleep(1000);         
                counter++;
            }while(filecnt<1 && counter<10);
                                 
            if(filecnt<1){
                  Framework_Utils.gtnFailLog("there are no files in tmp folder");
                  return null;
            }
            
            try{   
                  File[] listOfFiles = folder.listFiles();
                  if(listOfFiles.length==1){
                         if(listOfFiles[0].isFile()){
                          //System.out.println("File " + listOfFiles[0].getName());
                          flag=listOfFiles[0].getName().trim();
                         }  
                   }else{
                     Framework_Utils.gtnFailLog("Failed to get file as there multiple files in location << " + srcFolder +" >>");
                     flag = null;
                  }
            }catch(Exception e){
               Framework_Utils.gtnFailLog("Failed to get file " + e.getMessage()); 
            }
                
          return flag;
     }    
     

/**
 * @author Chethan Kumar
 * @param folderPath
 */
 public static void gtnDeleteAllFilesFromFolder(String folderPath){
        File file = new File(folderPath);         
  String[] myFiles;       
      if(file.isDirectory()){   
          myFiles = file.list();   
          for (int i=0; i<myFiles.length; i++) {   
              File myFile = new File(file, myFiles[i]);    
              myFile.delete(); 
          }   
       }   
 }


/**
 * @author Chethan Kumar
 * @param srcLocation
 * @param desLocation
 */
 public static boolean gtnMoveFile(String srcLocation,String desLocation){
        boolean retStatus = false;
   try{ 
        // File (or directory) to be moved
        File file = new File(srcLocation);
        // Destination directory
        File dir = new File(desLocation);
        // Move file to new directory
        boolean success = file.renameTo(dir);
        if (success) {
              Framework_Utils.gtnPassLog("Successfull moved file - " + desLocation);
              retStatus = true;
        }else{
              Framework_Utils.gtnFailLog("Failed to move file - " + desLocation);
        }
   }catch(Exception e){
          Framework_Utils.gtnFailLog("Failed to move file - " + e.getMessage());
   }
   return retStatus;
 }                   
			
             
             
}// class ends here.
