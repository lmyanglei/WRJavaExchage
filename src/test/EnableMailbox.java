package test;

import java.util.List;

import com.woodrice.javaExchange.util.ExchangeUtil;

public class EnableMailbox {

	public static void main(String[] args) {
		ExchangeUtil exchangeUtil = new ExchangeUtil();
		if(exchangeUtil.init()){
			String mail = "test";
			String storage = "mail1/storage1";
			List<String> result = exchangeUtil.enableMailbox(mail, storage);
			
			for(String item:result){
				System.out.println(item);
			}
		}
	}
}
