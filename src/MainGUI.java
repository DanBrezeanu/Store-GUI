import com.sun.javafx.beans.IDProperty;
import sun.applet.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class MainGUI implements ActionListener {
    private JFrame mainFrame;
    private JPanel importPanel;
    private JFileChooser fileChooser;
    private JLabel storeTxt, customersTxt;
    private JButton browseStoreTxt, browseCustomersTxt, importFilesButton;
    private JTextField pathStoreTxt, pathCustomersTxt;

    private JTabbedPane tabbedPane;
    private JPanel storePanel, customerPanel;

    private JTable departTable, productTable;
    private JScrollPane departScrollPane, prodScrollPane;

    private JPanel storeDepartPanel, storeProductsPanel, storeButtonsPanel;
    private JButton addProductButton, sortProductsButton, modifyProductButton, deleteProductButton;

    private JFrame popUpFrame;

    private String[] prodColNames = { "Name", "ID", "Price", "Department"};
    private Object[][] prodData;


    MainGUI(){
        mainFrame = new JFrame("Store");
        importPanel = new JPanel();

        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //mainFrame.setPreferredSize(new Dimension(900,500));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //mainFrame.setLocationRelativeTo(null);
        importPanel.setLayout(new GridBagLayout());
        //mainFrame.pack();
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = c.gridy = 0;

        fileChooser = new JFileChooser();
        storeTxt = new JLabel("   store.txt  ");
        customersTxt = new JLabel("   customers.txt  ");
        browseStoreTxt = new JButton("Browse..");
        browseCustomersTxt = new JButton("Browse..");
        pathStoreTxt = new JTextField("C:\\Users\\Dan\\Desktop\\test00\\store.txt"); //TODO: 50
        pathCustomersTxt = new JTextField("C:\\Users\\Dan\\Desktop\\test00\\customers.txt"); //TODO: 50

        importPanel.add(browseStoreTxt, c);
        c.gridx++;
        importPanel.add(storeTxt, c);
        c.gridx++;
        importPanel.add(pathStoreTxt, c);

        c.gridy+= 2; c.gridx = 0;

        importPanel.add(browseCustomersTxt, c);
        c.gridx++;
        importPanel.add(customersTxt, c);
        c.gridx++;
        importPanel.add(pathCustomersTxt, c);


        browseCustomersTxt.addActionListener(this);
        browseStoreTxt.addActionListener(this);

        c.gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 3;
        importFilesButton = new JButton("Import files");
        importFilesButton.addActionListener(this);

        importPanel.add(importFilesButton, c);

        importPanel.setVisible(true);
        mainFrame.add(importPanel);
        mainFrame.setVisible(true);
    }

    void startStore(File storeFile, File customersFile){
        Test testObj = new Test();
        Store store;

        testObj.parseStoreTxt(storeFile);
        testObj.parseCustomersTxt(customersFile);

        store = Store.getInstance("dummy_text");

        mainFrame.remove(importPanel);

        createMainGUI();
    }

    public void createMainGUI(){
        Store storeInstance = Store.getInstance("dummy_text");


        mainFrame.setTitle(storeInstance.getName());

        tabbedPane = new JTabbedPane();

        storePanel = new JPanel();
        createStorePanel();
        tabbedPane.addTab("Store", storePanel);


        customerPanel = new JPanel();
        //TODO: create panel contents

        tabbedPane.addTab("Customer", customerPanel);

        mainFrame.add(tabbedPane);
        mainFrame.revalidate();
    }

    public void createStorePanel(){
        Store store = Store.getInstance(mainFrame.getTitle());

        int totalProducts = 0;

        storeDepartPanel = new JPanel();
        storeProductsPanel = new JPanel();
        storeButtonsPanel = new JPanel();

        storePanel.setLayout(new FlowLayout());

        String[] departColNames = {"ID", "Name", "# Products"};
        Object[][] departData = new Object[store.getDepartments().size()][3];
        int currentDepart = 0;

        for(Department d : store.getDepartments()){
            departData[currentDepart][0] = d.getID();
            departData[currentDepart][1] = d.getName();
            departData[currentDepart][2] = d.getItems().size();

            totalProducts += d.getItems().size();

            currentDepart++;
        }

        departTable = new JTable(departData, departColNames);
        departScrollPane = new JScrollPane(departTable);
        departTable.setFillsViewportHeight(true);

        storeDepartPanel.add(departScrollPane);
        storePanel.add(storeDepartPanel);

        int currentProd = 0;
        prodData = new Object[totalProducts][4];

        for(Department d : store.getDepartments()){
            for(Item i : d.getItems()){
                prodData[currentProd][0] = i.getName();
                prodData[currentProd][1] = i.getID();
                prodData[currentProd][2] = i.getPrice();
                prodData[currentProd][3] = d.getName();

                currentProd++;
            }
        }

        productTable = new JTable(prodData, prodColNames);
        prodScrollPane = new JScrollPane(productTable);
        prodScrollPane.setSize(new Dimension(300, 1000));
        departTable.setFillsViewportHeight(true);

        storeProductsPanel.add(prodScrollPane);
        storePanel.add(storeProductsPanel);

        storeButtonsPanel.setLayout(new BoxLayout(storeButtonsPanel, BoxLayout.Y_AXIS));

        sortProductsButton = new JButton("Sort");
        addProductButton = new JButton("Add Product");
        modifyProductButton = new JButton("Modify Product");
        deleteProductButton = new JButton("Delete Product");

        addProductButton.addActionListener(this);
        sortProductsButton.addActionListener(this);

        storeButtonsPanel.add(sortProductsButton);
        storeButtonsPanel.add(addProductButton);
        storeButtonsPanel.add(modifyProductButton);
        storeButtonsPanel.add(deleteProductButton);

        storePanel.add(storeButtonsPanel);

        mainFrame.revalidate();
    }

    public void createPopUpAddProduct(){
        Store store = Store.getInstance("dummy_text");

        JLabel newProdName, newProdID, newProdPrice, newProdDepartment;
        JPanel mainPopUpPanel;
        JTextField nameTextField, IDTextField, priceTextField;
        JComboBox<String> departmentCombo;
        String[] comboData = new String[4];
        int currentDepart = 0;

        for(Department d : store.getDepartments()){
            comboData[currentDepart] = d.getName();
            currentDepart++;
        }

        departmentCombo = new JComboBox<>(comboData);

        newProdName = new JLabel("  Name:  ");
        newProdID = new JLabel("  ID:  ");
        newProdPrice = new JLabel("  Price:  ");
        newProdDepartment = new JLabel("  Department:  ");

        nameTextField = new JTextField(20);
        IDTextField = new JTextField(20);
        priceTextField = new JTextField(20);

        mainPopUpPanel = new JPanel(new GridLayout(0,1));

        mainPopUpPanel.add(newProdName);
        mainPopUpPanel.add(nameTextField);
        mainPopUpPanel.add(newProdID);
        mainPopUpPanel.add(IDTextField);
        mainPopUpPanel.add(newProdPrice);
        mainPopUpPanel.add(priceTextField);
        mainPopUpPanel.add(newProdDepartment);
        mainPopUpPanel.add(departmentCombo);


        int result = JOptionPane.showConfirmDialog(null, mainPopUpPanel, "Add New Product",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if(result == JOptionPane.OK_OPTION){
            Item newItem = new Item(nameTextField.getText(), Integer.parseInt(IDTextField.getText()),
                    Double.parseDouble(priceTextField.getText()));

           store.getDepartments().elementAt(departmentCombo.getSelectedIndex()).addItem(newItem);

           storePanel.removeAll();
           createStorePanel();
        }
    }

    public void createPopUpSortProducts(){
        JPanel mainPopUpPanel = new JPanel(new GridLayout(0,1));
        String[] comboData = {"Smallest to Highest (Price)", "Highest to Smallest (Price)", "A to Z", "Z to A"};
        JComboBox<String> comboBox = new JComboBox<>(comboData);

        mainPopUpPanel.add(comboBox);

        int result = JOptionPane.showConfirmDialog(null, mainPopUpPanel, "Sort Products",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if(result == JOptionPane.OK_OPTION){
            switch(comboBox.getSelectedIndex()){
                case 0: // <
                    for(int i = 0; i < prodData.length - 1; ++i)
                        for(int j = i + 1; j < prodData.length; ++j){
                            Double o1 = (Double)prodData[i][2];
                            Double o2 = (Double)prodData[j][2];

                            if(o1.compareTo(o2) > 0){
                                Object[] aux = new Object[4];

                                for(int k = 0; k < 4; ++k)
                                    aux[k] = prodData[i][k];

                                prodData[i] = prodData[j];
                                prodData[j] = aux;
                            }
                        }
                    break;

                case 1: // >
                    for(int i = 0; i < prodData.length - 1; ++i)
                        for(int j = i + 1; j < prodData.length; ++j){
                            Double o1 = (Double)prodData[i][2];
                            Double o2 = (Double)prodData[j][2];

                            if(o1.compareTo(o2) < 0){
                                Object[] aux = new Object[4];

                                for(int k = 0; k < 4; ++k)
                                    aux[k] = prodData[i][k];

                                prodData[i] = prodData[j];
                                prodData[j] = aux;
                            }
                        }
                    break;

                case 2: // A-Z
                    for(int i = 0; i < prodData.length - 1; ++i)
                        for(int j = i + 1; j < prodData.length; ++j){
                            String o1 = (String)prodData[i][0];
                            String o2 = (String)prodData[j][0];

                            if(o1.compareTo(o2) > 0){
                                Object[] aux = new Object[4];

                                for(int k = 0; k < 4; ++k)
                                    aux[k] = prodData[i][k];

                                prodData[i] = prodData[j];
                                prodData[j] = aux;
                            }
                        }
                    break;

                case 3: //Z-A
                    for(int i = 0; i < prodData.length - 1; ++i)
                        for(int j = i + 1; j < prodData.length; ++j){
                            String o1 = (String)prodData[i][0];
                            String o2 = (String)prodData[j][0];

                            if(o1.compareTo(o2) < 0){
                                Object[] aux = new Object[4];

                                for(int k = 0; k < 4; ++k)
                                    aux[k] = prodData[i][k];

                                prodData[i] = prodData[j];
                                prodData[j] = aux;
                            }
                        }
                    break;
            }

            storeProductsPanel.remove(prodScrollPane);
            prodScrollPane = new JScrollPane(new JTable(prodData, prodColNames));
            storeProductsPanel.add(prodScrollPane);
            storeProductsPanel.revalidate();

        }


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


        if(e.getSource() == addProductButton){
            createPopUpAddProduct();
        }

        if(e.getSource() == sortProductsButton){
            createPopUpSortProducts();

        }
    }

    public static void main(String args[]){
        new MainGUI();
    }

}
