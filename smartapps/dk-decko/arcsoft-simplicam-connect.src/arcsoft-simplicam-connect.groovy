/**
 *  Arcsoft Simplicam
 *
 *  Copyright 2016 Jonas Laursen
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Arcsoft Simplicam (Connect)",
    namespace: "dk.decko",
    author: "Jonas Laursen",
    description: "Turn Arcsoft Simplicams on/off",
    category: "Safety & Security",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/SafetyAndSecurity/Cat-SafetyAndSecurity.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/SafetyAndSecurity/Cat-SafetyAndSecurity@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/SafetyAndSecurity/Cat-SafetyAndSecurity@3x.png")


preferences {
	section("Closeli Credentials") {
		input(name: "email", type: "email", title: "Email address", description: "Enter Email Address", required: true)
        input(name: "password", type: "password", title: "Password", description: "Enter password", required: true)
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	initialize()
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
    if (closeliGetSession(settings.email, settings.password)) {
    	closeliGetDevices()
    }
}

def closeliGetSession(email, password) {
	try {
    	httpPost("https://client.closeli.com/login", "email=${email}&password=${password}") { resp ->
			if (resp.contentType == "application/json") {
            	if (resp.data["success"] == true) {
                	state.cookie = ""
                	resp.getHeaders('set-cookie').each {
                    	state.cookie = state.cookie + it.value.split(";")[0] + ";"
                    }
                    return true
                }
                else {
                	log.debug("Unable to log in: ${resp.data}")
                }
            }
            else {
            	log.debug("Reply was not json: ${resp.data}")
            }
        }
    } catch (e) {
    	log.debug("Didn't work: $e")
    }
}

def closeliGetDevices(session) {
	httpGet(["uri": "https://client.closeli.com/device/list", "headers": ["Cookie": state.cookie]]) { resp ->
    	resp.data.list.devicelist.each {
        	log.debug(it)
            def dni = [app.id, it.deviceid].join(".")
            if (! getChildDevice(dni)) {
	            addChildDevice(app.namespace, "Arcsoft Simplicam", dni, null, ["deviceid": it.deviceid, "devicename": it.devicename])
            } else {
            	log.debug("Device with id ${dni} already exists")
            }
        }
    }
}

// TODO: implement event handlers