/**
 *  Arcsoft Simplicam
 *
 *  Copyright 2016 Jonas
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
metadata {
	definition (name: "Arcsoft Simplicam", namespace: "dk.decko", author: "Jonas Laursen") {
		capability "Switch"
	}


	simulator {
		// TODO: define status and reply messages here
	}

	tiles(scale: 2) {
		standardTile("switch", "device.switch", decoration: "flat", width: 2, height: 2) {
        	state "off", label: "Off", action: "on", icon: "st.Electronics.electronics7", backgroundColor: "#ffffff"
            state "on", label: "On", action: "off", icon: "st.Electronics.electronics7", backgroundColor: "#79b821"
        }
        
        main("switch")
	}
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
	// TODO: handle 'switch' attribute

}

// handle commands
def on() {
	log.debug "Executing 'on'"
    parent.closeliCameraEnabled(state.deviceid, true)
}

def off() {
	log.debug "Executing 'off'"
	parent.closeliCameraEnabled(state.deviceid, false)
}

def setDeviceId(did) {
	state.deviceid = did
}