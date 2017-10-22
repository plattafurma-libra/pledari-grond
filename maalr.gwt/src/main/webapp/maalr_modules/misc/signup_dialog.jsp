<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div id="signupDialog" class="modal hide fade vertical-center" tabindex="-1" aria-hidden="true">
	<h3><fmt:message key="maalr.signup.header"/></h3>
	<div class="modal-body" style="overflow-y: hidden; overflow-x: hidden; max-height: 480px;">
		<div class="row-fluid">
			<div class="span12">
				<form id="registrationForm" class="form-horizontal">
					<div class="form-group">
						<label class="col-sm-3 control-label" style="padding-right: 1em; width: 80px !important; text-align: left !important;"><fmt:message key='maalr.login.name' /></label>
						<div class="col-sm-9">
							<input class="form-control sign-up-input" type="text" name="email"/>
						</div>
					</div>
					<div class="form-group" style="margin-top: 15px;">
                   		<label class="col-sm-3 control-label" style="padding-right: 1em; width: 80px !important; text-align: left !important;"><fmt:message key='maalr.login.pwd' /></label>
                   		<div class="col-sm-9">
                    		<input class="form-control sign-up-input" type="password" name="password"/>
                    	</div>
                	</div>
                	<div class="form-group" style="margin-top: 15px;">
                   		<label class="col-sm-3 control-label" style="padding-right: 1em; width: 80px !important; text-align: left !important;"><fmt:message key='maalr.login.pwd' />*</label>
                   		<div class="col-sm-9">
                    		<input class="form-control sign-up-input" type="password" name="confirm"/>
                    	</div>
                	</div>
                	<div class="form-group" style="margin-top: 40px;">
                		<div id="buttonGroupSignup" class="col-sm-offset-3 pull-right col-sm-9">
                			<a class="btn btn-default" href="#" data-dismiss="modal" data-target="#signupDialog" id="cancelButton">
                				<fmt:message key="maalr.signup.cancel" />
                			</a>
                			<a class="btn btn-default" href="#" id="registerSubmit">
                				<fmt:message key="maalr.signup.submit" />
                			</a>
						</div>
                		<div class="col-sm-offset-3 col-sm-9">
							<span id=errorMessage style="margin-left:8px; color: red;"></span>
						</div>
                	</div>
				</form>
				
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12">
				<span class="glyphicon icon-info-sign"></span>
				<p style="padding-top: 5px;"> 
					<fmt:message key='maalr.signup.information' />
				</p>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">

	resize();
	
	$(window).resize(function() {
		resize();
	});
	
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
		  url: "/surmiran/signup",
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
	
	function resize()
	{
		if($(window).width() > 768)
		{
	  		$('#buttonGroupSignup').addClass('pull-right')
		} else 
		{
			$('#buttonGroupSignup').removeClass('pull-right')
		}
	}
	
</script>