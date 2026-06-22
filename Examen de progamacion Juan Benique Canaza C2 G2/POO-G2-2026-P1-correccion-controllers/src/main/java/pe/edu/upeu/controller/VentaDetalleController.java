package pe.edu.upeu.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pe.edu.upeu.model.Producto;
import pe.edu.upeu.model.Venta;
import pe.edu.upeu.model.VentaDetalle;
import pe.edu.upeu.service.IProductoService;
import pe.edu.upeu.service.IVentaDetalleService;
import pe.edu.upeu.service.IVentaService;

public class VentaDetalleController {

    @FXML private TableView<VentaDetalle> tblDetalle;
    @FXML private TableColumn<VentaDetalle, String> colIdDetalle;
    @FXML private TableColumn<VentaDetalle, String> colVenta;
    @FXML private TableColumn<VentaDetalle, String> colProducto;
    @FXML private TableColumn<VentaDetalle, Double> colPrecioUni;
    @FXML private TableColumn<VentaDetalle, Double> colPorceUtil;
    @FXML private TableColumn<VentaDetalle, Integer> colCantidad;
    @FXML private TableColumn<VentaDetalle, Double> colNetoTotal;

    @FXML private TextField txtIdDetalle;
    @FXML private ComboBox<Venta> cmbVenta;
    @FXML private ComboBox<Producto> cmbProducto;
    @FXML private TextField txtPrecioUni;
    @FXML private TextField txtPorceUtil;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtNetoTotal;

    private final IVentaDetalleService ventaDetalleService;
    private final IVentaService ventaService;
    private final IProductoService productoService;
    private ObservableList<VentaDetalle> lista = FXCollections.observableArrayList();

    public VentaDetalleController(IVentaDetalleService ventaDetalleService,
                                  IVentaService ventaService,
                                  IProductoService productoService) {
        this.ventaDetalleService = ventaDetalleService;
        this.ventaService = ventaService;
        this.productoService = productoService;
    }

    @FXML
    public void initialize() {
        colIdDetalle.setCellValueFactory(new PropertyValueFactory<>("idVentaDetalle"));
        colPrecioUni.setCellValueFactory(new PropertyValueFactory<>("precioUni"));
        colPorceUtil.setCellValueFactory(new PropertyValueFactory<>("porceUtil"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colNetoTotal.setCellValueFactory(new PropertyValueFactory<>("netoTotal"));

        colVenta.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdVenta().getIdVenta()));
        colProducto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdProducto().getNombre()));

        cmbVenta.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Venta item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getIdVenta());
            }
        });
        cmbVenta.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Venta item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getIdVenta());
            }
        });

        cmbProducto.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Producto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getIdProducto() + " - " + item.getNombre());
            }
        });
        cmbProducto.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Producto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getIdProducto() + " - " + item.getNombre());
            }
        });

        cmbProducto.valueProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                txtPrecioUni.setText(String.valueOf(sel.getPreUnitario()));
                txtPorceUtil.setText(String.valueOf(sel.getPorceUtil()));
                calcularNeto();
            }
        });

        txtCantidad.textProperty().addListener((obs, old, val) -> calcularNeto());

        cargarDatos();

        tblDetalle.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                txtIdDetalle.setText(sel.getIdVentaDetalle());
                txtPrecioUni.setText(String.valueOf(sel.getPrecioUni()));
                txtPorceUtil.setText(String.valueOf(sel.getPorceUtil()));
                txtCantidad.setText(String.valueOf(sel.getCantidad()));
                txtNetoTotal.setText(String.valueOf(sel.getNetoTotal()));

                cmbVenta.getItems().stream()
                        .filter(v -> v.getIdVenta().equals(sel.getIdVenta().getIdVenta()))
                        .findFirst()
                        .ifPresent(cmbVenta::setValue);

                cmbProducto.getItems().stream()
                        .filter(p -> p.getIdProducto().equals(sel.getIdProducto().getIdProducto()))
                        .findFirst()
                        .ifPresent(cmbProducto::setValue);
            }
        });
    }

    private void calcularNeto() {
        try {
            double precioUni = Double.parseDouble(txtPrecioUni.getText());
            int cantidad = Integer.parseInt(txtCantidad.getText());
            double neto = precioUni * cantidad;
            txtNetoTotal.setText(String.format("%.2f", neto));
        } catch (NumberFormatException ignored) {}
    }

    private void cargarDatos() {
        cmbVenta.setItems(FXCollections.observableArrayList(ventaService.findAll()));
        cmbProducto.setItems(FXCollections.observableArrayList(productoService.findAll()));

        // Recargamos la tabla
        lista.setAll(ventaDetalleService.findAll());
        tblDetalle.setItems(lista);
        tblDetalle.refresh();
    }

    @FXML
    private void guardar() {
        if (txtIdDetalle.getText().isEmpty() || cmbVenta.getValue() == null || cmbProducto.getValue() == null) {
            mostrarAlerta("Error", "ID, Venta y Producto son obligatorios.");
            return;
        }
        try {
            VentaDetalle vd = VentaDetalle.builder()
                    .idVentaDetalle(txtIdDetalle.getText())
                    .idVenta(cmbVenta.getValue())
                    .idProducto(cmbProducto.getValue())
                    .precioUni(Double.parseDouble(txtPrecioUni.getText()))
                    .porceUtil(Double.parseDouble(txtPorceUtil.getText()))
                    .cantidad(Integer.parseInt(txtCantidad.getText()))
                    .netoTotal(Double.parseDouble(txtNetoTotal.getText()))
                    .build();

            if (ventaDetalleService.existsById(vd.getIdVentaDetalle())) {
                ventaDetalleService.update(vd.getIdVentaDetalle(), vd);
            } else {
                ventaDetalleService.save(vd);
            }

            limpiar();
            cargarDatos();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar: " + e.getMessage());
        }
    }

    @FXML
    private void eliminar() {
        // Obtener el objeto seleccionado según el controlador (Cambia según la clase: Categoria, Cliente, Venta, etc.)
        var sel = tblDetalle.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta("Error", "Seleccione un registro de la tabla.");
            return;
        }
        ventaDetalleService.delete(sel.getIdVentaDetalle());
        limpiar();
        cargarDatos();
    }

    @FXML
    private void limpiar() {
        txtIdDetalle.clear();
        cmbVenta.setValue(null);
        cmbProducto.setValue(null);
        txtPrecioUni.clear();
        txtPorceUtil.clear();
        txtCantidad.clear();
        txtNetoTotal.clear();
        tblDetalle.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
