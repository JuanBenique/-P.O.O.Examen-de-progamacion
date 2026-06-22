package pe.edu.upeu.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pe.edu.upeu.model.Categoria;
import pe.edu.upeu.model.Producto;
import pe.edu.upeu.service.ICategoriaService;
import pe.edu.upeu.service.IProductoService;

public class ProductoController {

    @FXML private TableView<Producto> tblProducto;
    @FXML private TableColumn<Producto, String> colIdProducto;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, Double> colPreUnitario;
    @FXML private TableColumn<Producto, Double> colPorceUtil;
    @FXML private TableColumn<Producto, String> colTalla;
    @FXML private TableColumn<Producto, String> colColor;
    @FXML private TableColumn<Producto, Integer> colStock;
    @FXML private TableColumn<Producto, String> colCategoria;

    @FXML private TextField txtIdProducto;
    @FXML private TextField txtNombre;
    @FXML private TextField txtPreUnitario;
    @FXML private TextField txtPorceUtil;
    @FXML private TextField txtTalla;
    @FXML private TextField txtColor;
    @FXML private TextField txtStock;
    @FXML private ComboBox<Categoria> cmbCategoria;

    private final IProductoService productoService;
    private final ICategoriaService categoriaService;
    private ObservableList<Producto> lista = FXCollections.observableArrayList();

    public ProductoController(IProductoService productoService, ICategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @FXML
    public void initialize() {
        colIdProducto.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPreUnitario.setCellValueFactory(new PropertyValueFactory<>("preUnitario"));
        colPorceUtil.setCellValueFactory(new PropertyValueFactory<>("porceUtil"));
        colTalla.setCellValueFactory(new PropertyValueFactory<>("talla"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        colCategoria.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdCateg().getNombre()));

        cmbCategoria.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Categoria item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });
        cmbCategoria.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Categoria item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });

        cargarDatos();

        tblProducto.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                txtIdProducto.setText(sel.getIdProducto());
                txtNombre.setText(sel.getNombre());
                txtPreUnitario.setText(String.valueOf(sel.getPreUnitario()));
                txtPorceUtil.setText(String.valueOf(sel.getPorceUtil()));
                txtTalla.setText(sel.getTalla());
                txtColor.setText(sel.getColor());
                txtStock.setText(String.valueOf(sel.getStock()));

                cmbCategoria.getItems().stream()
                        .filter(c -> c.getIdCateg().equals(sel.getIdCateg().getIdCateg()))
                        .findFirst()
                        .ifPresent(cmbCategoria::setValue);
            }
        });
    }

    private void cargarDatos() {
        cmbCategoria.setItems(FXCollections.observableArrayList(categoriaService.findAll()));
        lista.setAll(productoService.findAll());
        tblProducto.setItems(lista);
        tblProducto.refresh();
    }

    @FXML
    private void guardar() {
        if (txtIdProducto.getText().isEmpty() || txtNombre.getText().isEmpty() || cmbCategoria.getValue() == null) {
            mostrarAlerta("Error", "ID, Nombre y Categoría son obligatorios.");
            return;
        }
        try {
            Producto p = Producto.builder()
                    .idProducto(txtIdProducto.getText())
                    .nombre(txtNombre.getText())
                    .preUnitario(Double.parseDouble(txtPreUnitario.getText()))
                    .porceUtil(Double.parseDouble(txtPorceUtil.getText()))
                    .talla(txtTalla.getText())
                    .color(txtColor.getText())
                    .stock(Integer.parseInt(txtStock.getText()))
                    .idCateg(cmbCategoria.getValue())
                    .build();

            if (productoService.existsById(p.getIdProducto())) {
                productoService.update(p.getIdProducto(), p);
            } else {
                productoService.save(p);
            }
            limpiar();
            cargarDatos();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Precio, porcentaje y stock deben ser numéricos.");
        }
    }

    @FXML
    private void eliminar() {
        Producto sel = tblProducto.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta("Error", "Seleccione un producto.");
            return;
        }
        productoService.delete(sel.getIdProducto());
        limpiar();
        cargarDatos();
    }

    @FXML
    private void limpiar() {
        txtIdProducto.clear();
        txtNombre.clear();
        txtPreUnitario.clear();
        txtPorceUtil.clear();
        txtTalla.clear();
        txtColor.clear();
        txtStock.clear();
        cmbCategoria.setValue(null);
        tblProducto.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
