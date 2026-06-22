package pe.edu.upeu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pe.edu.upeu.model.Cliente;
import pe.edu.upeu.service.IClienteService;

public class ClienteController {

    @FXML private TableView<Cliente> tblCliente;
    @FXML private TableColumn<Cliente, String> colDni;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colDireccion;
    @FXML private TableColumn<Cliente, String> colCelular;

    @FXML private TextField txtDni;
    @FXML private TextField txtNombre;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtCelular;

    private final IClienteService clienteService;
    private ObservableList<Cliente> lista = FXCollections.observableArrayList();

    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @FXML
    public void initialize() {
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));

        cargarDatos();

        tblCliente.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                txtDni.setText(sel.getDni());
                txtNombre.setText(sel.getNombre());
                txtDireccion.setText(sel.getDireccion());
                txtCelular.setText(sel.getCelular());
            }
        });
    }

    private void cargarDatos() {
        lista.setAll(clienteService.findAll());
        tblCliente.setItems(lista);
        tblCliente.refresh();
    }

    @FXML
    private void guardar() {
        if (txtDni.getText().isEmpty() || txtNombre.getText().isEmpty()) {
            mostrarAlerta("Error", "DNI y Nombre son obligatorios.");
            return;
        }
        Cliente c = Cliente.builder()
                .dni(txtDni.getText())
                .nombre(txtNombre.getText())
                .direccion(txtDireccion.getText())
                .celular(txtCelular.getText())
                .build();

        if (clienteService.existsById(c.getDni())) {
            clienteService.update(c.getDni(), c);
        } else {
            clienteService.save(c);
        }
        limpiar();
        cargarDatos();
    }

    @FXML
    private void eliminar() {
        Cliente sel = tblCliente.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta("Error", "Seleccione un cliente.");
            return;
        }
        clienteService.delete(sel.getDni());
        limpiar();
        cargarDatos();
    }

    @FXML
    private void limpiar() {
        txtDni.clear();
        txtNombre.clear();
        txtDireccion.clear();
        txtCelular.clear();
        tblCliente.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
