package goo

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON

@Transactional(readOnly = true)
class LogController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    

    def getJsonTest(){
        Map<String,String> map = new HashMap<>()
        List<Map<String,String>> list = new ArrayList()

        map.put("name","tim")
        map.put("type","txt1")
        map.put("logList",Log.list())
        list.add(map)
        render list as JSON
    }

    def getLogJson(){
        
    }


    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Log.list(params), model:[logCount: Log.count()]
    }

    def show(Log log) {
        respond log
    }

    def create() {
        respond new Log(params)
    }

    @Transactional
    def save(Log log) {
        if (log == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (log.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond log.errors, view:'create'
            return
        }

        log.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'log.label', default: 'Log'), log.id])
                redirect log
            }
            '*' { respond log, [status: CREATED] }
        }
    }

    def edit(Log log) {
        respond log
    }

    @Transactional
    def update(Log log) {
        if (log == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (log.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond log.errors, view:'edit'
            return
        }

        log.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'log.label', default: 'Log'), log.id])
                redirect log
            }
            '*'{ respond log, [status: OK] }
        }
    }

    @Transactional
    def delete(Log log) {

        if (log == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        log.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'log.label', default: 'Log'), log.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'log.label', default: 'Log'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
