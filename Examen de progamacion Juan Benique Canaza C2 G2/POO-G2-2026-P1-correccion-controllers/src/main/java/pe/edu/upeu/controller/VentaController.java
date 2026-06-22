package pe.edu.upeu.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pe.edu.upeu.dto.SessionManager;
import pe.edu.upeu.model.Cliente;
import pe.edu.upeu.model.Usuario;
import pe.edu.upeu.model.Venta;
import pe.edu.upeu.service.IClienteService;
import pe.edu.upeu.service.IUsuarioService;
import pe.edu.upeu.service.IVentaService;

import java.time.LocalDate;
import java.util.Optional;

public class VentaController {

    @FXML private TableView<Venta> tblVenta;
    @FXML private TableColumn<Venta, String> colIdVenta;
    @FXML private TableColumn<Venta, String> colCliente;
    @FXML private TableColumn<Venta, LocalDate> colFecha;
    @FXML private TableColumn<Venta, Double> colNetoTotal;
    @FXML private TableColumn<Venta, Double> colIgv;
    @FXML private TableColumn<Venta, Double> colPrecioTotal;

    @FXML private TextField txtIdVenta;
    @FXML private DatePicker dpFechaVenta;
    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private TextField txtNetoTotal;
    @FXML private TextField txtIgv;
    @FXML private TextField txtPrecioTotal;

    private final IVentaService ventaService;
    private final IClienteService clienteService;
    private final IUsuarioService usuarioService;
    private ObservableList<Venta> lista = FXCollections.observableArrayList();

    public VentaController(IVentaService ventaService,
                           IClienteService clienteService,
                           IUsuarioService usuarioService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
    }

    @FXML
    public void initialize() {
        colIdVenta.setCellValueFactory(new PropertyValueFactory<>("idVenta"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaVenta"));
        colNetoTotal.setCellValueFactory(new PropertyValueFactory<>("netoTotal"));
        colIgv.setCellValueFactory(new PropertyValueFactory<>("igv"));
        colPrecioTotal.setCellValueFactory(new PropertyValueFactory<>("precioTotal"));

        // Mostrar nombre del cliente en la tabla
        colCliente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDniCliente().getNombre()));

        // Configurar ComboBox de Cliente
        cmbCliente.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Cliente item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDni() + " - " + item.getNombre());
            }
        });
        cmbCliente.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Cliente item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getDni() + " - " + item.getNombre());
            }
        });

        // Calcular IGV y Total automáticamente
        txtNetoTotal.textProperty().addListener((obs, old, val) -> calcularTotales(val));

        cargarDatos();

        // Rellenar formulario al seleccionar tabla
        tblVenta.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                txtIdVenta.setText(sel.getIdVenta());
                dpFechaVenta.setValue(sel.getFechaVenta());
                txtNetoTotal.setText(String.valueOf(sel.getNetoTotal()));
                txtIgv.setText(String.valueOf(sel.getIgv()));
                txtPrecioTotal.setText(String.valueOf(sel.getPrecioTotal()));

                cmbCliente.getItems().stream()
                        .filter(c -> c.getDni().equals(sel.getDniCliente().getDni()))
                        .findFirst()
                        .ifPresent(cmbCliente::setValue);
            }
        });
    }

    private void calcularTotales(String netoStr) {
        try {
            double neto = Double.parseDouble(netoStr);
            double igv = neto * 0.18;
            double total = neto + igv;
            txtIgv.setText(String.format(java.util.Locale.US, "%.2f", igv));
            txtPrecioTotal.setText(String.format(java.util.Locale.US, "%.2f", total));
        } catch (NumberFormatException ignored) {
            txtIgv.clear();
            txtPrecioTotal.clear();
        }
    }

    private void cargarDatos() {
        cmbCliente.setItems(FXCollections.observableArrayList(clienteService.findAll()));
        lista.setAll(ventaService.findAll());
        tblVenta.setItems(lista);
        tblVenta.refresh();
    }

    @FXML
    private void guardar() {
        if (txtIdVenta.getText().isEmpty() || cmbCliente.getValue() == null || dpFechaVenta.getValue() == null) {
            mostrarAlerta("Error", "ID, Cliente y Fecha son obligatorios.");
            return;
        }
        try {
            // CORRECCIÓN APLICADA: Se usa loginUsuario en lugar de findByUsuarioAndClave
            Usuario usuarioResponsable;
            Optional<Usuario> optUsu = usuarioService.loginUsuario("juan123", "juan123456");

            if(optUsu.isPresent()){
                usuarioResponsable = optUsu.get();
            } else {
                usuarioResponsable = new Usuario();
                usuarioResponsable.setIdUsuario("1");
            }

            Venta v = Venta.builder()
                    .idVenta(txtIdVenta.getText())
                    .dniCliente(cmbCliente.getValue())
                    .fechaVenta(dpFechaVenta.getValue())
                    .netoTotal(Double.parseDouble(txtNetoTotal.getText()))
                    .igv(Double.parseDouble(txtIgv.getText()))
                    .precioTotal(Double.parseDouble(txtPrecioTotal.getText()))
                    .idUsuario(usuarioResponsable)
                    .build();

            if (ventaService.existsById(v.getIdVenta())) {
                ventaService.update(v.getIdVenta(), v);
            } else {
                ventaService.save(v);
            }
            limpiar();
            cargarDatos();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Los montos deben ser numéricos.");
        }
    }

    @FXML
    private void eliminar() {
        Venta sel = tblVenta.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta("Error", "Seleccione una venta.");
            return;
        }
        ventaService.delete(sel.getIdVenta());
        limpiar();
        cargarDatos();
    }

    @FXML
    private void limpiar() {
        txtIdVenta.clear();
        dpFechaVenta.setValue(null);
        cmbCliente.setValue(null);
        txtNetoTotal.clear();
        txtIgv.clear();
        txtPrecioTotal.clear();
        tblVenta.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
