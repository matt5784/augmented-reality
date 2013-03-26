package edu.vu.augmented.reality;

import java.util.regex.*;

import android.util.Log;

public class CardParser {
	
	private final String LOGTAG = "augmented-reality";

	private String fullText;
	private String workingText;
	
	private String regexURL = "(((ht|f)tp(s?))\\://)?(www.|[a-zA-Z].)[a-zA-Z0-9\\-\\.]+\\.(com|edu|gov|mil|net|org|biz|info|name|museum|us|ca|uk)(\\:[0-9]+)*(/($|[a-zA-Z0-9\\.\\,\\;\\?\\'\\\\+&amp;%\\$#\\=~_\\-]+))*";
	private String regexEmail = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

	private String myRegexPhone = "[\\(]?\\d{3,3}[\\)]?[\\. -]?\\d{3,3}[\\. -]?\\d{4,4}";
	
	// Makes sure valid area code (lowest is 200)
	private String regexPhone = "(\\([2-9]|[2-9])(\\d{2}|\\d{2}\\))(-|.|\\s)?\\d{3}(-|.|\\s)?\\d{4}";
	
	// Default constructor
	public CardParser(){
		fullText = "";
		workingText = "";
	}
	
	// Constructor for a given piece of text
	public CardParser(String text){
		fullText = text;
		workingText = text;
	}
	
	// Returns an email address and increases position in text
	public String getEmail(){
		
		return getPattern(regexEmail);
	}
	
	// Returns a basic URL
	public String getURL(){
		
		return getPattern(regexURL);
	}
	
	// Returns a phone number
	public String getPhone(){
		
		return getPattern(myRegexPhone);
	}
	
	// Returns the first occurrence of a particular regular expression match or "" if not found
	private String getPattern(String regex){
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(workingText);
		
		// Start at beginning of text, find first occurrence
		if (matcher.find(0))
		{
			String s =  matcher.group();
			
			// Take out the matched string
			int posStart = matcher.start();
			int posEnd = matcher.end();
			workingText = workingText.substring(0, posStart) + workingText.substring(posEnd, workingText.length());
			return s;
		}
		else
			return "";
	}
	
	// Setter method for the piece of text
	public void setText(String text){
		fullText = text;
		workingText = text;
	}
	
	// Getter method for piece of text
	public String getText(){
		return fullText;
	}
	
}
