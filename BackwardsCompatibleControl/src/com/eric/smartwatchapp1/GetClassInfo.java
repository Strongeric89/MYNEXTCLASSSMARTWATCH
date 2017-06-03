package com.eric.smartwatchapp1;
/*
 * 	The following class was built by Eric Strong
 * It will get the time and date from the device and return an array
 * 
 * */

public class GetClassInfo {
	
	String module = "";
	String classroom= "";
	
	//constructor
	public GetClassInfo(String module, String classroom){
		
		this.module = module;
		this.classroom = classroom;
		
	}//end constructor

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getClassroom() {
		return classroom;
	}

	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}
	
}//end class
