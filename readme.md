Info: Script inspired from https://github.com/rundeck-plugins/rundeck-logstash-plugin.

# Rundeck Logstash Notification Plugin

This is a simple Rundeck Notifiction plugin that will pipe all executions logs to a Logstash server by writing Json to a TCP port. *For Rundeck version 2.6.x or later.*

# Installation

Copy the `Logstash.groovy` to your `$RDECK_BASE/libext/` directory for Rundeck.

# Configure Rundeck

The plugin supports these configuration properties:

* `host` - hostname of the logstash server
* `port` - TCP port to send JSON data to

You can update the your framework.properties file to set these configuration values:

in `framework.properties`:

    framework.plugin.Notification.Logstash.port=5000
    framework.plugin.Notification.Logstash.host=localhost

# Configure Logstash

Add a TCP input that uses Json format data.  Here is an example `rundeck-logstash.conf`:

    output {
      stdout { }
      elasticsearch {
        embedded => true
      }
    }

    input {
    	tcp {
    		port => 5000
    	}
    }

    filter{
    	mutate {
    		replace => {
    			"type" => "rundeck"
    		}
    	}
      json {
        source => "message"
        target => "rundeck"
      }
    }

    ## Or add your filters / logstash plugins configuration here

    output {
    	elasticsearch {
    		hosts => "elasticsearch:9200"
    	}
    }


# Start Logstash

Use the config file when starting logstash.

    https://hub.docker.com/_/logstash/
