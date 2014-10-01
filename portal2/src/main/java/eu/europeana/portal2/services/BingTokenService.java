package eu.europeana.portal2.services;

import com.memetix.mst.translate.Translate;


public class BingTokenService {

	public String getToken(String clientId,String clientSecret) {
		try{
			return Translate.getToken(clientId, clientSecret);			
		}
		catch(Exception e){
			return "";
		}
	}

	public static void main(String[] args){
		final String CLIENT_ID		= "AndyMacLeanTestTranslate";
		final String CLIENT_SECRET	= "eiaZ+05rRbfhHgmkjxAAgg3zmWV6rYPpJpblgogIZWM=";
		String token = new BingTokenService().getToken(CLIENT_ID, CLIENT_SECRET);
		System.err.println("Token:\t" + token);
	}
}
