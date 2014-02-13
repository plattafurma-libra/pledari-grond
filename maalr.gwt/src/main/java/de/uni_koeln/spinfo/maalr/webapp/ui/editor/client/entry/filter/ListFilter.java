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
package de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.filter;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ButtonGroup;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.Legend;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.datetimepicker.client.ui.DateTimeBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.uni_koeln.spinfo.maalr.common.shared.EditorQuery;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Status;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Verification;
import de.uni_koeln.spinfo.maalr.common.shared.LightUserInfo;
import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.shared.util.Logger;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorConstants;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.dataprovider.SimplePagingDataProvider;

/**
 * Filter-Composite with provides filter options to query for users by text
 * (email, login, firstname, lastname) and/or role. The layout of this
 * {@link Composite} is defined in the corresponding ui.xml-file.
 * 
 * @author sschwieb
 * 
 */
public class ListFilter extends Composite {

	private static UserDetailsUiBinder uiBinder = GWT
			.create(UserDetailsUiBinder.class);

	private EditorConstants constants = GWT.create(EditorConstants.class);

	@UiField
	Legend legend;

	//@UiField
	/**
	 * current user name or IP
	 */
	String nameOrIp;

	@UiField
	TextBox verifier;

	@UiField
	ListBox state;

	@UiField
	ListBox verification;

	@UiField
	ListBox role;

	@UiField
	Button apply;

	@UiField
	Button reset;

	@UiField
	DateTimeBox from;

	@UiField
	DateTimeBox to;

	@UiField
	ButtonGroup searchButtons;

	@UiField
	ControlGroup verificationGroup;

	@UiField
	ControlGroup verifierGroup;

	@UiField
	ControlGroup roleGroup;

	@UiField
	ControlGroup stateGroup;

	private int defaultVerificationIndex = 0;

	private int defaultRoleIndex = 0;

	private int defaultStateIndex = 0;

	/**
	 * The list of roles, which corresponds to the list provided by
	 * {@link ListFilter#state};
	 * 
	 * @see ListFilter#getState()
	 */
	private final Status[][] states;

	private final Verification[] verifications;

	private final Role[] roles;

	private SimplePagingDataProvider dataProvider;

	private int pageSize = 20;

	interface UserDetailsUiBinder extends UiBinder<Widget, ListFilter> {
	}

	/**
	 * Timer to fetch users while filter options are modified.
	 */
	Timer delayedSearchTimer = new Timer() {

		@Override
		public void run() {
			EditorQuery query = getQuery();
			query.setCurrent(0);
			dataProvider.setQuery(query);
			dataProvider.refreshQuery();
		}

	};

	public EditorQuery getQuery() {
		EditorQuery query = new EditorQuery();
		query.setUserOrIp(nameOrIp);
		query.setState(getState());
		query.setStartTime(from.getValue().getTime());
		query.setEndTime(to.getValue().getTime());
		query.setRole(getRole());
		query.setVerification(getVerification());
		query.setPageSize(pageSize);
		query.setVerifier(verifier.getText());
		query.setCurrent(dataProvider.getQuery().getCurrent());
		return query;
	}

	public void setQuery(EditorQuery query, boolean force) {
		if (force)
			dataProvider.dropLastQuery();
//		Logger.getLogger(getClass()).info("Setting current to zero in filter!");
		query.setCurrent(0);
		dataProvider.setQuery(query);
		dataProvider.refreshQuery();
	}

	public ListFilter(String title) {
		initWidget(uiBinder.createAndBindUi(this));
		legend.getElement().setInnerText(title);
		states = new Status[][] {
				{ Status.NEW_ENTRY, Status.NEW_MODIFICATION },
				{ Status.NEW_ENTRY }, { Status.NEW_MODIFICATION },
				{ Status.DELETED } };
		verifications = new Verification[] { Verification.ACCEPTED,
				Verification.OUTDATED, Verification.REJECTED,
				Verification.UNVERIFIED };
		verification.addItem(constants.any(), (String) null);
		for (Verification v : verifications) {
			verification.addItem(v.getVerificationName());
		}
		state.addItem(constants.any(), (String) null);
		for (Status[] r : states) {
			state.addItem(toString(r));
		}
		roles = Role.values();
		role.addItem(constants.any(), (String) null);
		for (Role r : roles) {
			role.addItem(r.getRoleName(), r.toString());
		}
		from.setAutoClose(true);
		to.setAutoClose(true);
		initHandlers();
		searchButtons.getElement().getStyle().setFloat(Float.RIGHT);
		// addPresets();
		to.setValue(new Date(System.currentTimeMillis() + EditorQuery.ONE_DAY));
		from.setValue(new Date(System.currentTimeMillis()
				- EditorQuery.HALF_YEAR));
		setState(1);
		verification.setSelectedIndex(4);
	}

	private String toString(Status[] r) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < r.length; i++) {
			builder.append(r[i].getStateName());
			if (i < r.length - 1) {
				builder.append(" " + constants.or() + " ");
			}
		}
		return builder.toString();
	}

	public void setVerification(Verification v) {
		int index = Arrays.asList(verifications).indexOf(v);
		verification.setSelectedIndex(index + 1);
		defaultVerificationIndex = index + 1;
	}

	public void setUserRole(Role role) {
		int index = Arrays.asList(roles).indexOf(role);
		this.role.setSelectedIndex(index + 1);
		defaultRoleIndex = index + 1;
	}

	public void setLemmaState(Status[] state) {
		int index = -1;
		if (state != null) {
			List<Status> stateList = Arrays.asList(state);
			for (int i = 0; i < states.length; i++) {
				if (stateList.equals(Arrays.asList(states[i]))) {
					index = i;
					break;
				}
			}
		}
		this.state.setSelectedIndex(index + 1);
		defaultStateIndex = index + 1;
	}

	public void setVerificationVisible(boolean visible) {
		verificationGroup.setVisible(visible);
	}

	public void setStateVisible(boolean visible) {
		stateGroup.setVisible(visible);
	}

	public void setRoleVisible(boolean visible) {
		roleGroup.setVisible(visible);
	}

	public void setVerifierVisible(boolean visible) {
		verifierGroup.setVisible(visible);
	}

	protected void setState(int index) {
		this.state.setSelectedIndex(index);
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	protected void setRole(Role role) {
		for (int i = 0; i < roles.length; i++) {
			if (roles[i].equals(role)) {
				this.role.setSelectedIndex(i + 1);
				return;
			}
		}
		this.role.setSelectedIndex(0);
	}

	/**
	 * Initializes handlers for input fields and buttons.
	 */
	private void initHandlers() {
//		name.addKeyUpHandler(new KeyUpHandler() {
//
//			String current = "";
//
//			@Override
//			public void onKeyUp(KeyUpEvent event) {
//				if (current.equals(name.getText()))
//					return;
//				current = name.getText();
//				// Schedule the timer with a delay of 300 milliseconds.
//				// If the text is modified within the delay time,
//				// the timer is cancelled and re-scheduled.
//				delayedSearchTimer.cancel();
//				delayedSearchTimer.schedule(300);
//			}
//		});
		verifier.addKeyUpHandler(new KeyUpHandler() {

			String current = "";

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (current.equals(verifier.getText()))
					return;
				current = verifier.getText();
				// Schedule the timer with a delay of 300 milliseconds.
				// If the text is modified within the delay time,
				// the timer is cancelled and re-scheduled.
				delayedSearchTimer.cancel();
				delayedSearchTimer.schedule(300);
			}

		});

		state.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// Immediately schedule update
				delayedSearchTimer.schedule(1);
			}
		});

		verification.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// Immediately schedule update
				delayedSearchTimer.schedule(1);
			}
		});

		role.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// Immediately schedule update
				delayedSearchTimer.schedule(1);
			}
		});

		from.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				// Immediately schedule update
				delayedSearchTimer.schedule(1);
			}
		});

		to.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				// Immediately schedule update
				delayedSearchTimer.schedule(1);
			}
		});

		apply.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dataProvider.dropLastQuery();
				// Immediately schedule update
				delayedSearchTimer.schedule(1);
			}

		});

		reset.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// Reset input fields and schedule update
				//name.setText("");
				verifier.setText("");
				state.setSelectedIndex(defaultStateIndex);
				role.setSelectedIndex(defaultRoleIndex);
				verification.setSelectedIndex(defaultVerificationIndex);
				long lastWeek = System.currentTimeMillis()
						- EditorQuery.HALF_YEAR;
				from.setValue(new Date(lastWeek), false);
				to.setValue(new Date(System.currentTimeMillis()
						+ EditorQuery.ONE_DAY), false);
				delayedSearchTimer.schedule(1);
			}
		});
	}

	/**
	 * Connect the filter with a {@link SimplePagingDataProvider}. The provider
	 * must not be <code>null</code>, and this method must be called before the
	 * {@link ListFilter} can be used.
	 * 
	 * @param dataProvider
	 */
	public void setDataProvider(SimplePagingDataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	/**
	 * Returns the selected state, or <code>null</code> if the 'Any'-option is
	 * selected.
	 */
	private Status[] getState() {
		int selected = state.getSelectedIndex();
		if (selected <= 0) {
			return null;
		}
		return states[selected - 1];
	}

	private Verification getVerification() {
		int selected = verification.getSelectedIndex();
		if (selected <= 0) {
			return null;
		}
		return verifications[selected - 1];
	}

	private Role getRole() {
		int selected = role.getSelectedIndex();
		if (selected <= 0) {
			return null;
		}
		return roles[selected - 1];
	}

	public void toggleUser(LemmaVersion mostRecent) {
	
		LightUserInfo userInfo = mostRecent.getUserInfo();
		if (userInfo == null || userInfo.getRole() == Role.GUEST_1) {
			if (nameOrIp.equals(mostRecent.getIP())) {
				nameOrIp = "";
			} else {
				nameOrIp = mostRecent.getIP();
			}
		} else {
			if (nameOrIp.equals(userInfo.getLogin())) {
				nameOrIp = "";
			} else {
				nameOrIp = userInfo.getLogin();
			}
		}
		delayedSearchTimer.schedule(1);
	}

	public void toggleVerifier(LemmaVersion mostRecent) {
		String id = mostRecent.getVerifierId();
		if (verifier.getText() != null && verifier.getText().equals(id)) {
			verifier.setText("");
		} else {
			verifier.setText(id);
		}
		delayedSearchTimer.schedule(1);
	}

}
