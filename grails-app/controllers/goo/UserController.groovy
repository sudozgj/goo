package goo

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

import grails.converters.JSON
import java.util.Map;
import java.util.HashMap;

@Transactional(readOnly = true)
class UserController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def addUser(String login,String password,String role){  //添加 C
        User user =  User.findByLogin(login)

        if(user==null){     //用户名不重复
            def u = new User(login:login,password:password,role:role)
            u.save()

            // System.out.println("user : "+u)
            Map<String,String> map = new HashMap<String,String>();
            map.put("result","1")
            render map as JSON
        }
        else{//哈哈
            Map<String,String> map = new HashMap<String,String>();
            map.put("result","the login is used!")
            render map as JSON
        }
    }

    def deleteUser(Integer id){        //删除 D
        def user = User.get(id);
        
        if(!user){      //no this id
            def result = "{'result':'no this user'}"
            render JSON.parse(result)
        }
        else{           //deleting...
            user.delete()    
            def i = User.get(id)

            Map<String,String> map = new HashMap<String,String>();
            map.put("result",1)
            render map as JSON
        }
    }

    def updateUser(Integer id,String login,String password,String role) {   //修改 U
        def user = User.get(id)

        if(login!=null){
            user.login = login
        }
        if(password!=null){
            user.password = password
        }
        if(role!=null){
            user.role = role
        }
        user.save()

        Map<String,String> map = new HashMap<String,String>();
        map.put("result","1");
        render map as JSON
    }

    def getUserList() {                 //获取列表 R
        def li = User.list()
        render li as JSON
    }

    def getUserListById(Integer id){   //查询某列表 R
        def li = User.get(id)
        render li as JSON
    }



    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond User.list(params), model:[userCount: User.count()]
    }

    def show(User user) {
        respond user
    }

    def create() {
        respond new User(params)
    }

    @Transactional
    def save(User user) {
        if (user == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (user.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond user.errors, view:'create'
            return
        }

        user.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'user.label', default: 'User'), user.id])
                redirect user
            }
            '*' { respond user, [status: CREATED] }
        }
    }

    def edit(User user) {
        respond user
    }

    @Transactional
    def update(User user) {
        if (user == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (user.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond user.errors, view:'edit'
            return
        }

        user.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'user.label', default: 'User'), user.id])
                redirect user
            }
            '*'{ respond user, [status: OK] }
        }
    }

    @Transactional
    def delete(User user) {

        if (user == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        user.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'user.label', default: 'User'), user.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
