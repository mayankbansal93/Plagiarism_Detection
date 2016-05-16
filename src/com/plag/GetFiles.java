package com.plag;

import java.io.File;
import java.util.ArrayList;

public class GetFiles {
	File[] files_list;
	boolean getstate;
	ArrayList<String> result_list;
	ArrayList<String> file_name;

	public GetFiles(File[] files_list,boolean getstate,ArrayList<String> result_list){
		this.files_list = files_list;
		this.getstate = getstate;
		this.result_list = result_list;
	}
	
	public String[][] getText() throws Exception{
		ArrayList<String> texts = new ArrayList<String>();
		file_name = new ArrayList<String>();
	    // File folder = new File("/home/shubham/programs/major/files/");
	    // File[] files_list = folder.listFiles();
	    
	    for(File file: files_list){
	    	if(file.isFile()){
//	    		System.out.println(file.getPath());
	    		TextExtractor extractor = new TextExtractor();
	    		String temp = extractor.process(file.getPath());
//	    		String temp = extractor.getString();
	    		TextParser parser = new TextParser();
	    		String temp2 = parser.formatString(temp);
//	    		System.out.println(temp2);
	    		texts.add(temp2);
	    		
	    	}
	    }
	    
	    for(int i=0;i<files_list.length;i++){
	    	file_name.add(files_list[i].getName());
	    }
	    
	    if(getstate){
	    	for(int i=0;i<result_list.size();i++){
	    		TextParser parser = new TextParser();
	    		String temp = parser.formatString(result_list.get(i));
	    		texts.add(temp);
	    	}
	    	
	    	for(int i=0;i<result_list.size();i++){
	    		file_name.add("wiki"+Integer.toString(i+1));
	    	}
	    }
	    
	    PlagDetector detect = new PlagDetector();
//	    detect.compareString(texts.get(0), texts.get(1));
	    
	    int len = file_name.size();
	    int len2 = (len*(len-1))/2;
	    String[] plag_answer = new String[(len*(len-1))/2];
	    int count=0;

	    String[][] final_answer = new String[len2][3];

	    for(int i=0;i<texts.size();i++){
	    	for(int j=i+1;j<texts.size();j++){
	    		double temp = detect.compareString(texts.get(i), texts.get(j), count);
	    		plag_answer[count] = file_name.get(i) + "\t\t" + file_name.get(j) + "\t\t" + Double.toString(temp);
	    		final_answer[count][0] = file_name.get(i);
	    		final_answer[count][1] = file_name.get(j);
	    		final_answer[count][2] = Double.toString(temp);
	    		System.out.println(plag_answer[count]);
	    		count++;
	    	}
	    }
	    System.out.println("\n");
	    for(int i=0;i<count;i++){
	    	System.out.println(plag_answer[i]);
	    }
	    
	    return final_answer;

//	    System.out.println(texts.get(0));
	    
//	    PlagDetector detector = new PlagDetector(texts.get(0), texts.get(1));
//	    boolean ans = detector.compareStrings();
//	    detector.compare2();
//	    System.out.println(ans);
	    
//	    TextParser parser = new TextParser();
////	    ArrayList<Word> words1 = parser.getWordCounts(texts.get(1));
//	    String[] lines = parser.getLines(texts.get(1));
//	    for(int i=0;i<lines.length;i++){
//	    	System.out.println(lines[i]);
//	    }
	    
//	    for(int i=0;i<words1.size();i++){
//	    	System.out.println(words1.get(i).getWord() + "\t" + words1.get(i).getCount());
//	    }
	    

	}
	
	// public static void main(String[] args) throws Exception{
	// 	GetFiles foo = new GetFiles();
	// 	foo.getText();
	// }
	
	

}
