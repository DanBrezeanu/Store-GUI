import sun.applet.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainGUI implements ActionListener {
    private JFrame mainFrame;
    private JFileChooser fileChooser;
    private JLabel storeTxt, customersTxt;
    private JButton browseStoreTxt, browseCustomersTxt, importFilesButton;
    private JTextField pathStoreTxt, pathCustomersTxt;


    MainGUI(){
        mainFrame = new JFrame("Store");

        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //mainFrame.setPreferredSize(new Dimension(900,500));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new GridBagLayout());
        //mainFrame.pack();
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = c.gridy = 0;

        fileChooser = new JFileChooser();
        storeTxt = new JLabel("   store.txt  ");
        customersTxt = new JLabel("   customers.txt  ");
        browseStoreTxt = new JButton("Browse..");
        browseCustomersTxt = new JButton("Browse..");
        pathStoreTxt = new JTextField(50);
        pathCustomersTxt = new JTextField(50);

        mainFrame.add(browseStoreTxt, c);
        c.gridx++;
        mainFrame.add(storeTxt, c);
        c.gridx++;
        mainFrame.add(pathStoreTxt, c);

        c.gridy+= 2; c.gridx = 0;

        mainFrame.add(browseCustomersTxt, c);
        c.gridx++;
        mainFrame.add(customersTxt, c);
        c.gridx++;
        mainFrame.add(pathCustomersTxt, c);


        browseCustomersTxt.addActionListener(this);
        browseStoreTxt.addActionListener(this);

        c.gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 3;
        importFilesButton = new JButton("Import files");
        importFilesButton.addActionListener(this);

        mainFrame.add(importFilesButton, c);


        mainFrame.setVisible(true);
    }

    void startStore(File storeFile, File customersFile){

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == browseStoreTxt){
            int returnValue = fileChooser.showOpenDialog(mainFrame);

            if(returnValue == JFileChooser.APPROVE_OPTION){
                File file = fileChooser.getSelectedFile();
                pathStoreTxt.setText(file.getPath());
            }
        }

        if(e.getSource() == browseCustomersTxt){
            int returnValue = fileChooser.showOpenDialog(mainFrame);

            if(returnValue == JFileChooser.APPROVE_OPTION){
                File file = fileChooser.getSelectedFile();
                pathCustomersTxt.setText(file.getPath());
            }
        }

        if(e.getSource() == importFilesButton){
            File storeTxtFile = new File(pathStoreTxt.getText());
            File customersTxtFile = new File(pathCustomersTxt.getText());

            startStore(storeTxtFile, customersTxtFile);
        }
    }

    public static void main(String args[]){
        new MainGUI();
    }

}
