package edu.usc.chla.vpicu.explorer.newui.connection;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JButton;
import javax.swing.filechooser.FileSystemView;

public class FileChooserButton extends JButton implements ActionListener {

  private static final long serialVersionUID = 1L;

  private final FileDialog fdialog;
  private String ext;
  private File selectedFile;
  private SaveFileCallback callback;

  public FileChooserButton(String text) {
    super(text);
    fdialog = new FileDialog((Frame)null, getText());
    addActionListener(this);
  }

  public void setMode(int mode) {
    fdialog.setMode(mode);
  }

  public void setSuffix(String suffix) {
    ext = suffix;
    if (ext != null && !ext.isEmpty()) {
      fdialog.setFilenameFilter(new FilenameFilter() {

         @Override
         public boolean accept(File dir, String name) {
           return name.endsWith(ext) || name.toLowerCase().endsWith(ext.toLowerCase());
         }

       });
    }
  }

  public File getSelectedFile() {
    return selectedFile;
  }

  public void setSaveFileCallback(SaveFileCallback cb) {
    callback = cb;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == this) {
      fdialog.setVisible(true);
      String dir = fdialog.getDirectory();
      String file = fdialog.getFile();
      if (file != null) {
        selectedFile = new File(dir, file);
        switch (fdialog.getMode()) {
        case FileDialog.LOAD:
          setText(selectedFile.getName());
          setToolTipText(selectedFile.getPath());
          setIcon(FileSystemView.getFileSystemView().getSystemIcon(selectedFile));
          break;
        case FileDialog.SAVE:
          callback.saveFile(selectedFile);
          break;
        }
      }
      else
        selectedFile = null;
    }
  }

}
