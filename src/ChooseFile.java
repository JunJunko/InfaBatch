import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

class ShowDialogLintener extends MouseAdapter{  
    JFrame frame;  
    public ShowDialogLintener(JFrame frame) {  
        this.frame = frame;  
    }  
    @Override  
    public void mouseClicked(MouseEvent arg0) {  
        super.mouseClicked(arg0);  
        JFileChooser chooser = new JFileChooser(".");  
        chooser.showOpenDialog(frame);  
        String filePath = chooser.getSelectedFile().getAbsolutePath();  
        System.out.println(filePath);  
    }  
}  