package com.devoxx.genie.ui;

import com.devoxx.genie.ui.renderer.FileListCellRenderer;
import com.devoxx.genie.ui.topic.AppTopics;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.messages.MessageBus;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * The factory class for creating a panel with a text field for filtering files and a list of open files
 */
public class FileSelectionPanelFactory implements DumbAware {

    private static final int DOUBLE_CLICK = 2;

    private FileSelectionPanelFactory() {
    }

    /**
     * Creates a panel with a text field for filtering files and a list of files
     * @param project The current project
     * @return The panel
     */
    public static JPanel createPanel(Project project) {
        JBList<VirtualFile> resultList = createResultList(project);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JBScrollPane(resultList), BorderLayout.CENTER);
        return mainPanel;
    }

    /**
     * Creates a list of open files and files from the FilenameIndex search
     * @param project The current project
     * @return The list of files
     */
    private static JBList<VirtualFile> createResultList(Project project) {
        DefaultListModel<VirtualFile> listModel = new DefaultListModel<>();
        JBList<VirtualFile> resultList = new JBList<>(listModel);
        resultList.setCellRenderer(new FileListCellRenderer(project));

        addMouseListenerToResultList(project, resultList);
        populateListModelWithOpenFiles(project, listModel);

        return resultList;
    }

    /**
     * Adds a mouse listener to the result list to open the selected file
     * @param project The current project
     * @param resultList The list of files
     */
    private static void addMouseListenerToResultList(Project project,
                                                     JBList<VirtualFile> resultList) {
        resultList.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == DOUBLE_CLICK) {
                openSelectedFile(project, resultList);
            }
            }
        });
    }

    /**
     * Opens the selected file in the editor
     * @param project The current project
     * @param resultList The list of files
     */
    private static void openSelectedFile(Project project, JBList<VirtualFile> resultList) {
        VirtualFile selectedFile = resultList.getSelectedValue();
        MessageBus messageBus = project.getMessageBus();
        messageBus.syncPublisher(AppTopics.FILE_SELECTION_TOPIC).fileSelected(selectedFile);
    }

    /**
     * Populates the list model with the open files
     * @param project The current project
     * @param listModel The list model
     */
    private static void populateListModelWithOpenFiles(Project project,
                                                       DefaultListModel<VirtualFile> listModel) {
        for (VirtualFile file : FileEditorManager.getInstance(project).getOpenFiles()) {
            listModel.addElement(file);
        }
    }
}

