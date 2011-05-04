package com.anteboth.agrisys.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;

/**
 * Image uploader.
 * @author michael
 */
public class UploadPhoto extends Composite implements HasHandlers {

	private static UploadPhotoUiBinder uiBinder = GWT.create(UploadPhotoUiBinder.class);

	private AgrisysServiceAsync service = Agrisys.getAgrisysservice();
	private HandlerManager handlerManager;

	interface UploadPhotoUiBinder extends UiBinder<Widget, UploadPhoto> {
	}

	@UiField
	Button uploadButton;

	@UiField
	FormPanel uploadForm;

	@UiField
	FileUpload uploadField;
	
	@UiField
	Hidden refIdField;

	private Long refId;

	/**
	 * Construtor.
	 * @param referenceId the id of the element the uploaded images will be assigned to
	 */
	public UploadPhoto(Long referenceId) {
		this.refId = referenceId;
		
		handlerManager = new HandlerManager(this);
		initWidget(uiBinder.createAndBindUi(this));

		uploadButton.setText("Loading...");
		uploadButton.setEnabled(false);

		uploadField.setName("image");
		startNewBlobstoreSession();
		
		refIdField.setValue(referenceId.toString());
		refIdField.setName("refId");

		uploadForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				uploadForm.reset();
				startNewBlobstoreSession();
				String key = event.getResults();

				onImageUploaded();
			}
		});
	}
	
	/**
	 * Can be overridden to implement a custom behaviour when the upload finishes.
	 */
	protected void onImageUploaded() {
	}

	private void startNewBlobstoreSession() {
		service.getBlobstoreUploadUrl(new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				uploadForm.setAction(result);
				uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
				uploadForm.setMethod(FormPanel.METHOD_POST);
				uploadButton.setText("Upload");
				uploadButton.setEnabled(true);
				refIdField.setValue(refId.toString());
				refIdField.setName("refId");
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		});
	}

	@UiHandler("uploadButton")
	void onSubmit(ClickEvent e) {
		uploadForm.submit();
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}
}