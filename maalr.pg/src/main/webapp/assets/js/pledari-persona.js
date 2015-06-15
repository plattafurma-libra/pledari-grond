/*******************************************************************************
 * Copyright 2013 Sprachliche Informationsverarbeitung, University of Cologne
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

var loggedInUser = null; // The user's email.
var browseridArguments = {
  privacyURL: '',
  tosURL: 'www.pledarigrond.ch/rumantschgrischun/infos',
  siteName: 'Pledari Grond'
};
/**
 * Anonymous function. Is called on document.ready. Checks if a user is currently logged in
 * with Mozilla Persona. See also <a href="https://developer.mozilla.org/en-US/Persona">Persona</a>
 */
$(function() {
	if(loggedInUser === null) {
		$.ajax({
			type: 'GET', 
			url: '/rumantschgrischun/persona/signedin',
			success: function(response) { 
				if (response !== '') {
					this.loggedInUser = response;
					loggedIn();
				} else {
					loggedOut();
				}
			}
		});
	}
});
/**
 * Called when user is currently logged in. Tries to assign a ClickHandler to the a logout link.
 * @returns -
 */
function loggedIn() {
	$("#maalr-current-user").bind('click', logout);
}
/**
 * Sign out current user.
 * @param ClickHanlder 
 * @returns -
 */
function logout(event) {
	event.preventDefault();
	this.loggedInUser = null;
	$.ajax({
		type: 'POST', 
		url: '/rumantschgrischun/persona/logout',
		success: function(response) { 
			window.location.href = response;
		}
  });
}
/**
 * Function is called when user is not signed in. Tries to assign a ClickHandler to the 
 * Persona sign in button, which triggers the Persona email verification. See also
 * <a href="https://developer.mozilla.org/en-US/docs/Web/API/navigator.id.get">navigator.id.get</a>
 * @returns -
 */
function loggedOut() {
	$("#persona_signin").bind('click', function(event) {
		navigator.id.get(verifiedEmail, browseridArguments);
	});
}

/**
 * This method is called after Persona verified the user's email and 
 * passes the assertion. 
 * @param assertion JSON - See also <a href="https://developer.mozilla.org/en-US/Persona/Remote_Verification_API">Remote Verification API</a>
 * @returns Redirect URL
 */
function verifiedEmail(assertion) {
	if (assertion !== null) {
		$.ajax({
			type : 'POST',
			url : '/rumantschgrischun/persona/login',
			data : { assertion : assertion },
			success : function(response) { 
				window.location.href = response;
			},
			error : function(xhr, status, error) {
				logout();
				//alert("login failure " + error); 
			}
		});
	} else {
		loggedOut();
	}
}