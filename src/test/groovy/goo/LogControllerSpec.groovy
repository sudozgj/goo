package goo

import grails.test.mixin.*
import spock.lang.*

@TestFor(LogController)
@Mock(Log)
class LogControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null

        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
        assert false, "TODO: Provide a populateValidParams() implementation for this generated test suite"
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.logList
            model.logCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.log!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def log = new Log()
            log.validate()
            controller.save(log)

        then:"The create view is rendered again with the correct model"
            model.log!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            log = new Log(params)

            controller.save(log)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/log/show/1'
            controller.flash.message != null
            Log.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def log = new Log(params)
            controller.show(log)

        then:"A model is populated containing the domain instance"
            model.log == log
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def log = new Log(params)
            controller.edit(log)

        then:"A model is populated containing the domain instance"
            model.log == log
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/log/index'
            flash.message != null

        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def log = new Log()
            log.validate()
            controller.update(log)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.log == log

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            log = new Log(params).save(flush: true)
            controller.update(log)

        then:"A redirect is issued to the show action"
            log != null
            response.redirectedUrl == "/log/show/$log.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/log/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def log = new Log(params).save(flush: true)

        then:"It exists"
            Log.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(log)

        then:"The instance is deleted"
            Log.count() == 0
            response.redirectedUrl == '/log/index'
            flash.message != null
    }
}
