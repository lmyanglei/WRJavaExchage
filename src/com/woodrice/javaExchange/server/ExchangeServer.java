package com.woodrice.javaExchange.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.woodrice.javaExchange.util.PropertiesUtil;

/**  
 * ActiveDirectoryUtil
 * 
 * @Project: WRJavaExchange
 * @Title: ExchangeServer.java
 * @Package com.woodrice.javaExchange.server
 * @Description: ExchangeServer
 * @author lmyanglei@gmail.com
 * @date 2014-1-20 17:56:01
 * @Copyright: 2014 woodrice.com All rights reserved.
 * @version v1.0  
 */

public class ExchangeServer {

    private int port;
    private int timeOut;
    private ServerSocket serverSocket;
    private ExecutorService executorService;//线程池
    private final int POOL_SIZE;//单个CPU线程池大小
    
    public ExchangeServer() throws IOException{
    	POOL_SIZE = 10;
    	
    	Properties properties = PropertiesUtil.getProperties("config.properties");
    	port = Integer.parseInt((String)properties.get("SERVER_PORT"));
    	timeOut = Integer.parseInt((String)properties.get("TIME_OUT"));

        serverSocket=new ServerSocket(port);
        //Runtime的availableProcessor()方法返回当前系统的CPU数目.
        executorService=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*POOL_SIZE);
        
        System.out.println("Server is running:"+serverSocket.getInetAddress()+serverSocket.getLocalPort());
    }
    
    public void service(){
        while(true){
            Socket socket=null;
            try {
                //接收客户连接,只要客户进行了连接,就会触发accept();从而建立连接
                socket=serverSocket.accept();
                executorService.execute(new Handler(socket));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        new ExchangeServer().service();
    }

}

class Handler implements Runnable{
    private Socket socket;
    
    public Handler(Socket socket){
        this.socket=socket;
    }
    
    private PrintWriter getWriter(Socket socket) throws IOException{
        OutputStream socketOut=socket.getOutputStream();
        return new PrintWriter(socketOut,true);
    }
    
    private BufferedReader getReader(Socket socket) throws IOException{
        InputStream socketIn=socket.getInputStream();
        return new BufferedReader(new InputStreamReader(socketIn));
    }

    public void run(){
    	
        try {
        	Properties properties = PropertiesUtil.getProperties("config.properties");
        	int timeOut = Integer.parseInt((String)properties.get("TIME_OUT"));
        	String SHUT_DOWN = (String)properties.get("SHUT_DOWN");
        			
        	System.out.println("Server：new connection： "+socket.getInetAddress()+":"+socket.getPort());
        	
        	socket.setSoTimeout(timeOut);
        	System.out.println("Server：time out： "+ timeOut +"mill seconds，as"+ timeOut/1000/60 +"min");
        	
            BufferedReader br=getReader(socket);
            PrintWriter pw=getWriter(socket);
            String msg=null;
            while((msg=br.readLine())!=null){
            	try {
            		System.out.println("Server：received：");
            		System.out.println(msg);
            		
            		if(SHUT_DOWN.equals(msg)){
            			System.out.println("Server：close connection");
	                	break;
	                }
            		
            		String result = exec(msg);
                
					pw.println(result);
					
					System.out.println("Server：send：");
					System.out.println(result);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        } catch (IOException e) {
        	e.printStackTrace();
        }finally{
            try {
                if(socket!=null)
                    socket.close();
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }
    }
    
    /**
	 * exec powershell
	 * 
	 * @param cmd
	 * @return
	 * success:0\r\n;a\r\n;a\r\n;a\r\n
	 * fail:-1\r\n;a\r\n;a\r\n;a\r\n
	 */
	public String exec(String cmd){
		String returnValue = "";
		String resultInput = "";
		String resultError = "";
		try {

			System.out.println("PowerShell：begin");
			System.out.println(cmd);
			Process p = (Process)Runtime.getRuntime().exec("powershell " + cmd);
			p.getOutputStream().close(); 
			BufferedReader rd = new BufferedReader(new InputStreamReader(p.getInputStream()));  
			String msg = "";  

			while((msg = rd.readLine()) != null){  
				
				if(null != msg && !"".equals(msg)){
					resultInput = resultInput + msg+"\r\n";
					System.out.println(msg+"\r\n");
				}
			}
			System.out.println("PowerShell：InputStream done");
			
			rd = new BufferedReader(new InputStreamReader(p.getErrorStream()));  
			while((msg = rd.readLine()) != null){  
				if(null != msg && !"".equals(msg)){
					resultError = resultError + msg+"\r\n";
					System.out.println(msg+"\r\n");
				}
		    }
			System.out.println("PowerShell：ErrorStream done");
		} catch (Exception e) {
			resultError = resultError + "\r\n" 
					+ e.getMessage() + "\r\n";
			e.printStackTrace();
		}
		
		if(null != resultError && !"".equals(resultError)){
			returnValue = "-1\r\n";
			returnValue = returnValue+resultError+"\r\n";
			System.out.println("PowerShell：error");
		}else{
			returnValue = "0\r\n";
			returnValue = returnValue+resultInput+"\r\n";
			System.out.println("PowerShell：ok");
		}
		
		System.out.println("PowerShell：end");
		
        return returnValue;
	}
}