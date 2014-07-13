package org.issg.upload;

import it.jrc.form.editor.EditorPanelHeading;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jrc.edit.Dao;

import com.google.common.io.Files;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;

/**
 * Uploads and intiates processing on an excel file, displaying any errors that
 * occur.
 * 
 * Subclasses provide a specialized upload parser via getUploadParser(). It is
 * better to construct a new {@link UploadParser} for each request to prevent
 * state being preserved improperly.
 * 
 * @author will
 * 
 */
public abstract class AbstractUploader extends CustomComponent implements
        Upload.SucceededListener, Upload.FailedListener, Upload.Receiver {

    private VerticalLayout root;
    
    private VerticalLayout statusPanel;
    
    private File file;
    
    protected Dao dao;

    private ProcessingCompleteListener processingCompleteListener;

    /**
     * Used to capture completed processing jobs
     * 
     * @author tempewi
     * 
     */
    public class ProcessingCompleteEvent extends EventObject {

        private List<?> results;

        public ProcessingCompleteEvent(Object source, List<?> entities) {
            super(source);
            this.results = entities;
        }

        public List<?> getResults() {
            return results;
        }
    }

    public interface ProcessingCompleteListener {

        public void processingComplete(ProcessingCompleteEvent p);

    }

    public AbstractUploader(Dao dao, String title) {

        this.dao = dao;

        root = new VerticalLayout();

        setCompositionRoot(root);

        EditorPanelHeading panelHead = new EditorPanelHeading(title);
        root.addComponent(panelHead);
        
        /*
         * Build upload component, giving it this class as a listener
         */
        final Upload upload = new Upload("Choose a file to upload:", this);
        upload.setButtonCaption("Upload");
        upload.addSucceededListener(this);
        upload.addFailedListener(this);

        root.addComponent(upload);

        /*
         * Displays the status of the upload.
         */
        statusPanel = new VerticalLayout();
        root.addComponent(statusPanel);
    }
    
    /**
     * Vaadin asks for an output stream to be constructed, to which it will
     * write the uploaded data.
     */
    public OutputStream receiveUpload(String filename, String MIMEType) {

        FileOutputStream fos = null; // Output stream to write to

        File tempDir = Files.createTempDir();
        file = new File(tempDir, filename);

        try {
            
            fos = new FileOutputStream(file);
            
        } catch (FileNotFoundException e) {
            showStatus("");
            e.printStackTrace();
            return null;
        }

        return fos; // Return the output stream to write to
    }

    /**
     * 
     * Called when the upload is finished. Performs post-upload processing.
     * 
     */
    @Override
    public void uploadSucceeded(Upload.SucceededEvent event) {

        String mimeType = event.getMIMEType();

        try {

            UploadParser<?> uploadParser = getUploadParser();

            Workbook wb;
            try {
                wb = WorkbookFactory.create(new FileInputStream(file));
            } catch (IllegalArgumentException e) {
                showErrors(Arrays.asList(new String[] { e.getMessage() }));
                return;
            }

            uploadParser.processWorkbook(wb);

            if (uploadParser.hasErrors()) {
                showErrors(uploadParser.getErrors());
                return;
            }

            /*
             * Get entities and persist
             */
            List<?> entities = uploadParser.getEntityList();
            EntityManager em = dao.getEntityManager();
            try {

                em.getTransaction().begin();
                for (Object object : entities) {
                    em.persist(object);
                }
                em.getTransaction().commit();
                
                notifyProcessingComplete(entities);

            } catch (Exception e) {

                showStatus("Unexpected error committing transaction: "
                        + e.getMessage());

                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
            }

            showStatus(entities.size() + " rows processed.");

        } catch (InvalidFormatException e) {
            showStatus("Invalid format: " + mimeType);
        } catch (FileNotFoundException e) {
            showStatus("Cannot find uploaded file.");
        } catch (IOException e) {
            showStatus("Failed due to unkown error.");
        }

    }

    @Override
    public void uploadFailed(Upload.FailedEvent event) {
        statusPanel.removeAllComponents();
        root.addComponent(new Label("Uploading " + event.getFilename()
                + " of type '" + event.getMIMEType() + "' failed."));
    }

    protected void showStatus(String status) {

        statusPanel.removeAllComponents();
        statusPanel.addComponent(new Label(status));

    }

    protected void showErrors(List<String> errs) {
        statusPanel.removeAllComponents();
        for (int i = 0; i < errs.size(); i++) {
            statusPanel.addComponent(new Label(errs.get(i)));
            if (i == 50) {
                statusPanel.addComponent(new Label("Not showing "
                        + (errs.size() - i) + " more errors."));
                break;
            }
        }
    }

    abstract UploadParser<?> getUploadParser();
    
    public void addProcessingCompleteListener(ProcessingCompleteListener listener) {
        
        this.processingCompleteListener = listener;
        
    }

    private void notifyProcessingComplete(List<?> entities) {
        if (processingCompleteListener!= null) {
            processingCompleteListener.processingComplete(new ProcessingCompleteEvent(this, entities));
        }
    }

}