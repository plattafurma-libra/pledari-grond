<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div id="signupDialog" class="modal hide fade vertical-center" tabindex="-1" aria-hidden="true" style="height: 242px !important; top: 43% !important;">
	<h3><fmt:message key="maalr.signup.header"/></h3>
	<div class="modal-body">
		<div class="row-fluid">
			<div class="span12">
				<p> 
					<fmt:message key='maalr.signup.information' />
				</p>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12">
				<form id="registrationForm" class="form-horizontal">
					<div class="form-group">
						<label class="col-sm-3 control-label" style="padding-right: 1em; width: 80px !important; text-align: left !important;">Email</label>
						<div class="col-sm-9">
							<input class="form-control" style="width:80% !important;" type="text" name="email"/>
						</div>
					</div>
					<div class="form-group" style="margin-top: 15px;">
                   		<label class="col-sm-3 control-label" style="padding-right: 1em; width: 80px !important; text-align: left !important;">Passwort</label>
                   		<div class="col-sm-9">
                    		<input class="form-control" style="width:80% !important;" type="password" name="password"/>
                    	</div>
                	</div>
                	<div class="form-group" style="margin-top: 15px;">
                   		<label class="col-sm-3 control-label" style="padding-right: 1em; width: 80px !important; text-align: left !important;">Passwort*</label>
                   		<div class="col-sm-9">
                    		<input class="form-control" style="width:80% !important;" type="password" name="confirm"/>
                    	</div>
                	</div>
                	<div class="form-group" style="margin-top: 15px;">
                		<div class="col-sm-offset-3 col-sm-9"><a class="btn btn-default" href="#" id="registerSubmit"><fmt:message key="maalr.signup.submit" /></a>
						<span id=errorMessage style="margin-left:8px; color: red;"></span></div>
                	</div>
				</form>
				
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	$("#registerSubmit").click(function(event) {
		
		event.preventDefault();
		
		var $inputs = $("#registrationForm").find('input');
		var data = {};
		for (var i = 0; i < $inputs.length; i++) {
			var $item = $($inputs[i]);
			data[$item.attr('name')] = $item.val();
		}
		
		$.ajax({
		  type: "POST",
		  url: "/rumantschgrischun/signup",
		  contentType: "application/json; charset=utf-8",
		  data: JSON.stringify(data),
		  success: function(response) {
			if (response.status == 'ERROR') {
			 	$("#errorMessage").text(response.errorMessage)
			} else {
				window.location.href = response.redirect;
			}
		  },
		});
	});
</script>