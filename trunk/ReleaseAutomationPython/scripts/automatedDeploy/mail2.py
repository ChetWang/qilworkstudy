from com.db.automateddeploy.util import EmailSender
from java.lang import StringBuffer
import string

from urllib import urlencode

#---------------------------------------------------------------------------
def sendMailNotification():
	
	global mail_disabled
	global mail_redirect
	global mail_smtphost
	global mail_from
	global mail_cc
	
	global errors
	global screen_output
	global BackupLocation
	
	global xml_filename
	global xml_filepath
	global taskName
	
	sender = EmailSender(mail_smtphost, mail_from, mail_cc, mail_disabled, mail_redirect)
	email_message = StringBuffer()
	log_message = StringBuffer()
	
	mail_to = 'feng.zhu@db.com,espear.platform-ops@db.com'
	
	# -----------------------------------------------------
	# mail header
	# -----------------------------------------------------
	email_message.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" ")
	email_message.append("\"http://www.w3.org/TR/html4/loose.dtd\"> \n")
	email_message.append("<html><head><style type='text/css'>\n")
	
	email_message.append(".Courier1 {font-family: 'Courier New'; font-size: 13px; } \n")
	email_message.append(".Courier2 {font-family: 'Courier New'; font-size: 14px; } \n")
	
	email_message.append(".Red1 {font-family: 'Courier New'; font-size: 13px; color: #FF0000;}\n")
	email_message.append(".Red2 {font-family: 'Courier New'; font-size: 14px; color: #FF0000;}\n")
	
	email_message.append(".log1 {font-family: 'Courier New'; font-size: 13px; color: #666666;}\n")
	email_message.append(".log2 {font-family: 'Courier New'; font-size: 14px; color: #2828FF;}\n")
	
	email_message.append(".Cyan1 {font-family: 'Courier New'; font-size: 13px; color: #4F9D9D;}\n")
	email_message.append(".Cyan2 {font-family: 'Courier New'; font-size: 14px; color: #4F9D9D;}\n")
	
	email_message.append("</style></head><body>\n");
	
		
	# -----------------------------------------------------
	# mail body - Status report, including error messages
	# -----------------------------------------------------
	email_message.append("<div class='Courier1'>Hi,</div><br>")
	
	
 	mail_subject = "[ERA] Web Deployment "
 	
	if( len(errors) != 0 ):
		
		mail_subject = mail_subject + "has failed"
		
		email_message.append("<div class='Courier1'>The deployment of application [ ")
		email_message.append(taskName)
		email_message.append(" ] has failed on server ")
		email_message.append(msg_hostname + ". Please check the below errors:</div>")
		email_message.append("<br><div class='Red1'>")
		for error_item in errors:
			email_message.append(`error_item` + "<br>\n")
	
		email_message.append("</div><br><br>")
		
	else:		
		mail_subject = mail_subject + "completed successfully"
		
		email_message.append("<div class='Courier1'>The deployment of application [ ")
		email_message.append(taskName)
		email_message.append(" ] is completed successfully on server ")
		email_message.append(msg_hostname + "</div>")
		email_message.append("</div><br><br>")
		
	# end if
	
	mail_subject = mail_subject + " [ "+ taskName + " on " + msg_hostname + " ]"
	
	
	# -----------------------------------------------------
	# mail body - xml file content
	# -----------------------------------------------------	
	email_message.append("<div class='Courier1'>The deployment xml file: " + xml_filename + "</div>")
	
	email_message.append("<br><div class='Cyan1'>")
	f = open(xml_filename)
	while true:
		line = f.readline()		
		if len(line) == 0:
			break
		else:
			# get component Email address
			mail_string_index = line.find("<ComponentEmail>")
			if ( mail_string_index > -1):
				mail_string_index_Str = mail_string_index + 16
				mail_string_index_End = line.find("</ComponentEmail>")
				mail_to = line[mail_string_index_Str:mail_string_index_End]
						
			# convert to HTML
			# make it works on all platforms
			line = line.replace(lineSeparator, '') + lineSeparator
			line = line.replace( "<", "&lt;" )
			line = line.replace( ">", "&gt;" )
			line = line.replace( " ", "&nbsp;" )
			line = line.replace( "\t", "&nbsp;&nbsp;&nbsp;&nbsp;" )
			line = line.replace( lineSeparator, "<br>" )
			
		#end if	
		email_message.append( line )
	f.close() 
	
	email_message.append("</div><br><br>")
	
	# -----------------------------------------------------
	# mail header - deployment logs
	# -----------------------------------------------------
	log(MAJOR_, "Sending Email to: " + mail_to )
	
	email_message.append("<div class='Courier1'>Please review the deploy log:</div>")
	email_message.append("<br><div class='log1'>")
	
	for screen_line in screen_output:
		log_message.append(screen_line + " #LINE#DELIMITOR# ")
		
	logs = log_message.toString()
	logs = string.replace( logs, "<", "&lt;" )
	logs = string.replace( logs, "#LINE#DELIMITOR#", "<br>\n" )
	
	email_message.append(logs)
	
	email_message.append("</div>")	
	
	
	# -----------------------------------------------------
	# mail footer
	# -----------------------------------------------------	
	email_message.append("<br><br>\n");
	email_message.append("<div class='Courier1'>");
	email_message.append("<hr><br>");
	email_message.append("This message is automatically generated by Release Automation script.</div>");
		
	email_message.append("<br></body></html>")
	
	
	# -----------------------------------------------------
	# dump screen_output to log file
	#   - save the logs to BackupLocation before sending mail.
	# -----------------------------------------------------
	
	logfile_dump = open( BackupLocation + "/deploy.log", "a")
	logfile_dump.writelines(screen_output)
	logfile_dump.flush()
	logfile_dump.close()
	
	# -----------------------------------------------------
	# sendEmail
	# -----------------------------------------------------
	try:
		sender.sendEmail(mail_to, mail_subject, email_message.toString() )
	except:
		log(DEBUG_, "sendEmail failed.")
	#endTry
# end of sendMail
#---------------------------------------------------------------------------