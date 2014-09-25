package eu.europeana.portal2.services;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;


public class BingTokenService {

	// TODO - put these into properties
	
	static String CLIENT_ID		= "AndyMacLeanTestTranslate";
	static String CLIENT_SECRET	= "eiaZ+05rRbfhHgmkjxAAgg3zmWV6rYPpJpblgogIZWM=";
	
	public static void main(String[] args) throws Exception {
		
		Translate.setClientId(CLIENT_ID);
		Translate.setClientSecret(CLIENT_SECRET);
		 
		// Translate an english string to spanish
		String englishString = "Hello World!";
		String spanishTranslation = Translate.execute(englishString, Language.SPANISH);
		 
		System.out.println("Original english phrase: " + englishString);
		System.out.println("Translated spanish phrase: " + spanishTranslation);
		
		/*
		OUTPUT:
		Original english phrase: Hello World!
		Translated spanish phrase: ¡Hola mundo!
		*/
		
		// get token
		String token = Translate.getToken(CLIENT_ID, CLIENT_SECRET);
		
		System.out.println("The (json-wrapped) token  for ajax calls:\n\t" + token);

	}

}
