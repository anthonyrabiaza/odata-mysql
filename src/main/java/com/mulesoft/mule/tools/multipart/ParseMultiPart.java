package com.mulesoft.mule.tools.multipart;

/**
 * @author anthony.rabiaza@mulesoft.com
 */
import java.util.ArrayList;
import java.util.List;

public class ParseMultiPart {

	public List<String> parseResponse(String response) {
		String[] lines = response.split("\n");
		List<String> result = new ArrayList<String>();
		
		int top = lines.length;
		boolean preActivated = false;
		boolean activated = false;
		
		for (int i = 0; i < top; i++) {
			String line = lines[i].replaceAll("\r", "");
			if (line.endsWith("HTTP/1.1")) {
				StringBuffer part = new StringBuffer();
				StringBuilder message = new StringBuilder();
				
				preActivated = false;
				activated = false;
				for (; i < top; i++) {
					line = lines[i].replaceAll("\r", "");
					if (line.startsWith("--")) {
						break;
					}
					
					if(activated) {
						message.append(line).append("\n");
					}
					
					if(line.startsWith("Content-Length:")) {
						preActivated = true;
					}
					
					if(preActivated && line.length()==0) {
						activated = true;
					}
				}
				part.append(message.toString());
				result.add(part.toString());
			}
		}
		
		return result;
	}

	public String toString(List<String> list) {
		String stringList = "[";
		for(int i=0;i<list.size();i++){
			stringList += list.get(i);
			if(i<list.size()-1){
				stringList += ",";
			}
		}
		stringList += "]";
		return stringList;
	}
}


