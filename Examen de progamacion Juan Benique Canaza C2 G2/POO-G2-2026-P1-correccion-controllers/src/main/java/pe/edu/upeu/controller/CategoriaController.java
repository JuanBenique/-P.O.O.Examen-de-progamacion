package pe.edu.upeu.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pe.edu.upeu.model.Categoria;
import pe.edu.upeu.service.ICategoriaService;

public class CategoriaController {

    @FXML private TableView<Categoria> tblCategoria;
    @FXML private TableColumn<Categoria, String> colIdCateg;
    @FXML private TableColumn<Categoria, String> colNombre;

    @FXML private TextField txtIdCateg;
    @FXML private TextField txtNombre;

    private final ICategoriaService categoriaService;
    private ObservableList<Categoria> lista = FXCollections.observableArrayList();

    public CategoriaController(ICategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @FXML
    public void initialize() {
        colIdCateg.setCellValueFactory(new PropertyValueFactory<>("idCateg"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        cargarDatos();

        tblCategoria.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                txtIdCateg.setText(sel.getIdCateg());
                txtNombre.setText(sel.getNombre());
            }
        });
    }

    private void cargarDatos() {
        lista.setAll(categoriaService.findAll());
        tblCategoria.setItems(lista);
        tblCategoria.refresh();
    }

    @FXML
    private void guardar() {
        if (txtIdCateg.getText().isEmpty() || txtNombre.getText().isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }
        Categoria c = Categoria.builder()
                .idCateg(txtIdCateg.getText())
                .nombre(txtNombre.getText())
                .build();

        if (categoriaService.existsById(c.getIdCateg())) {
            categoriaService.update(c.getIdCateg(), c);
        } else {
            categoriaService.save(c);
        }
        limpiar();
        cargarDatos();
    }

    @FXML
    private void eliminar() {
        Categoria sel = tblCategoria.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta("Error", "Seleccione una categoría.");
            return;
        }
        categoriaService.delete(sel.getIdCateg());
        limpiar();
        cargarDatos();
    }

    @FXML
    private void limpiar() {
        txtIdCateg.clear();
        txtNombre.clear();
        tblCategoria.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
