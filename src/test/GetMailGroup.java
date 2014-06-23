package test;

import java.util.List;

import com.woodrice.javaExchange.util.ExchangeUtil;

public class GetMailGroup {

	public static void main(String[] args) {
		ExchangeUtil exchangeUtil = new ExchangeUtil();
		if(exchangeUtil.init()){
			List<String> result = exchangeUtil.getMailGroup();
			
			for(String item:result){
				System.out.println(item);
			}
		}
	}
}
