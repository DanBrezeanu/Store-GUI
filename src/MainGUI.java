import com.sun.javafx.beans.IDProperty;
import sun.applet.Main;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

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

    private String[] prodColNames = { "Name", "ID", "Price", "Department"};
    private Object[][] prodData;

    private JTabbedPane customerTabbedPane;
    private JPanel shoppingCartPanel, wishListPanel;
    private Customer referenceCustomer = null;
    private JPanel selectCustomerPanel;

    JTable shoppingCartTable, wishListTable;
    JScrollPane shoppingCartScrollPane, wishListScrollPane;
    JLabel valueShoppingCartLabel = new JLabel();

    public Double findShoppingCartTotal(){
        Double totalValue = 0.0;
        ListIterator<Item> it = referenceCustomer.getShoppingCart().listIterator();

        while(it.hasNext()){
            totalValue += it.next().getPrice();
        }

        return totalValue;
    }

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
        pathStoreTxt = new JTextField("C:\\Users\\Dan\\Desktop\\test00\\store.txt"); //TODO: new TextField(50)
        pathCustomersTxt = new JTextField("C:\\Users\\Dan\\Desktop\\test00\\customers.txt"); //TODO: new TextField(50)

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

    public void startStore(File storeFile, File customersFile){
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

        tabbedPane.addTab("Customer", customerPanel);

       createCustomerPanel();

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
        departScrollPane.setPreferredSize(new Dimension(400,86));
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

        sortProductsButton = new JButton("         Sort           ");
        addProductButton = new JButton(" Add Product    ");
        modifyProductButton = new JButton("Modify Product");
        deleteProductButton = new JButton("Delete Product");

        addProductButton.addActionListener(this);
        sortProductsButton.addActionListener(this);
        modifyProductButton.addActionListener(this);
        deleteProductButton.addActionListener(this);

        storeButtonsPanel.add(sortProductsButton);
        storeButtonsPanel.add(new Box.Filler(new Dimension(10,20), new Dimension(20,10),
                new Dimension(10,20)));
        storeButtonsPanel.add(addProductButton);
        storeButtonsPanel.add(new Box.Filler(new Dimension(10,20), new Dimension(20,10),
                new Dimension(10,20)));
        storeButtonsPanel.add(modifyProductButton);
        storeButtonsPanel.add(new Box.Filler(new Dimension(10,20), new Dimension(20,10),
                new Dimension(10,20)));
        storeButtonsPanel.add(deleteProductButton);

        storePanel.add(storeButtonsPanel);

        mainFrame.revalidate();
    }

    public void createCustomerPanel(){
        customerTabbedPane = new JTabbedPane();
        customerTabbedPane.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));

        if(referenceCustomer == null)
            createSelectCustomerPanel();
        else
            createShoppingCartPanel();

        shoppingCartPanel = selectCustomerPanel;
        customerTabbedPane.addTab("Shopping Cart", shoppingCartPanel);

        createWishListPanel();

        customerTabbedPane.addTab("Wishlist", wishListPanel);

        customerPanel.add(customerTabbedPane);



    }

    public void createShoppingCartPanel(){
        Store store = Store.getInstance("dummy_text");
        shoppingCartPanel = new JPanel(new FlowLayout());
        JPanel tableShoppingCartPanel = new JPanel();
        ListIterator<Item> it = referenceCustomer.getShoppingCart().listIterator();
        Item currentItem;
        Integer current = 0;

        DefaultTableModel tableModel = new DefaultTableModel();
        for(String s : prodColNames)
            tableModel.addColumn(s);

        while(it.hasNext()){
            currentItem = it.next();
            Vector<Object> newRow = new Vector<>();


            newRow.add(currentItem.getName());
            newRow.add(currentItem.getID());
            newRow.add(currentItem.getPrice());

            for(Department d : store.getDepartments())
                for(Item i : d.getItems())
                    if(i.equals(currentItem))
                        newRow.add(d.getID());
        }


        shoppingCartTable = new JTable(tableModel);

        shoppingCartScrollPane = new JScrollPane(shoppingCartTable);
        shoppingCartTable.setFillsViewportHeight(true);

        tableShoppingCartPanel.add(shoppingCartScrollPane);
        shoppingCartPanel.add(tableShoppingCartPanel);


        JPanel buttonsShoppingCartPanel = new JPanel(new GridLayout(0,1));
        JButton addNewItemShoppingCartButton = new JButton("Add item");
        JButton deleteItemShoppingCartButton = new JButton("Delete item");
        JButton finaliseOrderShoppingCartButton = new JButton("Finalise order");

        addNewItemShoppingCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Item refItem = createPopUpAddRemoveToShoppingCart();

                if(refItem != null) {
                    referenceCustomer.getShoppingCart().add(refItem);
                    Object[] newEntry = new Object[4];
                    newEntry[0] = refItem.getName(); newEntry[1] = refItem.getID();
                    newEntry[2] = refItem.getPrice();

                    for(Department d : store.getDepartments())
                        for(Item i : d.getItems())
                            if(i.equals(refItem))
                                newEntry[3] = d.getID();

                    ((DefaultTableModel)shoppingCartTable.getModel()).addRow(newEntry);
                    shoppingCartTable.revalidate();
                    shoppingCartScrollPane.revalidate();

                    valueShoppingCartLabel.setText("           " + findShoppingCartTotal().toString());

                }
            }
        });

        deleteItemShoppingCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Item refItem = createPopUpAddRemoveToShoppingCart();
                boolean removedItem = false;
                ListIterator<Item> it = referenceCustomer.getShoppingCart().listIterator();

                if(refItem == null){
                    JOptionPane.showMessageDialog(mainFrame, "Product does not exist");
                    return;
                }

                while(it.hasNext()) {
                    Item currentItem = it.next();
                    if (currentItem.getID().equals(refItem.getID()))
                        for (int i = 0; i < shoppingCartTable.getModel().getRowCount(); ++i)
                            if ((shoppingCartTable.getModel().getValueAt(i, 1)).equals(refItem.getID())) {
                                DefaultTableModel shoppingCartTableModel = (DefaultTableModel) shoppingCartTable.getModel();
                                shoppingCartTableModel.removeRow(i);
                                referenceCustomer.getShoppingCart().remove(refItem);
                                shoppingCartTable.revalidate();
                                removedItem = true;
                                valueShoppingCartLabel.setText("           " + findShoppingCartTotal().toString());
                                break;
                            }
                }

                if(!removedItem){
                    JOptionPane.showMessageDialog(mainFrame, "Product is not in shopping cart");
                }

                shoppingCartTable.revalidate();
                shoppingCartScrollPane.revalidate();
            }
        });

        buttonsShoppingCartPanel.add(addNewItemShoppingCartButton);
        buttonsShoppingCartPanel.add(new JLabel(" ")); //filler
        buttonsShoppingCartPanel.add(deleteItemShoppingCartButton);
        buttonsShoppingCartPanel.add(new JLabel(" ")); //filler
        buttonsShoppingCartPanel.add(new JLabel(" ")); //filler
        buttonsShoppingCartPanel.add(new JLabel(" ")); //filler
        buttonsShoppingCartPanel.add(finaliseOrderShoppingCartButton);


        shoppingCartPanel.add(buttonsShoppingCartPanel);


        JPanel infoShoppingCartPanel = new JPanel(new GridLayout(0,1));
        JLabel maximumBudgetLabel = new JLabel("           Total budget:");
        JLabel valueBudgetLabel = new JLabel("           " + referenceCustomer.getShoppingCart().getBudget().toString());
        JLabel totalShopppingCartLabel = new JLabel("           Total products:");


        Double totalValue = findShoppingCartTotal();

        valueShoppingCartLabel.setText("           " + totalValue.toString());

        infoShoppingCartPanel.add(maximumBudgetLabel);

        infoShoppingCartPanel.add(valueBudgetLabel);
        infoShoppingCartPanel.add(new JLabel(" "));
        infoShoppingCartPanel.add(totalShopppingCartLabel);
        infoShoppingCartPanel.add(valueShoppingCartLabel);


        shoppingCartPanel.add(infoShoppingCartPanel);
        createWishListPanel();
        customerTabbedPane.removeAll();
        customerTabbedPane.add(shoppingCartPanel, "Shopping Cart");
        customerTabbedPane.add(wishListPanel, "Wishlist");
        customerTabbedPane.revalidate();

    }

    public void createWishListPanel(){
        if(referenceCustomer != null){


            Store store = Store.getInstance("dummy_text");
            wishListPanel = new JPanel(new FlowLayout());
            JPanel tableWishListPanel = new JPanel();
            ListIterator<Item> it = referenceCustomer.getWishlist().listIterator();
            Item currentItem;
            Integer current = 0;

            DefaultTableModel tableModel = new DefaultTableModel();
            for(String s : prodColNames)
                tableModel.addColumn(s);

            while(it.hasNext()){
                currentItem = it.next();
                Vector<Object> newRow = new Vector<>();


                newRow.add(currentItem.getName());
                newRow.add(currentItem.getID());
                newRow.add(currentItem.getPrice());

                for(Department d : store.getDepartments())
                    for(Item i : d.getItems())
                        if(i.equals(currentItem))
                            newRow.add(d.getID());
            }


            wishListTable = new JTable(tableModel);

            wishListScrollPane = new JScrollPane(wishListTable);
            wishListTable.setFillsViewportHeight(true);

            tableWishListPanel.add(wishListScrollPane);
            wishListPanel.add(tableWishListPanel);


            JPanel buttonsWishListPanel = new JPanel(new GridLayout(0,1));
            JButton addNewItemWishListButton = new JButton("Add item");
            JButton deleteItemWishListButton = new JButton("Delete item");

            addNewItemWishListButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Item refItem = createPopUpAddRemoveToShoppingCart();

                    if(refItem != null) {
                        referenceCustomer.getWishlist().add(refItem);
                        Object[] newEntry = new Object[4];
                        newEntry[0] = refItem.getName(); newEntry[1] = refItem.getID();
                        newEntry[2] = refItem.getPrice();

                        for(Department d : store.getDepartments())
                            for(Item i : d.getItems())
                                if(i.equals(refItem))
                                    newEntry[3] = d.getID();

                        ((DefaultTableModel)wishListTable.getModel()).addRow(newEntry);
                        wishListTable.revalidate();
                        wishListScrollPane.revalidate();

                    }
                }
            });

            deleteItemWishListButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Item refItem = createPopUpAddRemoveToShoppingCart();
                    boolean removedItem = false;
                    ListIterator<Item> it = referenceCustomer.getWishlist().listIterator();

                    if(refItem == null){
                        JOptionPane.showMessageDialog(mainFrame, "Product does not exist");
                        return;
                    }

                    while(it.hasNext()) {
                        Item currentItem = it.next();
                        if (currentItem.getID().equals(refItem.getID()))
                            for (int i = 0; i < wishListTable.getModel().getRowCount(); ++i)
                                if ((wishListTable.getModel().getValueAt(i, 1)).equals(refItem.getID())) {
                                    DefaultTableModel wishListTableModel = (DefaultTableModel) wishListTable.getModel();
                                    wishListTableModel.removeRow(i);
                                    referenceCustomer.getWishlist().remove(refItem);
                                    wishListTable.revalidate();
                                    removedItem = true;
                                    break;
                                }
                    }

                    if(!removedItem){
                        JOptionPane.showMessageDialog(mainFrame, "Product is not in wishlist");
                    }

                    wishListTable.revalidate();
                    wishListScrollPane.revalidate();
                }
            });

            buttonsWishListPanel.add(addNewItemWishListButton);
            buttonsWishListPanel.add(new JLabel(" ")); //filler
            buttonsWishListPanel.add(new JLabel(" ")); //filler
            buttonsWishListPanel.add(deleteItemWishListButton);



           wishListPanel.add(buttonsWishListPanel);

        }
    }

    public void createSelectCustomerPanel(){
        Store store = Store.getInstance("dummy_text");

        selectCustomerPanel = new JPanel();
        selectCustomerPanel.setLayout(new BoxLayout(selectCustomerPanel, BoxLayout.Y_AXIS));
        JLabel selectCustomerName = new JLabel("Customer name:");
        JTextField selectCustomerTextField = new JTextField(40);
        JButton selectCustomerButton = new JButton("OK");

        selectCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(Customer c : store.getCustomers())
                    if(c.getName().equals(selectCustomerTextField.getText())){
                        referenceCustomer = c;

                        createShoppingCartPanel();;
                        return;

                    }

                if(referenceCustomer == null)
                    JOptionPane.showMessageDialog( mainFrame, "Customer does not exist" );
            }
        });

        selectCustomerName.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectCustomerTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectCustomerButton.setAlignmentX(Component.CENTER_ALIGNMENT);



        selectCustomerPanel.add(selectCustomerName);
        selectCustomerName.setMaximumSize(new Dimension(100,20));
        selectCustomerPanel.add(selectCustomerTextField);
        selectCustomerTextField.setMaximumSize(new Dimension(200,20));
        selectCustomerPanel.add(new Box.Filler(new Dimension(10,10), new Dimension(10,10),
                new Dimension(10,10)));
        selectCustomerPanel.add(selectCustomerButton);
        selectCustomerButton.setMaximumSize(new Dimension(70,20));

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

    public Item createPopUpAddRemoveToShoppingCart(){
        Store store = Store.getInstance("dummy_text");

        JPanel mainPopUpPanel = new JPanel(new GridLayout(0,1));
        JLabel modifyIDProd = new JLabel("ID:");
        JTextField modifyIDProdText = new JTextField(50);
        Item referenceItem = null;

        mainPopUpPanel.add(modifyIDProd);
        mainPopUpPanel.add(modifyIDProdText);
        modifyIDProdText.setMaximumSize(new Dimension(50, 20));


        int result = JOptionPane.showConfirmDialog(null, mainPopUpPanel, "Select ID",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if(result == JOptionPane.OK_OPTION){
            if(modifyIDProdText.getText().length() != 0)
                for (Department d : store.getDepartments())
                    for (Item i : d.getItems())
                        if (i.getID().equals(Integer.parseInt(modifyIDProdText.getText())))
                            referenceItem = i;

            if(referenceItem == null){
                JOptionPane.showMessageDialog(mainPopUpPanel, "Product does not exist" );
                return null;
            }

            return referenceItem;
        }

        return null;
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

    public void createPopUpModifyProduct(){
        //TODO: MODIFY FOR CUSTOMERS

        Store store = Store.getInstance("dummy_text");

        JPanel mainPopUpPanel = new JPanel(new GridLayout(0,1));
        JLabel modifyIDProd = new JLabel("ID:");
        JTextField modifyIDProdText = new JTextField(50);
        Item referenceItem = null;

        mainPopUpPanel.add(modifyIDProd);
        mainPopUpPanel.add(modifyIDProdText);
        modifyIDProdText.setMaximumSize(new Dimension(50, 20));


        int result = JOptionPane.showConfirmDialog(null, mainPopUpPanel, "Select ID to modify",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if(result == JOptionPane.OK_OPTION){
            if(modifyIDProdText.getText().length() != 0)
                for (Department d : store.getDepartments())
                    for (Item i : d.getItems())
                        if (i.getID().equals(Integer.parseInt(modifyIDProdText.getText())))
                            referenceItem = i;

            if(referenceItem == null){
                JOptionPane.showMessageDialog(mainPopUpPanel, "Product does not exist" );
                return;
            }
        }
        else if(result == JOptionPane.CANCEL_OPTION)
            return;

        JLabel newProdName, newProdID, newProdPrice, newProdDepartment;
        JTextField nameTextField, IDTextField, priceTextField;
        JComboBox<String> departmentCombo;
        String[] comboData = new String[4];
        int currentDepart = 0;

        for(Department d : store.getDepartments()){
            comboData[currentDepart] = d.getName();
            currentDepart++;
        }

        departmentCombo = new JComboBox<>(comboData);
        departmentCombo.setEditable(false);

        newProdName = new JLabel("  Name:  ");
        newProdID = new JLabel("  ID:  ");
        newProdPrice = new JLabel("  Price:  ");
        newProdDepartment = new JLabel("  Department:  ");

        nameTextField = new JTextField(referenceItem.getName(), 20);
        IDTextField = new JTextField(referenceItem.getID().toString(), 20);
        priceTextField = new JTextField(referenceItem.getPrice().toString(), 20);

        mainPopUpPanel = new JPanel(new GridLayout(0,1));

        mainPopUpPanel.add(newProdName);
        mainPopUpPanel.add(nameTextField);
        mainPopUpPanel.add(newProdID);
        mainPopUpPanel.add(IDTextField);
        mainPopUpPanel.add(newProdPrice);
        mainPopUpPanel.add(priceTextField);
        mainPopUpPanel.add(newProdDepartment);
        mainPopUpPanel.add(departmentCombo);


         result = JOptionPane.showConfirmDialog(null, mainPopUpPanel, "Modify Product",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if(result == JOptionPane.OK_OPTION){
            referenceItem.setName(nameTextField.getText());
            referenceItem.setID(Integer.parseInt(IDTextField.getText()));
            referenceItem.setPrice(Double.parseDouble(priceTextField.getText()));

            storePanel.removeAll();
            createStorePanel();
        }
    }

    public void createPopUpDeleteProduct(){
        Store store = Store.getInstance("dummy_text");

        JPanel mainPopUpPanel = new JPanel(new GridLayout(0,1));
        JLabel deleteIDProd = new JLabel("ID:");
        JTextField deleteIDProdText = new JTextField(50);
        Item referenceItem = null;

        mainPopUpPanel.add(deleteIDProd);
        mainPopUpPanel.add(deleteIDProdText);
        deleteIDProdText.setMaximumSize(new Dimension(50, 20));


        int result = JOptionPane.showConfirmDialog(null, mainPopUpPanel, "Select ID to delete",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if(result == JOptionPane.OK_OPTION){
            if(deleteIDProdText.getText().length() != 0)
                for (Department d : store.getDepartments())
                    for (Item i : d.getItems())
                        if (i.getID().equals(Integer.parseInt(deleteIDProdText.getText())))
                            referenceItem = i;

            if(referenceItem == null){
                JOptionPane.showMessageDialog(mainPopUpPanel, "Product does not exist" );
                return;
            }

            int result2 = JOptionPane.showConfirmDialog(null, new JPanel().add(new JLabel("Are you sure?")),
                    "Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result2 == JOptionPane.OK_OPTION) {
                for (Customer c : store.getCustomers()) {
                    if (c.getShoppingCart().contains(referenceItem)) {
                        c.getShoppingCart().remove(referenceItem);

                        if(referenceCustomer == c){
                            customerPanel.removeAll();
                            createCustomerPanel();
                        }
                    }

                    if (c.getWishlist().contains(referenceItem))
                        c.getWishlist().remove(referenceItem);

                }

                for (Department d : store.getDepartments()) {
                    if (d.getItems().contains(referenceItem))
                        d.getItems().remove(referenceItem);
                }

                storePanel.removeAll();
                createStorePanel();
            }

        }
        else if(result == JOptionPane.CANCEL_OPTION)
            return;

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

        if(e.getSource() == modifyProductButton){
            createPopUpModifyProduct();
        }

        if(e.getSource() == deleteProductButton){
            createPopUpDeleteProduct();
        }
    }

    public static void main(String args[]){
        new MainGUI();
    }

}
