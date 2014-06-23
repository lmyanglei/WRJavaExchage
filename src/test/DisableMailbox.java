package test;

import java.util.List;

import com.woodrice.javaExchange.util.ExchangeUtil;

public class DisableMailbox {

	public static void main(String[] args) {
		ExchangeUtil exchangeUtil = new ExchangeUtil();
		if(exchangeUtil.init()){
			String mail = "test";
			List<String> result = exchangeUtil.disableMailbox(mail);
			
			for(String item:result){
				System.out.println(item);
			}
		}
	}
}
