class PersonObject {
    public String getFirstname() { 
        return "Hubert"; 
    }
    
    public String getLastname() { 
        return "Klein Ikkink"; 
    }
    
    public String getUsername() { 
        return "mrhaki"; 
    }
    
    public List<String> getTopics() {
	String[] sa = [ "Groovy", "Grails", "Java" ] 
        return Arrays.asList(sa); 
    }
}