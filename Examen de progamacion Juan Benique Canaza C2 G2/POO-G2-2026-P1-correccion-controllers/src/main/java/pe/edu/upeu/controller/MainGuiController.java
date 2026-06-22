package pe.edu.upeu.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import pe.edu.upeu.config.AppContext;
import pe.edu.upeu.dto.MenuMenuItenTO;
import pe.edu.upeu.service.IMenuMenuItemDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MainGuiController {

    private final IMenuMenuItemDao menuDao;

    @FXML private BorderPane bp;
    @FXML private MenuBar menuBarFx;
    @FXML private TabPane tabPaneFx;

    public MainGuiController(IMenuMenuItemDao menuDao) {
        this.menuDao = menuDao;
    }

    @FXML
    public void initialize() {
        Platform.runLater(this::graficarMenus);
    }

    private void graficarMenus() {
        try {
            menuBarFx.getMenus().clear();

            // Traemos los accesos directamente
            List<MenuMenuItenTO> accesos = menuDao.listaAccesos("Administrador", new Properties());
            Map<String, Menu> menuMap = new HashMap<>();

            for (MenuMenuItenTO acceso : accesos) {
                String menuNombre = acceso.getMenuNombre();
                if (menuNombre == null || menuNombre.isEmpty()) continue;

                Menu menu = menuMap.get(menuNombre);
                if (menu == null) {
                    menu = new Menu(menuNombre);
                    menuMap.put(menuNombre, menu);
                    menuBarFx.getMenus().add(menu);
                }

                MenuItem menuItem = new MenuItem(acceso.getMenuItemNombre());
                menuItem.setOnAction(event -> {
                    if ("S".equals(acceso.getEstado()) || "Salir".equalsIgnoreCase(acceso.getMenuNombre())) {
                        Platform.exit();
                        System.exit(0);
                    } else {
                        abrirMDI(acceso.getTitulo(), acceso.getUrl());
                    }
                });
                menu.getItems().add(menuItem);
            }
        } catch (Exception e) {
            System.err.println("Error dibujando los menús: " + e.getMessage());
        }
    }

    private void abrirMDI(String tituloTab, String fxmlPath) {
        try {
            for (Tab tab : tabPaneFx.getTabs()) {
                if (tab.getText().equals(tituloTab)) {
                    tabPaneFx.getSelectionModel().select(tab);
                    return;
                }
            }

            AppContext ctx = AppContext.getInstance();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            fxmlLoader.setControllerFactory(ctx::getBean);
            Parent root = fxmlLoader.load();

            ScrollPane scrollPane = new ScrollPane(root);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);

            Tab newTab = new Tab(tituloTab, scrollPane);
            newTab.setClosable(true);

            tabPaneFx.getTabs().add(newTab);
            tabPaneFx.getSelectionModel().select(newTab);

        } catch (Exception e) {
            System.err.println("Error al abrir la pestaña: " + fxmlPath);
            e.printStackTrace();
        }
    }
}
