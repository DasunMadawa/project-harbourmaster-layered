package lk.ijse.projectharbourmaster.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import lk.ijse.projectharbourmaster.bo.BOFactory;
import lk.ijse.projectharbourmaster.bo.custom.StockBO;
import lk.ijse.projectharbourmaster.dto.CustomDTO;
import lk.ijse.projectharbourmaster.dto.FishDTO;
import lk.ijse.projectharbourmaster.dto.tm.StockRecordsTM;
import lk.ijse.projectharbourmaster.dto.tm.StockStockTM;
//import lk.ijse.projectharbourmaster.model.FIshModel;
//import lk.ijse.projectharbourmaster.model.StockFishModel;
//import lk.ijse.projectharbourmaster.model.StockModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DashboardStockMenuFormController {

    @FXML
    public JFXSpinner s1StockSpinner;

    @FXML
    public TableColumn stockAmountCol;

    @FXML
    public TableColumn stockActionCol;

    @FXML
    private Pane stockMenuPane;

    @FXML
    private TableView<StockStockTM> stockTbl;

    @FXML
    private TableColumn<?, ?> stockStockIdCol;

    @FXML
    private TableColumn<?, ?> stockFishIdCol;

    @FXML
    private TableColumn<?, ?> stockFishNameCol;

    @FXML
    private TableColumn<?, ?> stockStockCol;

    @FXML
    private JFXButton stockAddFishToStockBtn;

    @FXML
    private ComboBox<?> stockFilterComboBox;

    @FXML
    private TableView<StockRecordsTM> stockTbl1;

    @FXML
    private TableColumn<?, ?> stockStockIdCol1;

    @FXML
    private TableColumn<?, ?> stockFishIdCol1;

    @FXML
    private TableColumn<?, ?> stockFishNameCol1;

    @FXML
    private TableColumn<?, ?> stockUniPriceCol;

    @FXML
    private TableColumn<?, ?> stockDateCol;

    @FXML
    private TableColumn<?, ?> stockStockCol1;


    StockBO stockBO = (StockBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.STOCK);

    @FXML
    void initialize() {
        s1StockSpinner.setProgress(calculateStockProgress() / 100);

        setCellValueFactory();
        setStockTableValues();
        setStockRecords();

    }

    private void setStockRecords() {
        try {

            ObservableList<StockRecordsTM> obListStock = FXCollections.observableArrayList();

            for (CustomDTO customDTO : stockBO.getAllStockRecords()) {
                obListStock.add(
                        new StockRecordsTM(
                                customDTO.getStockId(),
                                customDTO.getFishId(),
                                customDTO.getWeight(),
                                customDTO.getDate(),
                                customDTO.getUnitPriceBought(),
                                customDTO.getAddOrRemove(),
                                customDTO.getFishName()
                        )
                );
            }

            stockTbl1.setItems(obListStock);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void setStockTableValues() {
        try {
            List<FishDTO> fishDTOList = stockBO.getAllOrderBy("ORDER BY fishId");

            ObservableList<StockStockTM> obListStock = FXCollections.observableArrayList();

            for (FishDTO fishDTO : fishDTOList) {
                obListStock.add(new StockStockTM(
                                "S1",
                                fishDTO.getFishId(),
                                fishDTO.getFishName(),
                                fishDTO.getStock()
                        )
                );
            }

            stockTbl.setItems(obListStock);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    void setCellValueFactory() {
        stockStockIdCol.setCellValueFactory(new PropertyValueFactory<>("stockId"));
        stockFishIdCol.setCellValueFactory(new PropertyValueFactory<>("fishId"));
        stockFishNameCol.setCellValueFactory(new PropertyValueFactory<>("fishName"));
        stockStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        stockStockIdCol1.setCellValueFactory(new PropertyValueFactory<>("stockId"));
        stockFishIdCol1.setCellValueFactory(new PropertyValueFactory<>("fishId"));
        stockFishNameCol1.setCellValueFactory(new PropertyValueFactory<>("fishName"));
        stockUniPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPriceBought"));
        stockDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        stockAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        stockActionCol.setCellValueFactory(new PropertyValueFactory<>("action"));

    }

    private double calculateStockProgress() {
        try {
            double availableSpace = stockBO.getAvailableSpace("S1");

            return (((10 - availableSpace) / 10) * 100);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @FXML
    void stockAddFishToStockBtnOnAction(ActionEvent event) {
        DashboardFormController.fullScreen.getChildren().clear();
        try {
            DashboardFormController.fullScreen.getChildren().add(FXMLLoader.load(getClass().getResource("/view/stock_add_remove_fish_form.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
