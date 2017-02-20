package goo

class User {
	String login
	String password
	String role = "user"

    static constraints = {
    	login(blank:false, nullable:false, unique:true)
		password(blank:false, password:true)
		role(inList:["admin", "user"])		//限定输入值只为admin或user
    }

    String toString(){
		login
	}
}
