package kr.co.hanulit.harvest.jws;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import kr.co.hanulit.common.Logger;
import kr.co.hanulit.common.SessionTimerTask;
import kr.co.hanulit.harvest.jws.ui.CheckInWindow;
import kr.co.hanulit.harvest.jws.ui.CheckOutWindow;
import kr.co.hanulit.harvest.jws.ui.DeleteVersionWindow;
import kr.co.hanulit.harvest.jws.ui.KFTCDataWindow;
import kr.co.hanulit.harvest.jws.ui.RemoveItemWindow;
import kr.co.hanulit.harvest.model.HarvestContext;
import kr.co.hanulit.harvest.model.HarvestDataModel;
import kr.co.hanulit.harvest.model.KFTCDataInputProc;

public class HarvestModuleManager {
  private HarvestContext context = null;
  
  private HarvestDataModel dao = null;
  
  public boolean isInitialize = false;
  
  public boolean isLockFile = true;
  
  private Timer timer = null;
  
  public HarvestModuleManager() {
    this.timer = new Timer();
    this.timer.scheduleAtFixedRate((TimerTask)new SessionTimerTask(), 9900000L, 9900000L);
  }
  
  public boolean initHarModulePath() {
    boolean ret = false;
    try {
      try {
        Files.walkFileTree(HarvestModuleConstants.PATH_HARMODULE_TMP, new SimpleFileVisitor<Path>() {
              public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
              }
            });
      } catch (IOException ex) {
        Logger.storeMsg(HarvestModuleManager.class.getName(), Level.OFF, null, ex);
      } 
      if (!HarvestModuleConstants.PATH_HARMODULE_ROOT.toFile().exists())
        Files.createDirectories(HarvestModuleConstants.PATH_HARMODULE_ROOT, (FileAttribute<?>[])new FileAttribute[0]); 
      if (!HarvestModuleConstants.PATH_HARMODULE_EXT.toFile().exists())
        Files.createDirectories(HarvestModuleConstants.PATH_HARMODULE_EXT, (FileAttribute<?>[])new FileAttribute[0]); 
      if (!HarvestModuleConstants.PATH_HARMODULE_TMP.toFile().exists())
        Files.createDirectories(HarvestModuleConstants.PATH_HARMODULE_TMP, (FileAttribute<?>[])new FileAttribute[0]); 
      if (!HarvestModuleConstants.PATH_HARMODULE_LOG.toFile().exists())
        Files.createDirectories(HarvestModuleConstants.PATH_HARMODULE_LOG, (FileAttribute<?>[])new FileAttribute[0]); 
      InputStream is = null;
      if (!HarvestModuleConstants.PATH_VDIFF2_EXE.toFile().exists()) {
        Files.createFile(HarvestModuleConstants.PATH_VDIFF2_EXE, (FileAttribute<?>[])new FileAttribute[0]);
        try {
          is = getClass().getResourceAsStream("/kr/co/hanulit/harvest/resource/vdiff2_r12_1_3");
          Files.copy(is, HarvestModuleConstants.PATH_VDIFF2_EXE, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
        } catch (IOException ex) {
          Logger.storeMsg(HarvestModuleManager.class.getName(), Level.OFF, null, ex);
        } finally {
          close(is);
        } 
      } else {
        try {
          is = getClass().getResourceAsStream("/kr/co/hanulit/harvest/resource/vdiff2_r12_1_3");
          Files.copy(is, HarvestModuleConstants.PATH_VDIFF2_EXE, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
        } catch (IOException ex) {
          Logger.storeMsg(HarvestModuleManager.class.getName(), Level.OFF, null, ex);
        } finally {
          close(is);
        } 
      } 
      ret = true;
    } catch (IOException ex) {
      Logger.storeMsg(HarvestModuleManager.class.getName(), Level.OFF, null, ex);
    } 
    return ret;
  }
  
  public void openHarvestWindows() {
    try {
      for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        } 
      } 
    } catch (ClassNotFoundException|InstantiationException|IllegalAccessException|javax.swing.UnsupportedLookAndFeelException ex) {
      Logger.storeMsg(HarvestModuleManager.class.getName(), Level.SEVERE, null, ex);
    } 
    if (this.context.checkOutProcessObjId > 0) {
      SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              try {
                HarvestModuleManager.this.openCheckOutWin();
              } catch (RuntimeException e) {
                HarvestModuleManager.this.createAndShowErrorDialog(5, null, null, e.getMessage());
              } 
            }
          });
    } else if (this.context.checkInProcessObjId > 0) {
      SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              try {
                HarvestModuleManager.this.openCheckInWin();
              } catch (RuntimeException e) {
                HarvestModuleManager.this.createAndShowErrorDialog(5, null, null, e.getMessage());
              } 
            }
          });
    } else if (this.context.removeItemProcessObjId > 0) {
      SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              try {
                HarvestModuleManager.this.openRemoveItemWin();
              } catch (RuntimeException e) {
                HarvestModuleManager.this.createAndShowErrorDialog(5, null, null, e.getMessage());
              } 
            }
          });
    } else if (this.context.deleteVersionProcessObjId > 0) {
      SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              try {
                HarvestModuleManager.this.openDeleteVersionWin();
              } catch (RuntimeException e) {
                HarvestModuleManager.this.createAndShowErrorDialog(5, null, null, e.getMessage());
              } 
            }
          });
    } else if (this.context.isKFTCDataInput.booleanValue()) {
      SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              try {
                HarvestModuleManager.this.openKFTCDataWin();
              } catch (Exception e) {
                HarvestModuleManager.this.createAndShowErrorDialog(5, null, null, e.getMessage());
              } 
            }
          });
    } else {
      System.exit(1);
    } 
  }
  
  public void openCheckOutWin() {
    (new CheckOutWindow(this.context)).setVisible(true);
  }
  
  public void openCheckInWin() {
    (new CheckInWindow(this.context)).setVisible(true);
  }
  
  public void openDeleteVersionWin() {
    (new DeleteVersionWindow(this.context)).setVisible(true);
  }
  
  public void openRemoveItemWin() {
    (new RemoveItemWindow(this.context)).setVisible(true);
  }
  
  public void openKFTCDataWin() {
    (new KFTCDataWindow(this.context)).setVisible(true);
  }
  
  public boolean initHarvestContext(String _daoServletURL, String _brokerName, int _usrObjId, String _userPass, int _packageObjId, String _siteMode, String _navigationMode) {
    boolean ret = false;
    this.context = new HarvestContext();
    this.dao = new HarvestDataModel(_daoServletURL, _brokerName, _usrObjId, _userPass, _packageObjId, this.context);
    this.context.siteMode = _siteMode;
    this.context.navigationMode = _navigationMode;
    if (HarvestDataModel.isInit == true)
      ret = true; 
    return ret;
  }
  
  public void setCheckOutProcess(int _processObjId) {
    this.context.checkOutProcessObjId = _processObjId;
    this.context.checkOutProc = this.dao.getCheckOutProcessInfo(_processObjId);
  }
  
  public void setCheckInProcess(int _processObjId) {
    this.context.checkInProcessObjId = _processObjId;
    this.context.checkInProc = this.dao.getCheckInProcessInfo(_processObjId);
  }
  
  public void setDeleteVersionProcess(int _processObjId) {
    this.context.deleteVersionProcessObjId = _processObjId;
    this.context.deleteVersionProc = this.dao.getDelelteVersionProcessInfo(_processObjId);
  }
  
  public void setRemoveItemProcess(int _processObjId) {
    this.context.removeItemProcessObjId = _processObjId;
    this.context.removeItemProc = this.dao.getRemoveItemProcessInfo(_processObjId);
  }
  
  public void setKFTCDataInputProcess() {
    this.context.isKFTCDataInput = Boolean.valueOf(true);
    this.context.kftcDataInputProc = new KFTCDataInputProc();
  }
  
  public static void main(String[] args) {
    try {
      for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        } 
      } 
    } catch (ClassNotFoundException|InstantiationException|IllegalAccessException|javax.swing.UnsupportedLookAndFeelException ex) {
      Logger.storeMsg(HarvestModuleManager.class.getName(), Level.SEVERE, null, ex);
    } 
    String servletURL = "";
    String broker = "";
    int usrID = 0;
    String usrPass = "";
    int packageID = 0;
    int checkOutProcessID = 0;
    int checkInProcessID = 0;
    int deleteVersionProcessID = 0;
    int removeItemProcessID = 0;
    String siteMode = "";
    String navigationMode = "";
    String argsString = "";
    for (int i = 0; i < args.length; i++)
      argsString = argsString + args[i]; 
    String[] _args = argsString.split(" ");
    int iParms = _args.length;
    for (int j = 0; j < iParms; j++) {
      try {
        if (_args[j].compareToIgnoreCase("-url") == 0) {
          servletURL = _args[j + 1];
        } else if (_args[j].compareToIgnoreCase("-b") == 0) {
          broker = _args[j + 1];
        } else if (_args[j].compareToIgnoreCase("-usr") == 0) {
          usrID = Integer.parseInt(_args[j + 1]);
        } else if (_args[j].compareToIgnoreCase("-pass") == 0) {
          usrPass = _args[j + 1];
          
        } else if (_args[j].compareToIgnoreCase("-pkg") == 0) {
          packageID = Integer.parseInt(_args[j + 1]);
        } else if (_args[j].compareToIgnoreCase("-copn") == 0) {
          checkOutProcessID = Integer.parseInt(_args[j + 1]);
        } else if (_args[j].compareToIgnoreCase("-cipn") == 0) {
          checkInProcessID = Integer.parseInt(_args[j + 1]);
        } else if (_args[j].compareToIgnoreCase("-ripn") == 0) {
          removeItemProcessID = Integer.parseInt(_args[j + 1]);
        } else if (_args[j].compareToIgnoreCase("-dvpn") == 0) {
          deleteVersionProcessID = Integer.parseInt(_args[j + 1]);
        } else if (_args[j].compareToIgnoreCase("-siteMode") == 0) {
          siteMode = _args[j + 1];
        } else if (_args[j].compareToIgnoreCase("-navigationMode") == 0) {
          navigationMode = _args[j + 1];
        } 
      } catch (NullPointerException|NumberFormatException ex) {
        Logger.storeMsg(HarvestModuleManager.class.getName(), Level.OFF, null, ex);
      } 
    } 
    HarvestModuleManager manager = new HarvestModuleManager();
    if (!manager.initHarModulePath()) {
      manager.createAndShowErrorDialog(1, null, null, "");
      return;
    } 
    if (!manager.initHarvestEnvironment()) {
      manager.createAndShowErrorDialog(2, null, null, "");
      return;
    } 
    if (!manager.initHarvestContext(servletURL, broker, usrID, usrPass, packageID, siteMode, navigationMode)) {
      manager.createAndShowErrorDialog(3, manager.context.brokerName, manager.dao.getServletURL(), "");
      return;
    } 
    if (checkOutProcessID > 0) {
      manager.setCheckOutProcess(checkOutProcessID);
    } else if (checkInProcessID > 0) {
      manager.setCheckInProcess(checkInProcessID);
    } else if (deleteVersionProcessID > 0) {
      manager.setDeleteVersionProcess(deleteVersionProcessID);
    } else if (removeItemProcessID > 0) {
      manager.setRemoveItemProcess(removeItemProcessID);
    } else {
      manager.setKFTCDataInputProcess();
    } 
    manager.openHarvestWindows();
  }
  
  private void createAndShowErrorDialog(int _msgId, String _brokerName, String _serverURL, String _message) {
    String errorMsg = "";
    switch (_msgId) {
      case 1:
        errorMsg = ";
        break;
      case 2:
        errorMsg = "Harvest Workbench \nWorkbench;
        break;
      case 3:
        errorMsg = "Harvest Server \n\nbroker[" + _brokerName + "] / serverURL[" + _serverURL + "]";
        break;
      case 4:
        errorMsg = "\n;
        break;
      case 5:
        errorMsg = "Harvest Server \n\nMessage[" + _message + "]";
        break;
    } 
    JOptionPane.showMessageDialog(null, errorMsg);
    System.exit(1);
  }
  
  private boolean initHarvestEnvironment() {
    boolean ret = false;
    String caScmHome = System.getenv().get("CA_SCM_HOME");
    if (caScmHome != null && caScmHome.length() > 0) {
      File fileHco = new File(caScmHome + "\\" + "hco.exe");
      File fileHci = new File(caScmHome + "\\" + "hci.exe");
      File fileHri = new File(caScmHome + "\\" + "hri.exe");
      File fileHdv = new File(caScmHome + "\\" + "hdv.exe");
      ret = fileHco.canExecute();
      if (ret)
        ret = fileHci.canExecute(); 
      if (ret)
        ret = fileHri.canExecute(); 
      if (ret)
        ret = fileHdv.canExecute(); 
    } 
    return ret;
  }
  
  private static boolean isAlreadyRunning() {
    try {
      final RandomAccessFile randomAccessFile = new RandomAccessFile(HarvestModuleConstants.PATH_HARMODULE_LOCK.toFile(), "rw");
      final FileLock fileLock = randomAccessFile.getChannel().tryLock();
      if (fileLock != null) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
              public void run() {
                try {
                  fileLock.release();
                  randomAccessFile.close();
                  HarvestModuleConstants.PATH_HARMODULE_LOCK.toFile().delete();
                } catch (IOException ex) {
                  Logger.storeMsg(HarvestModuleManager.class.getName(), Level.OFF, null, ex);
                } 
              }
            });
        return false;
      } 
    } catch (IOException ex) {
      Logger.storeMsg(HarvestModuleManager.class.getName(), Level.OFF, null, ex);
    } 
    return true;
  }
  
  private void close(InputStream is) {
    try {
      if (is != null)
        is.close(); 
    } catch (IOException ex) {
      Logger.storeMsg(HarvestModuleManager.class.getName(), Level.OFF, null, ex);
    } 
  }
}

