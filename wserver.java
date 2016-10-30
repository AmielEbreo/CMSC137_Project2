/*
Amiel O. Ebreo
2012-48245
CMSC 137 Project 2: Mini-web server programmed in Java using sockets

references used:
http://www.java2s.com/Code/Java/Network-Protocol/ASimpleWebServer.htm

*/

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class wserver{

 
	protected void start(){
		ServerSocket server;

		System.out.println("Creating web server...");
		
		 //create a server running on port 80, check if there's another device connected to it
		try{
		  
			server = new ServerSocket(80);
		} 
		catch (Exception e){
			System.out.println("Error: " + e);
			return;
		}
		
		System.out.println("Waiting for connection...");
		
		
		//access localhost:80 on a web browser to establish a connection
		for (;;) {
			try{
			
				Socket remote = server.accept();
				String[] headers = { };
				
				//reader and writer for request header
				BufferedReader in = new BufferedReader(new InputStreamReader(remote.getInputStream()));
				
				
				PrintWriter out = new PrintWriter(remote.getOutputStream());

			   String req = in.readLine(); //read each line of text from the bufferedreader
			   if(req.length() > 0){
				   headers = req.split(" ");//store header info
				   
			   }
			   
			   //parse the other part of the header
				String body ="<html><table id='header'><tr><td>Method:</td><td> "+headers[0]+"</td></tr><tr><td> Request URI:</td><td> "+headers[1]+"</td></tr> <tr><td> HTTP Version:</td><td> "+headers[2]+"</td></tr>";
				String[] temp;
				temp=in.readLine().split(" ");
				body=body+"<tr><td>he"+temp[0]+"</td><td>"+temp[1]+"</td></tr>"; //HOST
				temp=in.readLine().split(" ");
				body=body+"<tr><td>"+temp[0]+"</td><td>"+temp[1]+"</td></tr>"; //CONNECTION
				temp=in.readLine().split(" ");
				body=body+"<tr><td>"+temp[0]+"</td><td>"+temp[1]+"</td></tr>"; //UPGRADE-INSCURE-REQUESTS
				temp=in.readLine().split(" ");
				body=body+"<tr><td>"+temp[0]+"</td><td>"+temp[1]+"</td></tr>"; //USER-AGENT
				temp=in.readLine().split(" ");
				body=body+"<tr><td>"+temp[0]+"</td><td>"+temp[1]+"</td></tr>"; //ACCEPT
				temp=in.readLine().split(" ");
				body=body+"<tr><td>"+temp[0]+"</td><td>"+temp[1]+"</td></tr>"; //ACCEPT ENCODING
					temp=in.readLine().split(" ");
					body=body+"<tr><td>"+temp[0]+"</td><td>"+temp[1]+"</td></tr>"; //ACCEPT LANGUAGE
				body=body+"</table>";
				
				//GET HTML, js or css
				String path = headers[1].substring(1,headers[1].length());//get the requested file in the url after localhost (eg. localhost/........)
				File file = new File(path);
				
				if(!file.exists()){	//if requested file does not exist or there is no file requested
					out.println("HTTP/1.0 404 Not Found");
					out.println("Content-Type: text/html");
					out.println("Server: Bot");            
					out.println("");	
					
					out.println(body+"<h1>File Not Found</h1>");        	
				}
				else{ //if file exists
					
					out.println("HTTP/1.0 200 OK");
					out.println("Content-Type: text/html");
					out.println("Server: Bot");	        
					out.println("");	  	
					
					//Read the requested file
					FileReader fw = new FileReader(file);
					BufferedReader bw = new BufferedReader(fw);
					String line;
					
					if(headers[1].contains(".css")){//append for cascading style sheet file
						body=body+"<style>";
					}
					else if(headers[1].contains(".js")){//append for javascript file
						body=body+"<script>";
					}
					while((line = bw.readLine()) != null){//read the line of code
						body=body+ line;
					}
				  
					if(headers[1].contains(".css")){
						body=body+"</style>";
					}
					else if(headers[1].contains(".js")){
						body=body+"</script>";
					}
					bw.close();
					out.println(body);
				}
				out.println("</htmL>");
				out.flush();
				remote.close();//close the connection 
			
			} 
			catch (Exception e) {
				System.out.println("Error: " + e);//print error
			}
		}
	}


  public static void main(String args[]) {
    wserver ws = new wserver();
    ws.start();
  }
}