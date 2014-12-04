package eu.europeana.portal2.services;

import com.memetix.mst.translate.Translate;


public class BingTokenService {

	private static Token token;
	private long TIMEOUT=(1000*60*10)-1000;
	
	public String getToken(String clientId, String clientSecret) {
		try{
			if(token==null || System.currentTimeMillis() - token.getTimestamp()>TIMEOUT){
				token = new Token();
				token.setToken(Translate.getToken(clientId, clientSecret));
				token.setTimestamp(System.currentTimeMillis());
			}
			return token.getToken() ;
		}
		catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}

	
	
	class Token{
		private long timestamp;
		private String token;
		
		public long getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		
	}
}
