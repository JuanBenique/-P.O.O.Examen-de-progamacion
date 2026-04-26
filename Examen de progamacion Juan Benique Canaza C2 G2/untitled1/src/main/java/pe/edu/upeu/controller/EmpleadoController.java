package pe.edu.upeu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pe.edu.upeu.model.Empleado;
import pe.edu.upeu.service.EmpleadoServiceImp;

import java.time.LocalDate;

public class EmpleadoController {

    @FXML
    private TextField txtNombre, txtNumero, txtArea, txtCargo, txtBuscar;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private TableView<Empleado> tabla;

    @FXML
    private TableColumn<Empleado, String> colNombre, colNumero, colArea, colCargo, colFecha;

    private EmpleadoServiceImp service = new EmpleadoServiceImp();
    private ObservableList<Empleado> listaObs = FXCollections.observableArrayList();

    private Empleado seleccionado;

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colNumero.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNumero()));
        colArea.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getArea()));
        colCargo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCargo()));
        colFecha.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFecha()));

        tabla.setItems(listaObs);

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                seleccionado = newSel;

                txtNombre.setText(newSel.getNombre());
                txtNumero.setText(newSel.getNumero());
                txtArea.setText(newSel.getArea());
                txtCargo.setText(newSel.getCargo());

                // 🔥 convertir String a fecha
                dpFecha.setValue(LocalDate.parse(newSel.getFecha()));
            }
        });
    }

    @FXML
    private void guardarEmpleado() {

        String nombre = txtNombre.getText();
        String numero = txtNumero.getText();
        String area = txtArea.getText();
        String cargo = txtCargo.getText();

        if (dpFecha.getValue() == null) {
            mostrarAlerta("Error", "Seleccione una fecha");
            return;
        }

        String fecha = dpFecha.getValue().toString();

        if (nombre.isEmpty() || numero.isEmpty()) {
            mostrarAlerta("Error", "Campos obligatorios vacíos");
            return;
        }

        if (service.existeNumero(numero)) {
            mostrarAlerta("Error", "Número de empleado ya existe");
            return;
        }

        Empleado emp = new Empleado(nombre, numero, area, cargo, fecha);
        service.guardar(emp);

        listaObs.add(emp);
        limpiarCampos();

        mostrarAlerta("Correcto", "Empleado guardado");
    }

    @FXML
    private void editarEmpleado() {

        if (seleccionado == null) {
            mostrarAlerta("Error", "Seleccione un empleado");
            return;
        }

        String nombre = txtNombre.getText();
        String numero = txtNumero.getText();
        String area = txtArea.getText();
        String cargo = txtCargo.getText();

        if (dpFecha.getValue() == null) {
            mostrarAlerta("Error", "Seleccione una fecha");
            return;
        }

        String fecha = dpFecha.getValue().toString();

        Empleado nuevo = new Empleado(nombre, numero, area, cargo, fecha);

        int index = listaObs.indexOf(seleccionado);
        listaObs.set(index, nuevo);

        service.getAll().set(index, nuevo);

        limpiarCampos();
        seleccionado = null;

        mostrarAlerta("Correcto", "Empleado editado");
    }

    @FXML
    private void eliminarEmpleado() {

        if (seleccionado == null) {
            mostrarAlerta("Error", "Seleccione un empleado");
            return;
        }

        listaObs.remove(seleccionado);
        service.eliminar(seleccionado);

        limpiarCampos();
        seleccionado = null;

        mostrarAlerta("Correcto", "Empleado eliminado");
    }

    @FXML
    private void buscarArea() {
        String area = txtBuscar.getText();

        listaObs.clear();
        listaObs.addAll(service.buscarArea(area));
    }

    @FXML
    private void buscarCargo() {
        String cargo = txtBuscar.getText();

        listaObs.clear();
        listaObs.addAll(service.buscarCargo(cargo));
    }

    @FXML
    private void mostrarTodos() {
        listaObs.clear();
        listaObs.addAll(service.getAll());
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtNumero.clear();
        txtArea.clear();
        txtCargo.clear();
        dpFecha.setValue(null); // 🔥 limpiar fecha
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}