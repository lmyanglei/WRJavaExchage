package com.woodrice.javaExchange.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ExchangeUtil {

	public String SERVER_IP = "";
	public String SERVER_PORT= "";
	public String TIME_OUT= "";
	public String SHUT_DOWN= "";
	
	public ExchangeUtil(){

	}
	
	/**
     * ��ȡpowershell���п�������
	 * ͨ��powershell��ִ���������Get-Command
	 * @return
	 */
	public List<String> getCommand(){
		String cmd = "";
		cmd = "Get-Command";
    	
    	return send(cmd);
    };
    
    /**
	 * ��ȡpowershell�汾����Ϣ
	 * ͨ��powershell��ִ���������Get-Host
	 * @return
	 */
	public List<String> getHost(){
		String cmd = "";
		cmd = "Get-Host";
    	
    	return send(cmd);
    };

    /**
     * ��ȡ�û��ʼ���ַ��Ϣ
	 * ͨ��powershell��ִ���������Get-Mailbox
	 * @return
	 */
	public List<String> getMailbox(){
		String cmd = "";
    	cmd = "Get-Mailbox";
    	
    	return send(cmd);
    };

    /**
     * ��ȡ�ʼ���
	 * ͨ��powershell��ִ���������get-DistributionGroup
	 * @return
	 */
	public List<String> getMailGroup(){
		String cmd = "";
		cmd = "get-DistributionGroup";
    	
    	return send(cmd);
    };
    
    /**
     * �������
     * @param mail ����ǰ׺
     * @param storage ����洢
     * @see �﷨������
     * Get-Help Enable-Mailbox
     * @see �﷨ʾ����
     * Enable-Mailbox -Identity Contoso\TedBremer -Database Mail01\Database01
     * @return
     */
	public List<String> enableMailbox(String mail,String storage){
		String cmd = "";
		cmd = "Enable-Mailbox -Identity '"+mail+"' -Database '"+storage+"'";
    	
		List<String> returnValue = new ArrayList<String>();
		returnValue = send(cmd);

    	try {
        	// ��������󣬵ȴ���Ч
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
    	return returnValue;
    };
    
    /**
     * ��������
     * @param mail ����ǰ׺
     * @see �﷨������
     * Get-Help Disable-Mailbox
     * @see �﷨ʾ����
     * Disable-Mailbox -Identity Contoso\TedBremer -confirm:$false
     * @return
     */
	public List<String> disableMailbox(String mail){
		String cmd = "";
		cmd = "Disable-Mailbox -Identity '"+mail+"' -confirm:$false ";
    	
    	return send(cmd);
    };
    
    /**
     * ����ʼ���
     * @param mailGroupUid �ʼ���uid
     * @param description �ʼ�����������
     * @param managerUid �ʼ��鸺����uid
     * @see ����ʾ����new-DistributionGroup -Type 'Security' -Name 'yanglei6mailgroup1' -SamAccountName 'yanglei6mailgroup1' -DisplayName 'yanglei6�ʼ���1' -Alias 'group.alias'
     * @return
     */
	public List<String> addMailGroup(String mailGroupUid,String description,String managerUid){
		
		String displayName = mailGroupUid + " [" + description + "]";
		
		String cmd = "";
		cmd = "new-DistributionGroup -Type 'Security' -Name '"+displayName+"' -SamAccountName '"+mailGroupUid+"' -DisplayName '"+displayName+"'" + "-Alias '"+ mailGroupUid +"'";
    	
    	return send(cmd);
    };
    
    /**
     * ɾ���ʼ���
     * @param mailGroupUid �ʼ���uid
     * @return
     * �﷨ʾ����
     * Remove-DistributionGroup -Identity "mailgroup" -confirm:$false
     */
	public List<String> removeMailGroup(String DJBH,String mailGroupUid){
		String cmd = "";
		cmd = "Remove-DistributionGroup -Identity '"+ mailGroupUid +"' -confirm:$false";
    	
    	return send(cmd);
    };
    
    /**
     * ������䵽�ʼ���
     * @param mail ����ǰ׺
     * @param mailGroup �ʼ���
     * @see ����ʾ����Add-DistributionGroupMember -Identity "mailgroup" -Member "testuser15@xxx.com"
     * @return
     */
	public List<String> addMailboxToMailGroup(String mail,String mailGroup){
		String cmd = "";
		cmd = "Add-DistributionGroupMember -Identity '"+mailGroup+"' -Member '"+ mail +"'";
    	
    	return send(cmd);
    };
    
    /**
     * �������Ƴ��ʼ���
     * 
     * @param mail ����ǰ׺
     * @param mailGroup �ʼ���
     * @see ����ʾ����Remove-DistributionGroupMember -Identity "mailgroup" -Member "testuser15@xxx.com" -Confirm:$false
     * @return
     */
	public List<String> removeMailboxFromMailGroup(String mail,String mailGroup){
		String cmd = "";
		cmd = "Remove-DistributionGroupMember -Identity '"+mailGroup+"' -Member '"+mail +"' -confirm:$false";
    	
    	return send(cmd);
    };
    
    /**
     * �����˷�����Ϣ������ȡ������Ϣ
     * 
     * @param paramStr
     * @return
     */
    public List<String> send(String paramStr){
    	List<String> returnValue = new ArrayList<String>();
    	
    	Socket socketTemp = null;
    	String ipTemp = SERVER_IP;
        int portTemp= Integer.parseInt(SERVER_PORT);
        int timeOutTemp = Integer.parseInt(TIME_OUT);

        try {
            socketTemp = new Socket(ipTemp, portTemp);
            System.out.println("client��new connection:"+ipTemp+","+portTemp);
            
            socketTemp.setSoTimeout(timeOutTemp);
            System.out.println("client��new��time out is �� "+ timeOutTemp +"mi seconds��as"+ timeOutTemp/1000/60 +"min");
            
            System.out.println("client��send:");
            System.out.println(paramStr);
        	
            // send cmd
            OutputStream socketOut = socketTemp.getOutputStream();
            socketOut.write((paramStr+"\r\n").getBytes());
            
            // send cmd to close the socket
            socketOut.write((SHUT_DOWN+"\r\n").getBytes());
            socketOut.flush();
            
            // receive content from server
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(socketTemp.getInputStream()));
            String msg = null;
            System.out.println("client��receive data:");

            while ((msg = br.readLine()) != null){
            	if(null != msg && !"".equals(msg)){
            		returnValue.add(msg);
            		System.out.println(msg);
            	}
            }
        } catch (IOException e) {
        	e.printStackTrace(System.err);
        	
        	returnValue.add(e.getMessage());
        	List<String> tempList = new ArrayList<String>();
        	tempList.add("-1");
        	tempList.addAll(returnValue);
        	returnValue = tempList;
        }
        
        return returnValue;
    }
	
	public boolean init(){
		boolean returnValue = false;
		
		try {
			Properties properties = PropertiesUtil.getProperties("config.properties");
			
			SERVER_IP = properties.getProperty("SERVER_IP");
			SERVER_PORT = properties.getProperty("SERVER_PORT");
			TIME_OUT = properties.getProperty("TIME_OUT");
			SHUT_DOWN = properties.getProperty("SHUT_DOWN");
			
			returnValue = true;
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		
		return returnValue;
	}
}
