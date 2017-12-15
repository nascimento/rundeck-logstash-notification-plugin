import com.dtolabs.rundeck.plugins.notification.NotificationPlugin
import com.dtolabs.rundeck.core.plugins.configuration.StringRenderingConstants
import com.dtolabs.rundeck.core.plugins.configuration.ValidationException
import com.fasterxml.jackson.databind.ObjectMapper;
import groovy.json.JsonOutput

/**
 * Send Log.
 * @param execution the execution data map
 * @param config the plugin configuration data map
 */
def sendNotification(Map execution, Map config) {

  // Socket
  def socket = new Socket(config.host, config.port.toInteger());
  def socketStream = socket.getOutputStream();

  // Execution Information
  def e2 = [:]
  execution.each{ e2["${it.key}"] = it.value }

  // Stream
  def json = new ObjectMapper()
  socketStream << json.writeValueAsString(e2) + "\n"
}

rundeckPlugin(NotificationPlugin) {
  title = 'Logstash Notification'
  description = 'Send Execution Results to LogStash'
  configuration {
    host required: true, description: "Hostname to connect to Logstash", scope: 'Framework'
    port required: true, description: "Port to connect to Logstash", type: 'Integer', scope: 'Framework'
  }

  onstart { Map executionData, Map configuration ->
    sendNotification(executionData, configuration)
    true
  }

  onfailure { Map executionData, Map configuration ->
    sendNotification(executionData, configuration)
    true
  }

  onsuccess { Map executionData, Map configuration ->
    sendNotification(executionData, configuration)
    true
  }
  
  onavgduration { Map executionData, Map configuration ->
    sendNotification(executionData, configuration)
    true
  }
}
