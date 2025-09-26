package com.example.stage2_svitozar.view;

import com.example.stage2_svitozar.controller.StoreController;
import com.example.stage2_svitozar.model.StoreModel;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class StoreView { // The graphical interface of the store
    private StoreController controller; // the view needs to have a reference to the controller and vice versa
    private StoreModel model;
    public Stage window;
    BorderPane mainPane;

    Scene scene;
    public Alert alert;

    // Main TabPane
    TabPane tabPane;

    // Customer tab variables
    public TextField textFieldName;
    public TextField textFieldEmail;
    public TextField textFieldPhoneNumber;
    public TextArea textArea;

    // Item tab variables
    public TextField textFieldItemType;
    public TextField textFieldBrand_Model;
    public TextField textFieldPrice;
    public TextArea textAreaItem;

    // Purchase tab variables
    public ComboBox<String> customerComboBox; // Generic type
    public ComboBox<String> itemComboBox; // Adding strings to our ComboBox
    public ComboBox<String> sortOrdersByComboBox;
    public TextArea textAreaOrder;

    // StressTest tab variables
    public TextArea textAreaStressTest;


    // Constructor
    public StoreView(StoreController controller_, StoreModel model_, Stage primaryStage) {
        this.controller = controller_; // so they are aware of each other
        this.model = model_;
        this.window = primaryStage;
        this.tabPane = new TabPane();
    }

    // Customer window // A tab for customers, this tab will look like the GUI you built in stage 1
    public Tab getCustomerTab () {
        Tab t = new Tab();
        t.setText("Customer");
        // Customer window like the GUI you built in Stage 1
        VBox vBoxCustomer = new VBox(10); // equal vertical spacing
        vBoxCustomer.setPadding(new Insets(20));

        HBox hbox1 = new HBox(10); // spacing between nodes
        hbox1.setPadding(new Insets(5)); // top, right, bottom, left
        HBox hbox2 = new HBox(10);
        hbox2.setPadding(new Insets(5));
        HBox hbox3 = new HBox(10);
        hbox3.setPadding(new Insets(5));
        HBox hbox4 = new HBox(10);
        hbox4.setPadding(new Insets(5));
        HBox hbox5 = new HBox(10);
        hbox5.setPadding(new Insets(5));
        HBox hbox6 = new HBox(10);
        hbox6.setPadding(new Insets(5));

        // Input fields
        Label labelName = new Label("Enter Name");
        textFieldName = new TextField();
        textFieldName.setPrefWidth(200);
        Label labelEmail = new Label("Enter Email");
        textFieldEmail = new TextField();
        textFieldEmail.setPrefWidth(200);
        Label labelPhoneNumber = new Label("Enter Phone Number");
        textFieldPhoneNumber = new TextField();
        textFieldPhoneNumber.setPrefWidth(200);

        Button add_ = new Button("Add");
        Button remove_ = new Button("Remove");
        Button list_ = new Button("List");

        textArea = new TextArea();
        textArea.setPrefSize(550, 200);
        textArea.setFont(Font.font("Monospaced", FontWeight.BOLD, 14));

        Button load_ = new Button("Load");
        Button save_ = new Button("Save");
        Button exit_ = new Button("Exit");

        // All actions related to the button is delegated to the controller.
        add_.setOnAction(e -> controller.addButton());
        remove_.setOnAction(e -> controller.removeButton());
        list_.setOnAction(e -> controller.listButton());

        load_.setOnAction(e -> controller.loadFile());
        save_.setOnAction(e -> controller.saveToFile());
        exit_.setOnAction(e -> controller.exitApplication());
        /////////////////////////////////////////////////////////////////////
        hbox1.getChildren().addAll(labelName, textFieldName);
        hbox2.getChildren().addAll(labelEmail, textFieldEmail);
        hbox6.getChildren().addAll(labelPhoneNumber, textFieldPhoneNumber);
        hbox3.getChildren().addAll(add_, remove_, list_);
        hbox4.getChildren().addAll(textArea);
        hbox5.getChildren().addAll(load_, save_, exit_);
        hbox5.setAlignment(Pos.CENTER);


        vBoxCustomer.getChildren().addAll(hbox1, hbox2, hbox6, hbox3, hbox4, hbox5);

        vBoxCustomer.setPrefSize(600, 500);
        vBoxCustomer.setMinSize(600, 500);
        vBoxCustomer.setMaxSize(600, 500);
        vBoxCustomer.setStyle("-fx-background-color: lightblue; -fx-background-radius: 8px;");

        BorderPane borderPaneCustomer = new BorderPane();
        borderPaneCustomer.setPadding(new Insets(10));

        BorderPane.setAlignment(vBoxCustomer, Pos.CENTER);
        borderPaneCustomer.setCenter(vBoxCustomer);
        t.setContent(borderPaneCustomer);

        return t;
    }

    // Item tab // This tab will operate similar to the GUI you built in stage 1
    public Tab getItemTab() {
        Tab itemTab = new Tab();
        itemTab.setText("Items");

        // Item window like the GUI you built in Stage 1
        VBox vBoxItem = new VBox(10); // equal vertical spacing
        vBoxItem.setPadding(new Insets(20));

        HBox hbox1 = new HBox(10); // spacing between nodes
        hbox1.setPadding(new Insets(5)); // top, right, bottom, left
        HBox hbox2 = new HBox(10);
        hbox2.setPadding(new Insets(5));
        HBox hbox3 = new HBox(10);
        hbox3.setPadding(new Insets(5));
        HBox hbox4 = new HBox(10);
        hbox4.setPadding(new Insets(5));
        HBox hbox5 = new HBox(10);
        hbox5.setPadding(new Insets(5));
        HBox hbox6 = new HBox(10);
        hbox6.setPadding(new Insets(5));

        // Input fields - item type // brand/model  // price
        Label labelItemType = new Label("Enter item type ");
        textFieldItemType = new TextField();
        textFieldItemType.setPromptText("Smartphone, Laptop, Headphones");
        textFieldItemType.setMaxWidth(200);
        textFieldItemType.prefWidthProperty().bind(hbox1.widthProperty().multiply(0.3));

        Label labelItemBrand_Model = new Label("Enter Brand/Model");
        textFieldBrand_Model = new TextField();
        textFieldBrand_Model.setPromptText("Asus, iPhone13, Sony");
        textFieldBrand_Model.setMaxWidth(200);
        textFieldBrand_Model.prefWidthProperty().bind(hbox2.prefWidthProperty().multiply(0.3));

        Label labelPrice = new Label("Enter price (€)");
        textFieldPrice = new TextField();
        textFieldPrice.setPromptText("450");
        textFieldPrice.setMaxWidth(100);
        textFieldPrice.prefWidthProperty().bind(hbox3.widthProperty().multiply(0.3));

        Button add_2 = new Button("Add");
        Button remove_2 = new Button("Remove");
        Button list_2 = new Button("List");

        textAreaItem = new TextArea();
        textAreaItem.setPrefHeight(200);
        textAreaItem.prefWidthProperty().bind(tabPane.widthProperty().multiply(0.9)); // Make it responsive
        textAreaItem.setFont(Font.font("Monospaced", FontWeight.BOLD, 14));
        Button load_2 = new Button("Load");
        Button save_2 = new Button("Save");
        Button exit_2 = new Button("Exit");


        // All actions related to the button is delegated to the controller.
        add_2.setOnAction(e -> controller.addButton2());
        remove_2.setOnAction(e -> controller.removeButton2());
        list_2.setOnAction(e -> controller.listButton2());

        load_2.setOnAction(e -> controller.loadFile2());
        save_2.setOnAction(e -> controller.saveToFile2());
        exit_2.setOnAction(e -> controller.exitApplication2());
        /////////////////////////////////////////////////////////////////////
        hbox1.getChildren().addAll(labelItemType, textFieldItemType);
        hbox2.getChildren().addAll(labelItemBrand_Model, textFieldBrand_Model);
        hbox3.getChildren().addAll(labelPrice, textFieldPrice);
        hbox4.getChildren().addAll(add_2, remove_2, list_2);
        hbox5.getChildren().addAll(textAreaItem);
        hbox6.getChildren().addAll(load_2, save_2, exit_2);
        hbox6.setAlignment(Pos.CENTER);

        // Set elements to expand when resizing
        HBox.setHgrow(textFieldItemType, Priority.ALWAYS);
        HBox.setHgrow(textFieldBrand_Model, Priority.ALWAYS);
        HBox.setHgrow(textFieldPrice, Priority.ALWAYS);
        VBox.setVgrow(textArea, javafx.scene.layout.Priority.ALWAYS);


        vBoxItem.getChildren().addAll(hbox1, hbox2, hbox3, hbox4, hbox5, hbox6);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vBoxItem);

        vBoxItem.setPrefSize(700, 500);
        vBoxItem.setMinSize(700, 500);
        vBoxItem.setMaxSize(700, 500);
        vBoxItem.setStyle("-fx-background-color: lightblue; -fx-background-radius: 8px;");

        BorderPane borderPaneItem = new BorderPane();
        borderPaneItem.setPadding(new Insets(10));
        BorderPane.setAlignment(vBoxItem, Pos.CENTER);
        borderPaneItem.setCenter(vBoxItem);

        // Make borderPane responsive
//        borderPane.prefWidthProperty().bind(tabPane.widthProperty());
//        borderPane.prefHeightProperty().bind(tabPane.heightProperty());

        itemTab.setContent(borderPaneItem);

        return itemTab;

    }

    // Purchase tab
    public Tab getPurchaseTab () {
        Tab purchaseTab = new Tab();
        purchaseTab.setText("Make Purchase");

        Label placeOrderLabel = new Label("Place your order");
        Label selectCustomerLabel = new Label("Select customer details");
        Label selectItemLabel = new Label("Select product");
        Label sortByLabel = new Label("Sort Orders by:");

        customerComboBox = new ComboBox();
        itemComboBox = new ComboBox();

        Button placeOrderButton = new Button("Place order");

        sortOrdersByComboBox = new ComboBox<>();
        sortOrdersByComboBox.getItems().addAll("Date Purchased", "Item Name");
        sortOrdersByComboBox.setValue("Date Purchased"); // Set default selection to "Date Purchased"

        // Select a product and see how many are in stock; how many sold last month.
        itemComboBox.setOnAction(e -> controller.selectProductReport());

        //  Select a customer and see what did customer A purchase last month and their active orders
        customerComboBox.setOnAction(e -> controller.selectCustomerReport());

        Button sortButton = new Button("Apply Sort");
        sortButton.setOnAction(e -> controller.sortButton());

        Button exitButtonOrder = new Button("Exit"); // Exit application

        // All actions related to the button is delegated to the controller.
        placeOrderButton.setOnAction(e -> controller.placeOrderButtonAction());
        exitButtonOrder.setOnAction(e -> controller.exitApplication3());

        textAreaOrder = new TextArea();
        textAreaOrder.setPrefSize(550, 300);
        textAreaOrder.setFont(Font.font("Monospaced", FontWeight.BOLD, 14));

        // Layout using GridPane
        GridPane grid = new GridPane();
        grid.setPrefSize(650, 550); // Fixed width and height
        grid.setMinSize(650, 550);
        grid.setMaxSize(650, 550);

        // Define column constrains for equal width of columns
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50); // Applies to column index 0


        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50); // Applies to column index 1

        grid.getColumnConstraints().addAll(col1, col2);

        grid.setVgap(5);
        grid.setHgap(5);
        grid.setPadding(new Insets(10, 10, 10, 10)); // padding around the grid
        grid.setAlignment(Pos.CENTER); // Center elements within the grid
//         GridPane border
        grid.setStyle("-fx-background-color: lightblue; -fx-background-radius: 8px;");
//        grid.setGridLinesVisible(true);

        grid.add(placeOrderLabel, 0, 0, 2, 1);
        GridPane.setHalignment(placeOrderLabel, HPos.CENTER);
        grid.add(selectCustomerLabel, 0, 1);
        GridPane.setHalignment(selectCustomerLabel, HPos.CENTER);
        grid.add(selectItemLabel, 1, 1);
        GridPane.setHalignment(selectItemLabel, HPos.LEFT);
        grid.add(customerComboBox, 0, 2);
        GridPane.setHalignment(customerComboBox, HPos.RIGHT);
        grid.add(itemComboBox, 1, 2);
        GridPane.setHalignment(itemComboBox, HPos.LEFT);
        grid.add(placeOrderButton, 0, 3, 2, 1);
        GridPane.setHalignment(placeOrderButton, HPos.CENTER);

        HBox sortHBox = new HBox(5);
        sortHBox.setPadding(new Insets(5));

        sortHBox.getChildren().addAll(sortByLabel, sortOrdersByComboBox, sortButton);
        sortOrdersByComboBox.setStyle("-fx-background-color: lightgray; -fx-background-radius: 5px;-fx-border-color: gray; -fx-border-radius: 5px;-fx-text-fill: black; -fx-font-size: 15px;");
        sortOrdersByComboBox.setPrefWidth(200);

        sortButton.setStyle("-fx-background-color: #6A0DAD; -fx-font-size: 16px; -fx-text-fill: white;");
        sortButton.setPrefWidth(100);

        sortHBox.setAlignment(Pos.CENTER_LEFT);
        sortByLabel.setPadding(new Insets(0, 5, 0, 5));
        grid.add(sortHBox, 0, 4, 2, 1);

        grid.add(textAreaOrder, 0, 5, 2, 1);
        grid.add(exitButtonOrder, 1, 6);
        GridPane.setHalignment(exitButtonOrder, HPos.RIGHT);


        placeOrderLabel.setStyle("-fx-font-size: 2em; -fx-font-weight: bold; -fx-text-fill: black;");
        selectCustomerLabel.setStyle("-fx-font-size: 1.5em;");
        selectItemLabel.setStyle("-fx-font-size: 1.5em;");
        sortByLabel.setStyle("-fx-font-size: 1.5em;");

        customerComboBox.setStyle("-fx-background-color: #2196F3; -fx-background-radius: 5px;-fx-text-fill: white; -fx-border-color: blue; -fx-border-radius: 5px;-fx-font-size: 1.2em;");
        customerComboBox.setPrefSize(250, 20);
        itemComboBox.setStyle("-fx-background-color: #2196F3; -fx-background-radius: 5px;-fx-border-color: blue; -fx-border-radius: 5px; -fx-text-fill: white;-fx-font-size: 1.2em;");
        itemComboBox.setPrefSize(250, 20);
        sortHBox.setStyle("-fx-background-color: pink; -fx-background-radius: 8px;");

        placeOrderButton.setStyle("-fx-background-color: #ffaa00; -fx-font-size: 1.5em; -fx-text-fill: black; -fx-font-weight: bold;");
        placeOrderButton.setPrefSize(250, 40);
        textAreaOrder.setStyle("-fx-font-size: 1.2em;");
        exitButtonOrder.setStyle("-fx-background-color: #ff4444; -fx-font-size: 1.2em; -fx-text-fill: white;");
        exitButtonOrder.setPrefSize(55, 35);
        GridPane.setMargin(placeOrderButton, new Insets(5, 0, 15, 0));
        GridPane.setMargin(textAreaOrder, new Insets(5, 0, 0, 0));
        GridPane.setMargin(placeOrderLabel, new Insets(0, 0, 20, 0));

        BorderPane borderPanePurchase = new BorderPane(grid);
        borderPanePurchase.setPadding(new Insets(10));
        BorderPane.setAlignment(grid, Pos.CENTER);
        purchaseTab.setContent(borderPanePurchase);
        return purchaseTab;
    }

    // Stress test on memory Tab
    public Tab getStressTestTab() {
        Tab stressTestTab = new Tab("\uD83D\uDCBE Stress Test");

        // Background setup
        Image background_image_stress_tab = controller.getHeapStackTabBackgroundImage();
        BackgroundSize backgroundSize = new BackgroundSize(850, 750, false, false, false, false);
        BackgroundImage background_image = new BackgroundImage(
             background_image_stress_tab,
             BackgroundRepeat.NO_REPEAT,
             BackgroundRepeat.NO_REPEAT,
             BackgroundPosition.CENTER,
             backgroundSize
        );

        Label stressTestHeader = new Label("Heap & Stack Experimentation");
        stressTestHeader.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 22));
        stressTestHeader.setStyle("-fx-text-fill: black;");
        stressTestHeader.setPadding(new Insets(0, 0, 20, 0));
        stressTestHeader.setAlignment(Pos.CENTER);

        Label heapStressLabel = new Label("\uD83D\uDCBDPerform Stress Test on Heap Memory");
        Label stackStressLabel = new Label("\uD83D\uDCDAPerform Stress Test on Stack Memory");

        heapStressLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        heapStressLabel.setStyle("-fx-text-fill: #16C47F;");

        stackStressLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        stackStressLabel.setStyle("-fx-text-fill: #FB4141;");

        Button heapStressTestButton = new Button("Start Heap Test \uD83D\uDE80");
        heapStressTestButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 1.2em;");
        heapStressTestButton.setOnAction(e -> controller.heapExperiment());

        Button stackStressTestButton = new Button("Start Stack Test \uD83D\uDD01");
        stackStressTestButton.setStyle("-fx-background-color: F93827; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 1.2em;");
        stackStressTestButton.setOnAction(e -> controller.stackExperiment());

        textAreaStressTest = new TextArea();
        textAreaStressTest.setPrefSize(550, 300);
        textAreaStressTest.setFont(Font.font("Consolas", FontWeight.BOLD, 14)); // Monospaced
        textAreaStressTest.setStyle("-fx-control-inner-background: #ffffffaa; -fx-border-color: AFDDFF; -fx-border-radius: 5px;");
        textAreaStressTest.setWrapText(true);

        VBox stressTestVBox = new VBox(10);
        stressTestVBox.setPadding(new Insets(20));
        stressTestVBox.setPrefSize(700, 500);
        stressTestVBox.setMinSize(700, 500);
        stressTestVBox.setMaxSize(700, 500);
        stressTestVBox.setStyle("-fx-background-color: FDFAF6; -fx-background-radius: 8px; -fx-border-color: gray; -fx-border-radius: 5px; -fx-opacity: 0.9");
        stressTestVBox.getChildren().addAll(stressTestHeader, heapStressLabel, heapStressTestButton, stackStressLabel, stackStressTestButton, textAreaStressTest);
        stressTestVBox.setAlignment(Pos.TOP_CENTER); // center children horizontally

        BorderPane borderPaneStressTest = new BorderPane();
        borderPaneStressTest.setPadding(new Insets(10));
        borderPaneStressTest.setCenter(stressTestVBox);
        BorderPane.setAlignment(stressTestVBox, Pos.CENTER);
        borderPaneStressTest.setBackground(new Background(background_image));

        stressTestTab.setContent(borderPaneStressTest);

        return stressTestTab;
    }


    public void mainView () {
        mainPane = new BorderPane();
        mainPane.setStyle("-fx-background-color: white;");
        scene = new Scene(mainPane, 850, 750); // width, height

        // Menu bar for Save, Load
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: F1F1F1; -fx-padding: 5px;");

        Menu fileMenu = new Menu("File");
        fileMenu.setStyle("-fx-background-color: #E5E5E5; -fx-text-fill: black; -fx-font-size: 13px; -fx-padding: 4 10 4 10; -fx-border-color: #D3D3D3; -fx-border-width: 0 0 2 0; -fx-background-radius: 3px;-fx-border-radius: 3px;-fx-border-style: solid;");

        Menu databaseMenu = new Menu("Database");
        databaseMenu.setStyle("-fx-background-color: #E5E5E5; -fx-text-fill: black; -fx-font-size: 13px; -fx-padding: 4 10 4 10; -fx-border-color: #D3D3D3; -fx-border-width: 0 0 2 0; -fx-background-radius: 3px;-fx-border-radius: 3px;-fx-border-style: solid;");

        MenuItem saveStoreData = new MenuItem("Save Store Data");
        MenuItem loadStoreData = new MenuItem("Load Store Data");
        saveStoreData.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");
        loadStoreData.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");

        // Menu items for Database operations
        MenuItem loadCustomerData = new MenuItem("Load Customer Data");
        loadCustomerData.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");
        MenuItem saveCustomerData = new MenuItem("Save Customer Data");
        saveCustomerData.setStyle("-fx-font-size: 13px; -fx-text-fill: black;");


        saveStoreData.setOnAction(e -> controller.saveStoreData());
        loadStoreData.setOnAction(e -> controller.loadStoreData());

        loadCustomerData.setOnAction(e -> controller.loadCustomerDataFromMySQL());
        saveCustomerData.setOnAction(e ->controller.saveCustomerDataToMySQL());

        fileMenu.getItems().addAll(saveStoreData, loadStoreData);
        databaseMenu.getItems().addAll(loadCustomerData, saveCustomerData);
        menuBar.getMenus().addAll(fileMenu, databaseMenu);

        mainPane.setTop(menuBar);

        tabPane.getTabs().add(getCustomerTab());
        tabPane.getTabs().add(getItemTab());
        tabPane.getTabs().add(getPurchaseTab());
        tabPane.getTabs().add(getStressTestTab());

        tabPane.prefWidthProperty().bind(mainPane.widthProperty()); // Makes TabPane fill the window width
        tabPane.prefHeightProperty().bind(mainPane.heightProperty()); // Makes TabPane fill the window height

        mainPane.setCenter(tabPane);
        mainPane.prefWidthProperty().bind(scene.widthProperty()); // Ensures mainPane scales with the window
        mainPane.prefHeightProperty().bind(scene.heightProperty()); // Ensures mainPane scales with the window

        window.setScene(scene);
        window.getIcons().add(controller.getStoreIcon()); // Pass the getting store icon operation to the controller
        window.setTitle("Welcome to " + model.getStoreName());
        window.show();
    }
}

